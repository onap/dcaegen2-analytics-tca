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

package org.onap.dcae.apod.analytics.cdap.common.utils;

import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.common.BaseAnalyticsCDAPCommonUnitTest;
import org.onap.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 12/12/2016.
 */
public class ValidationUtilsTest extends BaseAnalyticsCDAPCommonUnitTest {

    @Test
    public void testIsEmptyWhenStringIsNull() throws Exception {
        assertTrue(ValidationUtils.isEmpty(null));
    }

    @Test
    public void testIsEmptyWhenStringIsEmpty() throws Exception {
        String emptyString = "";
        assertTrue(ValidationUtils.isEmpty(emptyString));
    }

    @Test
    public void testIsEmptyWhenStringIsEmptyWithBlanks() throws Exception {
        String blankString = "   ";
        assertTrue(ValidationUtils.isEmpty(blankString));
    }


    @Test
    public void testIsNotPresent() throws Exception {
        assertFalse(ValidationUtils.isPresent(null));
        String emptyString = "";
        assertFalse(ValidationUtils.isPresent(emptyString));
        String blankString = "   ";
        assertFalse(ValidationUtils.isPresent(blankString));
        String validString = "SomeValue";
        assertTrue(ValidationUtils.isPresent(validString));
    }

    @Test
    public void testValidateSettingsWhenValidationPasses() throws Exception {
        CDAPTestAppSettings cdapTestAppSettings = new CDAPTestAppSettings();
        cdapTestAppSettings.setSettingsField("testValue");
        ValidationUtils.validateSettings(cdapTestAppSettings, new CDAPTestAppSettingsValidator());
    }

    @Test(expected = CDAPSettingsException.class)
    public void testValidateSettingsWhenValidationFails() throws Exception {

        CDAPTestAppSettings cdapTestAppSettings = new CDAPTestAppSettings();
        cdapTestAppSettings.setSettingsField("");
        ValidationUtils.validateSettings(cdapTestAppSettings, new CDAPTestAppSettingsValidator());
    }

}

