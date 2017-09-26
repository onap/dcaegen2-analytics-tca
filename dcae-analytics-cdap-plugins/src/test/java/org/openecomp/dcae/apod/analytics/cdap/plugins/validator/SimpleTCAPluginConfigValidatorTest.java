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
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.tca.SimpleTCAPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.tca.TestSimpleTCAPluginConfig;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 2/21/2017.
 */
public class SimpleTCAPluginConfigValidatorTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private TestSimpleTCAPluginConfig testSimpleTCAPluginConfig;
    private SimpleTCAPluginConfigValidator simpleTCAPluginConfigValidator;

    @Before
    public void before() {
        testSimpleTCAPluginConfig = getTestSimpleTCAPluginConfig();
        simpleTCAPluginConfigValidator = new SimpleTCAPluginConfigValidator();
    }

    @Test
    public void testValidateAppSettingsWhenAllSettingsAreValid() throws Exception {
        final GenericValidationResponse<SimpleTCAPluginConfig> validationResponse =
                simpleTCAPluginConfigValidator.validateAppSettings(testSimpleTCAPluginConfig);
        assertFalse(validationResponse.hasErrors());
    }

    @Test
    public void testValidateAppSettingsWhenVESMessageFieldNameIsMissing() throws Exception {
        testSimpleTCAPluginConfig.setVesMessageFieldName(null);
        assertResponseHasErrors(testSimpleTCAPluginConfig, simpleTCAPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenPolicyJsonIsMissing() throws Exception {
        testSimpleTCAPluginConfig.setPolicyJson(null);
        assertResponseHasErrors(testSimpleTCAPluginConfig, simpleTCAPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenAlertFieldNameIsMissing() throws Exception {
        testSimpleTCAPluginConfig.setAlertFieldName(null);
        assertResponseHasErrors(testSimpleTCAPluginConfig, simpleTCAPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenOutputSchemaIsNull() throws Exception {
        testSimpleTCAPluginConfig.setSchema(null);
        assertResponseHasErrors(testSimpleTCAPluginConfig, simpleTCAPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenMessageTypeFieldNameIsMissing() throws Exception {
        testSimpleTCAPluginConfig.setMessageTypeFieldName(null);
        assertResponseHasErrors(testSimpleTCAPluginConfig, simpleTCAPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenAlertFieldIsNullableInOutputSchema() throws Exception {
        testSimpleTCAPluginConfig.setSchema(
                "{\"type\":\"record\"," +
                        "\"name\":\"etlSchemaBody\"," +
                        "\"fields\":[" +
                            "{\"name\":\"ts\",\"type\":\"long\"}," +
                            "{\"name\":\"responseCode\",\"type\":\"int\"}," +
                            "{\"name\":\"responseMessage\",\"type\":\"string\"}," +
                            "{\"name\":\"message\",\"type\":\"string\"}," +
                            "{\"name\":\"alert\",\"type\":[\"string\",\"null\"]}," +
                            "{\"name\":\"tcaMessageType\",\"type\":\"string\"}]}");
        final GenericValidationResponse<SimpleTCAPluginConfig> validationResponse =
                simpleTCAPluginConfigValidator.validateAppSettings(testSimpleTCAPluginConfig);
        assertFalse(validationResponse.hasErrors());

    }

    @Test
    public void testValidateAppSettingsWhenAlertFieldIsNotPresentInOutputSchema() throws Exception {
        testSimpleTCAPluginConfig.setSchema(
                "{\"type\":\"record\"," +
                        "\"name\":\"etlSchemaBody\"," +
                        "\"fields\":[" +
                        "{\"name\":\"ts\",\"type\":\"long\"}," +
                        "{\"name\":\"responseCode\",\"type\":\"int\"}," +
                        "{\"name\":\"responseMessage\",\"type\":\"string\"}," +
                        "{\"name\":\"message\",\"type\":\"string\"}," +
                        "{\"name\":\"tcaMessageType\",\"type\":\"string\"}]}");
        final GenericValidationResponse<SimpleTCAPluginConfig> validationResponse =
                simpleTCAPluginConfigValidator.validateAppSettings(testSimpleTCAPluginConfig);
        assertFalse(validationResponse.hasErrors());

    }

    @Test
    public void testValidateAppSettingsWhenAlertFieldIsNullableButNotStringTypeInOutputSchema() throws Exception {
        testSimpleTCAPluginConfig.setSchema(
                "{\"type\":\"record\"," +
                        "\"name\":\"etlSchemaBody\"," +
                        "\"fields\":[" +
                        "{\"name\":\"ts\",\"type\":\"long\"}," +
                        "{\"name\":\"responseCode\",\"type\":\"int\"}," +
                        "{\"name\":\"responseMessage\",\"type\":\"string\"}," +
                        "{\"name\":\"message\",\"type\":\"string\"}," +
                        "{\"name\":\"alert\",\"type\":[\"int\",\"null\"]}," +
                        "{\"name\":\"tcaMessageType\",\"type\":\"string\"}]}");
        assertResponseHasErrors(testSimpleTCAPluginConfig, simpleTCAPluginConfigValidator);
    }

    @Test
    public void testValidateAppSettingsWhenAlertFieldNameIsNotNullableInOutputSchema() throws Exception {
        testSimpleTCAPluginConfig.setSchema(
                "{\"type\":\"record\"," +
                        "\"name\":\"etlSchemaBody\"," +
                        "\"fields\":[" +
                        "{\"name\":\"ts\",\"type\":\"long\"}," +
                        "{\"name\":\"responseCode\",\"type\":\"int\"}," +
                        "{\"name\":\"responseMessage\",\"type\":\"string\"}," +
                        "{\"name\":\"message\",\"type\":\"string\"}," +
                        "{\"name\":\"alert\",\"type\":\"string\"}," +
                        "{\"name\":\"tcaMessageType\",\"type\":\"string\"}]}");
        assertResponseHasErrors(testSimpleTCAPluginConfig, simpleTCAPluginConfigValidator);
    }



    private static void assertResponseHasErrors(final TestSimpleTCAPluginConfig pluginConfig,
                                                final SimpleTCAPluginConfigValidator validator) {
        final GenericValidationResponse validationResponse = validator.validateAppSettings(pluginConfig);
        assertTrue(validationResponse.hasErrors());
        LOG.debug("Validation Error Message: {}", validationResponse.getAllErrorMessage());
    }
}
