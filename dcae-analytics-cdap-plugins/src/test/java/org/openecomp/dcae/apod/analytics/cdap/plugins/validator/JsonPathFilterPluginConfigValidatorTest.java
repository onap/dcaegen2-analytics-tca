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

import org.junit.Before;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.filter.JsonPathFilterPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.filter.TestJsonPathFilterPluginConfig;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 3/3/2017.
 */
public class JsonPathFilterPluginConfigValidatorTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private TestJsonPathFilterPluginConfig jsonPathFilterPluginConfig;
    private JsonPathFilterPluginConfigValidator jsonPathFilterPluginConfigValidator;

    @Before
    public void before() {
        jsonPathFilterPluginConfig = getJsonPathFilterPluginConfig();
        jsonPathFilterPluginConfigValidator = new JsonPathFilterPluginConfigValidator();
    }


    @Test
    public void testValidateAppSettingsWhenNoValidationErrors() throws Exception {
        final GenericValidationResponse<JsonPathFilterPluginConfig> validationResponse =
                jsonPathFilterPluginConfigValidator.validateAppSettings(jsonPathFilterPluginConfig);
        assertFalse(validationResponse.hasErrors());
    }

    @Test
    public void testValidateAppSettingsWhenFilterMappingsAreEmpty() throws Exception {
        jsonPathFilterPluginConfig.setJsonFilterMappings("");
        assertResponseHasErrors(jsonPathFilterPluginConfig, jsonPathFilterPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenOutputSchemaIsNotPresent() throws Exception {
        jsonPathFilterPluginConfig.setSchema(null);
        assertResponseHasErrors(jsonPathFilterPluginConfig, jsonPathFilterPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenOutputSchemaFilterMatchedFieldIsNotBoolean() throws Exception {
        final String outputSchemaWithMatchedFieldNotBoolean =
                "{\"type\":\"record\"," +
                        "\"name\":\"etlSchemaBody\",\"fields\":" +
                        "[" +
                        "{\"name\":\"ts\",\"type\":\"long\"}," +
                        "{\"name\":\"filterMatched\",\"type\":[\"string\",\"null\"]}," +
                        "{\"name\":\"responseCode\",\"type\":\"int\"}," +
                        "{\"name\":\"responseMessage\",\"type\":\"string\"}," +
                        "{\"name\":\"message\",\"type\":\"string\"}" +
                        "]" +
                        "}";
        jsonPathFilterPluginConfig.setSchema(outputSchemaWithMatchedFieldNotBoolean);
        assertResponseHasErrors(jsonPathFilterPluginConfig, jsonPathFilterPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenOutputSchemaFilterMatchedFieldIsNotNullable() throws Exception {
        final String outputSchemaWithMatchedFieldNotNullable =
                "{\"type\":\"record\"," +
                        "\"name\":\"etlSchemaBody\",\"fields\":" +
                        "[" +
                        "{\"name\":\"ts\",\"type\":\"long\"}," +
                        "{\"name\":\"filterMatched\",\"type\":\"boolean\"}," +
                        "{\"name\":\"responseCode\",\"type\":\"int\"}," +
                        "{\"name\":\"responseMessage\",\"type\":\"string\"}," +
                        "{\"name\":\"message\",\"type\":\"string\"}" +
                        "]" +
                        "}";
        jsonPathFilterPluginConfig.setSchema(outputSchemaWithMatchedFieldNotNullable);
        assertResponseHasErrors(jsonPathFilterPluginConfig, jsonPathFilterPluginConfigValidator);
    }

    private static void assertResponseHasErrors(final TestJsonPathFilterPluginConfig jsonPluginConfig,
                                                final JsonPathFilterPluginConfigValidator validator) {
        final GenericValidationResponse validationResponse = validator.validateAppSettings(jsonPluginConfig);
        assertTrue(validationResponse.hasErrors());
    }

}
