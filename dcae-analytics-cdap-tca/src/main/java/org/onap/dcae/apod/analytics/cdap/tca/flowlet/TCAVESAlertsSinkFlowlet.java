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

package org.onap.dcae.apod.analytics.cdap.tca.flowlet;

import co.cask.cdap.api.annotation.ProcessInput;
import co.cask.cdap.api.annotation.Property;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.flow.flowlet.AbstractFlowlet;
import co.cask.cdap.api.flow.flowlet.FlowletContext;
import org.onap.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertEntity;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertsPersister;

/**
 * Saves TCA VES Alert Messages in a Time series Table
 *
 * @author Rajiv Singla . Creation Date: 11/15/2016.
 */
public class TCAVESAlertsSinkFlowlet extends AbstractFlowlet {

    @Property
    private final String tcaVESAlertsTableName;

    private ObjectMappedTable<TCAVESAlertEntity> tcaVESAlertsTable;

    public TCAVESAlertsSinkFlowlet(String tcaVESAlertsTableName) {
        this.tcaVESAlertsTableName = tcaVESAlertsTableName;
    }

    @Override
    public void configure() {
        setName(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_SINK_NAME_FLOWLET);
        setDescription(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_SINK_DESCRIPTION_FLOWLET);
    }

    @Override
    public void initialize(FlowletContext flowletContext) throws Exception {
        super.initialize(flowletContext);
        tcaVESAlertsTable = getContext().getDataset(tcaVESAlertsTableName);
    }

    /**
     * Saves messages to Alerts table
     *
     * @param alertMessage alert message
     */
    @ProcessInput(CDAPComponentsConstants.TCA_FIXED_VES_AAI_ENRICHMENT_NAME_OUTPUT)
    public void saveAlerts(String alertMessage) {
        // Saves alert message in alerts table
        TCAVESAlertsPersister.persist(alertMessage, tcaVESAlertsTable);
    }

}
