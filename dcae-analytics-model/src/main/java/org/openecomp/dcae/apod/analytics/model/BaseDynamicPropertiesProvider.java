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

package org.openecomp.dcae.apod.analytics.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 *     Base Dynamic Provider provide functionality so that all the
 *     additional dynamic Properties can be accumalated in a map.
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
@Data
public abstract class BaseDynamicPropertiesProvider implements DynamicPropertiesProvider {

    /**
     * All non-required properties should be captured in additional properties
     *
     * @param dynamicProperties Dynamic properties
     * @return dynamic properties
     */
    private Map<String, Object> dynamicProperties = new LinkedHashMap<>();


    /**
     * Add a dynamic property to Common Event Format Entity
     *
     * @param propertyName property name
     * @param propertyValue property value
     */
    @Override
    public void addDynamicProperties(String propertyName, Object propertyValue) {
        dynamicProperties.put(propertyName, propertyValue);
    }

    /**
     * Determines if dynamic properties are present for the CEF Entity
     *
     * @return return true if Dynamic Properties are present
     */
    public boolean isDynamicPropertiesPresent() {
        return dynamicProperties.size() == 0;
    }


}
