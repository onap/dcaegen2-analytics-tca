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

package org.onap.dcae.apod.analytics.cdap.plugins.sparkcompute.tca;

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.StageConfigurer;
import co.cask.cdap.etl.api.batch.SparkExecutionPluginContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCACalculatorMessageType;
import org.onap.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.onap.dcae.apod.analytics.cdap.plugins.domain.config.tca.TestSimpleTCAPluginConfig;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 2/17/2017.
 */
public class SimpleTCAPluginTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private SimpleTCAPlugin simpleTCAPlugin;

    @Before
    public void before() {
        final TestSimpleTCAPluginConfig testSimpleTCAPluginConfig = getTestSimpleTCAPluginConfig();
        Schema outputSchema = Schema.recordOf(
                "TestSimpleTCAPluginInputSchema",
                Schema.Field.of("message", Schema.of(Schema.Type.STRING)),
                Schema.Field.of("alert", Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                Schema.Field.of("tcaMessageType", Schema.of(Schema.Type.STRING))
        );
        testSimpleTCAPluginConfig.setSchema(outputSchema.toString());
        simpleTCAPlugin = new SimpleTCAPlugin(testSimpleTCAPluginConfig);
    }

    @Test
    public void testConfigurePipeline() throws Exception {
        final PipelineConfigurer pipelineConfigurer = mock(PipelineConfigurer.class);
        final StageConfigurer stageConfigurer = mock(StageConfigurer.class);
        when(pipelineConfigurer.getStageConfigurer()).thenReturn(stageConfigurer);
        when(stageConfigurer.getInputSchema()).thenReturn(getSimpleTCAPluginInputSchema());
        simpleTCAPlugin.configurePipeline(pipelineConfigurer);
        verify(stageConfigurer, times(1)).getInputSchema();
    }

    @Test
    public void testTransform() throws Exception {

        JavaSparkContext javaSparkContext = new JavaSparkContext("local", "test");

        Schema sourceSchema = Schema.recordOf("CEFMessageSourceSchema",
                Schema.Field.of("message", Schema.of(Schema.Type.STRING))
        );

        // Inapplicable Message Structured Record
        final StructuredRecord inapplicableSR =
                StructuredRecord.builder(sourceSchema).set("message", "test").build();
        // compliant
        final StructuredRecord compliantSR =
                StructuredRecord.builder(sourceSchema).set("message",
                        fromStream(CEF_MESSAGE_JSON_FILE_LOCATION)).build();
        // non compliant
        final String nonCompliantCEF = fromStream(CEF_NON_COMPLIANT_MESSAGE_JSON_FILE_LOCATION);
        final StructuredRecord nonCompliantSR =
                StructuredRecord.builder(sourceSchema).set("message", nonCompliantCEF).build();

        final List<StructuredRecord> records = new LinkedList<>();
        records.add(inapplicableSR);
        records.add(compliantSR);
        records.add(nonCompliantSR);

        final JavaRDD<StructuredRecord> input =
                javaSparkContext.parallelize(records);
        final SparkExecutionPluginContext context = Mockito.mock(SparkExecutionPluginContext.class);
        final MockStageMetrics stageMetrics = Mockito.mock(MockStageMetrics.class);
        when(context.getMetrics()).thenReturn(stageMetrics);
        final List<StructuredRecord> outputRecord = simpleTCAPlugin.transform(context, input).collect();
        assertNotNull(outputRecord);
        assertThat(outputRecord.size(), is(3));

        assertTrue(outputRecord.get(0).get("tcaMessageType").equals(TCACalculatorMessageType.INAPPLICABLE.toString()));
        assertTrue(outputRecord.get(1).get("tcaMessageType").equals(TCACalculatorMessageType.COMPLIANT.toString()));
        assertTrue(outputRecord.get(2).get("tcaMessageType").equals(TCACalculatorMessageType.NON_COMPLIANT.toString()));
    }

}
