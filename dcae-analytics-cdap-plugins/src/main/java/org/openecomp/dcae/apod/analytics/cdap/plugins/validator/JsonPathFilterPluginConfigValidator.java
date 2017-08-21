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
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.filter.JsonPathFilterPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.CDAPPluginUtils;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;

/**
 * Validator to validate {@link JsonPathFilterPluginConfig}
 * <p>
 * @author Rajiv Singla . Creation Date: 3/2/2017.
 */
public class JsonPathFilterPluginConfigValidator implements CDAPAppSettingsValidator<JsonPathFilterPluginConfig,
        GenericValidationResponse<JsonPathFilterPluginConfig>> {

    private static final long serialVersionUID = 1L;

    @Override
    public GenericValidationResponse<JsonPathFilterPluginConfig> validateAppSettings(
            final JsonPathFilterPluginConfig jsonPathFilterPluginConfig) {

        final GenericValidationResponse<JsonPathFilterPluginConfig> validationResponse =
                new GenericValidationResponse<>();

        final String jsonFilterMappings = jsonPathFilterPluginConfig.getJsonFilterMappings();
        if (ValidationUtils.isEmpty(jsonFilterMappings)) {

            validationResponse.addErrorMessage("JsonFilterMappings", "Json Filter Mappings must be present");
        }


        final String matchedField = jsonPathFilterPluginConfig.getOutputSchemaFieldName();
        final String outputSchemaJson = jsonPathFilterPluginConfig.getSchema();

        if (ValidationUtils.isEmpty(outputSchemaJson)) {

            validationResponse.addErrorMessage("output schema", "Output schema is not present");

        } else {

            // validate matched output field type is boolean
            if (matchedField != null &&
                    !CDAPPluginUtils.validateSchemaFieldType(outputSchemaJson, matchedField, Schema.Type.BOOLEAN)) {
                validationResponse.addErrorMessage("OutputSchemaFieldName",
                        String.format(
                                "OutputSchemaFieldName: %s must be marked as boolean type", matchedField));
            }

            // validate matched output field type is nullable
            if (matchedField != null &&
                    !CDAPPluginUtils.validateSchemaFieldType(outputSchemaJson, matchedField, Schema.Type.NULL)) {
                validationResponse.addErrorMessage("OutputSchemaFieldName",
                        String.format(
                                "OutputSchemaFieldName: %s must be marked as nullable type", matchedField));
            }

        }

        return validationResponse;
    }
}
