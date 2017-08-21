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

import org.junit.Before;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.quartz.Scheduler;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 12/19/2016.
 */
public class BaseTCADMaaPMRWorkerTest extends BaseAnalyticsCDAPTCAUnitTest {

    private static final String SCHEDULER_NAME = "testSchedulerName";

    private Scheduler mockScheduler;
    private AtomicBoolean mockIsSchedulerShutdown;
    private BaseTestTCADMaaPMRWorker baseTestTCADMaaPMRWorker;

    private static class BaseTestTCADMaaPMRWorker extends BaseTCADMaaPMRWorker {

        public BaseTestTCADMaaPMRWorker(final Scheduler scheduler, final AtomicBoolean isSchedulerShutdown) {
            this.scheduler = scheduler;
            this.isSchedulerShutdown = isSchedulerShutdown;
        }

    }

    @Before
    public void before() throws Exception {
        mockScheduler = mock(Scheduler.class);
        mockIsSchedulerShutdown = mock(AtomicBoolean.class);
        baseTestTCADMaaPMRWorker = new BaseTestTCADMaaPMRWorker(mockScheduler, mockIsSchedulerShutdown);
        when(mockScheduler.getSchedulerName()).thenReturn(SCHEDULER_NAME);
        doNothing().when(mockScheduler).start();
        doNothing().when(mockScheduler).shutdown();
    }

    @Test
    public void testRun() throws Exception {
        createShutdownHookThread();
        baseTestTCADMaaPMRWorker.run();
        verify(mockScheduler, times(1)).start();
    }

    @Test
    public void testStop() throws Exception {
        baseTestTCADMaaPMRWorker.stop();
        verify(mockScheduler, times(1)).shutdown();
    }

    /**
     * A helper thread which shuts down the scheduler after some time
     */
    public void createShutdownHookThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.yield();
                    Thread.sleep(AnalyticsConstants.TCA_DEFAULT_WORKER_SHUTDOWN_CHECK_INTERVAL_MS * 2);
                } catch (InterruptedException e) {
                    LOG.error("Interrupted Exception while running test: {}", e);
                    throw new RuntimeException(e);
                }
                mockIsSchedulerShutdown.getAndSet(true);
            }
        }).start();
    }

}
