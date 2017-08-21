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

package org.openecomp.dcae.apod.analytics.model.util.json.mixin.cef;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openecomp.dcae.apod.analytics.model.domain.cef.AlertType;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.JsonMixin;

/**
 * Mixin for Alert Type
 *
 * @author Rajiv Singla . Creation Date: 11/3/2016.
 */
public abstract class AlertTypeMixin implements JsonMixin {

    private String name;

    /**
     * Provides hint to Jackson Json to parse alert type string which are not valid java variable names
     * to proper java alert types
     *
     * @param name name of Alert coming from incoming Json
     *
     * @return java representation of alert type
     */
    @JsonCreator
    public static AlertType forValue(String name) {

        switch (name) {
            case "CARD-ANOMALY":
                return AlertType.CARD_ANOMALY;
            case "ELEMENT-ANOMALY":
                return AlertType.ELEMENT_ANOMALY;
            case "INTERFACE-ANOMALY":
                return AlertType.INTERFACE_ANOMALY;
            case "SERVICE-ANOMALY":
                return AlertType.SERVICE_ANOMALY;
            default:
                return AlertType.UNKNOWN;
        }

    }

    /**
     * Provide hint to Jackson Json to parse java object variable name to CEF specification alert String
     *
     * @return alert string in CEF format
     */
    @JsonValue
    public String getName() {
        return name;
    }


}
