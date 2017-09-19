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

package org.openecomp.dcae.apod.analytics.model.domain;

import java.util.Map;

/**
 * <p>
 *      Problem: Many Entities have dynamic properties as well as known properties.
 *      Known properites can be binded explicitly with all dynamic properties need
 *      to be captured also ensuring that there must not be any loss in information
 *      during deserialization / serialization process.
 * </p>
 * <p>
 *     This contract allows the deserialization mechanism to catch those dynamic properties
 *     in a Map so that deserialization mechanism will not loose any information and
 *     can be serialized back with no loss in dynamic properties information
 * </p>
 * @author Rajiv Singla . Creation Date: 10/18/2016.
 */
public interface DynamicPropertiesProvider extends DCAEAnalyticsModel {

    /**
     * Adds dynamic properties in a Map object
     *
     * @param propertyName property name
     * @param propertyValue property value
     */
    void addDynamicProperties(String propertyName, Object propertyValue);

    /**
     * Provides dynamic properties map
     *
     * @return dynamic properties map object
     */
    Map<String, Object> getDynamicProperties();
}
