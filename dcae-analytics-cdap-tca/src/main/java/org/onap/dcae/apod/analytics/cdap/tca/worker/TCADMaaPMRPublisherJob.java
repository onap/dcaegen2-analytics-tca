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

package org.onap.dcae.apod.analytics.cdap.tca.worker;

import co.cask.cdap.api.TxRunnable;
import co.cask.cdap.api.common.Bytes;
import co.cask.cdap.api.data.DatasetContext;
import co.cask.cdap.api.dataset.lib.CloseableIterator;
import co.cask.cdap.api.dataset.lib.KeyValue;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.metrics.Metrics;
import co.cask.cdap.api.worker.WorkerContext;
import com.google.common.base.Joiner;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.tephra.TransactionFailureException;
import org.onap.dcae.apod.analytics.cdap.common.CDAPMetricsConstants;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertEntity;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertsPersister;
import org.onap.dcae.apod.analytics.cdap.tca.utils.CDAPTCAUtils;
import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.onap.dcae.apod.analytics.common.utils.HTTPUtils;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.onap.dcae.apod.analytics.common.AnalyticsConstants.CDAP_ALERTS_TABLE_VARIABLE_NAME;
import static org.onap.dcae.apod.analytics.common.AnalyticsConstants.DMAAP_METRICS_VARIABLE_NAME;
import static org.onap.dcae.apod.analytics.common.AnalyticsConstants.DMAAP_PUBLISHER_VARIABLE_NAME;
import static org.onap.dcae.apod.analytics.common.AnalyticsConstants.WORKER_CONTEXT_VARIABLE_NAME;

