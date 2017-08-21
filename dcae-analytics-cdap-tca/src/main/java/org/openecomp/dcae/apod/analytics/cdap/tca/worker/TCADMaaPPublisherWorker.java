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

import co.cask.cdap.api.annotation.Property;
import co.cask.cdap.api.metrics.Metrics;
import co.cask.cdap.api.worker.WorkerContext;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.openecomp.dcae.apod.analytics.cdap.tca.utils.AppPreferencesToPublisherConfigMapper;
import org.openecomp.dcae.apod.analytics.cdap.tca.utils.CDAPTCAUtils;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

/**
 * TCA DMaaP Publisher will monitor alerts table at regular intervals and publish any alerts to DMaaP MR Publishing
 * Topic
 * <p>
 * @author Rajiv Singla . Creation Date: 11/16/2016.
 */
public class TCADMaaPPublisherWorker extends BaseTCADMaaPMRWorker {

    private static final Logger LOG = LoggerFactory.getLogger(TCADMaaPPublisherWorker.class);

    private DMaaPMRPublisher publisher;
    private Metrics metrics;
    @Property
    private final String tcaVESAlertsTableName;

    public TCADMaaPPublisherWorker(final String tcaVESAlertsTableName) {
        this.tcaVESAlertsTableName = tcaVESAlertsTableName;
    }

    @Override
    public void configure() {
        setName(CDAPComponentsConstants.TCA_FIXED_DMAAP_PUBLISHER_WORKER);
        setDescription(CDAPComponentsConstants.TCA_FIXED_DMAAP_PUBLISHER_DESCRIPTION_WORKER);
        LOG.debug("Configuring TCA MR DMaaP Publisher worker with name: {}",
                CDAPComponentsConstants.TCA_FIXED_DMAAP_PUBLISHER_WORKER);
    }


    @Override
    public void initialize(WorkerContext context) throws Exception {
        super.initialize(context);

        // Parse runtime arguments
        final TCAAppPreferences tcaAppPreferences = CDAPTCAUtils.getValidatedTCAAppPreferences(context);

        LOG.info("Initializing TCA MR DMaaP Publisher worker with preferences: {}", tcaAppPreferences);

        //  Map TCA App Preferences to DMaaP MR Publisher Config
        final DMaaPMRPublisherConfig publisherConfig = AppPreferencesToPublisherConfigMapper.map(tcaAppPreferences);

        LOG.info("TCA DMaaP MR Publisher worker will be polling TCA Alerts Table Name: {}", tcaVESAlertsTableName);

        // Create an instance of DMaaP MR Publisher
        LOG.debug("Creating an instance of DMaaP Publisher");
        publisher = DMaaPMRFactory.create().createPublisher(publisherConfig);

        // initialize a new Quartz scheduler
        initializeScheduler(tcaAppPreferences, new StdSchedulerFactory());

        // initialize scheduler state
        isSchedulerShutdown = new AtomicBoolean(true);
    }


    /**
     * Stop DMaaP Publisher
     */
    @Override
    public void stop() {
        // Close Publisher - which will flush any batch messages if present in batch queue
        if (publisher != null) {
            try {
                publisher.close();
            } catch (Exception e) {
                final String errorMessage = format("Error while shutting down DMaaP MR Publisher: %s", e);
                throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
            }
        }
        // Shut down scheduler
        super.stop();
    }


    /**
     * Initializes a scheduler instance for DMaaP MR Publisher Job
     *
     * @throws SchedulerException SchedulerException
     */
    private void initializeScheduler(final TCAAppPreferences tcaAnalyticsAppConfig,
                                     final StdSchedulerFactory stdSchedulerFactory) throws SchedulerException {

        // Get Publisher polling interval
        final Integer publisherPollingInterval = tcaAnalyticsAppConfig.getPublisherPollingInterval();

        // Publisher Quartz Properties file
        final String quartzPublisherPropertiesFileName = AnalyticsConstants.TCA_QUARTZ_PUBLISHER_PROPERTIES_FILE_NAME;

        // Create a new JobDataMap containing information required by TCA DMaaP Publisher Job
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(AnalyticsConstants.CDAP_ALERTS_TABLE_VARIABLE_NAME, tcaVESAlertsTableName);
        jobDataMap.put(AnalyticsConstants.WORKER_CONTEXT_VARIABLE_NAME, getContext());
        jobDataMap.put(AnalyticsConstants.DMAAP_PUBLISHER_VARIABLE_NAME, publisher);
        jobDataMap.put(AnalyticsConstants.DMAAP_METRICS_VARIABLE_NAME, metrics);

        // Create new publisher scheduler
        scheduler = TCAUtils.createQuartzScheduler(publisherPollingInterval, stdSchedulerFactory,
                quartzPublisherPropertiesFileName, jobDataMap, TCADMaaPMRPublisherJob.class,
                AnalyticsConstants.TCA_DMAAP_PUBLISHER_QUARTZ_JOB_NAME,
                AnalyticsConstants.TCA_DMAAP_PUBLISHER_QUARTZ_TRIGGER_NAME);
    }
}
