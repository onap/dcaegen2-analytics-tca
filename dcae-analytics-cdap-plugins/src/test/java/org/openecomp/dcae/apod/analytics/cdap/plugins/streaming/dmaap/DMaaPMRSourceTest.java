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
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.StageConfigurer;
import co.cask.cdap.etl.api.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.receiver.Receiver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSourcePluginConfig;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 1/24/2017.
 */
public class DMaaPMRSourceTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private PipelineConfigurer pipelineConfigurer;

    @Before
    public void before() {
        pipelineConfigurer = Mockito.mock(PipelineConfigurer.class);
        final StageConfigurer stageConfigurer = Mockito.mock(StageConfigurer.class);
        when(pipelineConfigurer.getStageConfigurer()).thenReturn(stageConfigurer);
        doNothing().when(stageConfigurer).setOutputSchema(any(Schema.class));
    }

    @Test
    public void testDMaaPMRSourceConfigurePipelineWithValidPluginSettings() throws Exception {
        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        final DMaaPMRSource dMaaPMRSource = new DMaaPMRSource(testDMaaPMRSourcePluginConfig);
        dMaaPMRSource.configurePipeline(pipelineConfigurer);
        assertNotNull(dMaaPMRSource);
    }

    @Test(expected = CDAPSettingsException.class)
    public void testDMaaPMRSourceConfigurePipelineWithInvalidPluginSettings() throws Exception {
        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        // blank out DMaaP MR Source Host
        testDMaaPMRSourcePluginConfig.setHostName(null);
        final DMaaPMRSource dMaaPMRSource = new DMaaPMRSource(testDMaaPMRSourcePluginConfig);
        dMaaPMRSource.configurePipeline(pipelineConfigurer);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void testGetStream() throws Exception {
        final StreamingContext streamingContext = Mockito.mock(StreamingContext.class);
        final JavaStreamingContext javaStreamingContext = Mockito.mock(JavaStreamingContext.class);
        final JavaReceiverInputDStream dMaaPMRReceiver = Mockito.mock(JavaReceiverInputDStream.class);
        when(streamingContext.getSparkStreamingContext()).thenReturn(javaStreamingContext);
        when(javaStreamingContext.receiverStream(any(Receiver.class))).thenReturn(dMaaPMRReceiver);

        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        final DMaaPMRSource dMaaPMRSource = new DMaaPMRSource(testDMaaPMRSourcePluginConfig);
        final JavaDStream<StructuredRecord> stream = dMaaPMRSource.getStream(streamingContext);
        assertNotNull(stream);
    }
}
