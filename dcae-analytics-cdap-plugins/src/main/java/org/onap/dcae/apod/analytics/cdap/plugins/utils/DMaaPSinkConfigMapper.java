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

package org.onap.dcae.apod.analytics.cdap.plugins.utils;

import com.google.common.base.Function;
import org.apache.hadoop.conf.Configuration;
import org.onap.dcae.apod.analytics.cdap.common.CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields;
import org.onap.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;

import javax.annotation.Nonnull;

import static org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isEmpty;
import static org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isPresent;

/**
 * Function that converts {@link Configuration} to {@link DMaaPMRPublisherConfig}
 * <p>
 * @author Rajiv Singla . Creation Date: 1/26/2017.
 */
public class DMaaPSinkConfigMapper implements Function<Configuration, DMaaPMRPublisherConfig> {

    /**
     * Static method to map {@link Configuration} to {@link DMaaPMRPublisherConfig}
     *
     * @param sinkPluginConfig DMaaP Sink Plugin Config
     *
     * @return DMaaP MR Publisher Config
     */
    public static DMaaPMRPublisherConfig map(final Configuration sinkPluginConfig) {
        return new DMaaPSinkConfigMapper().apply(sinkPluginConfig);
    }

    /**
     * Converts {@link Configuration} to {@link DMaaPMRPublisherConfig}
     *
     * @param configuration Hadoop Configuration containing DMaaP MR Sink field values
     *
     * @return DMaaP MR Publisher Config
     */
    @Nonnull
    @Override
    public DMaaPMRPublisherConfig apply(@Nonnull Configuration configuration) {

        // Create a new publisher settings builder
        final String hostName = configuration.get(DMaaPMRSinkHadoopConfigFields.HOST_NAME);
        final String topicName = configuration.get(DMaaPMRSinkHadoopConfigFields.TOPIC_NAME);

        if (isEmpty(hostName) || isEmpty(topicName)) {
            throw new IllegalStateException("DMaaP MR Sink Host Name and Topic Name must be present");
        }

        final DMaaPMRPublisherConfig.Builder publisherConfigBuilder =
                new DMaaPMRPublisherConfig.Builder(hostName, topicName);

        // Setup up any optional publisher parameters if they are present
        final String portNumber = configuration.get(DMaaPMRSinkHadoopConfigFields.PORT_NUMBER);
        if (portNumber != null) {
            publisherConfigBuilder.setPortNumber(Integer.parseInt(portNumber));
        }

        final String protocol = configuration.get(DMaaPMRSinkHadoopConfigFields.PROTOCOL);
        if (isPresent(protocol)) {
            publisherConfigBuilder.setProtocol(protocol);
        }

        final String userName = configuration.get(DMaaPMRSinkHadoopConfigFields.USER_NAME);
        if (isPresent(userName)) {
            publisherConfigBuilder.setUserName(userName);
        }

        final String userPassword = configuration.get(DMaaPMRSinkHadoopConfigFields.USER_PASS);
        if (isPresent(userPassword)) {
            publisherConfigBuilder.setUserPassword(userPassword);
        }

        final String contentType = configuration.get(DMaaPMRSinkHadoopConfigFields.CONTENT_TYPE);
        if (isPresent(contentType)) {
            publisherConfigBuilder.setContentType(contentType);
        }

        final String maxBatchSize = configuration.get(DMaaPMRSinkHadoopConfigFields.MAX_BATCH_SIZE);
        if (maxBatchSize != null) {
            publisherConfigBuilder.setMaxBatchSize(Integer.parseInt(maxBatchSize));
        }

        final String maxRecoveryQueueSize = configuration.get(DMaaPMRSinkHadoopConfigFields.MAX_RECOVER_QUEUE_SIZE);
        if (maxRecoveryQueueSize != null) {
            publisherConfigBuilder.setMaxRecoveryQueueSize(Integer.parseInt(maxRecoveryQueueSize));
        }

        return publisherConfigBuilder.build();

    }
}
