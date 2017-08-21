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

package org.openecomp.dcae.apod.analytics.cdap.plugins.batch.sink.dmaap;

import co.cask.cdap.api.data.batch.OutputFormatProvider;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields;
import org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSinkPluginConfig;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DMaaP MR Output Format Provider used to create Batch Sink Plugin
 * <p>
 * @author Rajiv Singla . Creation Date: 1/27/2017.
 */
public class DMaaPMROutputFormatProvider implements OutputFormatProvider {

    private final Map<String, String> sinkConfig;


    public DMaaPMROutputFormatProvider(DMaaPMRSinkPluginConfig sinkPluginConfig) {

        // initialize Sink Config - with DMaaP MR Publisher config values
        sinkConfig = new LinkedHashMap<>();

        // Required fields for sink config
        sinkConfig.put(DMaaPMRSinkHadoopConfigFields.HOST_NAME, sinkPluginConfig.getHostName());
        sinkConfig.put(DMaaPMRSinkHadoopConfigFields.TOPIC_NAME, sinkPluginConfig.getTopicName());

        final Integer configPortNumber = sinkPluginConfig.getPortNumber();
        if (configPortNumber != null) {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.PORT_NUMBER, configPortNumber.toString());
        } else {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.PORT_NUMBER,
                    AnalyticsConstants.DEFAULT_PORT_NUMBER.toString());
        }

        final String configProtocol = sinkPluginConfig.getProtocol();
        if (ValidationUtils.isPresent(configProtocol)) {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.PROTOCOL, configProtocol);
        } else {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.PROTOCOL, AnalyticsConstants.DEFAULT_PROTOCOL);
        }


        final String configUserName = sinkPluginConfig.getUserName();
        if (ValidationUtils.isPresent(configUserName)) {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.USER_NAME, configUserName);
        } else {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.USER_NAME, AnalyticsConstants.DEFAULT_USER_NAME);
        }

        final String configUserPass = sinkPluginConfig.getUserPassword();
        if (ValidationUtils.isPresent(configUserPass)) {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.USER_PASS, configUserPass);
        } else {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.USER_PASS, AnalyticsConstants.DEFAULT_USER_PASSWORD);
        }

        final String configContentType = sinkPluginConfig.getContentType();
        if (ValidationUtils.isPresent(configContentType)) {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.CONTENT_TYPE, configContentType);
        } else {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.CONTENT_TYPE, AnalyticsConstants.DEFAULT_CONTENT_TYPE);
        }


        final Integer configMaxBatchSize = sinkPluginConfig.getMaxBatchSize();
        if (configMaxBatchSize != null) {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.MAX_BATCH_SIZE, configMaxBatchSize.toString());
        } else {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.MAX_BATCH_SIZE,
                    String.valueOf(AnalyticsConstants.DEFAULT_PUBLISHER_MAX_BATCH_SIZE));
        }

        final Integer configMaxRecoveryQueueSize = sinkPluginConfig.getMaxRecoveryQueueSize();
        if (configMaxRecoveryQueueSize != null) {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.MAX_RECOVER_QUEUE_SIZE, configMaxRecoveryQueueSize.toString());
        } else {
            sinkConfig.put(DMaaPMRSinkHadoopConfigFields.MAX_RECOVER_QUEUE_SIZE,
                    String.valueOf(AnalyticsConstants.DEFAULT_PUBLISHER_MAX_RECOVERY_QUEUE_SIZE));
        }

    }

    @Override
    public String getOutputFormatClassName() {
        return DMaaPMROutputFormat.class.getName();
    }

    @Override
    public Map<String, String> getOutputFormatConfiguration() {
        return sinkConfig;
    }
}
