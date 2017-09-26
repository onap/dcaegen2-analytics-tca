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
 * @author Rajiv Singla. Creation Date: 08/15/2017.
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
    private Domain domain;

    /**
     * Event key that is unique to the event source
     *
     * @param eventId New value for event key
     * @return Event key that is unique to the event source
     */
    private String eventId;

    /**
     * Unique event name
     *
     * @param eventName New value for event name
     * @return Unique event name
     */
    private String eventName;


    /**
     * Event type e.g. applicationVnf, guestOS, hostOS, platform
     *
     * @param eventType New value for event type
     * @return Event type e.g. applicationVnf, guestOS, hostOS, platform
     */
    private String eventType;


    /**
     * Enrichment fields for internal VES Event Listener service use only, not supplied by event sources
     *
     * @param internalHeaderFields new value for internal Header Fields
     * @return Enrichment fields for internal VES Event Listener service use only, not supplied by event sources
     */
    private InternalHeaderFields internalHeaderFields;


    /**
     * The latest unix time aka epoch time associated with the event from any component--as microseconds elapsed since
     * 1 Jan 1970 not including leap seconds
     *
     * @param lastEpochMicrosec New value for last Epoc Microsec
     * @return The latest unix time associated with the event from any component
     */
    private Long lastEpochMicrosec;


    /**
     * Three character network function component type as aligned with vfc naming standards
     *
     * @param nfcNamingCode New value for nfc naming code
     * @return Three character network function component type as aligned with vfc naming standards
     */
    private String nfcNamingCode;


    /**
     * Four character network function type as aligned with vnf naming standards
     *
     * @param nfNamingCode New value for nf naming code
     * @return Four character network function type as aligned with vnf naming standards
     */
    private String nfNamingCode;


    /**
     * Processing Priority
     *
     * @param priority New value for processing Priority
     * @return Processing Priority
     */
    private Priority priority;


    /**
     * UUID identifying the entity reporting the event, for example an OAM VM; must be populated by the
     * enrichment process
     *
     * @param reportingEntityId New value for reporting entity Id. Must be populated by the enrichment process
     * @return UUID identifying the entity reporting the event populated by the enrichment process
     */
    private String reportingEntityId;


    /**
     * Name of the entity reporting the event, for example, an EMS name; may be the same as sourceName
     *
     * @param reportingEntityName New value for reporting Entity Name
     * @return Name of the entity reporting the event, may be the same as sourceName
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
     * UUID identifying the entity experiencing the event issue; must be populated by the enrichment process
     *
     * @param sourceId New value for source id. Must be populated by the enrichment process
     * @return UUID identifying the entity experiencing the event issue
     */
    private String sourceId;


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


    /**
     * Version of the event header
     *
     * @param version New value for version of the event header
     * @return Version of the event header
     */
    private Float version;
}
