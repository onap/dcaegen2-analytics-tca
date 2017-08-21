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

package org.openecomp.dcae.apod.analytics.cdap.tca.worker;

import co.cask.cdap.api.metrics.Metrics;
import co.cask.cdap.api.worker.WorkerContext;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPMetricsConstants;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 12/20/2016.
 */
public class TCADMaaPMRSubscriberJobTest extends BaseAnalyticsCDAPTCAUnitTest {

    private JobExecutionContext jobExecutionContext;
    private TCADMaaPMRSubscriberJob subscriberJob;
    private JobDataMap jobDataMap;
    private WorkerContext workerContext;
    private DMaaPMRSubscriber subscriber;
    private Metrics metrics;


    @Before
    public void before() throws Exception {

        jobExecutionContext = mock(JobExecutionContext.class);
        workerContext = mock(WorkerContext.class);

        metrics = mock(Metrics.class);
        doNothing().when(metrics).count(anyString(), anyInt());
        subscriber = mock(DMaaPMRSubscriber.class);

        jobDataMap = mock(JobDataMap.class);
        when(jobDataMap.getString(eq(AnalyticsConstants.CDAP_STREAM_VARIABLE_NAME))).thenReturn
                (CDAPComponentsConstants.TCA_DEFAULT_SUBSCRIBER_OUTPUT_NAME_STREAM);
        when(jobDataMap.get(eq(AnalyticsConstants.WORKER_CONTEXT_VARIABLE_NAME))).thenReturn(workerContext);
        when(jobDataMap.get(eq(AnalyticsConstants.DMAAP_SUBSCRIBER_VARIABLE_NAME))).thenReturn(subscriber);
        when(jobDataMap.get(AnalyticsConstants.DMAAP_METRICS_VARIABLE_NAME)).thenReturn(metrics);
        when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDataMap);

        doNothing().when(workerContext).write(anyString(), anyString());

        subscriberJob = new TCADMaaPMRSubscriberJob();
    }

    @Test
    public void testExecuteWhenMessagesAreFound() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(200);
        when(subscriberResponse.getResponseMessage()).thenReturn("testMessage");
        when(subscriberResponse.getFetchedMessages()).thenReturn(ImmutableList.of("testMessage1", "testMessage1"));
        when(subscriber.fetchMessages()).thenReturn(subscriberResponse);
        subscriberJob.execute(jobExecutionContext);
        verify(metrics, Mockito.times(1)).count(eq(CDAPMetricsConstants
                .DMAAP_MR_SUBSCRIBER_TOTAL_MESSAGES_PROCESSED_METRIC), eq(2));
    }

    @Test
    public void testExecuteWhenNoMessagesFound() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(200);
        when(subscriberResponse.getResponseMessage()).thenReturn("no messages");
        when(subscriberResponse.getFetchedMessages()).thenReturn(Collections.<String>emptyList());
        when(subscriber.fetchMessages()).thenReturn(subscriberResponse);
        subscriberJob.execute(jobExecutionContext);
        verify(metrics, Mockito.times(1)).count(eq(CDAPMetricsConstants
                .DMAAP_MR_SUBSCRIBER_RESPONSES_WITH_NO_MESSAGES_METRIC), eq(1));
    }


    @Test
    public void testExecuteWhenSubscriberReturnNonSuccessfulReturnCode() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(500);
        when(subscriber.fetchMessages()).thenReturn(subscriberResponse);
        subscriberJob.execute(jobExecutionContext);
        verify(metrics, Mockito.times(1)).count(eq(CDAPMetricsConstants
                .DMAAP_MR_SUBSCRIBER_UNSUCCESSFUL_RESPONSES_METRIC), eq(1));
    }

    @Test(expected = DCAEAnalyticsRuntimeException.class)
    public void testExecuteWhenWritingToCDAPStreamThrowsException() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(200);
        when(subscriberResponse.getFetchedMessages()).thenReturn(Arrays.asList("TestMessage"));
        when(subscriber.fetchMessages()).thenReturn(subscriberResponse);
        doThrow(new IOException()).when(workerContext).write(anyString(), anyString());
        subscriberJob.execute(jobExecutionContext);
    }



}
