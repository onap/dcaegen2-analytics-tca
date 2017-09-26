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
 * Describes the performance and errors of an identified virtual network interface card
 * <p>
 * @author Rajiv Singla. Creation Date: 08/15/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VNicPerformance extends BaseCEFModel {


    private static final long serialVersionUID = 1L;

    /**
     * Cumulative count of broadcast packets received as read at the end of the measurement interval
     *
     * @param receivedBroadcastPacketsAccumulated New value for cumulative count of broadcast packets received as
     * read at the end of the measurement interval
     * @return Cumulative count of broadcast packets received as read at the end of the measurement interval
     */
    private Long receivedBroadcastPacketsAccumulated;

    /**
     * Count of broadcast packets received within the measurement interval
     *
     * @param receivedBroadcastPacketsDelta New value for count of broadcast packets received within the measurement
     * interval
     * @return Count of broadcast packets received within the measurement interval
     */
    private Long receivedBroadcastPacketsDelta;

    /**
     * Cumulative count of discarded packets received as read at the end of the measurement interval
     *
     * @param receivedDiscardedPacketsAccumulated New value for cumulative count of discarded packets received as read
     * at the end of the measurement interval
     * @return  Cumulative count of discarded packets received as read at the end of the measurement interval
     */
    private Long receivedDiscardedPacketsAccumulated;

    /**
     * Count of discarded packets received within the measurement interval
     *
     * @param receivedDiscardedPacketsDelta New value for count of discarded packets received within the measurement
     * interval
     * @return Count of discarded packets received within the measurement interval
     */
    private Long receivedDiscardedPacketsDelta;

    /**
     * Cumulative count of error packets received as read at the end of the measurement interval
     *
     * @param receivedErrorPacketsAccumulated New value for cumulative count of error packets received as read at the
     * end of the measurement interval
     * @return Cumulative count of error packets received as read at the end of the measurement interval
     */
    private Long receivedErrorPacketsAccumulated;

    /**
     * Count of error packets received within the measurement interval
     *
     * @param receivedErrorPacketsDelta New value for count of error packets received within the measurement interval
     * @return Count of error packets received within the measurement interval
     */
    private Long receivedErrorPacketsDelta;

    /**
     * Cumulative count of multicast packets received as read at the end of the measurement interval
     *
     * @param receivedMulticastPacketsAccumulated New value for cumulative count of multicast packets received as
     * read at the end of the measurement interval
     * @return Cumulative count of multicast packets received as read at the end of the measurement interval
     */
    private Long receivedMulticastPacketsAccumulated;

    /**
     * Count of multicast packets received within the measurement interval
     *
     * @param receivedMulticastPacketsDelta New value for count of multicast packets received within the measurement
     * interval
     * @return Count of multicast packets received within the measurement interval
     */
    private Long receivedMulticastPacketsDelta;

    /**
     * Cumulative count of octets received as read at the end of the measurement interval
     *
     * @param receivedOctetsAccumulated New value for cumulative count of octets received as read at the end of the
     * measurement interval
     * @return Cumulative count of octets received as read at the end of the measurement interval
     */
    private Long receivedOctetsAccumulated;

    /**
     * Count of octets received within the measurement interval
     *
     * @param receivedOctetsDelta New value for count of octets received within the measurement interval
     * @return Count of octets received within the measurement interval
     */
    private Long receivedOctetsDelta;

    /**
     * Cumulative count of all packets received as read at the end of the measurement interval
     *
     * @param receivedTotalPacketsAccumulated New value for cumulative count of all packets received as read at the
     * end of the measurement interval
     * @return Cumulative count of all packets received as read at the end of the measurement interval
     */
    private Long receivedTotalPacketsAccumulated;

    /**
     * Count of all packets received within the measurement interval
     *
     * @param receivedTotalPacketsDelta New value for count of all packets received within the measurement interval
     * @return Count of all packets received within the measurement interval
     */
    private Long receivedTotalPacketsDelta;

    /**
     * Cumulative count of unicast packets received as read at the end of the measurement interval
     *
     * @param receivedUnicastPacketsAccumulated New value for cumulative count of unicast packets received as read at
     * the end of the measurement interval
     * @return Cumulative count of unicast packets received as read at the end of the measurement interval
     */
    private Long receivedUnicastPacketsAccumulated;

    /**
     * Count of unicast packets received within the measurement interval
     *
     * @param receivedUnicastPacketsDelta New value for count of unicast packets received within the measurement
     * interval
     * @return Count of unicast packets received within the measurement interval
     */
    private Long receivedUnicastPacketsDelta;

    /**
     * Cumulative count of broadcast packets transmitted as read at the end of the measurement interval
     *
     * @param transmittedBroadcastPacketsAccumulated New value for cumulative count of broadcast packets transmitted
     * as read at the end of the measurement interval
     * @return Cumulative count of broadcast packets transmitted as read at the end of the measurement interval
     */
    private Long transmittedBroadcastPacketsAccumulated;

    /**
     * Count of broadcast packets transmitted within the measurement interval
     *
     * @param transmittedBroadcastPacketsDelta New value for count of broadcast packets transmitted within the
     * measurement interval
     * @return Count of broadcast packets transmitted within the measurement interval
     */
    private Long transmittedBroadcastPacketsDelta;

    /**
     * Cumulative count of discarded packets transmitted as read at the end of the measurement interval
     *
     * @param transmittedDiscardedPacketsAccumulated New value for cumulative count of discarded packets transmitted
     * as read at the end of the measurement interval
     * @return Cumulative count of discarded packets transmitted as read at the end of the measurement interval
     */
    private Long transmittedDiscardedPacketsAccumulated;

    /**
     * Count of discarded packets transmitted within the measurement interval
     *
     * @param transmittedDiscardedPacketsDelta New value for count of discarded packets transmitted within the
     * measurement interval
     * @return Count of discarded packets transmitted within the measurement interval
     */
    private Long transmittedDiscardedPacketsDelta;

    /**
     * Cumulative count of error packets transmitted as read at the end of the measurement interval
     *
     * @param transmittedErrorPacketsAccumulated New value for cumulative count of error packets transmitted as read
     * at the end of the measurement interval
     * @return Cumulative count of error packets transmitted as read at the end of the measurement interval
     */
    private Long transmittedErrorPacketsAccumulated;

    /**
     * Count of error packets transmitted within the measurement interval
     *
     * @param transmittedErrorPacketsDelta New value for count of error packets transmitted within the measurement
     * interval
     * @return Count of error packets transmitted within the measurement interval
     */
    private Long transmittedErrorPacketsDelta;

    /**
     * Cumulative count of multicast packets transmitted as read at the end of the measurement interval
     *
     * @param transmittedMulticastPacketsAccumulated New value for cumulative count of multicast packets transmitted
     * as read at the end of the measurement interval
     * @return Cumulative count of multicast packets transmitted as read at the end of the measurement interval
     */
    private Long transmittedMulticastPacketsAccumulated;

    /**
     * Count of multicast packets transmitted within the measurement interval
     *
     * @param transmittedMulticastPacketsDelta New value for count of multicast packets transmitted within the
     * measurement interval
     * @return Count of multicast packets transmitted within the measurement interval
     */
    private Long transmittedMulticastPacketsDelta;

    /**
     * Cumulative count of octets transmitted as read at the end of the measurement interval
     *
     * @param transmittedOctetsAccumulated New value for cumulative count of octets transmitted as read at the end of
     * the measurement interval
     * @return Cumulative count of octets transmitted as read at the end of the measurement interval
     */
    private Long transmittedOctetsAccumulated;

    /**
     * Count of octets transmitted within the measurement interval
     *
     * @param transmittedOctetsDelta New value for count of octets transmitted within the measurement interval
     * @return Count of octets transmitted within the measurement interval
     */
    private Long transmittedOctetsDelta;

    /**
     * Cumulative count of all packets transmitted as read at the end of the measurement interval
     *
     * @param transmittedTotalPacketsAccumulated New value for cumulative count of all packets transmitted as read at
     * the end of the measurement interval
     * @return Cumulative count of all packets transmitted as read at the end of the measurement interval
     */
    private Long transmittedTotalPacketsAccumulated;

    /**
     * Count of all packets transmitted within the measurement interval
     *
     * @param transmittedTotalPacketsDelta New value for count of all packets transmitted within the measurement
     * interval
     * @return Count of all packets transmitted within the measurement interval
     */
    private Long transmittedTotalPacketsDelta;

    /**
     * Cumulative count of unicast packets transmitted as read at the end of the measurement interval
     *
     * @param transmittedUnicastPacketsAccumulated New value for cumulative count of unicast packets transmitted as
     * read at the end of the measurement interval
     * @return Cumulative count of unicast packets transmitted as read at the end of the measurement interval
     */
    private Long transmittedUnicastPacketsAccumulated;


    /**
     * Count of unicast packets transmitted within the measurement interval
     *
     * @param transmittedUnicastPacketsDelta New value for count of unicast packets transmitted within the
     * measurement interval
     * @return Count of unicast packets transmitted within the measurement interval
     */
    private Long transmittedUnicastPacketsDelta;


    /**
     * Indicates whether vNicPerformance values are likely inaccurate due to counter overflow or other conditions
     *
     * @param valuesAreSuspect New value to indicate whether vNicPerformance values are likely inaccurate due to
     * counter overflow or other conditions
     * @return Indicates whether vNicPerformance values are likely inaccurate due to counter overflow or other
     * conditions
     */
    private Boolean valuesAreSuspect;


    /**
     * Virtual Network Card Identifier
     *
     * @param vNicIdentifier New value for Virtual Network Card Identifier
     * @return Virtual Network Card Identifier
     */
    private String vNicIdentifier;


}
