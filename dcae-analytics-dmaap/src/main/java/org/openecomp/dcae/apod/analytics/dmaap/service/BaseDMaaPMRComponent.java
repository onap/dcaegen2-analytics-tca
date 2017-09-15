/*
 * ===============================LICENSE_START======================================
 *  dcae-analytics
 * ================================================================================
 *    Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  ============================LICENSE_END===========================================
 */

package org.openecomp.dcae.apod.analytics.dmaap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.common.utils.HTTPUtils;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponseImpl;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponseImpl;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisherQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;

/**
 * Base class for DMaaP MR Publishers and Subscriber Implementations containing various utility methods
 *
 * @author Rajiv Singla . Creation Date: 11/1/2016.
 */
public abstract class BaseDMaaPMRComponent implements DMaaPMRComponent {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDMaaPMRComponent.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates Base64 encoded Auth Header for given userName and Password
     * If either user name of password are null return absent
     *
     * @param userName username
     * @param userPassword user password
     * @return base64 encoded auth header if username or password are both non null
     */
    protected static Optional<String> getAuthHeader(@Nullable final String userName,
                                                    @Nullable final String userPassword) {
        if (userName == null || userPassword == null) {
            return Optional.absent();
        } else {
            final String auth = userName + ":" + userPassword;
            final Charset isoCharset = Charset.forName("ISO-8859-1");
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(isoCharset));
            return Optional.of("Basic " + new String(encodedAuth, isoCharset));
        }
    }


    /**
     * Creates Publisher URI for given {@link DMaaPMRPublisherConfig}
     *
     * @param publisherConfig publisher settings
     *
     * @return DMaaP MR Publisher Topic URI that can be used to post messages to MR Topic
     */
    protected static URI createPublisherURI(final DMaaPMRPublisherConfig publisherConfig) {
        final String hostName = publisherConfig.getHostName();
        final Integer portNumber = publisherConfig.getPortNumber();
        final String getProtocol = publisherConfig.getProtocol();
        final String topicName = publisherConfig.getTopicName();
        URI publisherURI = null;
        try {
            publisherURI = new URIBuilder().setScheme(getProtocol).setHost(hostName).setPort(portNumber)
                    .setPath(AnalyticsConstants.DMAAP_URI_PATH_PREFIX + topicName).build();
        } catch (URISyntaxException e) {
            final String errorMessage = format("Error while creating publisher URI: %s", e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }
        LOG.info("Created DMaaP MR Publisher URI: {}", publisherURI);
        return publisherURI;
    }


    /**
     * Creates Subscriber URI for given {@link DMaaPMRSubscriberConfig}
     *
     * @param subscriberConfig subscriber settings
     *
     * @return DMaaP MR Subscriber Topic URI that can be used to fetch messages from MR topic
     */
    protected static URI createSubscriberURI(final DMaaPMRSubscriberConfig subscriberConfig) {
        final String hostName = subscriberConfig.getHostName();
        final Integer portNumber = subscriberConfig.getPortNumber();
        final String getProtocol = subscriberConfig.getProtocol();
        final String topicName = subscriberConfig.getTopicName();
        final String consumerId = subscriberConfig.getConsumerId();
        final String consumerGroup = subscriberConfig.getConsumerGroup();
        final Integer timeoutMS = subscriberConfig.getTimeoutMS();
        final Integer messageLimit = subscriberConfig.getMessageLimit();
        URI subscriberURI = null;
        try {
            URIBuilder uriBuilder = new URIBuilder().setScheme(getProtocol).setHost(hostName).setPort(portNumber)
                    .setPath(AnalyticsConstants.DMAAP_URI_PATH_PREFIX
                            + topicName + "/"
                            + consumerGroup + "/" +
                            consumerId);
            // add query params if present
            if (timeoutMS > 0) {
                uriBuilder.addParameter(AnalyticsConstants.SUBSCRIBER_TIMEOUT_QUERY_PARAM_NAME, timeoutMS.toString());
            }
            if (messageLimit > 0) {
                uriBuilder.addParameter(AnalyticsConstants.SUBSCRIBER_MSG_LIMIT_QUERY_PARAM_NAME,
                        messageLimit.toString());
            }
            subscriberURI = uriBuilder.build();

        } catch (URISyntaxException e) {
            final String errorMessage = format("Error while creating subscriber URI: %s", e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }

        LOG.info("Created DMaaP MR Subscriber URI: {}", subscriberURI);
        return subscriberURI;
    }


    /**
     *  Creates 202 (Accepted) Response code message
     *
     * @param batchQueueSize batch Queue size
     *
     * @return response with 202 message code
     */
    protected static DMaaPMRPublisherResponse createPublisherAcceptedResponse(int batchQueueSize) {
        return createPublisherResponse(HTTPUtils.HTTP_ACCEPTED_RESPONSE_CODE,
                "Accepted - Messages queued for batch publishing to MR Topic", batchQueueSize);
    }


    /**
     *  Creates 204 (No Content) Response code message
     *
     * @return response with 204 message code
     */
    protected static DMaaPMRPublisherResponse createPublisherNoContentResponse() {
        return createPublisherResponse(HTTPUtils.HTTP_NO_CONTENT_RESPONSE_CODE,
                "No Content - No Messages in batch queue for flushing to MR Topic", 0);
    }


    /**
     * Creates Publisher Response for given response code, response Message and pending Message Count
     *
     * @param responseCode HTTP Status Code
     * @param responseMessage response message
     * @param pendingMessages pending messages in batch queue
     *
     * @return DMaaP MR Publisher Response
     */
    protected static DMaaPMRPublisherResponse createPublisherResponse(int responseCode, String
            responseMessage, int pendingMessages) {
        return new DMaaPMRPublisherResponseImpl(responseCode, responseMessage, pendingMessages);
    }


    /**
     * Returns weekly consistent pending messages in batch queue
     *
     * @param publisherQueue batch queue
     * @param publisherConfig publisher settings
     *
     * @return pending messages to be published
     */
    protected static int getPendingMessages(@Nonnull final DMaaPMRPublisherQueue publisherQueue,
                                            @Nonnull final DMaaPMRPublisherConfig publisherConfig) {
        return publisherConfig.getMaxBatchSize() - publisherQueue.getBatchQueueRemainingSize();
    }


    /**
     * Creates Subscriber Response for give response Code, response Message and fetch messages
     *
     * @param responseCode response Code
     * @param responseMessage response Message
     * @param fetchedMessages fetched messages
     *
     * @return DMaaP MR Subscriber Response
     */
    protected static DMaaPMRSubscriberResponse createSubscriberResponse(int responseCode, String
            responseMessage, List<String> fetchedMessages) {
        if (fetchedMessages == null) {
            return new DMaaPMRSubscriberResponseImpl(responseCode, responseMessage);
        } else {
            return new DMaaPMRSubscriberResponseImpl(responseCode, responseMessage, fetchedMessages);
        }
    }


    /**
     * Custom response handler which extract status code and response body
     *
     * @return Pair containing Response code and response body
     */
    protected static ResponseHandler<Pair<Integer, String>> responseHandler() {
        return new ResponseHandler<Pair<Integer, String>>() {
            @Override
            public Pair<Integer, String> handleResponse(HttpResponse response) throws IOException {
                // Get Response status code
                final int status = response.getStatusLine().getStatusCode();
                final HttpEntity responseEntity = response.getEntity();
                // If response entity is not null - extract response body as string
                String responseEntityString = "";
                if (responseEntity != null) {
                    responseEntityString = EntityUtils.toString(responseEntity);
                }
                return new ImmutablePair<>(status, responseEntityString);
            }
        };
    }


    /**
     *  Adds message to Publisher recovery queue. If recovery queue is full throws an error as messages will
     *  be lost
     *
     * @param publisherQueue publisher queue
     * @param messages recoverable messages to be published to recovery queue
     */
    protected static void addMessagesToRecoveryQueue(DMaaPMRPublisherQueue publisherQueue,
                                                     List<String> messages) {
        try {
            publisherQueue.addRecoverableMessages(messages);

            LOG.debug("Messages Added to Recovery Queue. Messages Size: {}, Recovery Queue Remaining Size: {}",
                    messages.size(), publisherQueue.getBatchQueueRemainingSize());

        } catch (IllegalStateException e) {
            final String errorMessage = format("Unable to put messages in recovery queue. Messages will be lost. " +
                            "Recovery Queue might be full. Message Size: %d, Recovery Queue Remaining Capacity: %d",
                    messages.size(), publisherQueue.getRecoveryQueueRemainingSize());
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }
    }


    /**
     * Converts List of messages to Json String Array which can be published to DMaaP MR topic.
     *
     * @param messages messages that need to parsed to Json Array representation
     * @return json string representation of message
     */
    protected static String convertToJsonString(@Nullable final List<String> messages) {
        // If messages are null or empty just return empty array
        if (messages == null || messages.isEmpty()) {
            return "[]";
        }


        List<JsonNode> jsonMessageObjectsList = new LinkedList<>();

        try {
            for (String message : messages) {
                final JsonNode jsonNode = objectMapper.readTree(message);
                jsonMessageObjectsList.add(jsonNode);
            }
            return objectMapper.writeValueAsString(jsonMessageObjectsList);
        } catch (JsonProcessingException e) {
            final String errorMessage =
                    format("Unable to convert publisher messages to Json. Messages: %s, Json Error: %s",
                            messages, e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);

        } catch (IOException e) {
            final String errorMessage =
                    format("IO Exception while converting publisher messages to Json. Messages: %s, Json Error: %s",
                            messages, e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }
    }


    /**
     * Converts subscriber messages json string to List of messages. If message Json String is empty
     * or null
     *
     * @param messagesJsonString json messages String
     *
     * @return List containing DMaaP MR Messages
     */
    protected static List<String> convertJsonToStringMessages(@Nullable final String messagesJsonString) {

        final LinkedList<String> messages = new LinkedList<>();

        // If message string is not null or not empty parse json message array to List of string messages
        if (messagesJsonString != null && !messagesJsonString.trim().isEmpty()
                && !("[]").equals(messagesJsonString.trim())) {

            try {
                // get root node
                final JsonNode rootNode = objectMapper.readTree(messagesJsonString);
                // iterate over root node and parse arrays messages
                for (JsonNode jsonNode : rootNode) {
                    // if array parse it is array of messages
                    final String incomingMessageString = jsonNode.toString();
                    if (jsonNode.isArray()) {
                        final List messageList = objectMapper.readValue(incomingMessageString, List.class);
                        for (Object message : messageList) {
                            final String jsonMessageString = objectMapper.writeValueAsString(message);
                            addUnescapedJsonToMessage(messages, jsonMessageString);
                        }
                    } else {
                        // parse it as object
                        addUnescapedJsonToMessage(messages, incomingMessageString);
                    }
                }

            } catch (IOException e) {
                final String errorMessage =
                        format("Unable to convert subscriber Json String to Messages. Subscriber Response String: %s," +
                                " Json Error: %s", messagesJsonString, e);
                throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
            }

        }
        return messages;
    }

    /**
     * Adds unescaped Json messages to given messages list
     *
     * @param messages message list in which unescaped messages will be added
     * @param incomingMessageString incoming message string that may need to be escaped
     */
    private static void addUnescapedJsonToMessage(List<String> messages, String incomingMessageString) {
        if (incomingMessageString.startsWith("\"") && incomingMessageString.endsWith("\"")) {
            messages.add(StringEscapeUtils.unescapeJson(
                    incomingMessageString.substring(1, incomingMessageString.length() - 1)));
        } else {
            messages.add(StringEscapeUtils.unescapeJson(incomingMessageString));
        }
    }


}
