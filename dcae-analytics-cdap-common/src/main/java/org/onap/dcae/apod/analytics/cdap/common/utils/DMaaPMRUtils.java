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

package org.onap.dcae.apod.analytics.cdap.common.utils;

import co.cask.cdap.api.metrics.Metrics;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import org.onap.dcae.apod.analytics.cdap.common.CDAPMetricsConstants;
import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.onap.dcae.apod.analytics.common.utils.HTTPUtils;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.onap.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Utility common methods for DMaaP MR functionality
 *
 * @author Rajiv Singla . Creation Date: 2/6/2017.
 */
public abstract class DMaaPMRUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRUtils.class);

    private DMaaPMRUtils() {
        // private constructor
    }


    /**
     * Returns messages fetched from DMaaP MR Subscriber.
     *
     * @param subscriber DMaaP MR Subscriber instance
     * @param metrics CDAP metrics
     *
     * @return messages fetched from DMaaP MR topic
     */
    public static Optional<List<String>> getSubscriberMessages(final DMaaPMRSubscriber subscriber,
                                                               final Metrics metrics) {

        final Optional<DMaaPMRSubscriberResponse> subscriberResponseOptional =
                getSubscriberResponse(subscriber, metrics);

        // If response is not present, unable to proceed
        if (!subscriberResponseOptional.isPresent()) {
            return Optional.absent();
        }

        final DMaaPMRSubscriberResponse subscriberResponse = subscriberResponseOptional.get();

        // If response code return by the subscriber call is not successful, unable to do proceed
        if (!HTTPUtils.isSuccessfulResponseCode(subscriberResponse.getResponseCode())) {
            LOG.error("Subscriber was unable to fetch messages properly.Subscriber Response Code: {} " +
                    "Unable to proceed further....", subscriberResponse.getResponseCode());
            metrics.count(CDAPMetricsConstants.DMAAP_MR_SUBSCRIBER_UNSUCCESSFUL_RESPONSES_METRIC, 1);
            return Optional.absent();
        }

        LOG.debug("Subscriber HTTP Response Status Code match successful:  {}", subscriberResponse,
                HTTPUtils.HTTP_SUCCESS_STATUS_CODE);

        final List<String> actualMessages = subscriberResponse.getFetchedMessages();

        // If there are no message returned during from Subscriber, nothing to write to CDAP Stream
        if (actualMessages.isEmpty()) {
            LOG.debug("Subscriber Response has no messages. Nothing to write....");
            metrics.count(CDAPMetricsConstants.DMAAP_MR_SUBSCRIBER_RESPONSES_WITH_NO_MESSAGES_METRIC, 1);
            return Optional.absent();
        }

        LOG.debug("DMaaP MR Subscriber found new messages in DMaaP Topic. Message count: {}", actualMessages.size());
        metrics.count(CDAPMetricsConstants.DMAAP_MR_SUBSCRIBER_TOTAL_MESSAGES_PROCESSED_METRIC, actualMessages.size());

        return Optional.of(actualMessages);

    }


    /**
     * Get Subscriber response and records time taken to fetch messages. Returns Optional.None if Subscriber response
     * is null or response status code is not present
     *
     * @param subscriber - DMaaP Subscriber
     * @param metrics - CDAP Metrics collector
     *
     * @return - Optional of Subscriber Response
     */
    public static Optional<DMaaPMRSubscriberResponse> getSubscriberResponse(final DMaaPMRSubscriber subscriber,
                                                                            final Metrics metrics) {

        // Record all response count from subscriber
        metrics.count(CDAPMetricsConstants.DMAAP_MR_SUBSCRIBER_ALL_RESPONSES_COUNT_METRIC, 1);

        // Check how long it took for subscriber to respond
        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        // Fetch messages from DMaaP MR Topic
        DMaaPMRSubscriberResponse subscriberResponse = null;
        try {
            subscriberResponse = subscriber.fetchMessages();
        } catch (DCAEAnalyticsRuntimeException e) {
            LOG.error("Error while fetching messages for DMaaP MR Topic: {}", e);
        }

        stopwatch.stop();
        final long subscriberResponseTimeMS = stopwatch.elapsedMillis();

        // If response is null is null or response code is null, unable to proceed nothing to do
        if (subscriberResponse == null || subscriberResponse.getResponseCode() == null) {
            LOG.error("Subscriber Response is null or subscriber Response code is null. Unable to proceed further...");
            return Optional.absent();
        }

        LOG.debug("Subscriber Response:{}, Subscriber HTTP Response Status Code {}, Subscriber Response Time(ms): {}",
                subscriberResponse, subscriberResponse.getResponseCode(), subscriberResponseTimeMS);

        // Record subscriber response time
        metrics.gauge(CDAPMetricsConstants.DMAAP_MR_SUBSCRIBER_RESPONSE_TIME_MS_METRIC, subscriberResponseTimeMS);

        return Optional.of(subscriberResponse);
    }

}
