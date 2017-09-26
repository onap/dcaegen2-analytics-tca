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

import co.cask.cdap.api.data.schema.Schema;
import org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils;
import org.openecomp.dcae.apod.analytics.cdap.common.validation.CDAPAppSettingsValidator;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.tca.SimpleTCAPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.CDAPPluginUtils;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;

/**
 * Validator that validate {@link SimpleTCAPluginConfig}
 * <p>
 * @author Rajiv Singla . Creation Date: 2/21/2017.
 */
public class SimpleTCAPluginConfigValidator implements CDAPAppSettingsValidator<SimpleTCAPluginConfig,
        GenericValidationResponse<SimpleTCAPluginConfig>> {

    private static final long serialVersionUID = 1L;

    @Override
    public GenericValidationResponse<SimpleTCAPluginConfig> validateAppSettings(
            final SimpleTCAPluginConfig tcaPluginConfig) {

        final GenericValidationResponse<SimpleTCAPluginConfig> validationResponse = new GenericValidationResponse<>();

        if (ValidationUtils.isEmpty(tcaPluginConfig.getVesMessageFieldName())) {
            validationResponse.addErrorMessage("vesMessageFieldName",
                    "Missing VES Message Field Name from plugin incoming schema");
        }

        if (ValidationUtils.isEmpty(tcaPluginConfig.getPolicyJson())) {
            validationResponse.addErrorMessage("policyJson",
                    "Missing tca Policy Json");
        }

        final String alertFieldValue = tcaPluginConfig.getAlertFieldName();
        final String alertFieldName = "alertFieldName";
        if (ValidationUtils.isEmpty(alertFieldValue)) {
            validationResponse.addErrorMessage(alertFieldName,
                    "Missing alert Field Name that will be placed in plugin outgoing schema");
        }

        if (ValidationUtils.isEmpty(tcaPluginConfig.getMessageTypeFieldName())) {
            validationResponse.addErrorMessage("messageTypeField",
                    "Missing message Type Field Name that will be placed in plugin outgoing schema");
        }


        final String outputSchemaJson = tcaPluginConfig.getSchema();
        if (ValidationUtils.isEmpty(outputSchemaJson)) {
            validationResponse.addErrorMessage("output schema", "Output schema is not present");
        } else {
            // validate output schema - alert field name is of type string
            if (alertFieldValue != null &&
                    !CDAPPluginUtils.validateSchemaFieldType(outputSchemaJson, alertFieldValue, Schema.Type.STRING)) {
                validationResponse.addErrorMessage(alertFieldName,
                        String.format(
                                "Alert Field Name: %s must be String type", alertFieldValue));
            }
            // validate output schema - alert field name is nullable
            if (alertFieldValue != null &&
                    !CDAPPluginUtils.validateSchemaFieldType(outputSchemaJson, alertFieldValue, Schema.Type.NULL)) {
                validationResponse.addErrorMessage(alertFieldName,
                        String.format(
                                "Alert Field Name: %s must be marked as nullable type", alertFieldValue));
            }
        }

        return validationResponse;
    }
}
