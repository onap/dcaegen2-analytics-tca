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

package org.openecomp.dcae.apod.analytics.dmaap.service.subscriber;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.openecomp.dcae.apod.analytics.dmaap.service.BaseDMaaPMRComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.openecomp.dcae.apod.analytics.common.utils.HTTPUtils.isSuccessfulResponseCode;
import static java.lang.String.format;

/**
 * Concrete Implementation of {@link DMaaPMRSubscriber} which uses {@link HttpClient}
 *
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public class DMaaPMRSubscriberImpl extends BaseDMaaPMRComponent implements DMaaPMRSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRSubscriberImpl.class);

    private final DMaaPMRSubscriberConfig subscriberConfig;
    private final CloseableHttpClient closeableHttpClient;
    private final URI subscriberUri;
    private final Date subscriberCreationTime;

    @Inject
    public DMaaPMRSubscriberImpl(@Assisted DMaaPMRSubscriberConfig subscriberConfig,
                                 CloseableHttpClient closeableHttpClient) {
        this.subscriberConfig = subscriberConfig;
        this.closeableHttpClient = closeableHttpClient;
        this.subscriberUri = createSubscriberURI(subscriberConfig);
        this.subscriberCreationTime = new Date();
    }

    @Override
    public DMaaPMRSubscriberResponse fetchMessages() {

        final String userName = subscriberConfig.getUserName();
        final String userPassword = subscriberConfig.getUserPassword();

        final HttpGet getRequest = new HttpGet(subscriberUri);

        // add Authorization Header if username and password are present
        final Optional<String> authHeader = getAuthHeader(userName, userPassword);
        if (authHeader.isPresent()) {
            getRequest.addHeader(HttpHeaders.AUTHORIZATION, authHeader.get());
        } else {
            LOG.debug("DMaaP MR Subscriber Authentication is disabled as username or password is not present.");
        }

        try {

            final Pair<Integer, String> responsePair = closeableHttpClient.execute(getRequest, responseHandler());
            final Integer responseCode = responsePair.getLeft();
            final String responseBody = responsePair.getRight();

            List<String> fetchedMessages = new LinkedList<>();
            String responseMessage = responseBody;

            // if messages were published successfully, return successful response
            if (isSuccessfulResponseCode(responseCode)) {
                if (responseBody != null) {
                    fetchedMessages = convertJsonToStringMessages(responseBody);
                    responseMessage = "Messages Fetched Successfully";
                } else {
                    responseMessage = "DMaaP Response Body had no messages";
                }
            } else {
                LOG.error("Unable to fetch messages to DMaaP MR Topic. DMaaP MR unsuccessful Response Code: {}, " +
                        "DMaaP Response Body: {}", responseCode, responseBody);
            }

            return createSubscriberResponse(responseCode, responseMessage, fetchedMessages);

        } catch (IOException e) {

            final String errorMessage =
                    format("IO Exception while fetching messages from DMaaP Topic. Exception %s", e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }


    }

    @Override
    public Date getSubscriberCreationTime() {
        return new Date(subscriberCreationTime.getTime());
    }

    @Override
    public void close() throws Exception {
        closeableHttpClient.close();
    }
}
