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

package org.openecomp.dcae.apod.analytics.cdap.tca.validator;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppConfig;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCATestAppConfig;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 12/16/2016.
 */
public class TCAAppConfigValidatorTest extends BaseAnalyticsCDAPTCAUnitTest {


    @Test
    public void validateAppSettingsWhenAppConfigIsValid() throws Exception {
        final TCAAppConfigValidator tcaAppConfigValidator = new TCAAppConfigValidator();
        final TCATestAppConfig tcaTestAppConfig = getTCATestAppConfig();
        final GenericValidationResponse<TCAAppConfig> validationResponse =
                tcaAppConfigValidator.validateAppSettings(tcaTestAppConfig);
        assertFalse(validationResponse.hasErrors());
    }

    @Test
    public void testWhenSubscriberOutputStreamIsNull() throws Exception {
        final TCAAppConfigValidator tcaAppConfigValidator = new TCAAppConfigValidator();
        final TCATestAppConfig tcaTestAppConfig = getTCATestAppConfig();
        tcaTestAppConfig.setTcaSubscriberOutputStreamName(null);
        final GenericValidationResponse<TCAAppConfig> validationResponse =
                tcaAppConfigValidator.validateAppSettings(tcaTestAppConfig);
        assertTrue(validationResponse.hasErrors());
    }

    @Test
    public void testWhenVESMessageStatusTableNameIsNull() throws Exception {
        final TCAAppConfigValidator tcaAppConfigValidator = new TCAAppConfigValidator();
        final TCATestAppConfig tcaTestAppConfig = getTCATestAppConfig();
        tcaTestAppConfig.setTcaVESMessageStatusTableName(null);
        final GenericValidationResponse<TCAAppConfig> validationResponse =
                tcaAppConfigValidator.validateAppSettings(tcaTestAppConfig);
        assertTrue(validationResponse.hasErrors());
    }

    @Test
    public void testWhenVESAlertsTableNameIsNull() throws Exception {
        final TCAAppConfigValidator tcaAppConfigValidator = new TCAAppConfigValidator();
        final TCATestAppConfig tcaTestAppConfig = getTCATestAppConfig();
        tcaTestAppConfig.setTcaVESAlertsTableName(null);
        final GenericValidationResponse<TCAAppConfig> validationResponse =
                tcaAppConfigValidator.validateAppSettings(tcaTestAppConfig);
        assertTrue(validationResponse.hasErrors());
    }

}
