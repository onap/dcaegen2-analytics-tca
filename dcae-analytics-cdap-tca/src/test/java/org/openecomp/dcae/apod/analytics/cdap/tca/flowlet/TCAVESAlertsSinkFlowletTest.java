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

import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.flow.flowlet.FlowletContext;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertEntity;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 12/16/2016.
 */
public class TCAVESAlertsSinkFlowletTest extends BaseAnalyticsCDAPTCAUnitTest {


    @Test
    public void testConfigure() throws Exception {
        final TCAVESAlertsSinkFlowlet tcavesAlertsSinkFlowlet =
                new TCAVESAlertsSinkFlowlet("testTCAVESAlertTableName");
        assertFlowletNameAndDescription(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_SINK_NAME_FLOWLET,
                CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_SINK_DESCRIPTION_FLOWLET, tcavesAlertsSinkFlowlet);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void saveAlerts() throws Exception {

        final String testAlertTableName = "testTCAVESAlertTableName";

        final TCAVESAlertsSinkFlowlet tcavesAlertsSinkFlowlet = new TCAVESAlertsSinkFlowlet(testAlertTableName);

        final FlowletContext mockFlowletContext = Mockito.mock(FlowletContext.class);
        final ObjectMappedTable mockObjectMappedTable = Mockito.mock(ObjectMappedTable.class);
        when(mockFlowletContext.getDataset(eq(testAlertTableName))).thenReturn(mockObjectMappedTable);
        tcavesAlertsSinkFlowlet.initialize(mockFlowletContext);
        final ObjectMappedTable tcaVESAlertsTableName =
                getPrivateFiledValue(tcavesAlertsSinkFlowlet, "tcaVESAlertsTable", ObjectMappedTable.class);
        assertTrue(tcaVESAlertsTableName == mockObjectMappedTable);

        doNothing().when(mockObjectMappedTable).write(any(String.class), any(TCAVESAlertEntity.class));
        final String testAlertMessage = "testMessage";
        tcavesAlertsSinkFlowlet.saveAlerts(testAlertMessage);

        verify(mockObjectMappedTable,
                times(1)).write(any(String.class), any(TCAVESAlertEntity.class));

    }

}
