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

package org.onap.dcae.apod.analytics.dmaap.it;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.onap.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;

/**
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
@Ignore
public class DMaaPMRPublisherImplIT extends BaseAnalyticsDMaaPIT {

    private DMaaPMRPublisher dMaaPMRPublisher;

    @Before
    public void before() throws Exception {
        DMaaPMRFactory dMaaPMRFactory = DMaaPMRFactory.create();
        dMaaPMRPublisher = dMaaPMRFactory.createPublisher(getPublisherConfig());
    }

    @Test
    public void testPublish() throws Exception {
        long pendingMessageCount = publishTwoSampleMessages(dMaaPMRPublisher).getPendingMessagesCount();
        Assert.assertTrue("Published Message Count must be 2", pendingMessageCount == 2);
    }

    @Test
    public void testFlush() throws Exception {
        publishTwoSampleMessages(dMaaPMRPublisher);
        DMaaPMRPublisherResponse publisherResponse = dMaaPMRPublisher.flush();
        Integer responseCode = publisherResponse.getResponseCode();
        Assert.assertTrue("Server Response code must be 200", responseCode == 200);
    }
}
