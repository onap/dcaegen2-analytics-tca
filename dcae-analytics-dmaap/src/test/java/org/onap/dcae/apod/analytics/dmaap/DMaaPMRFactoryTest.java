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

package org.onap.dcae.apod.analytics.dmaap;

import org.junit.Before;
import org.junit.Test;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.onap.dcae.apod.analytics.dmaap.module.AnalyticsDMaaPTestModule;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.onap.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.onap.dcae.apod.analytics.test.annotation.GuiceModules;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Manjesh Gowda. Creation Date: 11/7/2016.
 */
@GuiceModules(AnalyticsDMaaPTestModule.class)
public class DMaaPMRFactoryTest extends BaseAnalyticsDMaaPGuiceUnitTest {

    private DMaaPMRFactory dmaapMRFactory;

    @Before
    public void setUp() throws Exception {
        dmaapMRFactory = new DMaaPMRFactory(new AnalyticsDMaaPTestModule());
    }

    @Test
    public void createPublisher() throws Exception {
        DMaaPMRPublisher publisher = dmaapMRFactory.createPublisher(getPublisherConfig());
        DMaaPMRPublisherResponse response = publisher.publish(null);
        assertThat(response.getResponseCode(), is(102));
    }

    @Test
    public void createSubscriber() throws Exception {
        DMaaPMRSubscriber dmaapMRSubscriber = dmaapMRFactory.createSubscriber(getSubscriberConfig("", ""));
        DMaaPMRSubscriberResponse response = dmaapMRSubscriber.fetchMessages();
        assertThat(response.getResponseCode(), is(102));
    }

    @Test
    public void create() throws Exception {

    }

}
