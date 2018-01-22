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

package org.onap.dcae.apod.analytics.cdap.plugins.validator;

import org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils;
import org.onap.dcae.apod.analytics.cdap.common.validation.CDAPAppSettingsValidator;
import org.onap.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.BaseDMaaPMRPluginConfig;
import org.onap.dcae.apod.analytics.common.validation.GenericValidationResponse;

/**
 * Validates plugin config values which are common in DMaaP MR Configs - {@link BaseDMaaPMRPluginConfig}
 * <p>
 * @author Rajiv Singla . Creation Date: 1/23/2017.
 *
 * @param <T> {@link BaseDMaaPMRPluginConfig} Sub classes
 */
public abstract class BaseDMaaPMRPluginConfigValidator<T extends BaseDMaaPMRPluginConfig> implements
        CDAPAppSettingsValidator<T, GenericValidationResponse<T>> {

    private static final long serialVersionUID = 1L;

    /**
     * Validates the {@link BaseDMaaPMRPluginConfig} parameters
     *
     * @param baseDMaaPMRPluginConfig DMaaP MR Plugin Config
     *
     * @return Validation Response containing validation errors if any
     */
    @Override
    public GenericValidationResponse<T> validateAppSettings(final T baseDMaaPMRPluginConfig) {

        final GenericValidationResponse<T> validationResponse = new GenericValidationResponse<>();

        if (ValidationUtils.isEmpty(baseDMaaPMRPluginConfig.getHostName())) {
            validationResponse.addErrorMessage(
                    "hostName",
                    "DMaaPMRPluginConfig - hostname field is undefined: " + baseDMaaPMRPluginConfig);
        }

        if (baseDMaaPMRPluginConfig.getPortNumber() == null) {
            validationResponse.addErrorMessage(
                    "port Number",
                    "DMaaPMRPluginConfig - host port number field is undefined: " + baseDMaaPMRPluginConfig);
        }

        if (ValidationUtils.isEmpty(baseDMaaPMRPluginConfig.getTopicName())) {
            validationResponse.addErrorMessage(
                    "topic Name",
                    "DMaaPMRSourcePluginConfig - topic name field is undefined: " + baseDMaaPMRPluginConfig);
        }

        return validationResponse;
    }
}
