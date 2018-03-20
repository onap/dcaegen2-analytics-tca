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

package org.onap.dcae.apod.analytics.cdap.tca.validator;

import org.onap.dcae.apod.analytics.cdap.common.validation.CDAPAppSettingsValidator;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.onap.dcae.apod.analytics.common.validation.GenericValidationResponse;

import static org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isEmpty;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/3/2016.
 */
public class TCAPreferencesValidator implements CDAPAppSettingsValidator<TCAAppPreferences,
        GenericValidationResponse<TCAAppPreferences>> {

    private static final long serialVersionUID = 1L;

    @Override
    public GenericValidationResponse<TCAAppPreferences> validateAppSettings(TCAAppPreferences appPreferences) {

        final GenericValidationResponse<TCAAppPreferences> validationResponse = new GenericValidationResponse<>();

        // subscriber validations
        final String subscriberHostName = appPreferences.getSubscriberHostName();
        if (isEmpty(subscriberHostName)) {
            validationResponse.addErrorMessage("subscriberHostName", "Subscriber host name must be present");
        }
        final String subscriberTopicName = appPreferences.getSubscriberTopicName();
        if (isEmpty(subscriberTopicName)) {
            validationResponse.addErrorMessage("subscriberTopicName", "Subscriber topic name must be present");
        }

        // publisher validations
        final String publisherHostName = appPreferences.getPublisherHostName();
        if (isEmpty(publisherHostName)) {
            validationResponse.addErrorMessage("publisherHostName", "Publisher host name must be present");
        }
        final String publisherTopicName = appPreferences.getPublisherTopicName();
        if (isEmpty(publisherTopicName)) {
            validationResponse.addErrorMessage("publisherTopicName", "Publisher topic name must be present");
        }

        final Boolean enableAAIEnrichment = appPreferences.getEnableAAIEnrichment();

        // if aai enrichment is enabled then do some aai validations
        if (enableAAIEnrichment) {
            final String aaiEnrichmentHost = appPreferences.getAaiEnrichmentHost();
            if (isEmpty(aaiEnrichmentHost)) {
                validationResponse.addErrorMessage("aaiEnrichmentHost", "AAI Enrichment Host must be present");
            }
            final String aaiVMEnrichmentAPIPath = appPreferences.getAaiVMEnrichmentAPIPath();
            if (isEmpty(aaiVMEnrichmentAPIPath)) {
                validationResponse.addErrorMessage("aaiVMEnrichmentAPIPath", "AAI VM Enrichment path must be present");
            }
            final String aaiVNFEnrichmentAPIPath = appPreferences.getAaiVNFEnrichmentAPIPath();
            if (isEmpty(aaiVNFEnrichmentAPIPath)) {
                validationResponse.addErrorMessage("aaiVNFEnrichmentAPIPath", "AAI VNF Enrichment path must be " +
                        "present");
            }
        }

        final Boolean enableRedisCaching = appPreferences.getEnableRedisCaching();

        // if redis distributed caching is enabled then redis Hosts must be provided
        if(enableRedisCaching) {
            final String redisHosts = appPreferences.getRedisHosts();
            if(isEmpty(redisHosts)) {
                validationResponse.addErrorMessage("redisHosts",
                        "Redis Caching is enabled but no redis hosts are provided");
            }
        }

        return validationResponse;
    }
}
