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

package org.openecomp.dcae.apod.analytics.dmaap.service.publisher;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.openecomp.dcae.apod.analytics.dmaap.service.BaseDMaaPMRComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.openecomp.dcae.apod.analytics.common.utils.HTTPUtils.isSuccessfulResponseCode;
import static java.lang.String.format;

/**
 * Concrete Implementation of {@link DMaaPMRPublisher} which uses {@link HttpClient}
 *
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public class DMaaPMRPublisherImpl extends BaseDMaaPMRComponent implements DMaaPMRPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRPublisherImpl.class);

    private final DMaaPMRPublisherConfig publisherConfig;
    private final CloseableHttpClient closeableHttpClient;
    private final DMaaPMRPublisherQueue publisherQueue;
    private final Date publisherCreationTime;
    private URI publisherUri;

    @Inject
    public DMaaPMRPublisherImpl(@Assisted DMaaPMRPublisherConfig publisherConfig,
                                DMaaPMRPublisherQueueFactory dMaaPMRPublisherQueueFactory,
                                CloseableHttpClient closeableHttpClient) {

        this.publisherConfig = publisherConfig;
        this.publisherQueue = dMaaPMRPublisherQueueFactory.create(
                publisherConfig.getMaxBatchSize(), publisherConfig.getMaxRecoveryQueueSize());
        this.closeableHttpClient = closeableHttpClient;
        this.publisherUri = createPublisherURI(publisherConfig);
        this.publisherCreationTime = new Date();
    }


    @Override
    public DMaaPMRPublisherResponse publish(List<String> messages)  {

        final int batchQueueRemainingSize = publisherQueue.getBatchQueueRemainingSize();

        // if messages size is less than batch queue size - just queue them for batch publishing
        if (batchQueueRemainingSize > messages.size()) {
            LOG.debug("Adding messages to batch Queue. No flushing required. Messages Size:{}. Batch Queue Size:{}",
                    messages.size(), batchQueueRemainingSize);
            final int batchQueueSize = publisherQueue.addBatchMessages(messages);
            return createPublisherAcceptedResponse(batchQueueSize);

        } else {

            // grab all already queued messages, append current messages and force publish them to DMaaP MR topic
            final List<String> queueMessages = publisherQueue.getMessageForPublishing();
            LOG.debug("Batch Queue capacity exceeds messages size. Flushing of all pending messages to DMaaP MR " +
                    "Publisher Topic.");
            return forcePublish(Lists.newLinkedList(Iterables.concat(queueMessages, messages)));
        }

    }

    @Override
    public DMaaPMRPublisherResponse forcePublish(List<String> messages) {

        LOG.debug("Force publishing messages to DMaaP MR Topic. Messages Size: {}", messages.size());

        final String contentType = publisherConfig.getContentType();
        final String userName = publisherConfig.getUserName();
        final String userPassword = publisherConfig.getUserPassword();
        final HttpPost postRequest = new HttpPost(publisherUri);

        // add Authorization Header if username and password are present
        final Optional<String> authHeader = getAuthHeader(userName, userPassword);
        if (authHeader.isPresent()) {
            postRequest.addHeader(HttpHeaders.AUTHORIZATION, authHeader.get());
        } else {
            LOG.debug("DMaaP MR Publisher Authentication is disabled as username or password is not present.");
        }

        // Create post string entity
        final String messagesJson = convertToJsonString(messages);
        final StringEntity requestEntity =
                new StringEntity(messagesJson, ContentType.create(contentType, "UTF-8"));
        postRequest.setEntity(requestEntity);

        try {
            final Pair<Integer, String> responsePair = closeableHttpClient.execute(postRequest, responseHandler());
            final Integer responseCode = responsePair.getLeft();
            final String responseBody = responsePair.getRight();
            // if messages were published successfully, return successful response
            if (isSuccessfulResponseCode(responseCode)) {
                LOG.debug("DMaaP MR Messages published successfully. DMaaP Response Code: {}. DMaaP Response " +
                                "Body: {}, Number of Messages published: {}",
                        responseCode, responseBody, messages.size());

            } else {
                LOG.warn("Unable to publish messages to DMaaP MR Topic. DMaaP Response Code: {}, DMaaP Response " +
                        "Body: {}. Messages will be queued in recovery queue", responseCode, responseBody);
                addMessagesToRecoveryQueue(publisherQueue, messages);
            }

            return createPublisherResponse(responseCode, responseBody,
                    getPendingMessages(publisherQueue, publisherConfig));

        } catch (IOException e) {
            // If IO Error then we need to also put messages in recovery queue
            addMessagesToRecoveryQueue(publisherQueue, messages);
            final String errorMessage = format("IO Exception while publishing messages to DMaaP Topic. " +
                    "Messages will be queued in recovery queue. Messages Size: %d", messages.size());

            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }

    }


    @Override
    public DMaaPMRPublisherResponse flush() {
        final List<String> queueMessages = publisherQueue.getMessageForPublishing();
        // If there are no message return 204 (No Content) response code
        if (queueMessages.isEmpty()) {
            LOG.debug("No messages to publish to batch queue. Returning 204 status code");
            return createPublisherNoContentResponse();
        } else {
            // force publish messages in queue
            return forcePublish(queueMessages);
        }
    }

    @Override
    public Date getPublisherCreationTime() {
        return new Date(publisherCreationTime.getTime());
    }

    @Override
    public void close() throws Exception {

        // flush current message in the queue
        int retrialNumber = 0;
        int flushResponseCode;

        // automatic retries if messages cannot be flushed
        do {
            retrialNumber++;
            DMaaPMRPublisherResponse flushResponse = flush();
            flushResponseCode = flushResponse.getResponseCode();

            if (!isSuccessfulResponseCode(flushResponseCode)) {
                LOG.warn("Unable to flush batch messages to publisher due to DMaaP MR invalid Response: {}. " +
                                "Retrial No: {} of Max {} Retries", flushResponseCode, retrialNumber,
                        AnalyticsConstants.PUBLISHER_MAX_FLUSH_RETRIES_ON_CLOSE);

                Thread.sleep(AnalyticsConstants.PUBLISHER_DELAY_MS_ON_RETRIES_ON_CLOSE);
            }
        } while (retrialNumber <= AnalyticsConstants.PUBLISHER_MAX_FLUSH_RETRIES_ON_CLOSE &&
                !isSuccessfulResponseCode(flushResponseCode));

        if (!isSuccessfulResponseCode(flushResponseCode)) {
            LOG.error("Unable to flush batch messages to publisher. Messages loss cannot be prevented");
        } else {
            LOG.info("Successfully published all batched messages to publisher.");
        }

        // close http client
        closeableHttpClient.close();

    }
}
