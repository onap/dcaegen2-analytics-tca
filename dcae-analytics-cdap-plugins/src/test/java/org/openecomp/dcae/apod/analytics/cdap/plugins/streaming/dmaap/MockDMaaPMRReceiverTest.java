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

package org.openecomp.dcae.apod.analytics.cdap.plugins.streaming.dmaap;

import co.cask.cdap.api.data.format.StructuredRecord;
import org.apache.spark.storage.StorageLevel;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.schema.dmaap.DMaaPSourceOutputSchema;

import java.util.concurrent.TimeUnit;

/**
 * @author Rajiv Singla . Creation Date: 2/20/2017.
 */
public class MockDMaaPMRReceiverTest extends BaseAnalyticsCDAPPluginsUnitTest {

    protected class TestMockDMaaPMRReceiverTest extends MockDMaaPMRReceiver {

        private boolean canStop = false;

        public TestMockDMaaPMRReceiverTest(StorageLevel storageLevel, DMaaPMRSourcePluginConfig pluginConfig) {
            super(storageLevel, pluginConfig);
        }

        @Override
        public boolean isStopped() {
            return canStop;
        }

        @Override
        public void store(StructuredRecord dataItem) {
            LOG.debug("Mocking storing dataItem - {}",
                    dataItem.get(DMaaPSourceOutputSchema.FETCHED_MESSAGE.getSchemaColumnName()));
        }

        public void setCanStop(boolean canStop) {
            this.canStop = canStop;
        }
    }

    @Test
    public void testStoreStructuredRecords() throws Exception {
        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        testDMaaPMRSourcePluginConfig.setPollingInterval(100);
        final TestMockDMaaPMRReceiverTest mockDMaaPMRReceiver = new TestMockDMaaPMRReceiverTest(StorageLevel
                .MEMORY_ONLY(),
                testDMaaPMRSourcePluginConfig);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mockDMaaPMRReceiver.storeStructuredRecords(null);
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(1000);
        LOG.info("Killing Mock Subscriber after 1 ms");
        mockDMaaPMRReceiver.setCanStop(true);

    }

}
