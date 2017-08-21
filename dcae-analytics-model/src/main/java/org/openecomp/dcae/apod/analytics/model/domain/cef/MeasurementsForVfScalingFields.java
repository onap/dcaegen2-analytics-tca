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

import java.util.List;

/**
 * Common Event Format - MeasurementsForVfScaling fields
 * <p>
 * @author Rajiv Singla . Creation Date: 10/17/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeasurementsForVfScalingFields extends BaseCEFModel {


    private static final long serialVersionUID = 1L;

    /**
     * Interval over which measurements are being reported in seconds
     *
     * @param measurementInterval New value for measurement Interval
     * @return Interval over which measurements are being reported in seconds
     */
    private Long measurementInterval;

    /**
     * Virtual Network Card Usage Array
     *
     * @param vNicUsageArray New value for Virtual Network Card Usage Array
     * @return Virtual Network Card Usage Array
     */
    private List<VNicUsageArray> vNicUsageArray;


}
