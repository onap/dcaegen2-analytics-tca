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

package org.onap.dcae.apod.analytics.it.cucumber.steps;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.onap.dcae.apod.analytics.common.utils.HTTPUtils;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.onap.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.onap.dcae.apod.analytics.it.dmaap.DMaaPMRCreator;
import org.onap.dcae.apod.analytics.it.util.StepUtils;
import org.onap.dcae.apod.analytics.test.BaseDCAEAnalyticsIT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 2/1/2017.
 */
public class DMaaPMRSteps extends BaseDCAEAnalyticsIT {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRSteps.class);

    private final DMaaPMRCreator dMaaPMRCreator;
    private final String defaultPublisherTopicName;

    private static DMaaPMRSubscriberResponse subscriberResponse;
    private static String messageToPublish;
    private static String fetchedMessage;

    @Inject
    public DMaaPMRSteps(DMaaPMRCreator dMaaPMRCreator,
                        @Named("dmaap.mr.publisher.topicName") String defaultPublisherTopicName) {
        this.dMaaPMRCreator = dMaaPMRCreator;
        this.defaultPublisherTopicName = defaultPublisherTopicName;
    }


    @Given("^DMaaP MR Service is up$")
    public void dmaapMRServiceIsUp() throws Throwable {
        final DMaaPMRSubscriber subscriber =
                dMaaPMRCreator.getDMaaPMRSubscriberWithTopicName(defaultPublisherTopicName);
        final DMaaPMRSubscriberResponse subscriberResponse = subscriber.fetchMessages();
        assertNotNull(subscriberResponse.getResponseCode());
        assertTrue(HTTPUtils.isSuccessfulResponseCode(subscriberResponse.getResponseCode()));
        LOG.info("Subscriber is able to fetch messages successfully - Verified DMaaP MR Service is UP");
    }

    @When("^I publish json message to publisher topic name \"([^\"]*)\" in file \"([^\"]*)\"$")
    public void iPublishJsonMessageToPublisherTopicNameInFile(String publisherTopicName, String fileLocation)
            throws Throwable {
        String publisherTopic;
        if (StepUtils.isDefaultPublisherTopic(publisherTopicName)) {
            publisherTopic = defaultPublisherTopicName;
        } else {
            publisherTopic = publisherTopicName;
        }
        final DMaaPMRPublisher publisher = dMaaPMRCreator.getDMaaPMRPublisherWithTopicName(publisherTopic);
        messageToPublish = fromStream(fileLocation);
        final DMaaPMRPublisherResponse publisherResponse = publisher.publish(Arrays.asList(messageToPublish));
        LOG.info("Publisher published messages to DMaaP MR Topic - Response: {}", publisherResponse);
        assertTrue(HTTPUtils.isSuccessfulResponseCode(publisherResponse.getResponseCode()));
    }

    @And("^wait for \"([^\"]*)\" seconds$")
    public void waitForSeconds(Integer waitInSeconds) throws Throwable {
        TimeUnit.SECONDS.sleep(waitInSeconds);
        LOG.info("Waking up after sleep: {} seconds", waitInSeconds);
    }

    @And("^subscriber fetch message from publisher topic name \"([^\"]*)\"$")
    public void fetchMessageFrom(String publisherTopicName) throws Throwable {
        String publisherTopic;
        if (StepUtils.isDefaultPublisherTopic(publisherTopicName)) {
            publisherTopic = defaultPublisherTopicName;
        } else {
            publisherTopic = publisherTopicName;
        }
        final DMaaPMRSubscriber subscriber = dMaaPMRCreator.getDMaaPMRSubscriberWithTopicName(publisherTopic);
        subscriberResponse = subscriber.fetchMessages();
        LOG.info("Subscriber fetched messages to DMaaP MR Topic - Response: {}", subscriberResponse);
        assertTrue(HTTPUtils.isSuccessfulResponseCode(subscriberResponse.getResponseCode()));
    }

    @And("^compare fetched json message with published message$")
    public void compareFetchedJsonMessageWithPublishedMessage() throws Throwable {

        fetchedMessage = subscriberResponse.getFetchedMessages().get(0);
        LOG.info("Fetched Json Message: {}", fetchedMessage);
        LOG.info("Published Json Message: {}", messageToPublish);
    }

    @Then("^fetched message must be same as published message$")
    public void fetchedMessageMustBeSameAsPublishedMessage() throws Throwable {
        assertJson(messageToPublish, fetchedMessage);
    }



}
