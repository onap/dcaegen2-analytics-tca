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

package org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap;

import javax.annotation.Nullable;

/**
 * Test {@link DMaaPMRSinkPluginConfig} for testing purposes only
 * <p>
 * @author Rajiv Singla . Creation Date: 1/23/2017.
 */
public class TestDMaaPMRSinkPluginConfig extends DMaaPMRSinkPluginConfig {

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setPortNumber(@Nullable Integer portNumber) {
        this.portNumber = portNumber;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setProtocol(@Nullable String protocol) {
        this.protocol = protocol;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    public void setUserPassword(@Nullable String userPassword) {
        this.userPassword = userPassword;
    }

    public void setContentType(@Nullable String contentType) {
        this.contentType = contentType;
    }

    public void setMaxBatchSize(@Nullable Integer maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }

    public void setMaxRecoveryQueueSize(@Nullable Integer maxRecoveryQueueSize) {
        this.maxRecoveryQueueSize = maxRecoveryQueueSize;
    }

    public void setMessageColumnName(String messageColumnName) {
        this.messageColumnName = messageColumnName;
    }

}
