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

package org.onap.dcae.apod.analytics.model.domain.policy.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * TCA Metrics that need to applied to each Event Name
 *
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetricsPerEventName extends BaseTCAPolicyModel{


    private static final long serialVersionUID = 1L;

    /**
     * Event Name to which TCA Policy needs to applied.
     *
     * @param eventName New value for eventName to which TCA Policy needs to applied
     * @return Event Name to which TCA Policy needs to applied
     */
    private String eventName;

    /**
     * Control Loop Schema Type
     *
     * @param controlLoopSchemaType New value for Control Loop Schema Type
     * @return Control Loop Schema Type
     */
    private ControlLoopSchemaType controlLoopSchemaType;

    /**
     * Policy Scope
     *
     * @param policyScope New value for Policy Scope
     * @return Policy Scope
     */
    private String policyScope;

    /**
     * Policy Name
     *
     * @param policyName New value for Policy Name
     * @return Policy Name
     */
    private String policyName;

    /**
     * Policy Version
     *
     * @param policyVersion New value for Policy Version
     * @return Policy Version
     */
    private String policyVersion;

    /**
     * Policy Thresholds
     *
     * @param thresholds New value for Policy Thresholds
     * @return Policy Thresholds
     */
    private List<Threshold> thresholds;


    /**
     * Creates a deep copy of given {@link MetricsPerEventName}
     *
     * @param metricsPerEventName metrics Per Event Name that need to copied
     *
     * @return copy of new metrics per event Name with values copied from given metrics per Event Name
     */
    public static MetricsPerEventName copy(final MetricsPerEventName metricsPerEventName) {
        final MetricsPerEventName newMetricsPerEventName = new MetricsPerEventName();
        newMetricsPerEventName.setEventName(metricsPerEventName.getEventName());
        newMetricsPerEventName.setControlLoopSchemaType(metricsPerEventName.getControlLoopSchemaType());
        newMetricsPerEventName.setPolicyScope(metricsPerEventName.getPolicyScope());
        newMetricsPerEventName.setPolicyName(metricsPerEventName.getPolicyName());
        newMetricsPerEventName.setPolicyVersion(metricsPerEventName.getPolicyVersion());
        if (metricsPerEventName.getThresholds() != null) {
            List<Threshold> newThresholds = new ArrayList<>(metricsPerEventName.getThresholds().size());
            for( Threshold threshold : metricsPerEventName.getThresholds()) {
                newThresholds.add(Threshold.copy(threshold));
            }
            newMetricsPerEventName.setThresholds(newThresholds);
        }
        return newMetricsPerEventName;
    }


}
