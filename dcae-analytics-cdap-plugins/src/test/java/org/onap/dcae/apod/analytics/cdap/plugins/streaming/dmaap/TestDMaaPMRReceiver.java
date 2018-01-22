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

package org.onap.dcae.apod.analytics.cdap.plugins.streaming.dmaap;

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.metrics.Metrics;
import org.apache.spark.storage.StorageLevel;
import org.mockito.Mockito;
import org.onap.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

/**
 * Test implementation for {@link DMaaPMRReceiver}
 * <p>
 * @author Rajiv Singla . Creation Date: 1/24/2017.
 */
public class TestDMaaPMRReceiver extends DMaaPMRReceiver {

    protected static Metrics metrics;

    static {
        metrics = Mockito.mock(Metrics.class);
        doNothing().when(metrics).count(anyString(), anyInt());
        doNothing().when(metrics).gauge(anyString(), anyInt());
    }


    public TestDMaaPMRReceiver(StorageLevel storageLevel, DMaaPMRSourcePluginConfig pluginConfig) {

        super(storageLevel, pluginConfig, metrics);
    }

    @Override
    public void store(StructuredRecord dataItem) {
        // do nothing
    }
}
