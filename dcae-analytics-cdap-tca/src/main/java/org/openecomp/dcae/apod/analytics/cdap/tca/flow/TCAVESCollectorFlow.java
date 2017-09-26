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

package org.openecomp.dcae.apod.analytics.cdap.tca.flow;

import co.cask.cdap.api.flow.AbstractFlow;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.tca.flowlet.TCAVESAAIEnrichmentFlowlet;
import org.openecomp.dcae.apod.analytics.cdap.tca.flowlet.TCAVESAlertsAbatementFlowlet;
import org.openecomp.dcae.apod.analytics.cdap.tca.flowlet.TCAVESAlertsSinkFlowlet;
import org.openecomp.dcae.apod.analytics.cdap.tca.flowlet.TCAVESMessageRouterFlowlet;
import org.openecomp.dcae.apod.analytics.cdap.tca.flowlet.TCAVESThresholdViolationCalculatorFlowlet;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppConfig;

/**
 * TCA Flow for VES (Virtual Event Streaming) Collector Flow
 *
 * @author Rajiv Singla . Creation Date: 11/3/2016.
 */
public class TCAVESCollectorFlow extends AbstractFlow {

    private final TCAAppConfig tcaAppConfig;

    public TCAVESCollectorFlow(TCAAppConfig tcaAppConfig) {
        this.tcaAppConfig = tcaAppConfig;
    }

    @Override
    protected void configure() {

        setName(CDAPComponentsConstants.TCA_FIXED_VES_COLLECTOR_NAME_FLOW);
        setDescription(CDAPComponentsConstants.TCA_FIXED_VES_COLLECTOR_DESCRIPTION_FLOW);

        final TCAVESMessageRouterFlowlet messageRouterFlowlet = new TCAVESMessageRouterFlowlet();
        addFlowlet(messageRouterFlowlet);

        final TCAVESThresholdViolationCalculatorFlowlet thresholdViolationCalculatorFlowlet =
                new TCAVESThresholdViolationCalculatorFlowlet(tcaAppConfig.getTcaVESMessageStatusTableName());
        addFlowlet(thresholdViolationCalculatorFlowlet, tcaAppConfig.getThresholdCalculatorFlowletInstances());

        final TCAVESAlertsAbatementFlowlet tcavesAlertsAbatementFlowlet =
                new TCAVESAlertsAbatementFlowlet(tcaAppConfig.getTcaAlertsAbatementTableName());
        addFlowlet(tcavesAlertsAbatementFlowlet);

        final TCAVESAAIEnrichmentFlowlet tcavesaaiEnrichmentFlowlet = new TCAVESAAIEnrichmentFlowlet();
        addFlowlet(tcavesaaiEnrichmentFlowlet);

        final TCAVESAlertsSinkFlowlet alertsSinkFlowlet =
                new TCAVESAlertsSinkFlowlet(tcaAppConfig.getTcaVESAlertsTableName());
        addFlowlet(alertsSinkFlowlet);


        // connect DMaaP MR VES Subscriber output stream to VES Message Router Flowlet
        connectStream(tcaAppConfig.getTcaSubscriberOutputStreamName(), messageRouterFlowlet);
        // connect message router to VES threshold calculator
        connect(messageRouterFlowlet, thresholdViolationCalculatorFlowlet);
        // connect VES threshold calculator flowlet to Alerts Abatement Flowlet
        connect(thresholdViolationCalculatorFlowlet, tcavesAlertsAbatementFlowlet);
        // connect Alerts Abatement flowlet to AAI Enrichment Flowlet
        connect(tcavesAlertsAbatementFlowlet, tcavesaaiEnrichmentFlowlet);
        // connect A&AI Enrichment flowlet to Alerts Sink Flowlet
        connect(tcavesaaiEnrichmentFlowlet,  alertsSinkFlowlet);

    }
}
