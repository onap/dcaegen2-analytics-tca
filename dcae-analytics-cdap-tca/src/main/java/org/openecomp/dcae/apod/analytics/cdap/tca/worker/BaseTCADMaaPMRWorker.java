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

import co.cask.cdap.api.worker.AbstractWorker;
import com.google.common.base.Preconditions;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

/**
 * Base logic for DMaaP Workers which uses scheduler to poll DMaaP MR topics at frequent intervals
 * <p>
 * @author Rajiv Singla . Creation Date: 12/19/2016.
 */
public abstract class BaseTCADMaaPMRWorker extends AbstractWorker {

    private static final Logger LOG = LoggerFactory.getLogger(BaseTCADMaaPMRWorker.class);

    /**
     * Quartz Scheduler
     */
    protected Scheduler scheduler;
    /**
     * Determines if scheduler is shutdown
     */
    protected AtomicBoolean isSchedulerShutdown;


    @Override
    public void run() {

        Preconditions.checkNotNull(scheduler, "Scheduler must not be null");
        String schedulerName = "";

        // Start scheduler
        try {
            schedulerName = scheduler.getSchedulerName();
            scheduler.start();
            isSchedulerShutdown.getAndSet(false);

        } catch (SchedulerException e) {
            final String errorMessage =
                    format("Error while starting TCA DMaaP MR scheduler name: %s, error: %s", schedulerName, e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }

        LOG.info("Successfully started DMaaP MR Scheduler: {}", schedulerName);

        // indefinite loop which wakes up and confirms scheduler is indeed running
        while (!isSchedulerShutdown.get()) {
            try {

                Thread.sleep(AnalyticsConstants.TCA_DEFAULT_WORKER_SHUTDOWN_CHECK_INTERVAL_MS);

            } catch (InterruptedException e) {

                final String errorMessage =
                        format("Error while checking TCA DMaaP MR Scheduler worker status name: %s, error: %s",
                                schedulerName, e);
                throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
            }
        }

        LOG.info("Finished execution of TCA DMaaP MR worker thread: {}", schedulerName);

    }

    @Override
    public void stop() {

        Preconditions.checkNotNull(scheduler, "Scheduler must not be null");
        String schedulerName = "";

        // Stop Scheduler
        try {
            schedulerName = scheduler.getSchedulerName();
            LOG.info("Shutting TCA DMaaP MR Scheduler: {}", schedulerName);
            scheduler.shutdown();
            isSchedulerShutdown.getAndSet(true);

        } catch (SchedulerException e) {

            final String errorMessage =
                    format("Error while shutting down TCA DMaaP MR Scheduler: name: %s, error: %s", schedulerName, e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }
    }


}