/**
 * Quartz Job that will monitor any new alert messages in given TCA Alerts table and if any found publish them to
 * DMaaP MR topic
 *<p>
 * @author Rajiv Singla . Creation Date: 11/17/2016.
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
public class TCADMaaPMRPublisherJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(TCADMaaPMRPublisherJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LOG.debug("Starting DMaaP MR Topic Publisher fetch Job. Next firing time will be: {}",
                jobExecutionContext.getNextFireTime());

        // Get Job Data Map
        final JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        // Fetch all Job Params from Job Data Map
        final String cdapAlertsTableName = jobDataMap.getString(CDAP_ALERTS_TABLE_VARIABLE_NAME);
        final WorkerContext workerContext = (WorkerContext) jobDataMap.get(WORKER_CONTEXT_VARIABLE_NAME);
        final DMaaPMRPublisher publisher = (DMaaPMRPublisher) jobDataMap.get(DMAAP_PUBLISHER_VARIABLE_NAME);
        final Metrics metrics = (Metrics) jobDataMap.get(DMAAP_METRICS_VARIABLE_NAME);

        LOG.debug("Start looking for new message in Alerts Table: {}", cdapAlertsTableName);

        // Get new alerts from alerts table
        final Map<String, TCAVESAlertEntity> newAlertsMap = getNewAlertsMap(cdapAlertsTableName, workerContext);

        // If no new alerts are found - nothing to publish
        if (newAlertsMap.isEmpty()) {
            LOG.debug("No new alerts found in Alerts Table name: {}. Nothing to Publisher....", cdapAlertsTableName);
            metrics.count(CDAPMetricsConstants.TCA_PUBLISHER_NO_NEW_ALERTS_LOOKUP_METRIC, 1);
            return;
        }

        final int newAlertsCount = newAlertsMap.size();
        LOG.debug("Found new alerts in Alerts Table name: {}. No of new alerts: {}", cdapAlertsTableName,
                newAlertsCount);
        metrics.count(CDAPMetricsConstants.TCA_PUBLISHER_NEW_ALERTS_METRIC, newAlertsCount);

        // Get alert message strings from alert Entities
        final List<String> newAlertsMessages = CDAPTCAUtils.extractAlertFromAlertEntities(newAlertsMap.values());

        // Publish messages to DMaaP MR Topic
        try {

            final DMaaPMRPublisherResponse publisherResponse = publisher.publish(newAlertsMessages);

            final Integer responseCode = publisherResponse.getResponseCode();
            final String responseMessage = publisherResponse.getResponseMessage();
            final int pendingMessagesCount = publisherResponse.getPendingMessagesCount();

            LOG.debug("Publisher Response Code: {}, Publisher message: {}, Pending Messages Count: {}", responseCode,
                    responseMessage, pendingMessagesCount);

            if (HTTPUtils.isSuccessfulResponseCode(responseCode)) {
                LOG.debug("Successfully Published alerts to DMaaP MR Topic.");
                metrics.count(CDAPMetricsConstants.TCA_PUBLISHER_SUCCESSFUL_DMAAP_RESPONSE_METRIC, 1);
            } else {
                LOG.warn("Unable to publish alerts to DMaaP MR Topic. Publisher will try to send it later....");
                metrics.count(CDAPMetricsConstants.TCA_PUBLISHER_UNSUCCESSFUL_DMAAP_RESPONSE_METRIC, 1);
            }

        } catch (DCAEAnalyticsRuntimeException e) {
            LOG.error("Exception while publishing messages to DMaaP MR Topic: {}", e);
        } finally {
            // delete send message from alerts table
            deleteAlertsByKey(cdapAlertsTableName, workerContext, newAlertsMap.keySet(), metrics);
        }

        LOG.debug("Finished DMaaP MR Topic Publisher fetch Job.");

    }

    /**
     * Gets New Messages from alerts table as Map with row keys as keys and {@link TCAVESAlertEntity} as values
     *
     * @param cdapAlertsTableName alerts table name
     * @param workerContext worker context
     * @return Map with row keys as keys and {@link TCAVESAlertEntity} as values
     */
    protected Map<String, TCAVESAlertEntity> getNewAlertsMap(final String cdapAlertsTableName,
                                                           final WorkerContext workerContext) {
        final Map<String, TCAVESAlertEntity> newAlertsMap = new LinkedHashMap<>();
        try {
            workerContext.execute(new TxRunnable() {
                @Override
                public void run(DatasetContext context) throws Exception {
                    final ObjectMappedTable<TCAVESAlertEntity> alertsTable = context.getDataset(cdapAlertsTableName);
                    final Date currentTime = new Date();
                    final String rowKey = TCAVESAlertsPersister.createRowKey(currentTime);
                    final CloseableIterator<KeyValue<byte[], TCAVESAlertEntity>> scan = alertsTable.scan(null, rowKey);
                    while (scan.hasNext()) {
                        final KeyValue<byte[], TCAVESAlertEntity> alertEntityKeyValue = scan.next();
                        newAlertsMap.put(Bytes.toString(alertEntityKeyValue.getKey()), alertEntityKeyValue.getValue());
                    }
                }
            });
        } catch (TransactionFailureException e) {
            final String errorMessage = "Transaction Error while getting new alerts from alerts table: " + e.toString();
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }
        return newAlertsMap;
    }

    /**
     * Deletes rows in Alerts table for give rowKeys
     *
     * @param cdapAlertsTableName CDAP Alerts Table Name
     * @param workerContext Worker Context
     * @param rowKeys Row Key Set
     * @param metrics CDAP metrics
     */
    protected void deleteAlertsByKey(final String cdapAlertsTableName, final WorkerContext workerContext,
                                   final Set<String> rowKeys, final Metrics metrics) {
        LOG.debug("Deleting Published Alerts from alerts table with rowKeys: {}", Joiner.on(",").join(rowKeys));
        try {
            workerContext.execute(new TxRunnable() {
                @Override
                public void run(DatasetContext context) throws Exception {
                    final ObjectMappedTable<TCAVESAlertEntity> alertsTable = context.getDataset(cdapAlertsTableName);
                    for (String rowKey : rowKeys) {
                        alertsTable.delete(rowKey);
                        metrics.count(CDAPMetricsConstants.TCA_PUBLISHER_DELETED_ALERTS_METRIC, 1);
                    }
                }
            });
        } catch (TransactionFailureException e) {
            final String errorMessage =
                    "Transaction Error while deleting published alerts in alerts table: " + e.toString();
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }
    }
}
