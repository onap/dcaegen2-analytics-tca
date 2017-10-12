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
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity;

import java.math.BigDecimal;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Threshold extends BaseTCAPolicyModel {

    private static final long serialVersionUID = 1L;

    /**
     * Closed Loop Control Name
     *
     * @param closedLoopControlName New value for Closed Loop Control Name
     * @return Closed Loop Control Name
     */
    private String closedLoopControlName;


    /**
     * Closed Loop Event Status
     *
     * @param closedLoopEventStatus New value for Closed Loop Event Status
     * @return Closed Loop Event Status
     */
    private ClosedLoopEventStatus closedLoopEventStatus;

    /**
     * Threshold Version
     *
     * @param version New value for Threshold Version
     * @return Threshold Version
     */
    private String version;

    /**
     * Path of the field inside Common Event Format which needs to be monitored by TCA App
     * for threshold crossing
     *
     * @param fieldPath New value for Path of the field inside CEF which needs to be monitored for TCA
     * @return Path of the field inside Common Event Format which needs to be monitored by TCA App
     */
    private String fieldPath;

    /**
     * Threshold Value
     *
     * @param thresholdValue New value for Threshold Value
     * @return Threshold Value
     */
    private Long thresholdValue;

    /**
     * Direction of threshold
     *
     * @param direction New value for Direction of threshold
     * @return Direction of threshold
     */
    private Direction direction;

    /**
     * Severity of Event based on CEF Convention
     *
     * @param severity New value for Severity of Event based on CEF Convention
     * @return Severity of Event based on CEF Convention
     */
    private EventSeverity severity;


    /**
     * Actual Field value that caused the threshold violation. Note: Ignored for serialization / deserialization
     *
     *
     * @param actualFieldValue new value for actual Field value that caused the violation
     * @return actual field value that caused the violation
     */
    private BigDecimal actualFieldValue;

    /**
     * Creates a deep copy of give {@link Threshold}
     *
     * @param threshold threshold that need to be copied
     *
     * @return new instance of threshold with copied value for give threshold
     */
    public static Threshold copy(final Threshold threshold) {
        final Threshold newThreshold = new Threshold();
        newThreshold.setClosedLoopControlName(threshold.getClosedLoopControlName());
        newThreshold.setClosedLoopEventStatus(threshold.getClosedLoopEventStatus());
        newThreshold.setFieldPath(threshold.getFieldPath());
        newThreshold.setThresholdValue(threshold.getThresholdValue());
        newThreshold.setDirection(threshold.getDirection());
        newThreshold.setSeverity(threshold.getSeverity());
        newThreshold.setActualFieldValue(threshold.getActualFieldValue());
        return newThreshold;
    }

}
