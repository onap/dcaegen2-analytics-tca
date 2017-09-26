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
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;

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


    // A&AI Enrichment

    protected Boolean enableAAIEnrichment;

    protected String aaiEnrichmentHost;

    protected Integer aaiEnrichmentPortNumber;

    protected String aaiEnrichmentProtocol;

    protected String aaiEnrichmentUserName;

    protected String aaiEnrichmentUserPassword;

    protected Boolean aaiEnrichmentIgnoreSSLCertificateErrors;

    protected String aaiVNFEnrichmentAPIPath;

    protected String aaiVMEnrichmentAPIPath;


    // A&AI Enrichment Proxy

    protected String aaiEnrichmentProxyURL;

    /**
     * Default constructor to setup default values for TCA App Preferences
     */
    public TCAAppPreferences() {

        // subscriber defaults
        subscriberPollingInterval = AnalyticsConstants.TCA_DEFAULT_SUBSCRIBER_POLLING_INTERVAL_MS;

        // publisher defaults
        publisherMaxBatchSize = AnalyticsConstants.TCA_DEFAULT_PUBLISHER_MAX_BATCH_QUEUE_SIZE;
        publisherMaxRecoveryQueueSize = AnalyticsConstants.TCA_DEFAULT_PUBLISHER_MAX_RECOVERY_QUEUE_SIZE;
        publisherPollingInterval = AnalyticsConstants.TCA_DEFAULT_PUBLISHER_POLLING_INTERVAL_MS;

        enableAlertCEFFormat = AnalyticsConstants.TCA_DEFAULT_ENABLE_CEF_FORMATTED_ALERT;

        enableAAIEnrichment = AnalyticsConstants.TCA_DEFAULT_ENABLE_AAI_ENRICHMENT;
        aaiEnrichmentIgnoreSSLCertificateErrors =
                AnalyticsConstants.TCA_DEFAULT_AAI_ENRICHMENT_IGNORE_SSL_CERTIFICATE_ERRORS;
        aaiEnrichmentProxyURL = AnalyticsConstants.TCA_DEFAULT_AAI_ENRICHMENT_PROXY_URL;

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

    public Boolean getEnableAAIEnrichment() {
        return enableAAIEnrichment;
    }

    public String getAaiEnrichmentHost() {
        return aaiEnrichmentHost;
    }

    public Integer getAaiEnrichmentPortNumber() {
        return aaiEnrichmentPortNumber;
    }

    public String getAaiEnrichmentProtocol() {
        return aaiEnrichmentProtocol;
    }

    public String getAaiEnrichmentUserName() {
        return aaiEnrichmentUserName;
    }

    public String getAaiEnrichmentUserPassword() {
        return aaiEnrichmentUserPassword;
    }

    public Boolean getAaiEnrichmentIgnoreSSLCertificateErrors() {
        return aaiEnrichmentIgnoreSSLCertificateErrors;
    }

    public String getAaiVNFEnrichmentAPIPath() {
        return aaiVNFEnrichmentAPIPath;
    }

    public String getAaiVMEnrichmentAPIPath() {
        return aaiVMEnrichmentAPIPath;
    }

    public String getAaiEnrichmentProxyURL() {
        return aaiEnrichmentProxyURL;
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
                .add("enableAlertCEFFormat", enableAlertCEFFormat)
                .add("enableAAIEnrichment", enableAAIEnrichment)
                .add("aaiEnrichmentHost", aaiEnrichmentHost)
                .add("aaiEnrichmentPortNumber", aaiEnrichmentPortNumber)
                .add("aaiEnrichmentProtocol", aaiEnrichmentProtocol)
                .add("aaiEnrichmentUserName", aaiEnrichmentUserName)
                .add("aaiEnrichmentIgnoreSSLCertificateErrors", aaiEnrichmentIgnoreSSLCertificateErrors)
                .add("aaiVNFEnrichmentAPIPath", aaiVNFEnrichmentAPIPath)
                .add("aaiVMEnrichmentAPIPath", aaiVMEnrichmentAPIPath)
                .add("aaiEnrichmentProxyEnabled", aaiEnrichmentProxyURL == null ? "false" : "true")
                .toString();
    }
}
