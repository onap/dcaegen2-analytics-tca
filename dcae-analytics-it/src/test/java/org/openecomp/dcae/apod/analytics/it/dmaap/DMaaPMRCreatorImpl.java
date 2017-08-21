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

package org.openecomp.dcae.apod.analytics.it.dmaap;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.openecomp.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rajiv Singla . Creation Date: 2/1/2017.
 */
public class DMaaPMRCreatorImpl implements DMaaPMRCreator {

    private final String subscriberHostName;

    private final Integer subscriberHostPort;

    private final String subscriberTopicName;

    private final String subscriberProtocol;

    private final String subscriberUserName;

    private final String subscriberUserPassword;

    private final String subscriberContentType;

    private final String subscriberConsumerId;

    private final String subscriberConsumerGroup;

    private final Integer subscriberTimeoutMS;

    private final Integer subscriberMessageLimit;

    private final Integer subscriberPollingInterval;

    // publisher preferences
    private final String publisherHostName;

    private final Integer publisherHostPort;

    private final String publisherTopicName;

    private final String publisherProtocol;

    private final String publisherUserName;

    private final String publisherUserPassword;

    private final String publisherContentType;

    private final Integer publisherPollingInterval;

    private final Integer publisherMaxBatchSize;

    private final Integer publisherMaxRecoveryQueueSize;


    private final DMaaPMRFactory dMaaPMRFactory;

    @Inject
    public DMaaPMRCreatorImpl(@Named("dmaap.mr.subscriber.hostname") String subscriberHostName,
                              @Named("dmaap.mr.subscriber.portNumber") Integer subscriberHostPort,
                              @Named("dmaap.mr.subscriber.topicName") String subscriberTopicName,
                              @Named("dmaap.mr.subscriber.protocol") String subscriberProtocol,
                              @Named("dmaap.mr.subscriber.username") String subscriberUserName,
                              @Named("dmaap.mr.subscriber.userPassword") String subscriberUserPassword,
                              @Named("dmaap.mr.subscriber.contentType") String subscriberContentType,
                              @Named("dmaap.mr.subscriber.consumerId") String subscriberConsumerId,
                              @Named("dmaap.mr.subscriber.consumerGroup") String subscriberConsumerGroup,
                              @Named("dmaap.mr.subscriber.timeoutMS") Integer subscriberTimeoutMS,
                              @Named("dmaap.mr.subscriber.messageLimit") Integer subscriberMessageLimit,
                              @Named("dmaap.mr.subscriber.pollingInterval") Integer subscriberPollingInterval,
                              @Named("dmaap.mr.publisher.hostname") String publisherHostName,
                              @Named("dmaap.mr.publisher.portNumber") Integer publisherHostPort,
                              @Named("dmaap.mr.publisher.topicName") String publisherTopicName,
                              @Named("dmaap.mr.publisher.protocol") String publisherProtocol,
                              @Named("dmaap.mr.publisher.username") String publisherUserName,
                              @Named("dmaap.mr.publisher.userPassword") String publisherUserPassword,
                              @Named("dmaap.mr.publisher.contentType") String publisherContentType,
                              @Named("dmaap.mr.publisher.pollingInterval") Integer publisherPollingInterval,
                              @Named("dmaap.mr.publisher.maxBatchSize") Integer publisherMaxBatchSize,
                              @Named("dmaap.mr.publisher.maxRecoveryQueueSize") Integer publisherMaxRecoveryQueueSize) {
        this.subscriberHostName = subscriberHostName;
        this.subscriberHostPort = subscriberHostPort;
        this.subscriberTopicName = subscriberTopicName;
        this.subscriberProtocol = subscriberProtocol;
        this.subscriberUserName = subscriberUserName;
        this.subscriberUserPassword = subscriberUserPassword;
        this.subscriberContentType = subscriberContentType;
        this.subscriberConsumerId = subscriberConsumerId;
        this.subscriberConsumerGroup = subscriberConsumerGroup;
        this.subscriberTimeoutMS = subscriberTimeoutMS;
        this.subscriberMessageLimit = subscriberMessageLimit;
        this.subscriberPollingInterval = subscriberPollingInterval;
        this.publisherHostName = publisherHostName;
        this.publisherHostPort = publisherHostPort;
        this.publisherTopicName = publisherTopicName;
        this.publisherProtocol = publisherProtocol;
        this.publisherUserName = publisherUserName;
        this.publisherUserPassword = publisherUserPassword;
        this.publisherContentType = publisherContentType;
        this.publisherPollingInterval = publisherPollingInterval;
        this.publisherMaxBatchSize = publisherMaxBatchSize;
        this.publisherMaxRecoveryQueueSize = publisherMaxRecoveryQueueSize;

        this.dMaaPMRFactory = DMaaPMRFactory.create();
    }


