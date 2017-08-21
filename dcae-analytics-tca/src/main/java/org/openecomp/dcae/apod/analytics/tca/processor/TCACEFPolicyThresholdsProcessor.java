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

import com.google.common.base.Optional;
import com.google.common.collect.Table;
import org.openecomp.dcae.apod.analytics.common.exception.MessageProcessingException;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerFunctionalRole;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 *<p>
 *     TCA CEF Policy Threshold processor
 *     <br>
 *     Pre Conditions: Domain and Functional Role must be present in CEF Event Listener Object
 *</p>
 *
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public class TCACEFPolicyThresholdsProcessor extends AbstractTCAECEFPolicyProcessor {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(TCACEFPolicyThresholdsProcessor.class);

    @Override
    public TCACEFProcessorContext preProcessor(@Nonnull TCACEFProcessorContext processorContext) {
        // validates Domain and Functional Role are present
        final EventListener eventListener = processorContext.getCEFEventListener();
        final String domain = eventListener.getEvent().getCommonEventHeader().getDomain();
        final String functionalRole = eventListener.getEvent().getCommonEventHeader().getFunctionalRole();
        if (domain == null || functionalRole == null) {
            final String errorMessage = "CEF Event Listener domain or functional role not Present. " +
                    "Invalid use of this Processor";
            throw new MessageProcessingException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        }
        return super.preProcessor(processorContext);
    }

    @Override
    public String getProcessorDescription() {
        return "Applies TCA Policy rules to incoming CEF message. If any thresholds are violated attaches max " +
                "Severity violated threshold to TCA Processor Context";
    }

    @Override
    public TCACEFProcessorContext processMessage(TCACEFProcessorContext processorContext) {

        final String cefMessage = processorContext.getMessage();

        // Determine domain and functional Role
        final EventListener eventListener = processorContext.getCEFEventListener();
        final String functionalRole = eventListener.getEvent().getCommonEventHeader().getFunctionalRole();

        // Get Table containing Functional Role and Thresholds Field Path
        final TCAPolicy tcaPolicy = processorContext.getTCAPolicy();
        final Table<String, String, List<Threshold>> functionalRoleFieldPathsTable =
                TCAUtils.getPolicyFRThresholdsTableSupplier(tcaPolicy).get();

        // Get Policy Field Paths for that functional Role
        final Map<String, List<Threshold>> policyFieldPathsMap = functionalRoleFieldPathsTable.row(functionalRole);
        final Set<String> policyFieldPaths = policyFieldPathsMap.keySet();

        // Get Json Values for Policy Fields
        final Map<String, List<Long>> messageFieldValuesMap = TCAUtils.getJsonPathValue(cefMessage, policyFieldPaths);

        // Determine all violated thresholds per message field Path
        final Map<String, Threshold> violatedThresholdsMap = new HashMap<>();
        for (Map.Entry<String, List<Long>> messageFieldValuesMapEntry : messageFieldValuesMap.entrySet()) {
            final String messageFieldPath = messageFieldValuesMapEntry.getKey();
            final List<Threshold> messageFieldAssociatedPolicyThresholds = policyFieldPathsMap.get(messageFieldPath);
            if (messageFieldAssociatedPolicyThresholds != null) {
                final Optional<Threshold> thresholdOptional = TCAUtils.thresholdCalculator(
                        messageFieldValuesMapEntry.getValue(), messageFieldAssociatedPolicyThresholds);
                if (thresholdOptional.isPresent()) {
                    violatedThresholdsMap.put(messageFieldPath, thresholdOptional.get());
                }
            }
        }

        // No threshold were violated
        if (violatedThresholdsMap.isEmpty()) {

            final String terminationMessage = "No Policy Threshold violated by the VES CEF Message.";
            setTerminatingProcessingMessage(terminationMessage, processorContext);

        } else {

            // If there are policy violations then determine max priority violation
            final Threshold maxSeverityThresholdViolation =
                    TCAUtils.prioritizeThresholdViolations(violatedThresholdsMap);
            final MetricsPerFunctionalRole violatedMetrics = TCAUtils.createViolatedMetrics(tcaPolicy,
                    maxSeverityThresholdViolation, functionalRole);
            // attach policy violation to processor Context
            processorContext.setMetricsPerFunctionalRole(violatedMetrics);

            final String finishMessage = String.format("Policy Threshold violation detected for threshold: %s",
                    maxSeverityThresholdViolation);
            setFinishedProcessingMessage(finishMessage, processorContext);

        }

        return processorContext;
    }
}
