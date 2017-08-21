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

package org.openecomp.dcae.apod.analytics.dmaap.it;

import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.openecomp.dcae.apod.analytics.test.BaseDCAEAnalyticsIT;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;

/**
 * Base class for all DCAE DMaaP Integration Tests
 * <p>
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public abstract class BaseAnalyticsDMaaPIT extends BaseDCAEAnalyticsIT {

    // Integration Test Settings
    protected static final String HOST_NAME = "mrlocal-mtnjftle01.homer.com";
    protected static final Integer PORT_NUMBER = 3905;
    protected static final String TOPIC_NAME = "com.dcae.dmaap.mtnje2.DcaeTestVESPub";

    protected static final String USERNAME = "USER";
    protected static final String PASSWORD = "PASSWORD";
    protected static final String HTTP_PROTOCOL = "https";
    protected static final String CONTENT_TYPE = "application/json";

    protected static final int PUBLISHER_MAX_BATCH_QUEUE_SIZE = 20;
    protected static final int PUBLISHER_MAX_RECOVERY_QUEUE_SIZE = 200;

    protected static final String SUBSCRIBER_CONSUMER_ID = "123";
    protected static final String SUBSCRIBER_CONSUMER_GROUP_NAME = "testGonsumerName-" + SUBSCRIBER_CONSUMER_ID;
    protected static final int SUBSCRIBER_TIMEOUT_MS = 2000;
    protected static final int SUBSCRIBER_MESSAGE_LIMIT = 20;

    /**
     * Creates Sample Publisher settings for integration testing purposes
     *
     * @return DMaaP MR Publisher Config
     */
    protected static DMaaPMRPublisherConfig getPublisherConfig() {
        return new DMaaPMRPublisherConfig.Builder(HOST_NAME, TOPIC_NAME)
                .setPortNumber(PORT_NUMBER)
                .setProtocol(HTTP_PROTOCOL)
                .setContentType(CONTENT_TYPE)
                .setUserName(USERNAME)
                .setUserPassword(PASSWORD)
                .setMaxBatchSize(PUBLISHER_MAX_BATCH_QUEUE_SIZE)
                .setMaxRecoveryQueueSize(PUBLISHER_MAX_RECOVERY_QUEUE_SIZE)
                .build();
    }

    /**
     * Creates Sample Subscriber settings for integration testing purposes
     *
     * @return DMaaP MR Subscriber Config
     */
    protected static DMaaPMRSubscriberConfig getSubscriberConfig(String consumerId) {
        return new DMaaPMRSubscriberConfig.Builder(HOST_NAME, TOPIC_NAME)
                .setPortNumber(PORT_NUMBER)
                .setProtocol(HTTP_PROTOCOL)
                .setContentType(CONTENT_TYPE)
                .setUserName(USERNAME)
                .setUserPassword(PASSWORD)
                .setConsumerGroup(SUBSCRIBER_CONSUMER_GROUP_NAME)
                .setConsumerId(consumerId != null ? consumerId : SUBSCRIBER_CONSUMER_ID)
                .setTimeoutMS(SUBSCRIBER_TIMEOUT_MS)
                .setMessageLimit(SUBSCRIBER_MESSAGE_LIMIT).build();
    }

    /**
     * Publishes 2 sample message to DMaaP Topic for integration test purposes
     *
     * @param dMaaPMRPublisher DMaaP MR Publisher
     * @return DMaaP MR Publisher Response
     */
    protected static DMaaPMRPublisherResponse publishTwoSampleMessages(DMaaPMRPublisher dMaaPMRPublisher) {
        return dMaaPMRPublisher.publish(getTwoSampleMessage());
    }

    protected static List<String> getTwoSampleMessage() {
        String message1 = "{ \"message\" : \"Test Message1\"}";
        String message2 = "{ \"message\" : \"Test Message2\"}";
        return of(message1, message2);
    }

}
