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
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class DMaaPMRSourcePluginConfigValidatorTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private TestDMaaPMRSourcePluginConfig sourcePluginConfig;
    private DMaaPMRSourcePluginConfigValidator sourcePluginConfigValidator;

    @Before
    public void before() {
        sourcePluginConfigValidator = new DMaaPMRSourcePluginConfigValidator();
        sourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
    }

    @Test
    public void validateAppSettingsWithValidDMaaPSourceConfig() throws Exception {
        final GenericValidationResponse<DMaaPMRSourcePluginConfig> validationResponse =
                sourcePluginConfigValidator.validateAppSettings(sourcePluginConfig);
        assertFalse(validationResponse.hasErrors());
    }


    @Test
    public void validateAppSettingsWithValidDMaaPSourceConfigWhenHostNameIsNotPresent() throws Exception {
        sourcePluginConfig.setHostName(null);
        assertResponseHasErrors(sourcePluginConfig, sourcePluginConfigValidator);
    }

    @Test
    public void validateAppSettingsWithValidDMaaPSourceConfigWhenHostPortIsNotPresent() throws Exception {
        sourcePluginConfig.setPortNumber(null);
        assertResponseHasErrors(sourcePluginConfig, sourcePluginConfigValidator);
    }

    @Test
    public void validateAppSettingsWithValidDMaaPSourceConfigWhenTopicNameIsNotPresent() throws Exception {
        sourcePluginConfig.setTopicName(null);
        assertResponseHasErrors(sourcePluginConfig, sourcePluginConfigValidator);
    }

    @Test
    public void validateAppSettingsWithValidDMaaPSourcePollingIntervalIsNotPresent() throws Exception {
        sourcePluginConfig.setPollingInterval(null);
        assertResponseHasErrors(sourcePluginConfig, sourcePluginConfigValidator);
    }

    private static void assertResponseHasErrors(final TestDMaaPMRSourcePluginConfig sourcePluginConfig,
                                                final DMaaPMRSourcePluginConfigValidator validator) {
        final GenericValidationResponse validationResponse = validator.validateAppSettings(sourcePluginConfig);
        assertTrue(validationResponse.hasErrors());
    }

}
