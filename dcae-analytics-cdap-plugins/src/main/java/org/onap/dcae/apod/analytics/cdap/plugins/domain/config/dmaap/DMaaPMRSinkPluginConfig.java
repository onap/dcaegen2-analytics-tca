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
 * DMaaP MR Publisher Config
 * <p>
 * @author Rajiv Singla . Creation Date: 1/17/2017.
 */
public class DMaaPMRSinkPluginConfig extends BaseDMaaPMRPluginConfig {

    private static final long serialVersionUID = 1L;

    @Description("Column name of input schema which contains the message that needs to be written to DMaaP MR Topic")
    @Macro
    protected String messageColumnName;

    @Description("DMaaP MR Publisher Max Batch Size. Defaults to no Batch")
    @Nullable
    @Macro
    protected Integer maxBatchSize;

    @Description("DMaaP MR Publisher Recovery Queue Size. Default to 1000K messages which can be buffered in memory " +
            "in case DMaaP MR Publisher is temporarily unavailable")
    @Nullable
    @Macro
    protected Integer maxRecoveryQueueSize;

    // Required No Arg constructor
    public DMaaPMRSinkPluginConfig() {
        this(null, null, null, null);
    }

    public DMaaPMRSinkPluginConfig(String referenceName, String hostName, String topicName, String messageColumnName) {
        super(referenceName, hostName, topicName);
        this.messageColumnName = messageColumnName;
    }

    /**
     * Column name of incoming Schema field that contains the message that needs to published to DMaaP MR Topic
     *
     * @return Column name of incoming schema which contains message that needs to published to DMaaP MR Topic
     */
    public String getMessageColumnName() {
        return messageColumnName;
    }

    /**
     * DMaaP MR Publisher Max Batch Size.
     *
     * @return DMaaP MR Publisher Max Batch Size
     */
    @Nullable
    public Integer getMaxBatchSize() {
        return maxBatchSize;
    }

    /**
     * DMaaP MR Publisher Max Recovery Queue Size
     *
     * @return DMaaP MR Publisher Max Recovery Queue Size
     */
    @Nullable
    public Integer getMaxRecoveryQueueSize() {
        return maxRecoveryQueueSize;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("messageColumnName", messageColumnName)
                .add("maxBatchSize", maxBatchSize)
                .add("maxRecoveryQueueSize", maxRecoveryQueueSize)
                .toString();
    }
}
