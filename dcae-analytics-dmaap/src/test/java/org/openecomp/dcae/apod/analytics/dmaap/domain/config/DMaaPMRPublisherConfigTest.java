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
public class DMaaPMRPublisherConfigTest extends BaseAnalyticsDMaaPUnitTest {


    @Test
    public void testPublisherConfigDefaults() throws Exception {

        final DMaaPMRPublisherConfig actualDefaultPublisherConfig =
                new DMaaPMRPublisherConfig.Builder(HOST_NAME, TOPIC_NAME).build();

        final DMaaPMRPublisherConfig expectedDefaultPublisherConfig =
                new DMaaPMRPublisherConfig.Builder(HOST_NAME, TOPIC_NAME)
                        .setPortNumber(AnalyticsConstants.DEFAULT_PORT_NUMBER)
                        .setContentType(AnalyticsConstants.DEFAULT_CONTENT_TYPE)
                        .setProtocol(AnalyticsConstants.DEFAULT_PROTOCOL)
                        .setMaxBatchSize(AnalyticsConstants.DEFAULT_PUBLISHER_MAX_BATCH_SIZE)
                        .setMaxRecoveryQueueSize(AnalyticsConstants.DEFAULT_PUBLISHER_MAX_RECOVERY_QUEUE_SIZE)
                        .build();

        assertTrue("Default Publisher Config parameters must match",
                actualDefaultPublisherConfig.equals(expectedDefaultPublisherConfig));

    }


    @Test
    public void testPublisherCustomConfig() throws Exception {


        final DMaaPMRPublisherConfig actualCustomPublisherConfig = getPublisherConfig();

        final DMaaPMRPublisherConfig expectedCustomPublisherConfig =
                new DMaaPMRPublisherConfig.Builder(HOST_NAME, TOPIC_NAME)
                        .setPortNumber(PORT_NUMBER)
                        .setUserName(USERNAME)
                        .setUserPassword(PASSWORD)
                        .setContentType(CONTENT_TYPE)
                        .setProtocol(HTTP_PROTOCOL)
                        .setMaxBatchSize(PUBLISHER_MAX_BATCH_QUEUE_SIZE)
                        .setMaxRecoveryQueueSize(PUBLISHER_MAX_RECOVERY_QUEUE_SIZE)
                        .build();

        assertTrue("Custom Publisher Config parameters must match",
                actualCustomPublisherConfig.equals(expectedCustomPublisherConfig));
    }


}
