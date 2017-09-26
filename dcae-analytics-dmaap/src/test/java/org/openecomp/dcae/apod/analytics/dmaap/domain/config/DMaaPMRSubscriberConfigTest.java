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

package org.openecomp.dcae.apod.analytics.dmaap.domain.config;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.dmaap.BaseAnalyticsDMaaPUnitTest;

import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 10/14/2016.
 */
public class DMaaPMRSubscriberConfigTest extends BaseAnalyticsDMaaPUnitTest {

    @Test
    public void testSubscriberConfigDefaults() throws Exception {

        DMaaPMRSubscriberConfig actualDefaultSubscriberConfig =
                new DMaaPMRSubscriberConfig.Builder(HOST_NAME, TOPIC_NAME)
                        .setConsumerGroup(AnalyticsConstants.DEFAULT_SUBSCRIBER_GROUP_PREFIX + SUBSCRIBER_CONSUMER_ID)
                        .setConsumerId(SUBSCRIBER_CONSUMER_ID).build();

        DMaaPMRSubscriberConfig expectedSubscriberConfig =
                new DMaaPMRSubscriberConfig.Builder(HOST_NAME, TOPIC_NAME)
                        .setPortNumber(AnalyticsConstants.DEFAULT_PORT_NUMBER)
                        .setContentType(AnalyticsConstants.DEFAULT_CONTENT_TYPE)
                        .setProtocol(AnalyticsConstants.DEFAULT_PROTOCOL)
                        .setConsumerGroup(AnalyticsConstants.DEFAULT_SUBSCRIBER_GROUP_PREFIX + SUBSCRIBER_CONSUMER_ID)
                        .setConsumerId(SUBSCRIBER_CONSUMER_ID)
                        .setMessageLimit(AnalyticsConstants.DEFAULT_SUBSCRIBER_MESSAGE_LIMIT)
                        .setTimeoutMS(AnalyticsConstants.DEFAULT_SUBSCRIBER_TIMEOUT_MS)
                        .build();

        assertTrue("Default Subscriber Config parameters must match",
                actualDefaultSubscriberConfig.equals(expectedSubscriberConfig));

    }


    @Test
    public void testSubscriberCustomConfig() throws Exception {

        DMaaPMRSubscriberConfig actualSubscriberCustomConfig = getSubscriberConfig(SUBSCRIBER_CONSUMER_ID,
                SUBSCRIBER_CONSUMER_GROUP_NAME);

        DMaaPMRSubscriberConfig expectedSubscriberCustomConfig =
                new DMaaPMRSubscriberConfig.Builder(HOST_NAME, TOPIC_NAME)
                        .setPortNumber(PORT_NUMBER)
                        .setUserName(USERNAME)
                        .setUserPassword(PASSWORD)
                        .setContentType(CONTENT_TYPE)
                        .setProtocol(HTTP_PROTOCOL)
                        .setConsumerGroup(SUBSCRIBER_CONSUMER_GROUP_NAME)
                        .setConsumerId(SUBSCRIBER_CONSUMER_ID)
                        .setMessageLimit(SUBSCRIBER_MESSAGE_LIMIT)
                        .setTimeoutMS(SUBSCRIBER_TIMEOUT_MS)
                        .build();

        assertTrue("Custom Subscriber Config parameters must match",
                actualSubscriberCustomConfig.equals(expectedSubscriberCustomConfig));

    }


}
