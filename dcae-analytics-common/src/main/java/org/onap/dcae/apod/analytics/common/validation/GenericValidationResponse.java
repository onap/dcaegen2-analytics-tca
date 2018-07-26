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

package org.onap.dcae.apod.analytics.common.validation;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A generic implementation of Validation Response
 *
 * @param <T> Validation Entity Type
 *
 * @author Rajiv Singla . Creation Date: 10/24/2016.
 */
public class GenericValidationResponse<T> implements ValidationResponse {

    private LinkedHashMap<String, String> errorMessageMap = new LinkedHashMap<>();

    @Override
    public boolean hasErrors() {
        return errorMessageMap.size() != 0;
    }

    @Override
    public Set<String> getFieldNamesWithError() {
        return errorMessageMap.keySet();
    }

    @Override
    public Collection<String> getErrorMessages() {
        return errorMessageMap.values();
    }

    @Override
    public Map<String, String> getValidationResultsAsMap() {
        return errorMessageMap;
    }

    @Override
    public String getAllErrorMessage() {
        return getAllErrorMessage(",");
    }

    @Override
    public String getAllErrorMessage(String delimiter) {
        return Joiner.on(delimiter).join(errorMessageMap.values());
    }

    @Override
    public void addErrorMessage(String fieldName, String filedErrorMessage) {
        errorMessageMap.put(fieldName, filedErrorMessage);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("hasErrors", hasErrors())
                .add("errorMessageMap", errorMessageMap)
                .toString();
    }
}
