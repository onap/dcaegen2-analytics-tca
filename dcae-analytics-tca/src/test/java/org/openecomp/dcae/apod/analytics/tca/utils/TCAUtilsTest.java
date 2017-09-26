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

package org.openecomp.dcae.apod.analytics.tca.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.MessageProcessingException;
import org.openecomp.dcae.apod.analytics.model.domain.cef.CommonEventHeader;
import org.openecomp.dcae.apod.analytics.model.domain.cef.Domain;
import org.openecomp.dcae.apod.analytics.model.domain.cef.Event;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ClosedLoopEventStatus;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ControlLoopSchemaType;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Direction;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.openecomp.dcae.apod.analytics.model.facade.tca.AAI;
import org.openecomp.dcae.apod.analytics.model.facade.tca.TCAVESResponse;
import org.openecomp.dcae.apod.analytics.tca.BaseAnalyticsTCAUnitTest;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFProcessorContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public class TCAUtilsTest extends BaseAnalyticsTCAUnitTest {

    @Test
    public void testGetPolicyEventNames() throws Exception {

        final TCAPolicy sampleTCAPolicy = getSampleTCAPolicy();
        final List<String> eventNames = TCAUtils.getPolicyEventNames(sampleTCAPolicy);

        assertThat("Policy event names must contain vFirewall, vLoadBalancer, virtualVMEventName", eventNames,
                containsInAnyOrder("Mfvs_eNodeB_RANKPI", "vLoadBalancer", "virtualVMEventName"));
    }

    @Test
    public void testGetPolicyEventNamesSupplier() throws Exception {
        final TCAPolicy sampleTCAPolicy = getSampleTCAPolicy();
        final Supplier<List<String>> policyEventNamesSupplier = TCAUtils.getPolicyEventNamesSupplier
                (sampleTCAPolicy);
        final List<String> eventNames = policyEventNamesSupplier.get();
        assertThat("Policy event names must contain vFirewall and vLoadBalancer", eventNames,
                containsInAnyOrder("Mfvs_eNodeB_RANKPI", "vLoadBalancer", "virtualVMEventName"));
    }

    @Test
    public void testProcessCEFMessage() throws Exception {
        final String cefMessageString = fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
        final TCACEFProcessorContext tcacefProcessorContext = TCAUtils.filterCEFMessage(cefMessageString,
                getSampleTCAPolicy());
        assertThat("TCAECEFProcessor Processor Context can continue flag is true", tcacefProcessorContext
                .canProcessingContinue(), is(true));
    }

    @Test
    public void testGetPolicyFRThresholdsTableSupplier() throws Exception {
        final Table<String, String, List<Threshold>> policyFRThresholdPathTable = TCAUtils
                .getPolicyEventNameThresholdsTableSupplier(getSampleTCAPolicy()).get();

        final Map<String, List<Threshold>> eNodeBRankpi = policyFRThresholdPathTable.row("Mfvs_eNodeB_RANKPI");
        final Map<String, List<Threshold>> vLoadBalancer = policyFRThresholdPathTable.row("vLoadBalancer");

        final Set<String> eNodeBRankpiFieldPaths = eNodeBRankpi.keySet();
        final Set<String> vLoadBalancerPaths = vLoadBalancer.keySet();

        final String receivedBroadcastPacketsFieldPath =
                "$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated";
        assertThat("eNodeBRankpi threshold field path size must be " +
                        "$.event.measurementsForVfScalingFields.vNicPerformanceArray[*]" +
                        ".receivedBroadcastPacketsAccumulated",
                eNodeBRankpiFieldPaths.iterator().next(),
                is(receivedBroadcastPacketsFieldPath));

        assertThat("vLoadBalancer threshold field path size must be " +
                        "$.event.measurementsForVfScalingFields.vNicPerformanceArray[*]" +
                        ".receivedBroadcastPacketsAccumulated",
                vLoadBalancerPaths.iterator().next(),
                is(receivedBroadcastPacketsFieldPath));

        final List<Threshold> eNodeBRankpiThresholds = policyFRThresholdPathTable.get("Mfvs_eNodeB_RANKPI",
                receivedBroadcastPacketsFieldPath);
        final List<Threshold> vLoadBalancerThresholds = policyFRThresholdPathTable.get("vLoadBalancer",
                receivedBroadcastPacketsFieldPath);

        assertThat("eNodeBRankpi Threshold size must be 3", eNodeBRankpiThresholds.size(), is(3));
        assertThat("vLoadBalancer Threshold size must be 2", vLoadBalancerThresholds.size(), is(2));
    }

    @Test
    public void testGetJsonPathValueWithValidMessageAndPolicy() throws Exception {
        final String cefMessageString = fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
        final String jsonPath =
                "$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated";
        final ImmutableSet<String> fieldPaths = ImmutableSet.of(jsonPath);
        final Map<String, List<Long>> jsonPathValueMap = TCAUtils.getJsonPathValue(cefMessageString, fieldPaths);
        assertThat("Json Path value must match", jsonPathValueMap.get(jsonPath).get(0), is(5000L));

    }

    @Test
    public void testGetJsonPathValueWithValidPath() throws Exception {
        final String cefMessageString = fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
        final String jsonPath = "$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].invalid";
        final ImmutableSet<String> fieldPaths = ImmutableSet.of(jsonPath);
        final Map<String, List<Long>> jsonPathValueMap = TCAUtils.getJsonPathValue(cefMessageString, fieldPaths);
        assertThat("Json path value must be empty", jsonPathValueMap.size(), is(0));

    }


    @Test
    public void testCreateNewTCAVESResponseWithVFControlLoopSchemaType() throws Exception {
        TCACEFProcessorContext tcacefProcessorContext = mock(TCACEFProcessorContext.class);

        MetricsPerEventName metricsPerEventName = mock(MetricsPerEventName.class);
        when(metricsPerEventName.getThresholds()).thenReturn(getThresholds());
        when(metricsPerEventName.getPolicyScope()).thenReturn("Test Policy scope");
        when(tcacefProcessorContext.getMetricsPerEventName()).thenReturn(metricsPerEventName);
        when(metricsPerEventName.getEventName()).thenReturn("testEventName");
        when(metricsPerEventName.getControlLoopSchemaType()).thenReturn(ControlLoopSchemaType.VM);

        when(tcacefProcessorContext.getCEFEventListener()).thenReturn(getCEFEventListener());
        TCAVESResponse tcaVESResponse = TCAUtils.createNewTCAVESResponse(tcacefProcessorContext, "TCA_APP_NAME");

        //TODO :  Add proper assertions, as the usage is not clearly understood
        assertThat(tcaVESResponse.getClosedLoopControlName(),
                is("CL-LBAL-LOW-TRAFFIC-SIG-FB480F95-A453-6F24-B767-FD703241AB1A"));
        assertThat(tcaVESResponse.getVersion(), is("Test Version"));
        assertThat(tcaVESResponse.getPolicyScope(), is("Test Policy scope"));
        assertNull(tcaVESResponse.getAai().getGenericVNFId());
        assertNotNull(tcaVESResponse.getAai().getGenericServerId());
    }

    @Test
    public void testCreateNewTCAVESResponseWithFunctionalRolevFirewall() throws Exception {
        TCACEFProcessorContext tcacefProcessorContext = mock(TCACEFProcessorContext.class);

        MetricsPerEventName metricsPerEventName = mock(MetricsPerEventName.class);
        when(metricsPerEventName.getThresholds()).thenReturn(getThresholds());
        when(metricsPerEventName.getPolicyScope()).thenReturn("Test Policy scope");
        when(tcacefProcessorContext.getMetricsPerEventName()).thenReturn(metricsPerEventName);
        when(metricsPerEventName.getEventName()).thenReturn("vFirewall");

        when(tcacefProcessorContext.getCEFEventListener()).thenReturn(getCEFEventListener());
        TCAVESResponse tcaVESResponse = TCAUtils.createNewTCAVESResponse(tcacefProcessorContext, "TCA_APP_NAME");

        //TODO :  Add proper assertions, as the usage is not clearly understood
        assertThat(tcaVESResponse.getClosedLoopControlName(),
                is("CL-LBAL-LOW-TRAFFIC-SIG-FB480F95-A453-6F24-B767-FD703241AB1A"));
        assertThat(tcaVESResponse.getVersion(), is("Test Version"));
        assertThat(tcaVESResponse.getPolicyScope(), is("Test Policy scope"));
        assertNotNull(tcaVESResponse.getAai().getGenericVNFId());
        assertNull(tcaVESResponse.getAai().getGenericServerId());
    }

    @Rule
    public ExpectedException expectedIllegalArgumentException = ExpectedException.none();

    @Test
    public void testCreateNewTCAVESResponseNullFunctionalRole() throws Exception {
        expectedIllegalArgumentException.expect(MessageProcessingException.class);
        expectedIllegalArgumentException.expectCause(isA(IllegalArgumentException.class));
        expectedIllegalArgumentException.expectMessage("No violations metrics. Unable to create VES Response");

        TCACEFProcessorContext tcacefProcessorContext = mock(TCACEFProcessorContext.class);
        TCAVESResponse tcaVESResponse = TCAUtils.createNewTCAVESResponse(tcacefProcessorContext, "TCA_APP_NAME");
        assertNotNull(tcaVESResponse.getClosedLoopControlName());
    }

    @Test
    public void testPrioritizeThresholdViolations() throws Exception {

        Map<String, Threshold> thresholdMap = new HashMap<>();
        Threshold majorThreshold = mock(Threshold.class);
        when(majorThreshold.getSeverity()).thenReturn(EventSeverity.MAJOR);
        thresholdMap.put("MAJOR", majorThreshold);

        Threshold result1 = TCAUtils.prioritizeThresholdViolations(thresholdMap);
        assertEquals(result1.getSeverity(), EventSeverity.MAJOR);

        Threshold criticalThreshold = mock(Threshold.class);
        when(criticalThreshold.getSeverity()).thenReturn(EventSeverity.CRITICAL);
        thresholdMap.put("CRITICAL", criticalThreshold);

        Threshold result2 = TCAUtils.prioritizeThresholdViolations(thresholdMap);
        assertEquals(result2.getSeverity(), EventSeverity.CRITICAL);
    }

    @Test
    public void testCreateViolatedMetrics() throws Exception {
        TCAPolicy tcaPolicy = getSampleTCAPolicy();
        Threshold violatedThreshold = getCriticalThreshold();
        String functionalRole = "Mfvs_eNodeB_RANKPI";
        MetricsPerEventName result = TCAUtils.createViolatedMetrics(tcaPolicy, violatedThreshold, functionalRole);
        assertThat(result.getPolicyScope(), is("resource=vFirewall;type=configuration"));
        assertThat(result.getPolicyName(), is("configuration.dcae.microservice.tca.xml"));
    }

    @Test
    public void testCreateViolatedMetricsWrongEventName() throws Exception {
        expectedIllegalArgumentException.expect(MessageProcessingException.class);
        expectedIllegalArgumentException.expectCause(isA(IllegalStateException.class));
        String eventName = "badEventName";
        expectedIllegalArgumentException.expectMessage("TCA Policy must contain eventName: " + eventName);
        TCAPolicy tcaPolicy = getSampleTCAPolicy();
        Threshold violatedThreshold = getCriticalThreshold();
        TCAUtils.createViolatedMetrics(tcaPolicy, violatedThreshold, eventName);
    }

    @Test
    public void testGetDomainAndEventName() {
        TCACEFProcessorContext tcacefProcessorContext = mock(TCACEFProcessorContext.class);
        EventListener eventListener = mock(EventListener.class);
        Event event = mock(Event.class);
        CommonEventHeader commonEventHeader = mock(CommonEventHeader.class);

        Pair<String, String> result = TCAUtils.getDomainAndEventName(tcacefProcessorContext);
        assertNull(result.getLeft());
        assertNull(result.getRight());

        when(tcacefProcessorContext.getCEFEventListener()).thenReturn(eventListener);
        result = TCAUtils.getDomainAndEventName(tcacefProcessorContext);
        assertNull(result.getLeft());
        assertNull(result.getRight());

        when(eventListener.getEvent()).thenReturn(event);
        result = TCAUtils.getDomainAndEventName(tcacefProcessorContext);
        assertNull(result.getLeft());
        assertNull(result.getRight());

        when(event.getCommonEventHeader()).thenReturn(commonEventHeader);
        result = TCAUtils.getDomainAndEventName(tcacefProcessorContext);
        assertNull(result.getLeft());
        assertNull(result.getRight());

        when(commonEventHeader.getDomain()).thenReturn(Domain.other);
        when(commonEventHeader.getEventName()).thenReturn("eventName");

        result = TCAUtils.getDomainAndEventName(tcacefProcessorContext);
        assertEquals(result.getLeft(), "other");
        assertEquals(result.getRight(), "eventName");

    }

    @Test
    public void testComputeThresholdViolationsNotPresent() throws Exception {
        TCACEFProcessorContext tcacefProcessorContext = mock(TCACEFProcessorContext.class);
        when(tcacefProcessorContext.canProcessingContinue()).thenReturn(true);
        when(tcacefProcessorContext.getMessage()).thenReturn(getValidCEFMessage());

        when(tcacefProcessorContext.getTCAPolicy()).thenReturn(getSampleTCAPolicy());
        when(tcacefProcessorContext.getCEFEventListener()).thenReturn(getCEFEventListener());

        TCACEFProcessorContext result = TCAUtils.computeThresholdViolations(tcacefProcessorContext);
        assertNotNull(result);
        verify(result, times(0)).setMetricsPerEventName(Mockito.any(MetricsPerEventName.class));
        assertEquals("Policy must not change", getSampleTCAPolicy(), result.getTCAPolicy());
    }

    @Test
    public void testComputeThresholdViolationsPresent() throws Exception {
        TCACEFProcessorContext tcacefProcessorContext = mock(TCACEFProcessorContext.class);
        when(tcacefProcessorContext.canProcessingContinue()).thenReturn(true);
        final String cefMessageString = fromStream(CEF_MESSAGE_WITH_THRESHOLD_VIOLATION_JSON_FILE_LOCATION);
        when(tcacefProcessorContext.getMessage()).thenReturn(cefMessageString);

        when(tcacefProcessorContext.getTCAPolicy()).thenReturn(getSampleTCAPolicy());
        when(tcacefProcessorContext.getCEFEventListener()).thenReturn(getCEFEventListener());

        TCACEFProcessorContext result = TCAUtils.computeThresholdViolations(tcacefProcessorContext);
        verify(result, times(1)).setMetricsPerEventName(Mockito.any(MetricsPerEventName.class));

        assertEquals("Policy must not change", getSampleTCAPolicy(), result.getTCAPolicy());
    }


    @Test
    public void testCreateTCAPolicyMetricsPerKeyName() throws Exception {

        final Map<String, String> tcaPolicyMap = TCAUtils.filterMapByKeyNamePrefix(getControllerRuntimeArguments(),
                AnalyticsConstants.TCA_POLICY_METRICS_PER_FUNCTIONAL_ROLE_PATH);

        // determine functional Roles
        final Map<String, Map<String, String>> functionalRolesMap =
                TCAUtils.extractSubTree(tcaPolicyMap, 2, 3, AnalyticsConstants.TCA_POLICY_DELIMITER);

        final List<MetricsPerEventName> tcaPolicyMetricsPerEventNameList =
                TCAUtils.createTCAPolicyMetricsPerEventNameList(functionalRolesMap);

        assertThat("There are two Metrics per function role", 2,
                is(tcaPolicyMetricsPerEventNameList.size()));
    }


    @Test
    public void testCreateQuartzScheduler() throws Exception {
        final Scheduler scheduler = Mockito.mock(Scheduler.class);
        final StdSchedulerFactory stdSchedulerFactory = Mockito.mock(StdSchedulerFactory.class);
        when(stdSchedulerFactory.getScheduler()).thenReturn(scheduler);
        final JobDataMap jobDataMap = Mockito.mock(JobDataMap.class);
        TCAUtils.createQuartzScheduler(1000, stdSchedulerFactory,
                "data/properties/quartz-test.properties", jobDataMap, Job.class,
                "testJob", "testTigger");
        verify(scheduler, times(1))
                .scheduleJob(Mockito.any(JobDetail.class), Mockito.any(SimpleTrigger.class));
    }


    @Test
    public void testCreateTCAAlertStringWhenCEFIsEnabled() throws Exception {
        final MetricsPerEventName violatedMetrics = createViolatedMetricsPerEventName(EventSeverity.CRITICAL);
        TCACEFProcessorContext processorContext = mock(TCACEFProcessorContext.class);
        when(processorContext.getMetricsPerEventName()).thenReturn(violatedMetrics);
        when(processorContext.getCEFEventListener()).thenReturn(getCEFEventListener());
        final String alertString = TCAUtils.createTCAAlertString(processorContext, "testApp", true);
        assertTrue(alertString.contains("thresholdCrossingAlertFields"));
    }

    @Test(expected = MessageProcessingException.class)
    public void testCreateTCAAlertStringWhenViolatedMetricsNotPresentAndCEFIsEnabled() throws Exception {
        TCACEFProcessorContext processorContext = mock(TCACEFProcessorContext.class);
        when(processorContext.getMetricsPerEventName()).thenReturn(null);
        TCAUtils.createTCAAlertString(processorContext, "testApp", true);
    }

    @Test
    public void testCreateTCAAlertStringWhenCEFIsDisabled() throws Exception {
        final MetricsPerEventName violatedMetrics = createViolatedMetricsPerEventName(EventSeverity.MAJOR);
        TCACEFProcessorContext processorContext = mock(TCACEFProcessorContext.class);
        when(processorContext.getMetricsPerEventName()).thenReturn(violatedMetrics);
        when(processorContext.getCEFEventListener()).thenReturn(getCEFEventListener());
        final String alertString = TCAUtils.createTCAAlertString(processorContext, "testApp", false);
        assertFalse(alertString.contains("thresholdCrossingAlertFields"));
    }

    @Test(expected = MessageProcessingException.class)
    public void testCreateTCAAlertStringWhenViolatedMetricsNotPresentAndCEFIsDisabled() throws Exception {
        TCACEFProcessorContext processorContext = mock(TCACEFProcessorContext.class);
        when(processorContext.getMetricsPerEventName()).thenReturn(null);
        TCAUtils.createTCAAlertString(processorContext, "testApp", false);
    }

    private static MetricsPerEventName createViolatedMetricsPerEventName(EventSeverity severity) {
        final Threshold violatedThreshold = new Threshold();
        violatedThreshold.setSeverity(severity);
        violatedThreshold.setDirection(Direction.GREATER);
        violatedThreshold.setClosedLoopControlName("violatedThresholdClosedLoopName");
        violatedThreshold.setActualFieldValue(100L);
        violatedThreshold.setFieldPath("violatedThresholdFieldPath");
        violatedThreshold.setVersion("violatedThresholdVersion");
        violatedThreshold.setClosedLoopEventStatus(ClosedLoopEventStatus.ONSET);
        violatedThreshold.setThresholdValue(50L);

        final MetricsPerEventName violatedMetrics = new MetricsPerEventName();
        violatedMetrics.setPolicyName("violatePolicyName");
        violatedMetrics.setPolicyVersion("violatedPolicyVersion");
        violatedMetrics.setPolicyScope("violatedPolicyScope");
        violatedMetrics.setEventName("violatedEventName");
        violatedMetrics.setThresholds(Arrays.asList(violatedThreshold));
        return violatedMetrics;
    }
}
