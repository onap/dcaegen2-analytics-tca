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
 * Fields common to all Events
 * <p>
 * @author Rajiv Singla . Creation Date: 10/17/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonEventHeader extends BaseCEFModel {


    private static final long serialVersionUID = 1L;

    /**
     * The eventing domain associated with this event
     *
     * @param domain New value for domain
     * @return The eventing domain associated with this event
     */
    private String domain;

    /**
     * Event key that is unique to the event source
     *
     * @param eventId New value for event key
     * @return Event key that is unique to the event source
     */
    private String eventId;

    /**
     * Function of the source e.g. eNodeB, MME, PCRF
     *
     * @param functionalRole New value for functional Role
     * @return Function of the source e.g. eNodeB, MME, PCRF
     */
    private String functionalRole;


    /**
     * The latest unix time aka epoch time associated with the event from any component--as microseconds elapsed since
     * 1 Jan 1970 not including leap seconds
     *
     * @param lastEpochMicrosec New value for last Epoc Microsec
     * @return The latest unix time associated with the event from any component
     */
    private Long lastEpochMicrosec;


    /**
     * Processing Priority
     *
     * @param priority New value for processing Priority
     * @return Processing Priority
     */
    private Priority priority;


    /**
     * Name of the entity reporting the event, for example, an OAM VM
     *
     * @param reportingEntityName New value for reporting Entity Name
     * @return Name of the entity reporting the event, for example, an OAM VM
     */
    private String reportingEntityName;


    /**
     * Ordering of events communicated by an event source instance or 0 if not needed
     *
     * @param sequence New value for Sequence
     * @return Ordering of events communicated by an event source instance or 0 if not needed
     */
    private Integer sequence;


    /**
     * Name of the entity experiencing the event issue
     *
     * @param sourceName New value for source name
     * @return Name of the entity experiencing the event issue
     */
    private String sourceName;


    /**
     * the earliest unix time aka epoch time associated with the event from any component--as microseconds elapsed
     * since 1 Jan 1970 not including leap seconds
     *
     * @param startEpochMicrosec New value for start Epoc Microsec
     * @return The earliest unix time associated with the event from any component
     */
    private Long startEpochMicrosec;
}
