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

package org.openecomp.dcae.apod.analytics.common.validation;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.common.BaseAnalyticsCommonUnitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 12/12/2016.
 */
public class GenericValidationResponseTest extends BaseAnalyticsCommonUnitTest {


    @Test
    public void testHasErrorsWhenResponseHasErrors() throws Exception {

        final String fieldName = "testField";
        final String errorMessage = "Some error message";
        final GenericValidationResponse<CDAPTestAppSettings> validationResponse =
                createTestValidationResponse(fieldName, errorMessage);

        validationResponse.addErrorMessage("testField", "Some error message");
        assertThat("Validation Response must has errors", validationResponse.hasErrors(), is(true));
    }

    @Test
    public void testHasErrorsWhenResponseDoesNotHaveErrors() throws Exception {
        GenericValidationResponse<CDAPTestAppSettings> validationResponse = new
                GenericValidationResponse<>();
        assertThat("Validation Response must has errors", validationResponse.hasErrors(), is(false));
    }

    @Test
    public void testGetFieldNamesWithError() throws Exception {

        final String fieldName = "testField";
        final String errorMessage = "Some error message";
        final GenericValidationResponse<CDAPTestAppSettings> validationResponse =
                createTestValidationResponse(fieldName, errorMessage);

        assertThat("Validation Response must has errors", validationResponse.hasErrors(), is(true));
        assertThat("Validation Field Name must match",
                validationResponse.getFieldNamesWithError().iterator().next(), is(fieldName));
    }

    @Test
    public void testGetErrorMessages() throws Exception {

        final String fieldName = "testField";
        final String errorMessage = "Some error message";
        final GenericValidationResponse<CDAPTestAppSettings> validationResponse =
                createTestValidationResponse(fieldName, errorMessage);

        assertThat("Validation Response must has errors", validationResponse.hasErrors(), is(true));
        assertThat("Validation Error Message must match",
                validationResponse.getErrorMessages().iterator().next(), is(errorMessage));
    }

    @Test
    public void getValidationResultsAsMap() throws Exception {
        final String fieldName = "testField";
        final String errorMessage = "Some error message";
        final GenericValidationResponse<CDAPTestAppSettings> validationResponse =
                createTestValidationResponse(fieldName, errorMessage);
        assertThat("Validation Response must has errors", validationResponse.hasErrors(), is(true));
        assertThat("Validation Field Name must match",
                validationResponse.getValidationResultsAsMap().keySet().iterator().next(), is(fieldName));
        assertThat("Validation Error Message must match",
                validationResponse.getValidationResultsAsMap().values().iterator().next(), is(errorMessage));
    }

    @Test
    public void getAllErrorMessage() throws Exception {
        final String fieldName = "testField";
        final String errorMessage = "Some error message";
        final GenericValidationResponse<CDAPTestAppSettings> validationResponse =
                createTestValidationResponse(fieldName, errorMessage);
        final String allErrorMessage = validationResponse.getAllErrorMessage();
        assertThat("All Error messages should match", allErrorMessage, is(errorMessage));
    }

    @Test
    public void addErrorMessage() throws Exception {
        final String fieldName = "testField";
        final String errorMessage = "Some error message";
        GenericValidationResponse<CDAPTestAppSettings> validationResponse = new
                GenericValidationResponse<>();
        validationResponse.addErrorMessage(fieldName, errorMessage);

        assertThat("Validation Response must has errors", validationResponse.hasErrors(), is(true));
        assertThat("Validation Field Name must match",
                validationResponse.getValidationResultsAsMap().keySet().iterator().next(), is(fieldName));
        assertThat("Validation Error Message must match",
                validationResponse.getValidationResultsAsMap().values().iterator().next(), is(errorMessage));
    }

    private static GenericValidationResponse<CDAPTestAppSettings> createTestValidationResponse(
            final String fieldName, final String errorMessage) {
        GenericValidationResponse<CDAPTestAppSettings> validationResponse = new
                GenericValidationResponse<>();
        if (fieldName != null || errorMessage != null) {
            validationResponse.addErrorMessage(fieldName, errorMessage);
        }

        return validationResponse;
    }

}
