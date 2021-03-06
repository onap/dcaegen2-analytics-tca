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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Generic Event Format
 * <p>
 * @author Rajiv Singla. Creation Date: 08/15/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseCEFModel {


    private static final long serialVersionUID = 1L;

    /**
     * Fields common to all Events
     *
     * @param commonEventHeader New value for common Event Header
     * @return Fields common to all Events
     */
    private CommonEventHeader commonEventHeader;

    /**
     * Measurements for Vf scaling fields
     *
     * @param measurementsForVfScalingFields New value for MeasurementsForVfScaling
     * @return MeasurementsForVfScaling fields
     */
    private MeasurementsForVfScalingFields measurementsForVfScalingFields;

    /**
     * Threshold crossing alert Fields.
     *
     * @param thresholdCrossingAlertFields New value for Threshold crossing Fields
     * @return Threshold crossing Fields
     */
    private ThresholdCrossingAlertFields thresholdCrossingAlertFields;
}
