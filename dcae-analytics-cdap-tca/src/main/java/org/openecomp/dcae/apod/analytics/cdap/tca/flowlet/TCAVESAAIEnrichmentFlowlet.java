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

package org.openecomp.dcae.apod.analytics.cdap.tca.flowlet;

import co.cask.cdap.api.annotation.Output;
import co.cask.cdap.api.annotation.ProcessInput;
import co.cask.cdap.api.flow.flowlet.AbstractFlowlet;
import co.cask.cdap.api.flow.flowlet.FlowletContext;
import co.cask.cdap.api.flow.flowlet.OutputEmitter;
import org.openecomp.dcae.apod.analytics.aai.AAIClientFactory;
import org.openecomp.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfig;
import org.openecomp.dcae.apod.analytics.aai.service.AAIEnrichmentClient;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.openecomp.dcae.apod.analytics.cdap.tca.utils.CDAPTCAUtils;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ClosedLoopEventStatus;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ControlLoopSchemaType;
import org.openecomp.dcae.apod.analytics.model.facade.tca.TCAVESResponse;
import org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Flowlet responsible for doing A&AI Enrichment
 *
 * @author Rajiv Singla . Creation Date: 9/20/2017.
 */
public class TCAVESAAIEnrichmentFlowlet extends AbstractFlowlet {

    private static final Logger LOG = LoggerFactory.getLogger(TCAVESAAIEnrichmentFlowlet.class);

    @Output(CDAPComponentsConstants.TCA_FIXED_VES_AAI_ENRICHMENT_NAME_OUTPUT)
    protected OutputEmitter<String> aaiEnrichmentOutputEmitter;

    private TCAAppPreferences tcaAppPreferences;
    private AAIEnrichmentClient aaiEnrichmentClient;

    @Override
    public void configure() {
        setName(CDAPComponentsConstants.TCA_FIXED_VES_AAI_ENRICHMENT_NAME_FLOWLET);
        setDescription(CDAPComponentsConstants.TCA_FIXED_VES_AAI_ENRICHMENT_DESCRIPTION_FLOWLET);
    }

    @Override
    public void initialize(FlowletContext flowletContext) throws Exception {
        super.initialize(flowletContext);
        tcaAppPreferences = CDAPTCAUtils.getValidatedTCAAppPreferences(flowletContext);
        if (tcaAppPreferences.getEnableAAIEnrichment()) {
            final AAIHttpClientConfig aaiHttpClientConfig =
                    CDAPTCAUtils.createAAIEnrichmentClientConfig(tcaAppPreferences);
            aaiEnrichmentClient = AAIClientFactory.create().getEnrichmentClient(aaiHttpClientConfig);
        }
    }

    @ProcessInput(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_ABATEMENT_NAME_OUTPUT)
    public void performAAIEnrichment(final String alertMessageString) throws IOException {

        // if A&AI enrichment is disabled - no A&AI lookups are required
        if (!tcaAppPreferences.getEnableAAIEnrichment()) {

            LOG.debug("A&AI Enrichment is disabled. Skip A&AI Enrichment for alert: {}", alertMessageString);
            aaiEnrichmentOutputEmitter.emit(alertMessageString);

        } else {

            // determine closed Loop Event Status
            final TCAVESResponse tcavesResponse = TCAUtils.readValue(alertMessageString, TCAVESResponse.class);
            final ClosedLoopEventStatus closedLoopEventStatus =
                    ClosedLoopEventStatus.valueOf(tcavesResponse.getClosedLoopEventStatus());

            if (closedLoopEventStatus == ClosedLoopEventStatus.ONSET) {
                LOG.debug("Performing A&AI Enrichment of ONSET Alert: {}", alertMessageString);
                final ControlLoopSchemaType controlLoopSchemaType =
                        TCAUtils.determineControlLoopSchemaType(tcavesResponse);
                final String sourceName = TCAUtils.determineSourceName(tcavesResponse);
                LOG.debug("A&AI Source Name: {}, Control Loop Schema Type: {} for ONSET Alert: {}",
                        sourceName, controlLoopSchemaType, alertMessageString);

                if (controlLoopSchemaType == ControlLoopSchemaType.VM) {
                    final String aaiVMEnrichmentAPIPath = tcaAppPreferences.getAaiVMEnrichmentAPIPath();
                    TCAUtils.doAAIVMEnrichment(tcavesResponse, aaiEnrichmentClient, aaiVMEnrichmentAPIPath,
                            alertMessageString, sourceName);
                } else {
                    final String aaiVNFEnrichmentAPIPath = tcaAppPreferences.getAaiVNFEnrichmentAPIPath();
                    TCAUtils.doAAIVNFEnrichment(tcavesResponse, aaiEnrichmentClient, aaiVNFEnrichmentAPIPath,
                            alertMessageString, sourceName);
                }

                final String aaiEnrichedAlert = TCAUtils.writeValueAsString(tcavesResponse);
                LOG.debug("Emitting Alert after A&AI Enrichment: {}", aaiEnrichedAlert);
                aaiEnrichmentOutputEmitter.emit(alertMessageString);

                // skip A&AI Enrichment of alerts with closed Loop Event Status - ABATED
            } else if (closedLoopEventStatus == ClosedLoopEventStatus.ABATED) {
                LOG.debug("Skipping Enrichment of Abated Alert: {}", alertMessageString);
                aaiEnrichmentOutputEmitter.emit(alertMessageString);

            } else {
                // unsupported closed loop event status
                final String errorMessage = String.format(
                        "Unexpected ClosedLoopEventStatus: %s. Only ONSET and ABATED are supported." +
                                "Ignoring alert: %s", closedLoopEventStatus, alertMessageString);
                throw new CDAPSettingsException(errorMessage, LOG, new IllegalStateException(errorMessage));
            }
        }
    }
}
