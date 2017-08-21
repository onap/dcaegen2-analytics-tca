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

package org.openecomp.dcae.apod.analytics.cdap.plugins.validator;

import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;

/**
 * Validates plugin config values in {@link DMaaPMRSourcePluginConfig}
 * <p>
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class DMaaPMRSourcePluginConfigValidator extends BaseDMaaPMRPluginConfigValidator<DMaaPMRSourcePluginConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * Validates plugin config values in {@link DMaaPMRSourcePluginConfig}
     *
     * @param sourcePluginConfig Source Plugin Config
     *
     * @return Validation response containing validation errors if any
     */
    @Override
    public GenericValidationResponse<DMaaPMRSourcePluginConfig> validateAppSettings(
            final DMaaPMRSourcePluginConfig sourcePluginConfig) {

        // validate settings in BaseDMaaPMRPluginConfig
        final GenericValidationResponse<DMaaPMRSourcePluginConfig> validationResponse =
                super.validateAppSettings(sourcePluginConfig);

        if (sourcePluginConfig.getPollingInterval() == null) {
            validationResponse.addErrorMessage(
                    "port Number",
                    "DMaaPMRSourcePluginConfig - polling interval is undefined: " + sourcePluginConfig);
        }

        return validationResponse;
    }
}
