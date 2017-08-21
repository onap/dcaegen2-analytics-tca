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

import co.cask.cdap.api.app.ApplicationSpecification;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.flow.flowlet.FlowletContext;
import co.cask.cdap.api.flow.flowlet.OutputEmitter;
import co.cask.cdap.api.metrics.Metrics;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca.TCAMessageStatusEntity;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.tca.utils.CDAPTCAUtils;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 12/19/2016.
 */
@SuppressWarnings("unchecked")
public class TCAVESThresholdViolationCalculatorFlowletTest extends BaseAnalyticsCDAPTCAUnitTest {

    private static final String messageStatusTableName = "TEST_MESSAGE_STATUS_TABLE";

    private TCAVESThresholdViolationCalculatorFlowlet violationCalculatorFlowlet;
    private Metrics metrics;
    private OutputEmitter outputEmitter;
    private ObjectMappedTable<TCAMessageStatusEntity> vesMessageStatusTable;

    private static class TCATestVESThresholdViolationCalculatorFlowlet extends
            TCAVESThresholdViolationCalculatorFlowlet {
        public TCATestVESThresholdViolationCalculatorFlowlet(
                final String messageStatusTableName,
                final OutputEmitter tcaAlertOutputEmitter,
                ObjectMappedTable<TCAMessageStatusEntity> vesMessageStatusTable,
                Metrics metrics) {
            super(messageStatusTableName);
            this.tcaAlertOutputEmitter = tcaAlertOutputEmitter;
            this.metrics = metrics;
        }
    }

    @Before
    public void before() {
        violationCalculatorFlowlet = new TCAVESThresholdViolationCalculatorFlowlet(messageStatusTableName);
        vesMessageStatusTable = Mockito.mock(ObjectMappedTable.class);
        outputEmitter = Mockito.mock(OutputEmitter.class);
        metrics = Mockito.mock(Metrics.class);
    }

    @Test
    public void testConfigure() throws Exception {
        assertFlowletNameAndDescription(
                CDAPComponentsConstants.TCA_FIXED_VES_THRESHOLD_VIOLATION_CALCULATOR_NAME_FLOWLET,
                CDAPComponentsConstants.TCA_FIXED_VES_THRESHOLD_VIOLATION_CALCULATOR_DESCRIPTION_FLOWLET,
                violationCalculatorFlowlet);
    }

    @Test
    public void testInitialize() throws Exception {
        final FlowletContext mockFlowletContext = initializeFlowlet(violationCalculatorFlowlet, vesMessageStatusTable);
        verify(mockFlowletContext, times(1)).getDataset(anyString());
    }

    @Test
    public void testFilterVESMessagesWhenVESMessageIsInApplicable() throws Exception {
        final TCATestVESThresholdViolationCalculatorFlowlet thresholdViolationCalculatorFlowlet =
                createTestViolationCalculator(vesMessageStatusTable, outputEmitter, metrics);
        initializeFlowlet(thresholdViolationCalculatorFlowlet, vesMessageStatusTable);
        thresholdViolationCalculatorFlowlet.filterVESMessages("inapplicable");
        verify(vesMessageStatusTable, times(1)).write(anyString(),
                any(TCAMessageStatusEntity.class));
    }

    @Test
    public void testFilterVESMessagesWhenVESMessageIsCompliant() throws Exception {
        final TCATestVESThresholdViolationCalculatorFlowlet thresholdViolationCalculatorFlowlet =
                createTestViolationCalculator(vesMessageStatusTable, outputEmitter, metrics);
        initializeFlowlet(thresholdViolationCalculatorFlowlet, vesMessageStatusTable);
        thresholdViolationCalculatorFlowlet.filterVESMessages(getValidCEFMessage());
        verify(vesMessageStatusTable, times(1)).write(anyString(),
                any(TCAMessageStatusEntity.class));
    }

    @Test
    public void testFilterVESMessagesWhenVESMessageNonCompliant() throws Exception {
        final TCATestVESThresholdViolationCalculatorFlowlet thresholdViolationCalculatorFlowlet =
                createTestViolationCalculator(vesMessageStatusTable, outputEmitter, metrics);
        final FlowletContext flowletContext =
                initializeFlowlet(thresholdViolationCalculatorFlowlet, vesMessageStatusTable);
        final TCAPolicy policy = CDAPTCAUtils.getValidatedTCAPolicyPreferences(flowletContext);
        final Threshold threshold = policy.getMetricsPerFunctionalRole().get(0).getThresholds().get(0);
        final Long thresholdValue = threshold.getThresholdValue();
        final EventListener thresholdViolatingMessage = getCEFEventListener();
        thresholdViolatingMessage.getEvent().getMeasurementsForVfScalingFields().getVNicUsageArray().get(0).setBytesIn
                (thresholdValue - 1);
        thresholdViolationCalculatorFlowlet.filterVESMessages(
                ANALYTICS_MODEL_OBJECT_MAPPER.writeValueAsString(thresholdViolatingMessage));
        verify(vesMessageStatusTable, times(1)).write(anyString(),
                any(TCAMessageStatusEntity.class));
        verify(outputEmitter, times(1)).emit(anyString());
    }

    private static TCATestVESThresholdViolationCalculatorFlowlet createTestViolationCalculator(
            final ObjectMappedTable<TCAMessageStatusEntity> vesMessageStatusTable,
            final OutputEmitter outputEmitter, final Metrics metrics) {
        doNothing().when(outputEmitter).emit(anyString());
        doNothing().when(metrics).count(anyString(), anyInt());
        doNothing().when(vesMessageStatusTable).write(anyString(), any(TCAMessageStatusEntity.class));
        return new TCATestVESThresholdViolationCalculatorFlowlet(messageStatusTableName, outputEmitter,
                vesMessageStatusTable, metrics);
    }

    private static <T extends TCAVESThresholdViolationCalculatorFlowlet> FlowletContext initializeFlowlet(
            T calculatorFlowlet, ObjectMappedTable<TCAMessageStatusEntity> vesMessageStatusTable) {
        final FlowletContext mockFlowletContext = getTestFlowletContextWithValidPolicy();
        when(mockFlowletContext.getDataset(anyString())).thenReturn(vesMessageStatusTable);
        when(mockFlowletContext.getInstanceId()).thenReturn(1);
        ApplicationSpecification mockApplicationSpecification = Mockito.mock(ApplicationSpecification.class);
        when(mockFlowletContext.getApplicationSpecification()).thenReturn(mockApplicationSpecification);
        when(mockApplicationSpecification.getName()).thenReturn("TestTCAAppName");
        try {
            calculatorFlowlet.initialize(mockFlowletContext);
            return mockFlowletContext;
        } catch (Exception e) {
            LOG.error("error while flowlet initialization");
            throw new RuntimeException(e);
        }
    }

}
