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

package org.openecomp.dcae.apod.analytics.model.domain.policy.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 *     TCA (Threshold Crossing Alert) Root
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TCAPolicy extends BaseTCAPolicyModel {

    private static final long serialVersionUID = 1L;

    /**
     * TCA Policy domain which is associated with TCA incoming CEF message domain
     *
     * @param domain New value for domain
     * @return Policy domain which is associated with incoming CEF message
     */
    private String domain;

    /**
     * Contains TCA Policy metrics that needs to be applied to each Functional Role
     *
     * @param metricsPerEventName New value for metrics that needs to be applied to each Functional Role
     * @return Contains TCA Policy metrics that needs to be applied to each Functional Role
     */
    private List<MetricsPerEventName> metricsPerEventName;


}
