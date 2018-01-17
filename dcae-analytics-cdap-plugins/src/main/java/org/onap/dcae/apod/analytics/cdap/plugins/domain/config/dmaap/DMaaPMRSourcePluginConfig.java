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

package org.onap.dcae.apod.analytics.cdap.plugins.domain.config.dmaap;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import com.google.common.base.Objects;

import javax.annotation.Nullable;

/**
 * DMaaP MR Subscriber Config
 * <p>
 * @author Rajiv Singla . Creation Date: 1/17/2017.
 */
public class DMaaPMRSourcePluginConfig extends BaseDMaaPMRPluginConfig {

    private static final long serialVersionUID = 1L;

    @Description("DMaaP MR Polling Interval in MS")
    @Macro
    protected Integer pollingInterval;

    @Description("DMaaP Message Router Subscriber Consumer ID. Defaults to some randomly created userID")
    @Nullable
    @Macro
    protected String consumerId;

    @Description("DMaaP Message Router Subscriber Consumer Group. Defaults to some randomly created user Group")
    @Nullable
    @Macro
    protected String consumerGroup;

    @Description("DMaaP Message Router Subscriber Timeout in MS. Defaults to no timeout")
    @Nullable
    @Macro
    protected Integer timeoutMS;

    @Description("DMaaP Message Router Subscriber Message Limit. Defaults to no message limit")
    @Nullable
    @Macro
    protected Integer messageLimit;

    // Required No Arg constructor
    public DMaaPMRSourcePluginConfig() {
        this(null, null, null, 0);
    }

    public DMaaPMRSourcePluginConfig(String referenceName, String hostName, String topicName, Integer pollingInterval) {
        super(referenceName, hostName, topicName);
        this.pollingInterval = pollingInterval;
    }

    /**
     * DMaaP MR Subscriber Polling interval
     *
     * @return DMaaP MR Subscriber Polling interval
     */
    public Integer getPollingInterval() {
        return pollingInterval;
    }

    /**
     * DMaaP MR Subscriber Consumer ID
     *
     * @return DMaaP MR Subscriber Consumer ID
     */
    @Nullable
    public String getConsumerId() {
        return consumerId;
    }

    /**
     * DMaaP MR Subscriber Consumer Group
     *
     * @return DMaaP MR Subscriber Consumer Group
     */
    @Nullable
    public String getConsumerGroup() {
        return consumerGroup;
    }

    /**
     * DMaaP MR Subscriber Timeout in MS
     *
     * @return DMaaP MR Subscriber Timeout in MS
     */
    @Nullable
    public Integer getTimeoutMS() {
        return timeoutMS;
    }

    /**
     * DMaaP MR Subscriber message limit
     *
     * @return DMaaP MR Subscriber Message limit
     */
    @Nullable
    public Integer getMessageLimit() {
        return messageLimit;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("pollingInterval", pollingInterval)
                .add("consumerId", consumerId)
                .add("consumerGroup", consumerGroup)
                .add("timeoutMS", timeoutMS)
                .add("messageLimit", messageLimit)
                .toString();
    }

}
