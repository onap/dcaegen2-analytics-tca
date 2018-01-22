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

import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.flow.flowlet.FlowletContext;
import co.cask.cdap.api.flow.flowlet.OutputEmitter;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.onap.dcae.apod.analytics.cdap.common.domain.tca.ThresholdCalculatorOutput;
import org.onap.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCAAlertsAbatementEntity;
import org.onap.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAPolicyPreferences;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.ClosedLoopEventStatus;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.onap.dcae.apod.analytics.tca.utils.TCAUtils;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 9/12/2017.
 */
@SuppressWarnings("unchecked")
public class TCAVESAlertsAbatementFlowletTest extends BaseAnalyticsCDAPTCAUnitTest {

    private static final TCAPolicyPreferences sampleTCAPolicyPreferences = getSampleTCAPolicyPreferences();
    private static final List<MetricsPerEventName> metricsPerEventNames = sampleTCAPolicyPreferences
            .getMetricsPerEventName();
    private final OutputEmitter<String> mockOutputEmitter = mock(OutputEmitter.class);

    private class TestTCAVESAlertsAbatementFlowlet extends TCAVESAlertsAbatementFlowlet {

        public TestTCAVESAlertsAbatementFlowlet(String tcaAlertsAbatementTableName) {
            super(tcaAlertsAbatementTableName);
            this.alertsAbatementOutputEmitter = mockOutputEmitter;
            doNothing().when(mockOutputEmitter).emit(any(String.class));
        }
    }

    @Test
    public void testConfigure() throws Exception {
        final TCAVESAlertsAbatementFlowlet tcavesAlertsAbatementFlowlet =
                new TCAVESAlertsAbatementFlowlet("testTCAAlertsAbatementTableName");
        assertFlowletNameAndDescription(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_ABATEMENT_NAME_FLOWLET,
                CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_ABATEMENT_DESCRIPTION_FLOWLET,
                tcavesAlertsAbatementFlowlet);
    }

    @Test(expected = CDAPSettingsException.class)
    public void testDetermineAbatementAlertsWhenViolatedMetricsEventNameIsBlank() throws Exception {

        final TestTCAVESAlertsAbatementFlowlet tcaAlertsAbatementFlowlet =
                new TestTCAVESAlertsAbatementFlowlet("testTCAAlertsAbatementTableName");
        final Threshold violatedThreshold = getViolatedThreshold(ClosedLoopEventStatus.ONSET);
        final ThresholdCalculatorOutput mockThresholdCalculatorOutput =
                getMockThresholdCalculatorOutput(violatedThreshold);
        when(mockThresholdCalculatorOutput.getViolatedMetricsPerEventName()).thenReturn("");

        tcaAlertsAbatementFlowlet.determineAbatementAlerts(mockThresholdCalculatorOutput);
    }

    @Test
    public void testDetermineAbatementAlertsWhenControlLoopTypeIsONSET() throws Exception {

        final String testTCAAlertsAbatementTableName = "testTCAAlertsAbatementTableName";
        final TestTCAVESAlertsAbatementFlowlet tcaAlertsAbatementFlowlet =
                new TestTCAVESAlertsAbatementFlowlet("testTCAAlertsAbatementTableName");

        final FlowletContext mockFlowletContext = mock(FlowletContext.class);
        final ObjectMappedTable<TCAAlertsAbatementEntity> mockObjectMappedTable = mock(ObjectMappedTable.class);
        when(mockFlowletContext.getDataset(eq(testTCAAlertsAbatementTableName))).thenReturn(mockObjectMappedTable);
        tcaAlertsAbatementFlowlet.initialize(mockFlowletContext);

        doNothing().when(mockObjectMappedTable).write(any(String.class), any(TCAAlertsAbatementEntity.class));

        final Threshold violatedThreshold = getViolatedThreshold(ClosedLoopEventStatus.ONSET);
        final ThresholdCalculatorOutput mockThresholdCalculatorOutput =
                getMockThresholdCalculatorOutput(violatedThreshold);

        tcaAlertsAbatementFlowlet.determineAbatementAlerts(mockThresholdCalculatorOutput);
        verify(mockObjectMappedTable,
                times(1)).write(any(String.class), any(TCAAlertsAbatementEntity.class));
        verify(mockOutputEmitter, times(1)).emit(any(String.class));

    }


