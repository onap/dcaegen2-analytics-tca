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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Validation Response contract
 *
 * @param <T> Entity class type which is being validated
 *
 * @author Rajiv Singla . Creation Date: 10/24/2016.
 */
public interface ValidationResponse<T> {

    /**
     * Returns true if validation resulted in one or more errors
     *
     * @return true if validation has errors
     */
    boolean hasErrors();

    /**
     * Returns all field names which have error
     *
     * @return names of fields which have error
     */
    Set<String> getFieldNamesWithError();

    /**
     * Returns list of all error messages
     *
     * @return list of error messages
     */
    Collection<String> getErrorMessages();


    /**
     * Returns all error messages as string delimited by comma
     *
     * @return all error messages delimited by given delimiter
     */
    String getAllErrorMessage();

    /**
     * Returns all error messages as string delimited by given delimited
     *
     * @param delimiter delimited to be used for error message
     * @return all error messages delimited by given delimiter
     */
    String getAllErrorMessage(String delimiter);

    /**
     * Adds field name and error message to the validation response
     *
     * @param fieldName field name which has validation error
     * @param filedErrorMessage validation error message
     */
    void addErrorMessage(String fieldName, String filedErrorMessage);


    /**
     * Returns validation results as map containing values as keys and values
     * as error Message
     *
     * @return Map containing field names and error message associated with those fields
     */
    Map<String, String> getValidationResultsAsMap();

}
