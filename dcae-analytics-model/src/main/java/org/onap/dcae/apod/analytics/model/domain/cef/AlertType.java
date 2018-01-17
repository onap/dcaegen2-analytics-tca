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

package org.onap.dcae.apod.analytics.model.domain.cef;

/**
 *
 * @author Rajiv Singla. Creation Date: 08/15/2017.
 */
public enum AlertType implements CEFModel {

    CARD_ANOMALY("CARD-ANOMALY"),
    ELEMENT_ANOMALY("ELEMENT-ANOMALY"),
    INTERFACE_ANOMALY("INTERFACE-ANOMALY"),
    SERVICE_ANOMALY("SERVICE-ANOMALY"),
    UNKNOWN(null);

    private final String name;

    AlertType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