    @Test
    public void testDetermineAbatementAlertsWhenControlLoopTypeIsABATEDAndNoPreviousAlertWasSent() throws Exception {

        final String testTCAAlertsAbatementTableName = "testTCAAlertsAbatementTableName";
        final TestTCAVESAlertsAbatementFlowlet tcaAlertsAbatementFlowlet =
                new TestTCAVESAlertsAbatementFlowlet("testTCAAlertsAbatementTableName");

        final FlowletContext mockFlowletContext = mock(FlowletContext.class);
        final ObjectMappedTable<TCAAlertsAbatementEntity> mockObjectMappedTable = mock(ObjectMappedTable.class);
        when(mockFlowletContext.getDataset(eq(testTCAAlertsAbatementTableName))).thenReturn(mockObjectMappedTable);
        tcaAlertsAbatementFlowlet.initialize(mockFlowletContext);

        doNothing().when(mockObjectMappedTable).write(any(String.class), any(TCAAlertsAbatementEntity.class));
        final TCAAlertsAbatementEntity tcaAlertsAbatementEntity = mock(TCAAlertsAbatementEntity.class);
        when(mockObjectMappedTable.read(any(String.class))).thenReturn(tcaAlertsAbatementEntity);
        when(tcaAlertsAbatementEntity.getAbatementSentTS()).thenReturn(null);

        final Threshold violatedThreshold = getViolatedThreshold(ClosedLoopEventStatus.ABATED);
        final ThresholdCalculatorOutput mockThresholdCalculatorOutput =
                getMockThresholdCalculatorOutput(violatedThreshold);

        tcaAlertsAbatementFlowlet.determineAbatementAlerts(mockThresholdCalculatorOutput);
        verify(mockObjectMappedTable,
                times(1)).write(any(String.class), any(TCAAlertsAbatementEntity.class));
        verify(mockOutputEmitter, times(1)).emit(any(String.class));

    }

    @Test
    public void testDetermineAbatementAlertsWhenControlLoopTypeIsABATEDAndPreviousAlertWasAlreadySent() throws
            Exception {

        final String testTCAAlertsAbatementTableName = "testTCAAlertsAbatementTableName";
        final TestTCAVESAlertsAbatementFlowlet tcaAlertsAbatementFlowlet =
                new TestTCAVESAlertsAbatementFlowlet("testTCAAlertsAbatementTableName");

        final FlowletContext mockFlowletContext = mock(FlowletContext.class);
        final ObjectMappedTable<TCAAlertsAbatementEntity> mockObjectMappedTable = mock(ObjectMappedTable.class);
        when(mockFlowletContext.getDataset(eq(testTCAAlertsAbatementTableName))).thenReturn(mockObjectMappedTable);
        tcaAlertsAbatementFlowlet.initialize(mockFlowletContext);

        doNothing().when(mockObjectMappedTable).write(any(String.class), any(TCAAlertsAbatementEntity.class));
        final TCAAlertsAbatementEntity tcaAlertsAbatementEntity = mock(TCAAlertsAbatementEntity.class);
        when(mockObjectMappedTable.read(any(String.class))).thenReturn(tcaAlertsAbatementEntity);
        final long time = new Date().getTime();
        when(tcaAlertsAbatementEntity.getAbatementSentTS()).thenReturn(Long.toString(time));

        final Threshold violatedThreshold = getViolatedThreshold(ClosedLoopEventStatus.ABATED);
        final ThresholdCalculatorOutput mockThresholdCalculatorOutput =
                getMockThresholdCalculatorOutput(violatedThreshold);

        tcaAlertsAbatementFlowlet.determineAbatementAlerts(mockThresholdCalculatorOutput);
        verify(mockObjectMappedTable,
                times(0)).write(any(String.class), any(TCAAlertsAbatementEntity.class));
        verify(mockOutputEmitter, times(0)).emit(any(String.class));

    }


