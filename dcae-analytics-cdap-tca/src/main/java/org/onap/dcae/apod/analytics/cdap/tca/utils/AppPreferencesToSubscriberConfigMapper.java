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

package org.onap.dcae.apod.analytics.cdap.tca.utils;

import com.google.common.base.Function;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.onap.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;

import javax.annotation.Nonnull;

import static org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isPresent;


/**
 * Function which translates {@link TCAAppPreferences} to {@link DMaaPMRSubscriberConfig}
 *
 * @author Rajiv Singla . Creation Date: 11/17/2016.
 */
public class AppPreferencesToSubscriberConfigMapper implements Function<TCAAppPreferences, DMaaPMRSubscriberConfig> {

    /**
     * Factory Method to converts {@link TCAAppPreferences} to {@link DMaaPMRSubscriberConfig} object
     *
     * @param tcaAppPreferences tca app preferences
     * @return DMaaP Subscriber Config
     */
    public static DMaaPMRSubscriberConfig map(final TCAAppPreferences tcaAppPreferences) {
        return new AppPreferencesToSubscriberConfigMapper().apply(tcaAppPreferences);
    }

    /**
     * Implementation to convert {@link TCAAppPreferences} to {@link DMaaPMRSubscriberConfig} object
     *
     * @param tcaAppPreferences tca app preferences
     *
     * @return DMaaP Subscriber Config
     */
    @Nonnull
    @Override
    public DMaaPMRSubscriberConfig apply(@Nonnull TCAAppPreferences tcaAppPreferences) {

        // Create a new subscriber settings builder
        final DMaaPMRSubscriberConfig.Builder subscriberConfigBuilder = new DMaaPMRSubscriberConfig.Builder(
                tcaAppPreferences.getSubscriberHostName(), tcaAppPreferences.getSubscriberTopicName());

        // Setup up any optional subscriber parameters if they are present
        final Integer subscriberHostPortNumber = tcaAppPreferences.getSubscriberHostPort();
        if (subscriberHostPortNumber != null) {
            subscriberConfigBuilder.setPortNumber(subscriberHostPortNumber);
        }

        final String subscriberProtocol = tcaAppPreferences.getSubscriberProtocol();
        if (isPresent(subscriberProtocol)) {
            subscriberConfigBuilder.setProtocol(subscriberProtocol);
        }

        final String subscriberUserName = tcaAppPreferences.getSubscriberUserName();
        if (isPresent(subscriberUserName)) {
            subscriberConfigBuilder.setUserName(subscriberUserName);
        }

        final String subscriberUserPassword = tcaAppPreferences.getSubscriberUserPassword();
        if (isPresent(subscriberUserPassword)) {
            subscriberConfigBuilder.setUserPassword(subscriberUserPassword);
        }

        final String subscriberContentType = tcaAppPreferences.getSubscriberContentType();
        if (isPresent(subscriberContentType)) {
            subscriberConfigBuilder.setContentType(subscriberContentType);
        }

        final String subscriberConsumerId = tcaAppPreferences.getSubscriberConsumerId();
        if (isPresent(subscriberConsumerId)) {
            subscriberConfigBuilder.setConsumerId(subscriberConsumerId);
        }

        final String subscriberConsumerGroup = tcaAppPreferences.getSubscriberConsumerGroup();
        if (isPresent(subscriberConsumerGroup)) {
            subscriberConfigBuilder.setConsumerGroup(subscriberConsumerGroup);
        }

        final Integer subscriberTimeoutMS = tcaAppPreferences.getSubscriberTimeoutMS();
        if (subscriberTimeoutMS != null) {
            subscriberConfigBuilder.setTimeoutMS(subscriberTimeoutMS);
        }
        final Integer subscriberMessageLimit = tcaAppPreferences.getSubscriberMessageLimit();
        if (subscriberMessageLimit != null) {
            subscriberConfigBuilder.setMessageLimit(subscriberMessageLimit);
        }

        // return Subscriber settings
        return subscriberConfigBuilder.build();

    }
}
