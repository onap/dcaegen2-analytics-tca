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

package org.openecomp.dcae.apod.analytics.cdap.tca.validator;

import org.openecomp.dcae.apod.analytics.cdap.common.validation.CDAPAppSettingsValidator;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAPolicyPreferences;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Direction;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerFunctionalRole;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils;

import java.util.List;

import static org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isEmpty;

/**
 * Validates TCA Policy Preferences
 * <p>
 * @author Rajiv Singla . Creation Date: 11/29/2016.
 */
public class TCAPolicyPreferencesValidator implements CDAPAppSettingsValidator<TCAPolicyPreferences,
        GenericValidationResponse<TCAPolicyPreferences>> {

    private static final long serialVersionUID = 1L;

    @Override
    public GenericValidationResponse<TCAPolicyPreferences> validateAppSettings(
            final TCAPolicyPreferences tcaPolicyPreferences) {

        final GenericValidationResponse<TCAPolicyPreferences> validationResponse = new GenericValidationResponse<>();

        // validate TCA Policy must domain present
        final String domain = tcaPolicyPreferences.getDomain();
        if (isEmpty(domain)) {
            validationResponse.addErrorMessage("domain", "TCA Policy must have only one domain present");
        }

        // validate TCA Policy must have at least one functional role
        final List<String> policyFunctionalRoles = TCAUtils.getPolicyFunctionalRoles(tcaPolicyPreferences);
        if (policyFunctionalRoles.isEmpty()) {
            validationResponse.addErrorMessage("metricsPerFunctionalRoles",
                    "TCA Policy must have at least one or more functional roles");
        }

        final List<MetricsPerFunctionalRole> metricsPerFunctionalRoles =
                tcaPolicyPreferences.getMetricsPerFunctionalRole();

        // validate each Functional Role must have at least one threshold
        for (MetricsPerFunctionalRole metricsPerFunctionalRole : metricsPerFunctionalRoles) {
            if (metricsPerFunctionalRole.getThresholds().isEmpty()) {
                validationResponse.addErrorMessage("thresholds",
                        "TCA Policy Functional Role must have at least one threshold. " +
                                "Functional Role causing this validation error:" + metricsPerFunctionalRole);
            }
        }

        // validate each threshold must have non null - fieldPath, thresholdValue, direction and severity
        for (MetricsPerFunctionalRole metricsPerFunctionalRole : metricsPerFunctionalRoles) {
            final List<Threshold> functionalRoleThresholds = metricsPerFunctionalRole.getThresholds();
            for (Threshold functionalRoleThreshold : functionalRoleThresholds) {
                final String fieldPath = functionalRoleThreshold.getFieldPath();
                final Long thresholdValue = functionalRoleThreshold.getThresholdValue();
                final Direction direction = functionalRoleThreshold.getDirection();
                final EventSeverity severity = functionalRoleThreshold.getSeverity();
                if (isEmpty(fieldPath) || thresholdValue == null || direction == null || severity == null) {
                    validationResponse.addErrorMessage("threshold",
                            "TCA Policy threshold must have fieldPath, thresholdValue, direction and severity present."
                                    + "Threshold causing this validation error:" + functionalRoleThreshold);
                }
            }
        }


        return validationResponse;
    }
}
