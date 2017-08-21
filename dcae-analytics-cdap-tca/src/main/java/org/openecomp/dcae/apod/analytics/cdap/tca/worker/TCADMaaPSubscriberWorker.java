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
import org.openecomp.dcae.apod.analytics.cdap.tca.utils.AppPreferencesToSubscriberConfigMapper;
import org.openecomp.dcae.apod.analytics.cdap.tca.utils.CDAPTCAUtils;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TCA DMaaP Subscriber will read messages and post them to cdap stream at regular intervals
 * <p>
 * @author Rajiv Singla . Creation Date: 10/14/2016.
 */
public class TCADMaaPSubscriberWorker extends BaseTCADMaaPMRWorker {

    private static final Logger LOG = LoggerFactory.getLogger(TCADMaaPSubscriberWorker.class);

    private DMaaPMRSubscriber subscriber;
    private Metrics metrics;
    @Property
    private final String tcaSubscriberOutputStreamName;

    public TCADMaaPSubscriberWorker(final String tcaSubscriberOutputStreamName) {
        this.tcaSubscriberOutputStreamName = tcaSubscriberOutputStreamName;
    }


    @Override
    public void configure() {
        setName(CDAPComponentsConstants.TCA_FIXED_DMAAP_SUBSCRIBER_WORKER);
        setDescription(CDAPComponentsConstants.TCA_FIXED_DMAAP_SUBSCRIBER_DESCRIPTION_WORKER);
        LOG.debug("Configuring TCA MR DMaaP Subscriber worker with name: {}",
                CDAPComponentsConstants.TCA_FIXED_DMAAP_SUBSCRIBER_WORKER);
    }

    @Override
    public void initialize(WorkerContext context) throws Exception {
        super.initialize(context);

        // Parse runtime arguments
        final TCAAppPreferences tcaAppPreferences = CDAPTCAUtils.getValidatedTCAAppPreferences(context);

        LOG.info("Initializing TCA MR DMaaP Subscriber worker with preferences: {}", tcaAppPreferences);

        // Map TCA App Preferences to DMaaP MR Subscriber Config
        final DMaaPMRSubscriberConfig subscriberConfig = AppPreferencesToSubscriberConfigMapper.map(tcaAppPreferences);

        LOG.info("TCA DMaaP MR Subscriber worker will be writing to CDAP Stream: {}", tcaSubscriberOutputStreamName);

        // Create an instance of DMaaP MR Subscriber
        LOG.debug("Creating an instance of DMaaP Subscriber");
        subscriber = DMaaPMRFactory.create().createSubscriber(subscriberConfig);

        // initialize a new Quartz scheduler
        initializeScheduler(tcaAppPreferences, new StdSchedulerFactory());

        // initialize scheduler state
        isSchedulerShutdown = new AtomicBoolean(true);
    }

    /**
     * Initializes a scheduler instance for DMaaP MR Subscriber Job
     *
     * @throws SchedulerException SchedulerException
     */
    private void initializeScheduler(final TCAAppPreferences tcaAppPreferences,
                                     final StdSchedulerFactory stdSchedulerFactory) throws SchedulerException {

        // Get Subscriber polling interval
        final Integer subscriberPollingInterval = tcaAppPreferences.getSubscriberPollingInterval();

        // Subscriber Quartz Properties file
        final String quartzSubscriberPropertiesFileName = AnalyticsConstants.TCA_QUARTZ_SUBSCRIBER_PROPERTIES_FILE_NAME;

        // Create a new JobDataMap containing information required by TCA DMaaP Subscriber Job
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(AnalyticsConstants.CDAP_STREAM_VARIABLE_NAME, tcaSubscriberOutputStreamName);
        jobDataMap.put(AnalyticsConstants.WORKER_CONTEXT_VARIABLE_NAME, getContext());
        jobDataMap.put(AnalyticsConstants.DMAAP_SUBSCRIBER_VARIABLE_NAME, subscriber);
        jobDataMap.put(AnalyticsConstants.DMAAP_METRICS_VARIABLE_NAME, metrics);

        // Create new publisher scheduler
        scheduler = TCAUtils.createQuartzScheduler(subscriberPollingInterval, stdSchedulerFactory,
                quartzSubscriberPropertiesFileName, jobDataMap, TCADMaaPMRSubscriberJob.class,
                AnalyticsConstants.TCA_DMAAP_SUBSCRIBER_QUARTZ_JOB_NAME,
                AnalyticsConstants.TCA_DMAAP_SUBSCRIBER_QUARTZ_TRIGGER_NAME);
    }


}
