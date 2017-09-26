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
import com.google.common.base.Optional;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPMetricsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.utils.DMaaPMRUtils;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

/**
 * Quartz Job which polls DMaaP MR VES Collector Topic for messages and writes them to
 * a given CDAP Stream
 *
 * @author Rajiv Singla . Creation Date: 10/24/2016.
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class TCADMaaPMRSubscriberJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(TCADMaaPMRSubscriberJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LOG.debug("Starting DMaaP MR Topic Subscriber fetch Job. Next firing time will be: {}",
                jobExecutionContext.getNextFireTime());

        // Get Job Data Map
        final JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        // Fetch all Job Params from Job Data Map
        final String cdapStreamName = jobDataMap.getString(AnalyticsConstants.CDAP_STREAM_VARIABLE_NAME);
        final WorkerContext workerContext =
                (WorkerContext) jobDataMap.get(AnalyticsConstants.WORKER_CONTEXT_VARIABLE_NAME);
        final DMaaPMRSubscriber subscriber =
                (DMaaPMRSubscriber) jobDataMap.get(AnalyticsConstants.DMAAP_SUBSCRIBER_VARIABLE_NAME);
        final Metrics metrics = (Metrics) jobDataMap.get(AnalyticsConstants.DMAAP_METRICS_VARIABLE_NAME);

        final Optional<List<String>> subscriberMessagesOptional =
                DMaaPMRUtils.getSubscriberMessages(subscriber, metrics);

        // Write message to CDAP Stream using Stream Writer
        if (subscriberMessagesOptional.isPresent()) {
            writeMessageToCDAPStream(subscriberMessagesOptional.get(), cdapStreamName, workerContext, metrics);
        }
    }


    /**
     * Writes given messages to CDAP Stream
     *
     * @param actualMessages List of messages that need to written to cdap stream
     * @param cdapStreamName cdap stream name
     * @param workerContext cdap worker context
     * @param metrics cdap metrics
     */
    private void writeMessageToCDAPStream(final List<String> actualMessages, final String cdapStreamName,
                                          final WorkerContext workerContext, final Metrics metrics) {
        LOG.debug("Writing message to CDAP Stream: {}, Message Count: {}", cdapStreamName, actualMessages.size());
        try {

            for (String message : actualMessages) {
                workerContext.write(cdapStreamName, message);
            }

        } catch (IOException e) {
            metrics.count(CDAPMetricsConstants.TCA_SUBSCRIBER_FAILURE_TO_WRITE_TO_STREAM_METRIC, 1);
            final String errorMessage =
                    format("Error while DMaaP message router subscriber attempting to write to CDAP Stream: %s, " +
                            "Exception: %s", cdapStreamName, e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }

        LOG.debug("DMaaP MR Subscriber successfully finished writing messages to CDAP Stream: {}, Message count: {}",
                cdapStreamName, actualMessages.size());

    }

}
