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

package org.openecomp.dcae.apod.analytics.cdap.tca.utils;

import com.google.common.base.Function;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;

import javax.annotation.Nonnull;

import static org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isPresent;


/**
 * Function which translates {@link TCAAppPreferences} to {@link DMaaPMRPublisherConfig}
 * <p>
 * @author Rajiv Singla . Creation Date: 11/17/2016.
 */
public class AppPreferencesToPublisherConfigMapper implements Function<TCAAppPreferences, DMaaPMRPublisherConfig> {

    /**
     * Factory method to convert {@link TCAAppPreferences} to {@link DMaaPMRPublisherConfig} object
     *
     * @param tcaAppPreferences tca App Preferences
     *
     * @return publisher config object
     */
    public static DMaaPMRPublisherConfig map(final TCAAppPreferences tcaAppPreferences) {
        return new AppPreferencesToPublisherConfigMapper().apply(tcaAppPreferences);
    }

    /**
     * Implementation to convert {@link TCAAppPreferences} to {@link DMaaPMRPublisherConfig} object
     *
     * @param tcaAppPreferences tca App Preferences
     *
     * @return publisher config object
     */
    @Nonnull
    @Override
    public DMaaPMRPublisherConfig apply(@Nonnull TCAAppPreferences tcaAppPreferences) {

        // Create a new publisher settings builder
        final DMaaPMRPublisherConfig.Builder publisherConfigBuilder = new DMaaPMRPublisherConfig.Builder(
                tcaAppPreferences.getPublisherHostName(), tcaAppPreferences.getPublisherTopicName());

        // Setup up any optional publisher parameters if they are present
        final Integer publisherHostPort = tcaAppPreferences.getPublisherHostPort();
        if (publisherHostPort != null) {
            publisherConfigBuilder.setPortNumber(publisherHostPort);
        }
        final String publisherProtocol = tcaAppPreferences.getPublisherProtocol();
        if (isPresent(publisherProtocol)) {
            publisherConfigBuilder.setProtocol(publisherProtocol);
        }
        final String publisherUserName = tcaAppPreferences.getPublisherUserName();
        if (isPresent(publisherUserName)) {
            publisherConfigBuilder.setUserName(publisherUserName);
        }
        final String publisherUserPassword = tcaAppPreferences.getPublisherUserPassword();
        if (isPresent(publisherUserPassword)) {
            publisherConfigBuilder.setUserPassword(publisherUserPassword);
        }
        final String publisherContentType = tcaAppPreferences.getPublisherContentType();
        if (isPresent(publisherContentType)) {
            publisherConfigBuilder.setContentType(publisherContentType);
        }
        final Integer publisherMaxBatchSize = tcaAppPreferences.getPublisherMaxBatchSize();
        if (publisherMaxBatchSize != null) {
            publisherConfigBuilder.setMaxBatchSize(publisherMaxBatchSize);
        }
        final Integer publisherMaxRecoveryQueueSize = tcaAppPreferences.getPublisherMaxRecoveryQueueSize();
        if (publisherMaxRecoveryQueueSize != null) {
            publisherConfigBuilder.setMaxRecoveryQueueSize(publisherMaxRecoveryQueueSize);
        }

        return publisherConfigBuilder.build();
    }
}
