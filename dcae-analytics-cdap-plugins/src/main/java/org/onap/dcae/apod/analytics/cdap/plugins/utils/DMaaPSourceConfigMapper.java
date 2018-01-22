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
import org.onap.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;
import org.onap.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;

import javax.annotation.Nonnull;

import static org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isEmpty;
import static org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isPresent;

/**
 * Function that converts {@link DMaaPMRSourcePluginConfig} to {@link DMaaPMRSubscriberConfig}
 * <p>
 * @author Rajiv Singla . Creation Date: 1/18/2017.
 */
public class DMaaPSourceConfigMapper implements Function<DMaaPMRSourcePluginConfig, DMaaPMRSubscriberConfig> {

    /**
     * Static factory method to map {@link DMaaPMRSourcePluginConfig} to {@link DMaaPMRSubscriberConfig}
     *
     * @param pluginConfig DMaaP MR Souce Plugin Config
     *
     * @return DMaaP MR Subscriber Config
     */
    public static DMaaPMRSubscriberConfig map(final DMaaPMRSourcePluginConfig pluginConfig) {
        return new DMaaPSourceConfigMapper().apply(pluginConfig);
    }

    /**
     * Converts {@link DMaaPMRSourcePluginConfig} to {@link DMaaPMRSubscriberConfig} object
     *
     * @param sourcePluginConfig DMaaP MR Source Plugin Config
     *
     * @return DMaaP MR Subscriber Config
     */
    @Nonnull
    @Override
    public DMaaPMRSubscriberConfig apply(@Nonnull DMaaPMRSourcePluginConfig sourcePluginConfig) {

        // Create a new subscriber settings builder
        final String hostName = sourcePluginConfig.getHostName();
        final String topicName = sourcePluginConfig.getTopicName();
        if (isEmpty(hostName) || isEmpty(topicName)) {
            throw new IllegalStateException("DMaaP MR Source Host Name and Topic Name must be present");
        }
        final DMaaPMRSubscriberConfig.Builder subscriberConfigBuilder = new DMaaPMRSubscriberConfig.Builder(
                hostName, topicName);

        // Setup up any optional subscriber parameters if they are present
        final Integer subscriberHostPortNumber = sourcePluginConfig.getPortNumber();
        if (subscriberHostPortNumber != null) {
            subscriberConfigBuilder.setPortNumber(subscriberHostPortNumber);
        }

        final String subscriberProtocol = sourcePluginConfig.getProtocol();
        if (isPresent(subscriberProtocol)) {
            subscriberConfigBuilder.setProtocol(subscriberProtocol);
        }

        final String subscriberUserName = sourcePluginConfig.getUserName();
        if (isPresent(subscriberUserName)) {
            subscriberConfigBuilder.setUserName(subscriberUserName);
        }

        final String subscriberUserPassword = sourcePluginConfig.getUserPassword();
        if (isPresent(subscriberUserPassword)) {
            subscriberConfigBuilder.setUserPassword(subscriberUserPassword);
        }

        final String subscriberContentType = sourcePluginConfig.getContentType();
        if (isPresent(subscriberContentType)) {
            subscriberConfigBuilder.setContentType(subscriberContentType);
        }

        final String subscriberConsumerId = sourcePluginConfig.getConsumerId();
        if (isPresent(subscriberConsumerId)) {
            subscriberConfigBuilder.setConsumerId(subscriberConsumerId);
        }

        final String subscriberConsumerGroup = sourcePluginConfig.getConsumerGroup();
        if (isPresent(subscriberConsumerGroup)) {
            subscriberConfigBuilder.setConsumerGroup(subscriberConsumerGroup);
        }

        final Integer subscriberTimeoutMS = sourcePluginConfig.getTimeoutMS();
        if (subscriberTimeoutMS != null) {
            subscriberConfigBuilder.setTimeoutMS(subscriberTimeoutMS);
        }
        final Integer subscriberMessageLimit = sourcePluginConfig.getMessageLimit();
        if (subscriberMessageLimit != null) {
            subscriberConfigBuilder.setMessageLimit(subscriberMessageLimit);
        }

        // return Subscriber config
        return subscriberConfigBuilder.build();
    }
}
