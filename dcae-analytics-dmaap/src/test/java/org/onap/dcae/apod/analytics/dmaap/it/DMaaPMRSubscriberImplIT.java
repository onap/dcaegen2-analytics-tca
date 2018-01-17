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

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.onap.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.onap.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
@Ignore
public class DMaaPMRSubscriberImplIT extends BaseAnalyticsDMaaPIT {

    private DMaaPMRPublisher dMaaPMRPublisher;
    private DMaaPMRSubscriber dMaaPMRSubscriber;

    @Before
    public void before() throws Exception {
        String randomConsumerID = UUID.randomUUID().toString();
        DMaaPMRFactory dMaaPMRFactory = DMaaPMRFactory.create();
        dMaaPMRSubscriber = dMaaPMRFactory.createSubscriber(getSubscriberConfig(randomConsumerID));
        dMaaPMRPublisher = dMaaPMRFactory.createPublisher(getPublisherConfig());
    }

    @After
    public void after() throws Exception {
        dMaaPMRSubscriber.close();
        dMaaPMRPublisher.close();
    }


    @Test
    public void testFetchMessages() throws Exception {

        // This call is used to just register a brand new subscriber with DMaaP
        DMaaPMRSubscriberResponse subscriberRegistrationResponse = dMaaPMRSubscriber.fetchMessages();
        assertTrue("Subscriber Registration Response code must be 200 confirming subscriber was registered " +
                "successfully", subscriberRegistrationResponse.getResponseCode() == 200);
        assertTrue("Subscriber Registration Response must not contain any messages", subscriberRegistrationResponse
                .getFetchedMessages().size() == 0);

        // Force push couple of test messages
        DMaaPMRPublisherResponse publisherResponse = dMaaPMRPublisher.forcePublish(getTwoSampleMessage());
        assertTrue("Message must be posted successfully before subscriber can fetch it", publisherResponse
                .getResponseCode() == 200);

        // Now fetch messages from DMaaP
        DMaaPMRSubscriberResponse subscriberResponse = dMaaPMRSubscriber.fetchMessages();
        List<String> messageList = new LinkedList<>();
        for (String message : subscriberResponse.getFetchedMessages()) {
            messageList.add(message);
        }
        assertTrue("Subscriber message count must be 2", messageList.size() == 2);
    }

}
