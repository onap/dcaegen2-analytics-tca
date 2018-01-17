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

package org.onap.dcae.apod.analytics.cdap.plugins.transform.filter;

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.etl.api.Emitter;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.StageConfigurer;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.onap.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 3/3/2017.
 */
public class JsonPathFilterTest extends BaseAnalyticsCDAPPluginsUnitTest {


    @Test
    public void testInitializeWhenFilterMappingIsValid() throws Exception {
        final JsonPathFilter jsonPathFilter = new JsonPathFilter(getJsonPathFilterPluginConfig());
        jsonPathFilter.initialize(null);
    }


    @Test
    public void configurePipeline() throws Exception {
        final JsonPathFilter jsonPathFilter = new JsonPathFilter(getJsonPathFilterPluginConfig());
        final PipelineConfigurer pipelineConfigurer = mock(PipelineConfigurer.class);
        final StageConfigurer stageConfigurer = mock(StageConfigurer.class);
        when(pipelineConfigurer.getStageConfigurer()).thenReturn(stageConfigurer);
        when(stageConfigurer.getInputSchema()).thenReturn(getSimpleTCAPluginInputSchema());
        doNothing().when(stageConfigurer).setOutputSchema(any(Schema.class));
        jsonPathFilter.configurePipeline(pipelineConfigurer);
        verify(stageConfigurer, times(1)).setOutputSchema(any(Schema.class));
    }

    @Test
    public void testTransform() throws Exception {
        final JsonPathFilter jsonPathFilter = new JsonPathFilter(getJsonPathFilterPluginConfig());
        jsonPathFilter.initialize(null);
        final StructuredRecord inputSR = StructuredRecord.builder(getJsonFilterPluginInputSchema())
                .set("ts", new Date().getTime())
                .set("responseCode", 200)
                .set("responseMessage", "OK")
                .set("message", fromStream(CEF_MESSAGE_JSON_FILE_LOCATION))
                .build();

        final Emitter emitter = Mockito.mock(Emitter.class);
        doNothing().when(emitter).emit(ArgumentMatchers.any(StructuredRecord.class));
        jsonPathFilter.transform(inputSR, emitter);
        verify(emitter, times(1)).emit(any(StructuredRecord.class));
    }

}