    @Override
    public DMaaPMRSubscriber getDMaaPMRSubscriber() {
        final DMaaPMRSubscriberConfig subscriberConfig =
                new DMaaPMRSubscriberConfig.Builder(subscriberHostName, subscriberTopicName)
                        .setPortNumber(subscriberHostPort)
                        .setProtocol(subscriberProtocol)
                        .setUserName(subscriberUserName)
                        .setUserPassword(subscriberUserPassword)
                        .setContentType(subscriberContentType)
                        .setMessageLimit(subscriberMessageLimit)
                        .setTimeoutMS(subscriberTimeoutMS)
                        .setConsumerId(subscriberConsumerId)
                        .setConsumerGroup(subscriberConsumerGroup)
                        .build();
        return dMaaPMRFactory.createSubscriber(subscriberConfig);
    }

    @Override
    public DMaaPMRPublisher getDMaaPMRPublisher() {
        final DMaaPMRPublisherConfig publisherConfig =
                new DMaaPMRPublisherConfig.Builder(publisherHostName, publisherTopicName)
                        .setPortNumber(publisherHostPort)
                        .setProtocol(publisherProtocol)
                        .setUserName(publisherUserName)
                        .setUserPassword(publisherUserPassword)
                        .setContentType(publisherContentType)
                        .setMaxBatchSize(publisherMaxBatchSize)
                        .setMaxRecoveryQueueSize(publisherMaxRecoveryQueueSize)
                        .build();
        return dMaaPMRFactory.createPublisher(publisherConfig);
    }

    @Override
    public DMaaPMRSubscriber getDMaaPMRSubscriberWithTopicName(String subscriberTopicName) {
        final DMaaPMRSubscriberConfig subscriberConfig =
                new DMaaPMRSubscriberConfig.Builder(subscriberHostName, subscriberTopicName)
                        .setPortNumber(subscriberHostPort)
                        .setProtocol(subscriberProtocol)
                        .setUserName(subscriberUserName)
                        .setUserPassword(subscriberUserPassword)
                        .setContentType(subscriberContentType)
                        .setMessageLimit(subscriberMessageLimit)
                        .setTimeoutMS(subscriberTimeoutMS)
                        .setConsumerId(subscriberConsumerId)
                        .setConsumerGroup(subscriberConsumerGroup)
                        .build();
        return dMaaPMRFactory.createSubscriber(subscriberConfig);
    }


    @Override
    public DMaaPMRPublisher getDMaaPMRPublisherWithTopicName(String publisherTopicName) {
        final DMaaPMRPublisherConfig publisherConfig =
                new DMaaPMRPublisherConfig.Builder(publisherHostName, publisherTopicName)
                        .setPortNumber(publisherHostPort)
                        .setProtocol(publisherProtocol)
                        .setUserName(publisherUserName)
                        .setUserPassword(publisherUserPassword)
                        .setContentType(publisherContentType)
                        .setMaxBatchSize(publisherMaxBatchSize)
                        .setMaxRecoveryQueueSize(publisherMaxRecoveryQueueSize)
                        .build();
        return dMaaPMRFactory.createPublisher(publisherConfig);
    }

    @Override
    public Map<String, String> getDMaaPMRSubscriberConfig() {
        Map<String, String> sourceConfigurationMap = new HashMap<>();
        sourceConfigurationMap.put("referenceName", "source-referenceName");
        sourceConfigurationMap.put("hostName", subscriberHostName);
        sourceConfigurationMap.put("portNumber", subscriberHostPort.toString());
        sourceConfigurationMap.put("topicName", subscriberTopicName);
        sourceConfigurationMap.put("pollingInterval", subscriberPollingInterval.toString());
        sourceConfigurationMap.put("protocol", subscriberProtocol);
        sourceConfigurationMap.put("userName", subscriberUserName);
        sourceConfigurationMap.put("userPassword", subscriberUserPassword);
        sourceConfigurationMap.put("contentType", subscriberContentType);
        sourceConfigurationMap.put("consumerId", subscriberConsumerId);
        sourceConfigurationMap.put("consumerGroup", subscriberConsumerGroup);
        sourceConfigurationMap.put("timeoutMS", subscriberTimeoutMS.toString());
        sourceConfigurationMap.put("messageLimit", subscriberMessageLimit.toString());
        return sourceConfigurationMap;
    }

    @Override
    public Map<String, String> getDMaaPMRPublisherConfig() {
        return null;
    }
}
