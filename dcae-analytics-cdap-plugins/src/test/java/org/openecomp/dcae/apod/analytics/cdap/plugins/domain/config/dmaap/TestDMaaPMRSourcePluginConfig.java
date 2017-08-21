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
 * Test {@link DMaaPMRSourcePluginConfig} for testing purposes only
 * <p>
 * @author Rajiv Singla . Creation Date: 1/23/2017.
 */
public class TestDMaaPMRSourcePluginConfig extends DMaaPMRSourcePluginConfig {

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

    public void setPollingInterval(Integer pollingInterval) {
        this.pollingInterval = pollingInterval;
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

    public void setConsumerId(@Nullable String consumerId) {
        this.consumerId = consumerId;
    }

    public void setConsumerGroup(@Nullable String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public void setTimeoutMS(@Nullable Integer timeoutMS) {
        this.timeoutMS = timeoutMS;
    }

    public void setMessageLimit(@Nullable Integer messageLimit) {
        this.messageLimit = messageLimit;
    }

}
