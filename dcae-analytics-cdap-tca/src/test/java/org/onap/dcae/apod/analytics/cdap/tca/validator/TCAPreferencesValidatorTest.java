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

package org.onap.dcae.apod.analytics.cdap.tca.validator;

import org.junit.Before;
import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCATestAppPreferences;
import org.onap.dcae.apod.analytics.common.validation.GenericValidationResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 12/19/2016.
 */
public class TCAPreferencesValidatorTest extends BaseAnalyticsCDAPTCAUnitTest {

    private TCAPreferencesValidator tcaPreferencesValidator;
    private TCATestAppPreferences tcaTestAppPreferences;

    @Before
    public void before() {
        tcaPreferencesValidator = new TCAPreferencesValidator();
        tcaTestAppPreferences = getTCATestAppPreferences();
    }

    @Test
    public void validateAppSettingsWithValidParameters() throws Exception {
        final GenericValidationResponse<TCAAppPreferences> validationResponse =
                tcaPreferencesValidator.validateAppSettings(tcaTestAppPreferences);
        assertFalse(validationResponse.hasErrors());
    }

    @Test
    public void validateAppSettingsWhenSubscriberHostOrTopicNameIsNotPresent() throws Exception {
        tcaTestAppPreferences.setSubscriberHostName(null);
        tcaTestAppPreferences.setSubscriberTopicName(null);
        final GenericValidationResponse<TCAAppPreferences> validationResponse =
                tcaPreferencesValidator.validateAppSettings(tcaTestAppPreferences);
        assertTrue(validationResponse.hasErrors());
        assertTrue(validationResponse.getErrorMessages().size() == 2);
    }

    @Test
    public void validateAppSettingsWhenPublisherHostOrTopicNameIsNotPresent() throws Exception {
        tcaTestAppPreferences.setPublisherHostName(null);
        tcaTestAppPreferences.setPublisherTopicName(null);
        final GenericValidationResponse<TCAAppPreferences> validationResponse =
                tcaPreferencesValidator.validateAppSettings(tcaTestAppPreferences);
        assertTrue(validationResponse.hasErrors());
        assertTrue(validationResponse.getErrorMessages().size() == 2);
    }

    @Test
    public void validateAppSettingsWhenAAIEnrichmentIsEnabledAndAAIRequiredFieldsAreNotPresent() throws Exception {
        tcaTestAppPreferences.setEnableAAIEnrichment(true);
        tcaTestAppPreferences.setAaiEnrichmentHost(null);
        tcaTestAppPreferences.setAaiVMEnrichmentAPIPath(null);
        tcaTestAppPreferences.setAaiVNFEnrichmentAPIPath(null);
        final GenericValidationResponse<TCAAppPreferences> validationResponse =
                tcaPreferencesValidator.validateAppSettings(tcaTestAppPreferences);
        assertTrue(validationResponse.hasErrors());
        assertTrue(validationResponse.getErrorMessages().size() == 3);
    }

}
