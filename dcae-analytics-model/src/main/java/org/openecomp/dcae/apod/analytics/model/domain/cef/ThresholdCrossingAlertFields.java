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

package org.openecomp.dcae.apod.analytics.model.domain.cef;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Common Event Format - Fields Specific to threshold crossing alert events
 *
 * @author Rajiv Singla. Creation Date: 08/15/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ThresholdCrossingAlertFields extends BaseCEFModel {

    private static final long serialVersionUID = 1L;

    /**
     * Additional Performance counters parameters.
     *
     * @param additionalParameters New value for Additional Performance counters
     * @return Additional Performance counters
     */
    private List<PerformanceCounter> additionalParameters;

    /**
     *  Event alert action
     *
     *  @param alertAction New Event Action
     *  @return Event alert action
     */
    private AlertAction alertAction;

    /**
     * Unique short alert description such as IF-SHUB-ERRDROP
     *
     * @param alertDescription New value for Unique short alert description
     * @return Unique short alert description
     */
    private String alertDescription;

    /**
     * Alert type
     *
     * @param alertType New value for Alert Type
     * @return Alert Type
     */
    private AlertType alertType;

    /**
     * Calculated API value (if applicable)
     *
     * @param alertValue New Calculated API value
     * @return Calculated API value (if applicable)
     */
    private String alertValue;

    /**
     * List of eventIds associated with the event being reported
     *
     * @param associatedAlertIdList New value for eventIds associated with the event
     * @return List of eventIds associated with the event being reported
     */
    private List<String> associatedAlertIdList;

    /**
     * Time when the performance collector picked up the data; with RFC 2822 compliant format:
     * ‘Sat, 13 Mar 2010 11:29:05 -0800’
     *
     * @param collectionTimestamp Set new value for time when the performance collector picked up the data
     * @return Time when the performance collector picked up the data
     */
    private String collectionTimestamp;

    /**
     * Specific performance collector instance used
     *
     * @param dataCollector New value for specific performance collector instance used
     * @return Specific performance collector instance used
     */
    private String dataCollector;

    /**
     * Type of network element
     *
     * @param elementType New value for type of network element
     * @return Type of network element
     */
    private String elementType;

    /**
     * Event severity or priority
     *
     * @param eventSeverity New value for event severity or priority
     * @return Event severity or priority
     */
    private EventSeverity eventSeverity;

    /**
     * Time closest to when the measurement was made; with RFC 2822 compliant format: ‘Sat, 13 Mar 2010 11:29:05 -0800’
     *
     * @param eventStartTimestamp New value for time closest to when the measurement was made
     * @return Time closest to when the measurement was made
     */
    private String eventStartTimestamp;

    /**
     * Physical or logical port or card (if applicable)
     *
     * @param interfaceName New value for Physical or logical port or card (if applicable)
     * @return Physical or logical port or card (if applicable)
     */
    private String interfaceName;

    /**
     * Network name
     *
     * @param networkService New value for network name
     * @return Network name
     */
    private String networkService;

    /**
     * Possible Root Cause (reserved for future use)
     *
     * @param possibleRootCause New value for possible root cause (reserved for future)
     * @return Possible Root Cause (reserved for future use)
     */
    private String possibleRootCause;

    /**
     * Version of the thresholdCrossingAlertFields block
     *
     * @param thresholdCrossingFieldsVersion New value for version of the thresholdCrossingAlertFields block
     * @return Version of the thresholdCrossingAlertFields block
     */
    private Integer thresholdCrossingFieldsVersion;
}
