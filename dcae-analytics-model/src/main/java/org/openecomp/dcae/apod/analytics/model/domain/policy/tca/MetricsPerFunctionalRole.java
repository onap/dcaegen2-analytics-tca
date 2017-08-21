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

package org.openecomp.dcae.apod.analytics.model.domain.policy.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * TCA Metrics that need to applied to each functional Role
 *
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetricsPerFunctionalRole extends BaseTCAPolicyModel{


    private static final long serialVersionUID = 1L;

    /**
     * Functional Role to which TCA Policy needs to applied.
     *
     * @param functionalRole New value for Functional Role to which TCA Policy needs to applied
     * @return Functional Role to which TCA Policy needs to applied
     */
    private String functionalRole;

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
     * Creates a copy of given {@link MetricsPerFunctionalRole}
     *
     * @param metricsPerFunctionalRole metrics Per functional role that need to copied
     *
     * @return copy of new metrics per function role with values copied from given metrics per functional role
     */
    public static MetricsPerFunctionalRole copy(final MetricsPerFunctionalRole metricsPerFunctionalRole) {
        final MetricsPerFunctionalRole newMetricsPerFunctionalRole = new MetricsPerFunctionalRole();
        newMetricsPerFunctionalRole.setFunctionalRole(metricsPerFunctionalRole.getFunctionalRole());
        newMetricsPerFunctionalRole.setPolicyScope(metricsPerFunctionalRole.getPolicyScope());
        newMetricsPerFunctionalRole.setPolicyName(metricsPerFunctionalRole.getPolicyName());
        newMetricsPerFunctionalRole.setPolicyVersion(metricsPerFunctionalRole.getPolicyVersion());
        if (metricsPerFunctionalRole.getThresholds() != null) {
            newMetricsPerFunctionalRole.setThresholds(new ArrayList<>(metricsPerFunctionalRole.getThresholds()));
        }
        return newMetricsPerFunctionalRole;
    }


}
