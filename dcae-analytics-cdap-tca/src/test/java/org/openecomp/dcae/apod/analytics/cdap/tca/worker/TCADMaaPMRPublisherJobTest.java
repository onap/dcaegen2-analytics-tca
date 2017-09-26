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

import co.cask.cdap.api.TxRunnable;
import co.cask.cdap.api.metrics.Metrics;
import co.cask.cdap.api.worker.WorkerContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPMetricsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertEntity;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 12/20/2016.
 */
public class TCADMaaPMRPublisherJobTest extends BaseAnalyticsCDAPTCAUnitTest {

    private JobExecutionContext jobExecutionContext;
    private TCADMaaPMRPublisherJob publisherJob;
    private JobDataMap jobDataMap;
    private WorkerContext workerContext;
    private DMaaPMRPublisher publisher;
    private Metrics metrics;

    private class TCATestDMaaPMRPublisherJob extends TCADMaaPMRPublisherJob {

        private Map<String, TCAVESAlertEntity> alertEntityMap;

        public TCATestDMaaPMRPublisherJob(Map<String, TCAVESAlertEntity> alertEntityMap) {
            this.alertEntityMap = alertEntityMap;
        }

        @Override
        protected Map<String, TCAVESAlertEntity> getNewAlertsMap(
                String cdapAlertsTableName, WorkerContext workerContext) {
            return alertEntityMap;
        }

        @Override
        protected void deleteAlertsByKey(String cdapAlertsTableName, WorkerContext workerContext,
                                         Set<String> rowKeys, Metrics metrics) {
            // do nothing
        }
    }

    @Before
    public void before() throws Exception {

        jobExecutionContext = mock(JobExecutionContext.class);
        workerContext = mock(WorkerContext.class);

        metrics = mock(Metrics.class);
        doNothing().when(metrics).count(anyString(), anyInt());
        publisher = mock(DMaaPMRPublisher.class);

        jobDataMap = mock(JobDataMap.class);
        when(jobDataMap.getString(eq(AnalyticsConstants.CDAP_ALERTS_TABLE_VARIABLE_NAME))).thenReturn
                ("testAlertTableName");
        when(jobDataMap.get(eq(AnalyticsConstants.WORKER_CONTEXT_VARIABLE_NAME))).thenReturn(workerContext);
        when(jobDataMap.get(eq(AnalyticsConstants.DMAAP_PUBLISHER_VARIABLE_NAME))).thenReturn(publisher);
        when(jobDataMap.get(AnalyticsConstants.DMAAP_METRICS_VARIABLE_NAME)).thenReturn(metrics);
        when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDataMap);


        publisherJob = new TCADMaaPMRPublisherJob();
    }

    @Test
    public void testExecuteWhenNoAlertsFoundInAlertsTable() throws Exception {
        doNothing().when(workerContext).execute(any(TxRunnable.class));
        publisherJob.execute(jobExecutionContext);
        verify(metrics, times(1))
                .count(eq(CDAPMetricsConstants.TCA_PUBLISHER_NO_NEW_ALERTS_LOOKUP_METRIC), eq(1));
    }

    @Test
    public void testExecuteWhenAlertsWereFoundInAlertsTable() throws Exception {

        final DMaaPMRPublisherResponse publisherResponse = mock(DMaaPMRPublisherResponse.class);
        when(publisherResponse.getResponseCode()).thenReturn(200);
        when(publisherResponse.getResponseMessage()).thenReturn("success");
        when(publisherResponse.getPendingMessagesCount()).thenReturn(0);
        when(publisher.publish(ArgumentMatchers.<String>anyList())).thenReturn(publisherResponse);

        final TCAVESAlertEntity tcavesAlertEntity = mock(TCAVESAlertEntity.class);
        when(tcavesAlertEntity.getAlertMessage()).thenReturn("testAlertMessage");
        Map<String, TCAVESAlertEntity> alertEntityMap = new HashMap<>();
        alertEntityMap.put("key1", tcavesAlertEntity);
        final TCATestDMaaPMRPublisherJob testPublisherJob = new TCATestDMaaPMRPublisherJob(alertEntityMap);
        testPublisherJob.execute(jobExecutionContext);
        verify(metrics, times(1))
                .count(eq(CDAPMetricsConstants.TCA_PUBLISHER_NEW_ALERTS_METRIC), eq(1));
        verify(metrics, times(1))
                .count(eq(CDAPMetricsConstants.TCA_PUBLISHER_SUCCESSFUL_DMAAP_RESPONSE_METRIC), eq(1));
    }

    @Test
    public void testExecuteWhenAlertsWereFoundButPublisherReturnedNon200ResponseCode() throws Exception {

        final DMaaPMRPublisherResponse publisherResponse = mock(DMaaPMRPublisherResponse.class);
        when(publisherResponse.getResponseCode()).thenReturn(500);
        when(publisherResponse.getResponseMessage()).thenReturn("failed");
        when(publisherResponse.getPendingMessagesCount()).thenReturn(0);
        when(publisher.publish(ArgumentMatchers.<String>anyList())).thenReturn(publisherResponse);

        final TCAVESAlertEntity tcavesAlertEntity = mock(TCAVESAlertEntity.class);
        when(tcavesAlertEntity.getAlertMessage()).thenReturn("testAlertMessage");
        Map<String, TCAVESAlertEntity> alertEntityMap = new HashMap<>();
        alertEntityMap.put("key1", tcavesAlertEntity);
        final TCATestDMaaPMRPublisherJob testPublisherJob = new TCATestDMaaPMRPublisherJob(alertEntityMap);
        testPublisherJob.execute(jobExecutionContext);
        verify(metrics, times(1))
                .count(eq(CDAPMetricsConstants.TCA_PUBLISHER_NEW_ALERTS_METRIC), eq(1));
        verify(metrics, times(1))
                .count(eq(CDAPMetricsConstants.TCA_PUBLISHER_UNSUCCESSFUL_DMAAP_RESPONSE_METRIC), eq(1));
    }


}
