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

package org.onap.dcae.apod.analytics.it.dmaap;

import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.onap.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;

import java.util.Map;

/**
 * Creates DMaaP MR Publisher and Subscriber Instances for Integration Testing purposes
 * <p>
 * @author Rajiv Singla . Creation Date: 2/1/2017.
 */
public interface DMaaPMRCreator {

    /**
     * Provides {@link DMaaPMRSubscriber} instance for Integration testing configured for the specific test
     * environment
     *
     * @return DMaaP MR Subscriber instance for integration testing
     */
    DMaaPMRSubscriber getDMaaPMRSubscriber();


    /**
     * Provides {@link DMaaPMRSubscriber} instance for Integration testing configured with given topic name
     *
     * @param subscriberTopicName DMaaP MR Subscriber Topic Name
     * @return DMaaP MR Subscriber instance which is subscriber to given subscriber topic
     */
    DMaaPMRSubscriber getDMaaPMRSubscriberWithTopicName(String subscriberTopicName);

    /**
     * Provides {@link DMaaPMRPublisher} instance for Integration testing configured for the specific test
     * environment
     *
     * @return DMaaP MR Publisher instance for integration testing
     */
    DMaaPMRPublisher getDMaaPMRPublisher();


    /**
     * Provides {@link DMaaPMRPublisher} instance for Integration testing configured with given topic name
     *
     * @param publisherTopicName DMaaP MR publisher topic name
     * @return DMaaP MR Publisher instance for integration testing
     */
    DMaaPMRPublisher getDMaaPMRPublisherWithTopicName(String publisherTopicName);

    /**
     * Provides a map of DMaaP subscriber config for Integration testing configured with given topic name
     *
     * @return Map of key-value pair of subscriber config
     */
    Map<String, String> getDMaaPMRSubscriberConfig();

    /**
     * Provides a map of DMaaP publisher config for Integration testing configured with given topic name
     *
     * @return Map of key-value pair of publisher config
     */
    Map<String, String> getDMaaPMRPublisherConfig();

}
