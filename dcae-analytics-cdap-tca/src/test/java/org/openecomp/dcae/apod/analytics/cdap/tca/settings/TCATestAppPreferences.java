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

package org.openecomp.dcae.apod.analytics.cdap.tca.settings;


import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

/**
 * TCA Test App Preferences are used for testing purposes only
 *
 * @author Rajiv Singla . Creation Date: 11/3/2016.
 */
public class TCATestAppPreferences extends TCAAppPreferences {

    private Map<String, String> policyPreferences;

    public TCATestAppPreferences() {
    }

    public TCATestAppPreferences(final Map<String, String> policyPreferences) {
        this.policyPreferences = policyPreferences;
    }

    @JsonAnyGetter
    public Map<String, String> getPolicyPreferences() {
        return policyPreferences;
    }

    public void setSubscriberHostName(String subscriberHostName) {
        this.subscriberHostName = subscriberHostName;
    }

    public void setSubscriberHostPortNumber(Integer subscriberHostPort) {
        this.subscriberHostPort = subscriberHostPort;
    }

    public void setSubscriberTopicName(String subscriberTopicName) {
        this.subscriberTopicName = subscriberTopicName;
    }

    public void setSubscriberProtocol(String subscriberProtocol) {
        this.subscriberProtocol = subscriberProtocol;
    }

    public void setSubscriberUserName(String subscriberUserName) {
        this.subscriberUserName = subscriberUserName;
    }

    public void setSubscriberUserPassword(String subscriberUserPassword) {
        this.subscriberUserPassword = subscriberUserPassword;
    }

    public void setSubscriberContentType(String subscriberContentType) {
        this.subscriberContentType = subscriberContentType;
    }

    public void setSubscriberConsumerId(String subscriberConsumerId) {
        this.subscriberConsumerId = subscriberConsumerId;
    }

    public void setSubscriberConsumerGroup(String subscriberConsumerGroup) {
        this.subscriberConsumerGroup = subscriberConsumerGroup;
    }

    public void setSubscriberTimeoutMS(Integer subscriberTimeoutMS) {
        this.subscriberTimeoutMS = subscriberTimeoutMS;
    }

    public void setSubscriberMessageLimit(Integer subscriberMessageLimit) {
        this.subscriberMessageLimit = subscriberMessageLimit;
    }

    public void setSubscriberPollingInterval(Integer subscriberPollingInterval) {
        this.subscriberPollingInterval = subscriberPollingInterval;
    }

    public void setPublisherHostName(String publisherHostName) {
        this.publisherHostName = publisherHostName;
    }

    public void setPublisherHostPort(Integer publisherHostPort) {
        this.publisherHostPort = publisherHostPort;
    }

    public void setPublisherTopicName(String publisherTopicName) {
        this.publisherTopicName = publisherTopicName;
    }

    public void setPublisherProtocol(String publisherProtocol) {
        this.publisherProtocol = publisherProtocol;
    }

    public void setPublisherUserName(String publisherUserName) {
        this.publisherUserName = publisherUserName;
    }

    public void setPublisherUserPassword(String publisherUserPassword) {
        this.publisherUserPassword = publisherUserPassword;
    }

    public void setPublisherContentType(String publisherContentType) {
        this.publisherContentType = publisherContentType;
    }

    public void setPublisherMaxBatchSize(Integer publisherMaxBatchSize) {
        this.publisherMaxBatchSize = publisherMaxBatchSize;
    }

    public void setPublisherMaxRecoveryQueueSize(Integer publisherMaxRecoveryQueueSize) {
        this.publisherMaxRecoveryQueueSize = publisherMaxRecoveryQueueSize;
    }

    public void setPublisherPollingInterval(Integer publisherPollingInterval) {
        this.publisherPollingInterval = publisherPollingInterval;
    }

    public void setEnableAlertCEFFormat(Boolean enableAlertCEFFormat) {
        this.enableAlertCEFFormat = enableAlertCEFFormat;
    }
}
