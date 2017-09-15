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

import com.google.common.base.Objects;
import org.openecomp.dcae.apod.analytics.cdap.common.settings.CDAPAppPreferences;

import static org.openecomp.dcae.apod.analytics.common.AnalyticsConstants.TCA_DEFAULT_PUBLISHER_MAX_BATCH_QUEUE_SIZE;
import static org.openecomp.dcae.apod.analytics.common.AnalyticsConstants.TCA_DEFAULT_PUBLISHER_MAX_RECOVERY_QUEUE_SIZE;
import static org.openecomp.dcae.apod.analytics.common.AnalyticsConstants.TCA_DEFAULT_PUBLISHER_POLLING_INTERVAL_MS;
import static org.openecomp.dcae.apod.analytics.common.AnalyticsConstants.TCA_DEFAULT_SUBSCRIBER_POLLING_INTERVAL_MS;

/**
 * <p>
 *     App Preferences for Analytics TCA (Threshold Crossing Alert) App
 * <p>
 * @author Rajiv Singla . Creation Date: 10/4/2016.
 */
public class TCAAppPreferences implements CDAPAppPreferences {

    private static final long serialVersionUID = 1L;

    // subscriber preferences
    protected String subscriberHostName;

    protected Integer subscriberHostPort;

    protected String subscriberTopicName;

    protected String subscriberProtocol;

    protected String subscriberUserName;

    protected String subscriberUserPassword;

    protected String subscriberContentType;

    protected String subscriberConsumerId;

    protected String subscriberConsumerGroup;

    protected Integer subscriberTimeoutMS;

    protected Integer subscriberMessageLimit;

    protected Integer subscriberPollingInterval;

    // publisher preferences
    protected String publisherHostName;

    protected Integer publisherHostPort;

    protected String publisherTopicName;

    protected String publisherProtocol;

    protected String publisherUserName;

    protected String publisherUserPassword;

    protected String publisherContentType;

    protected Integer publisherMaxBatchSize;

    protected Integer publisherMaxRecoveryQueueSize;

    protected Integer publisherPollingInterval;

    protected Boolean enableAlertCEFFormat;

    /**
     * Default constructor to setup default values for TCA App Preferences
     */
    public TCAAppPreferences() {

        // subscriber defaults
        subscriberPollingInterval = TCA_DEFAULT_SUBSCRIBER_POLLING_INTERVAL_MS;

        // publisher defaults
        publisherMaxBatchSize = TCA_DEFAULT_PUBLISHER_MAX_BATCH_QUEUE_SIZE;
        publisherMaxRecoveryQueueSize = TCA_DEFAULT_PUBLISHER_MAX_RECOVERY_QUEUE_SIZE;
        publisherPollingInterval = TCA_DEFAULT_PUBLISHER_POLLING_INTERVAL_MS;

        enableAlertCEFFormat = false;

    }

    public String getSubscriberHostName() {
        return subscriberHostName;
    }

    public Integer getSubscriberHostPort() {
        return subscriberHostPort;
    }

    public String getSubscriberTopicName() {
        return subscriberTopicName;
    }

    public String getSubscriberProtocol() {
        return subscriberProtocol;
    }

    public String getSubscriberUserName() {
        return subscriberUserName;
    }

    public String getSubscriberUserPassword() {
        return subscriberUserPassword;
    }

    public String getSubscriberContentType() {
        return subscriberContentType;
    }

    public String getSubscriberConsumerId() {
        return subscriberConsumerId;
    }

    public String getSubscriberConsumerGroup() {
        return subscriberConsumerGroup;
    }

    public Integer getSubscriberTimeoutMS() {
        return subscriberTimeoutMS;
    }

    public Integer getSubscriberMessageLimit() {
        return subscriberMessageLimit;
    }

    public Integer getSubscriberPollingInterval() {
        return subscriberPollingInterval;
    }

    public String getPublisherHostName() {
        return publisherHostName;
    }

    public Integer getPublisherHostPort() {
        return publisherHostPort;
    }

    public String getPublisherTopicName() {
        return publisherTopicName;
    }

    public String getPublisherProtocol() {
        return publisherProtocol;
    }

    public String getPublisherUserName() {
        return publisherUserName;
    }

    public String getPublisherUserPassword() {
        return publisherUserPassword;
    }

    public String getPublisherContentType() {
        return publisherContentType;
    }

    public Integer getPublisherMaxBatchSize() {
        return publisherMaxBatchSize;
    }

    public Integer getPublisherMaxRecoveryQueueSize() {
        return publisherMaxRecoveryQueueSize;
    }

    public Integer getPublisherPollingInterval() {
        return publisherPollingInterval;
    }

    public Boolean getEnableAlertCEFFormat() {
        return enableAlertCEFFormat;
    }


    public void setSubscriberHostName(String subscriberHostName) {
        this.subscriberHostName = subscriberHostName;
    }

    public void setSubscriberHostPort(Integer subscriberHostPort) {
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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("subscriberHostName", subscriberHostName)
                .add("subscriberHostPort", subscriberHostPort)
                .add("subscriberTopicName", subscriberTopicName)
                .add("subscriberProtocol", subscriberProtocol)
                .add("subscriberUserName", subscriberUserName)
                .add("subscriberContentType", subscriberContentType)
                .add("subscriberConsumerId", subscriberConsumerId)
                .add("subscriberConsumerGroup", subscriberConsumerGroup)
                .add("subscriberTimeoutMS", subscriberTimeoutMS)
                .add("subscriberMessageLimit", subscriberMessageLimit)
                .add("subscriberPollingInterval", subscriberPollingInterval)
                .add("publisherHostName", publisherHostName)
                .add("publisherHostPort", publisherHostPort)
                .add("publisherTopicName", publisherTopicName)
                .add("publisherProtocol", publisherProtocol)
                .add("publisherUserName", publisherUserName)
                .add("publisherContentType", publisherContentType)
                .add("publisherMaxBatchSize", publisherMaxBatchSize)
                .add("publisherMaxRecoveryQueueSize", publisherMaxRecoveryQueueSize)
                .add("publisherPollingInterval", publisherPollingInterval)
                .toString();
    }
}
