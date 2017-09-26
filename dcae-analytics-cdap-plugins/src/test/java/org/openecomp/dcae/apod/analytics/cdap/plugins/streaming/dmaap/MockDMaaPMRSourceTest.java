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
import co.cask.cdap.etl.api.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.receiver.Receiver;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSourcePluginConfig;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 2/20/2017.
 */
@SuppressWarnings("unchecked")
public class MockDMaaPMRSourceTest extends BaseAnalyticsCDAPPluginsUnitTest {

    @Test
    public void testGetStream() throws Exception {
        final StreamingContext streamingContext = Mockito.mock(StreamingContext.class);
        final JavaStreamingContext javaStreamingContext = Mockito.mock(JavaStreamingContext.class);
        final JavaReceiverInputDStream dMaaPMRReceiver = Mockito.mock(JavaReceiverInputDStream.class);
        when(streamingContext.getSparkStreamingContext()).thenReturn(javaStreamingContext);
        when(javaStreamingContext.receiverStream(any(Receiver.class))).thenReturn(dMaaPMRReceiver);

        MockDMaaPMRSource mockDMaaPMRSource = new MockDMaaPMRSource(getTestDMaaPMRSourcePluginConfig());
        final JavaDStream<StructuredRecord> stream = mockDMaaPMRSource.getStream(streamingContext);
        assertNotNull(stream);
    }

    @Test(expected = CDAPSettingsException.class)
    public void testConfigurePipelineWhenPollingIntervalNotPresent() throws Exception {
        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        testDMaaPMRSourcePluginConfig.setPollingInterval(null);
        final MockDMaaPMRSource mockDMaaPMRSource = new MockDMaaPMRSource(testDMaaPMRSourcePluginConfig);
        mockDMaaPMRSource.configurePipeline(null);
    }

    @Test
    public void testConfigurePipelineWhenPollingIntervalIsPresent() throws Exception {
        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        final MockDMaaPMRSource mockDMaaPMRSource = new MockDMaaPMRSource(testDMaaPMRSourcePluginConfig);
        mockDMaaPMRSource.configurePipeline(null);
        assertNotNull(mockDMaaPMRSource);
    }

}
