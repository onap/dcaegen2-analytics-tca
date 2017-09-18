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
 * Usage of an array of virtual network interface cards
 * <p>
 * @author Rajiv Singla . Creation Date: 10/17/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VNicUsageArray extends BaseCEFModel {

    private static final long serialVersionUID = 1L;

    /**
     * Virtual Network Card - Bytes In.
     *
     * @param bytesIn New value for Virtual Network Card - Bytes In
     * @return Virtual Network Card - Bytes In
     */
    private Long bytesIn;

    /**
     * Virtual Network Card - Bytes Out.
     *
     * @param bytesOut New value for Virtual Network Card - Bytes Out
     * @return Virtual Network Card - Bytes Out
     */
    private Long bytesOut;

    /**
     * Virtual Network Card - Packets In.
     *
     * @param packetsIn New value for Virtual Network Card - Packets In
     * @return Virtual Network Card - Packets In
     */
    private Long packetsIn;

    /**
     * Virtual Network Card - Packets Out.
     *
     * @param packetsOut New value for Virtual Network Card - Packets Out
     * @return Virtual Network Card - Packets Out
     */
    private Long packetsOut;

    /**
     * Virtual Network Card Identifier.
     *
     * @param vNicIdentifier New value for Virtual Network Card Identifier
     * @return Virtual Network Card Identifier
     */
    private String vNicIdentifier;
}
