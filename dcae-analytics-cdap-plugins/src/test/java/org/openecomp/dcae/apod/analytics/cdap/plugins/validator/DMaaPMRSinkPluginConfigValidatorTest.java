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
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSinkPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSinkPluginConfig;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class DMaaPMRSinkPluginConfigValidatorTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private TestDMaaPMRSinkPluginConfig sinkPluginConfig;
    private DMaaPMRSinkPluginConfigValidator sinkPluginConfigValidator;

    @Before
    public void before() {
        sinkPluginConfigValidator = new DMaaPMRSinkPluginConfigValidator();
        sinkPluginConfig = getTestDMaaPMRSinkPluginConfig();
    }

    @Test
    public void validateAppSettingsWithValidDMaaPSinkConfig() throws Exception {
        final GenericValidationResponse<DMaaPMRSinkPluginConfig> validationResponse =
                sinkPluginConfigValidator.validateAppSettings(sinkPluginConfig);
        assertFalse(validationResponse.hasErrors());
    }


    @Test
    public void validateAppSettingsWithValidDMaaPSinkConfigWhenHostNameIsNotPresent() throws Exception {
        sinkPluginConfig.setHostName(null);
        assertResponseHasErrors(sinkPluginConfig, sinkPluginConfigValidator);
    }

    @Test
    public void validateAppSettingsWithValidDMaaPSinkConfigWhenHostPortIsNotPresent() throws Exception {
        sinkPluginConfig.setPortNumber(null);
        assertResponseHasErrors(sinkPluginConfig, sinkPluginConfigValidator);
    }

    @Test
    public void validateAppSettingsWithValidDMaaPSinkConfigWhenTopicNameIsNotPresent() throws Exception {
        sinkPluginConfig.setTopicName(null);
        assertResponseHasErrors(sinkPluginConfig, sinkPluginConfigValidator);
    }

    @Test
    public void validateAppSettingsWithValidDMaaPSinkConfigWhenColumnNameIsNotPresent() throws Exception {
        sinkPluginConfig.setMessageColumnName(null);
        assertResponseHasErrors(sinkPluginConfig, sinkPluginConfigValidator);
    }

    private static void assertResponseHasErrors(final TestDMaaPMRSinkPluginConfig sinkPluginConfig,
                                                final DMaaPMRSinkPluginConfigValidator validator) {
        final GenericValidationResponse validationResponse = validator.validateAppSettings(sinkPluginConfig);
        assertTrue(validationResponse.hasErrors());
    }


}