    @Test
    public void testDetermineAbatementAlertsWhenControlLoopTypeIsABATEDAndNoPreviousONSETEventFound() throws
            Exception {

        final String testTCAAlertsAbatementTableName = "testTCAAlertsAbatementTableName";
        final TestTCAVESAlertsAbatementFlowlet tcaAlertsAbatementFlowlet =
                new TestTCAVESAlertsAbatementFlowlet("testTCAAlertsAbatementTableName");

        final FlowletContext mockFlowletContext = mock(FlowletContext.class);
        final ObjectMappedTable<TCAAlertsAbatementEntity> mockObjectMappedTable = mock(ObjectMappedTable.class);
        when(mockFlowletContext.getDataset(eq(testTCAAlertsAbatementTableName))).thenReturn(mockObjectMappedTable);
        tcaAlertsAbatementFlowlet.initialize(mockFlowletContext);

        doNothing().when(mockObjectMappedTable).write(any(String.class), any(TCAAlertsAbatementEntity.class));
        when(mockObjectMappedTable.read(any(String.class))).thenReturn(null);

        final Threshold violatedThreshold = getViolatedThreshold(ClosedLoopEventStatus.ABATED);
        final ThresholdCalculatorOutput mockThresholdCalculatorOutput =
                getMockThresholdCalculatorOutput(violatedThreshold);

        tcaAlertsAbatementFlowlet.determineAbatementAlerts(mockThresholdCalculatorOutput);
        verify(mockObjectMappedTable,
                times(0)).write(any(String.class), any(TCAAlertsAbatementEntity.class));
        verify(mockOutputEmitter, times(0)).emit(any(String.class));

    }

    @Test(expected = CDAPSettingsException.class)
    public void testDetermineAbatementAlertsWhenControlLoopTypeIsNotOnsetOrAbated() throws
            Exception {
        final TestTCAVESAlertsAbatementFlowlet tcaAlertsAbatementFlowlet =
                new TestTCAVESAlertsAbatementFlowlet("testTCAAlertsAbatementTableName");
        final Threshold violatedThreshold = getViolatedThreshold(ClosedLoopEventStatus.CONTINUE);
        final ThresholdCalculatorOutput mockThresholdCalculatorOutput =
                getMockThresholdCalculatorOutput(violatedThreshold);

        tcaAlertsAbatementFlowlet.determineAbatementAlerts(mockThresholdCalculatorOutput);

    }

    private static Threshold getViolatedThreshold(final ClosedLoopEventStatus closedLoopEventStatus) {
        final Threshold violatedThreshold = Threshold.copy(metricsPerEventNames.get(0).getThresholds().get(0));
        violatedThreshold.setClosedLoopEventStatus(closedLoopEventStatus);
        return violatedThreshold;
    }


    private static ThresholdCalculatorOutput getMockThresholdCalculatorOutput(final Threshold violatedThreshold) throws
            Exception {

        final MetricsPerEventName violatedMetricsPerEventName =
                MetricsPerEventName.copy(metricsPerEventNames.get(0));
        violatedMetricsPerEventName.setThresholds(ImmutableList.of(violatedThreshold));
        return getMockThresholdCalculatorOutput(
                fromStream(CEF_MESSAGE_JSON_FILE_LOCATION),
                fromStream(TCA_POLICY_JSON_FILE_LOCATION),
                TCAUtils.writeValueAsString(violatedMetricsPerEventName),
                fromStream(TCA_ALERT_JSON_FILE_LOCATION)
        );
    }


    private static ThresholdCalculatorOutput getMockThresholdCalculatorOutput(final String cefMessage,
                                                                              final String tcaPolicy,
                                                                              final String violatedMetricsPerEventName,
                                                                              final String alertMessage) {
        final ThresholdCalculatorOutput thresholdCalculatorOutput = mock(ThresholdCalculatorOutput.class);
        when(thresholdCalculatorOutput.getCefMessage()).thenReturn(cefMessage);
        when(thresholdCalculatorOutput.getTcaPolicy()).thenReturn(tcaPolicy);
        when(thresholdCalculatorOutput.getViolatedMetricsPerEventName()).thenReturn(violatedMetricsPerEventName);
        when(thresholdCalculatorOutput.getAlertMessage()).thenReturn(alertMessage);
        return thresholdCalculatorOutput;
    }

}
