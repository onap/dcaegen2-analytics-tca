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

package org.openecomp.dcae.apod.analytics.model.domain.cef;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Common Event Format - Base Event Listener
 * <p>
 * @author Rajiv Singla . Creation Date: 10/17/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventListener extends BaseCEFModel {


    private static final long serialVersionUID = 1L;
    /**
     * Common Event Format - Event
     *
     * @param event New value for Event
     * @return Common Event Format Event
     */
    private Event event;


}
