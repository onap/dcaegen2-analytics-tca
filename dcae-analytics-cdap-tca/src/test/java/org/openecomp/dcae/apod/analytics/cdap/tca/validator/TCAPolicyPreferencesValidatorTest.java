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

import org.junit.Before;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAPolicyPreferences;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerFunctionalRole;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 12/16/2016.
 */
public class TCAPolicyPreferencesValidatorTest extends BaseAnalyticsCDAPTCAUnitTest {

    private TCAPolicyPreferencesValidator tcaPolicyPreferencesValidator;
    private TCAPolicyPreferences tcaPolicyPreferences;

    @Before
    public void before() {
        tcaPolicyPreferencesValidator = new TCAPolicyPreferencesValidator();
        tcaPolicyPreferences = getSampleTCAPolicyPreferences();
    }

    @Test
    public void testValidateAppSettingsWhenSettingsAreValid() throws Exception {
        final GenericValidationResponse<TCAPolicyPreferences> validationResponse =
                tcaPolicyPreferencesValidator.validateAppSettings(tcaPolicyPreferences);
        assertFalse(validationResponse.hasErrors());
    }

    @Test
    public void testValidateAppSettingsWhenDomainIsNullAndFunctionRoleIsEmpty() throws Exception {
        tcaPolicyPreferences.setDomain(null);
        tcaPolicyPreferences.setMetricsPerFunctionalRole(Collections.<MetricsPerFunctionalRole>emptyList());
        final GenericValidationResponse<TCAPolicyPreferences> validationResponse =
                tcaPolicyPreferencesValidator.validateAppSettings(tcaPolicyPreferences);
        assertTrue(validationResponse.hasErrors());
        assertTrue(validationResponse.getErrorMessages().size() == 2);
    }

    @Test
    public void testValidateAppSettingsWhenThresholdIsEmpty() throws Exception {
        tcaPolicyPreferences.getMetricsPerFunctionalRole().get(0).setThresholds(Collections.<Threshold>emptyList());
        final GenericValidationResponse<TCAPolicyPreferences> validationResponse =
                tcaPolicyPreferencesValidator.validateAppSettings(tcaPolicyPreferences);
        assertTrue(validationResponse.hasErrors());
        assertTrue(validationResponse.getErrorMessages().size() == 1);
    }

    @Test
    public void testValidateAppSettingsWhenThresholdPathIsMissing() throws Exception {
        tcaPolicyPreferences.getMetricsPerFunctionalRole().get(0).getThresholds().get(0).setFieldPath(null);
        final GenericValidationResponse<TCAPolicyPreferences> validationResponse =
                tcaPolicyPreferencesValidator.validateAppSettings(tcaPolicyPreferences);
        assertTrue(validationResponse.hasErrors());
        assertTrue(validationResponse.getErrorMessages().size() == 1);
    }

}
