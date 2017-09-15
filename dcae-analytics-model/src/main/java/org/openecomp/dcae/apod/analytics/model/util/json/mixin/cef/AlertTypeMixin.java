/*
 * ============LICENSE_START=========================================================
 * dcae-analytics
 * ================================================================================
 *  Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.openecomp.dcae.apod.analytics.model.util.json.mixin.cef;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openecomp.dcae.apod.analytics.model.domain.cef.AlertType;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.JsonMixin;

/**
 * Mixin for Alert Type
 *
 * @author Rajiv Singla. Creation Date: 08/15/2017.
 */
public abstract class AlertTypeMixin implements JsonMixin {

    private String name;

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

    @JsonValue
    public String getName() {
        return name;
    }


}
