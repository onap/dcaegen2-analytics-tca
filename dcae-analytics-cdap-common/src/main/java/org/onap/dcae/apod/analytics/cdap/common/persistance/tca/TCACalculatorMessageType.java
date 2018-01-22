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

package org.onap.dcae.apod.analytics.cdap.common.persistance.tca;

/**
 * TCA Calculator applies TCA Policy to incoming VES messages and classifies them as per this enum
 *
 * @author Rajiv Singla . Creation Date: 11/15/2016.
 */
public enum TCACalculatorMessageType {

    /**
     * VES messages that are not applicable as per TCA Policy
     */
    INAPPLICABLE,
    /**
     * VES messages that are applicable as per TCA Policy but don't violate any thresholds
     */
    COMPLIANT,
    /**
     * VES messages that are applicable as per TCA Policy and also in violation of TCA Policy thresholds
     */
    NON_COMPLIANT;

}
