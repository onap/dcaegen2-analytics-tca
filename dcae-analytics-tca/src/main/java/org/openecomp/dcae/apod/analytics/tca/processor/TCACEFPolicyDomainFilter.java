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

package org.openecomp.dcae.apod.analytics.tca.processor;

import org.openecomp.dcae.apod.analytics.model.domain.cef.Domain;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;

/**
 * <p>
 *     TCA Processor which acts like a filter to filter out messages which does not belong to TCA Policy Domain
 *     <br>
 *     Pre Conditions: CEF Event Listener must be present
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/7/2016.
 */
public class TCACEFPolicyDomainFilter extends AbstractTCAECEFPolicyProcessor {


    private static final long serialVersionUID = 1L;

    @Override
    public String getProcessorDescription() {
        return "Filters out CEF Messages which does not match TCAPolicy Domain";
    }

    @Override
    public TCACEFProcessorContext processMessage(TCACEFProcessorContext processorContext) {

        // Safe to get event Listener here without null check as pre processor will validate if
        // event listener is indeed present
        final EventListener eventListener = processorContext.getCEFEventListener();

        Domain cefMessageDomain;

        // Extract CEF domain as it is must be present as per CEF Schema
        if (eventListener.getEvent() != null &&
                eventListener.getEvent().getCommonEventHeader() != null &&
                eventListener.getEvent().getCommonEventHeader().getDomain() != null) {
            cefMessageDomain = eventListener.getEvent().getCommonEventHeader().getDomain();

        } else {
            final String terminatingMessage = "Invalid CEF Message.Common Event Header Domain not present.";
            setTerminatingProcessingMessage(terminatingMessage, processorContext);
            return processorContext;
        }

        // Get Policy Domain. TCA Policy Validation must ensure that Domain is indeed present
        // no null check will be required here
        final String policyDomain = processorContext.getTCAPolicy().getDomain();

        // If Policy domain matches CEF message domain then continue processing
        if (cefMessageDomain.toString().equalsIgnoreCase(policyDomain)) {
            final String finishMessage = String.format("Policy Domain and CEF Message Domain match successful." +
                    " Message Domain: %s, Policy Domain: %s", cefMessageDomain, policyDomain);
            setFinishedProcessingMessage(finishMessage, processorContext);
        } else {
            // If policy domain does not match with CEF message terminate processing chain
            final String terminatingMessage = String.format("Policy Domain and CEF Message Domain match unsuccessful." +
                    " Message Domain: %s, Policy Domain: %s", cefMessageDomain, policyDomain);
            setTerminatingProcessingMessage(terminatingMessage, processorContext);
        }

        return processorContext;
    }
}
