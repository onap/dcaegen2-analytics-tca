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

package org.openecomp.dcae.apod.analytics.cdap.plugins.batch.sink.dmaap;

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.etl.api.Emitter;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.StageConfigurer;
import co.cask.cdap.etl.api.batch.BatchSinkContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class DMaaPMRSinkTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private DMaaPMRSink dMaaPMRSink;

    @Before
    public void before() {
        dMaaPMRSink = new DMaaPMRSink(getTestDMaaPMRSinkPluginConfig());
    }

    @Test
    public void testConfigurePipeline() throws Exception {
        final PipelineConfigurer pipelineConfigurer = Mockito.mock(PipelineConfigurer.class);
        final StageConfigurer stageConfigurer = Mockito.mock(StageConfigurer.class);
        when(pipelineConfigurer.getStageConfigurer()).thenReturn(stageConfigurer);
        when(stageConfigurer.getInputSchema()).thenReturn(getDMaaPMRSinkTestSchema());
        dMaaPMRSink.configurePipeline(pipelineConfigurer);
        verify(stageConfigurer, times(1)).getInputSchema();
    }

    @Test(expected = CDAPSettingsException.class)
    public void testConfigurePipelineWithInvalidSchema() throws Exception {
        final PipelineConfigurer pipelineConfigurer = Mockito.mock(PipelineConfigurer.class);
        final StageConfigurer stageConfigurer = Mockito.mock(StageConfigurer.class);
        when(pipelineConfigurer.getStageConfigurer()).thenReturn(stageConfigurer);
        when(stageConfigurer.getInputSchema()).thenReturn(Schema.recordOf(
                "DMaaPMRSinkInvalidSchema",
                Schema.Field.of("message1", Schema.of(Schema.Type.STRING)),
                Schema.Field.of("field1", Schema.of(Schema.Type.STRING))
        ));
        dMaaPMRSink.configurePipeline(pipelineConfigurer);
    }

    @Test
    public void testPrepareRun() throws Exception {
        final BatchSinkContext batchSinkContext = Mockito.mock(BatchSinkContext.class);
        dMaaPMRSink.prepareRun(batchSinkContext);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTransform() throws Exception {
        final StructuredRecord structuredRecord = Mockito.mock(StructuredRecord.class);
        final Emitter emitter = Mockito.mock(Emitter.class);
        final String incomingTestMessage = "test message";
        when(structuredRecord.get(
                eq(getTestDMaaPMRSinkPluginConfig().getMessageColumnName()))).thenReturn(incomingTestMessage);
        doNothing().when(emitter).emit(any());
        dMaaPMRSink.transform(structuredRecord, emitter);
    }

}
