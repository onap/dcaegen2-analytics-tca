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

package org.onap.dcae.apod.analytics.cdap.tca.validator;

import org.onap.dcae.apod.analytics.cdap.common.validation.CDAPAppSettingsValidator;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAPolicyPreferences;
import org.onap.dcae.apod.analytics.common.validation.GenericValidationResponse;
import org.onap.dcae.apod.analytics.model.domain.cef.EventSeverity;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.ClosedLoopEventStatus;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.ControlLoopSchemaType;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.Direction;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.onap.dcae.apod.analytics.tca.utils.TCAUtils;

import java.util.List;

import static org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils.isEmpty;

/**
 * Validates TCA Policy Preferences
 * <p>
 *
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

        // validate TCA Policy must have at least one event name
        final List<String> policyEventNames = TCAUtils.getPolicyEventNames(tcaPolicyPreferences);
        if (policyEventNames.isEmpty()) {
            validationResponse.addErrorMessage("metricsPerEventNames",
                    "TCA Policy must have at least one or more event names");
        }

        final List<MetricsPerEventName> metricsPerEventNames =
                tcaPolicyPreferences.getMetricsPerEventName();

        // validate Metrics Per Event Name
        for (MetricsPerEventName metricsPerEventName : metricsPerEventNames) {

            // event name must be present
            final String eventName = metricsPerEventName.getEventName();
            if (isEmpty(eventName)) {
                validationResponse.addErrorMessage("eventName",
                        "TCA Policy eventName is not present for metricsPerEventName:" + metricsPerEventName);
            }

            // control Loop Schema type must be present
            final ControlLoopSchemaType controlLoopSchemaType = metricsPerEventName.getControlLoopSchemaType();
            if (controlLoopSchemaType == null) {
                validationResponse.addErrorMessage("controlLoopEventType",
                        "TCA Policy controlLoopSchemaType is not present for metricsPerEventName:"
                                + metricsPerEventName);
            }

            // must have at least 1 threshold defined
            if (metricsPerEventName.getThresholds() == null || metricsPerEventName.getThresholds().isEmpty()) {
                validationResponse.addErrorMessage("thresholds",
                        "TCA Policy event Name must have at least one threshold. " +
                                "Event Name causing this validation error:" + metricsPerEventName);
            } else {
                // validate each threshold must have non null - fieldPath, thresholdValue, direction and severity
                final List<Threshold> eventNameThresholds = metricsPerEventName.getThresholds();
                for (Threshold eventNameThreshold : eventNameThresholds) {
                    final String fieldPath = eventNameThreshold.getFieldPath();
                    final Long thresholdValue = eventNameThreshold.getThresholdValue();
                    final Direction direction = eventNameThreshold.getDirection();
                    final EventSeverity severity = eventNameThreshold.getSeverity();
                    final ClosedLoopEventStatus closedLoopEventStatus = eventNameThreshold.getClosedLoopEventStatus();
                    if (isEmpty(fieldPath) || thresholdValue == null || direction == null || severity == null ||
                            closedLoopEventStatus == null) {
                        validationResponse.addErrorMessage("threshold",
                                "TCA Policy threshold must have fieldPath,thresholdValue,direction, " +
                                        "closedLoopEventStatus and severity defined." +
                                        "Threshold causing this validation error:" + eventNameThreshold);
                    }
                }
            }
        }
        return validationResponse;
    }
}
