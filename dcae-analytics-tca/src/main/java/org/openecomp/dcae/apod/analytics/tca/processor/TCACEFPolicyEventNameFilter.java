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

import com.google.common.base.Joiner;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;

import java.util.List;

import static org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils.getPolicyEventNamesSupplier;

/**
 * <p>
 *     TCA Processor that acts like a filter to filter out messages which does not belong to TCA Policy Event Name
 *     <br>
 *     Pre Conditions: CEF Event Listener must be present
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public class TCACEFPolicyEventNameFilter extends AbstractTCAECEFPolicyProcessor {

    private static final long serialVersionUID = 1L;

    @Override
    public String getProcessorDescription() {
        return "Filters out CEF Messages which does not match Policy Functional Roles";
    }

    @Override
    public TCACEFProcessorContext processMessage(TCACEFProcessorContext processorContext) {

        // Safe to get event Listener here without null check as pre processor will validate if
        // event listener is indeed present
        final EventListener eventListener = processorContext.getCEFEventListener();

        String cefMessageEventName;

        if (eventListener.getEvent() != null &&
                eventListener.getEvent().getCommonEventHeader() != null &&
                eventListener.getEvent().getCommonEventHeader().getEventName() != null) {
            cefMessageEventName = eventListener.getEvent().getCommonEventHeader().getEventName();
        } else {
            String terminationMessage = "Invalid CEF Message.Common Event Header Event Name not present.";
            setTerminatingProcessingMessage(terminationMessage, processorContext);
            return processorContext;
        }

        // Determine Policy Functional Roles
        final TCAPolicy tcaPolicy = processorContext.getTCAPolicy();
        final List<String> policyFunctionalRoles = getPolicyEventNamesSupplier(tcaPolicy).get();
        final String policyFunctionalRolesString = Joiner.on(",").join(policyFunctionalRoles);

        // If Policy functional Roles contains CEF message Functional Role then continue processing
        if (policyFunctionalRoles.contains(cefMessageEventName)) {
            final String finishMessage = String.format(
                    "Policy Functional Roles and CEF Message Functional match successful." +
                            "Message Functional Role: %s, Policy Functional Roles: %s",
                    cefMessageEventName, policyFunctionalRolesString);
            setFinishedProcessingMessage(finishMessage, processorContext);
        } else {
            // If Policy functional Roles does not contain CEF message Functiona Role then terminate processing
            final String terminatingMessage = String.format(
                    "Policy Domain and CEF Message Domain match unsuccessful." +
                            "Message Functional Role: %s, Policy Functional Roles: %s",
                    cefMessageEventName, policyFunctionalRolesString);
            setTerminatingProcessingMessage(terminatingMessage, processorContext);
        }

        return processorContext;
    }
}
