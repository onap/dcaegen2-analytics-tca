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

package org.onap.dcae.apod.analytics.cdap.plugins.it;

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.api.dataset.table.Table;
import co.cask.cdap.api.plugin.PluginClass;
import co.cask.cdap.api.plugin.PluginPropertyField;
import co.cask.cdap.common.utils.Tasks;
import co.cask.cdap.datapipeline.DataPipelineApp;
import co.cask.cdap.datapipeline.SmartWorkflow;
import co.cask.cdap.etl.api.batch.SparkCompute;
import co.cask.cdap.etl.mock.batch.MockSink;
import co.cask.cdap.etl.mock.batch.MockSource;
import co.cask.cdap.etl.mock.test.HydratorTestBase;
import co.cask.cdap.etl.proto.v2.ETLBatchConfig;
import co.cask.cdap.etl.proto.v2.ETLPlugin;
import co.cask.cdap.etl.proto.v2.ETLStage;
import co.cask.cdap.proto.artifact.AppRequest;
import co.cask.cdap.proto.artifact.ArtifactSummary;
import co.cask.cdap.proto.id.ApplicationId;
import co.cask.cdap.proto.id.ArtifactId;
import co.cask.cdap.proto.id.NamespaceId;
import co.cask.cdap.test.ApplicationManager;
import co.cask.cdap.test.DataSetManager;
import co.cask.cdap.test.WorkflowManager;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCACalculatorMessageType;
import org.onap.dcae.apod.analytics.cdap.common.validation.CDAPAppSettingsValidator;
import org.onap.dcae.apod.analytics.cdap.plugins.domain.config.tca.SimpleTCAPluginConfig;
import org.onap.dcae.apod.analytics.cdap.plugins.sparkcompute.tca.SimpleTCAPlugin;
import org.onap.dcae.apod.analytics.common.validation.DCAEValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Integration Test which used CDAP Hydrator Test Base to Test Simple TCA Plugin
 *
 * @author Rajiv Singla . Creation Date: 2/17/2017.
 */
public class SimpleTCAPluginCDAPIT extends HydratorTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleTCAPluginCDAPIT.class);

    private static final String CDAP_PLUGIN_VERSION = "3.0-SNAPSHOT";
    private static final String CDAP_PLUGIN_ARTIFACT_NAME = "dcae-analytics-cdap-plugins";

    protected static final ArtifactId DATAPIPELINE_ARTIFACT_ID = NamespaceId.DEFAULT.artifact("data-pipeline",
            "4.0.0");
    protected static final ArtifactSummary DATAPIPELINE_ARTIFACT = new ArtifactSummary("data-pipeline", "4.0.0");

    private static Schema sourceSchema = Schema.recordOf("CEFMessageSourceSchema",
            Schema.Field.of("message", Schema.of(Schema.Type.STRING))
    );

    final Schema outputSchema = Schema.recordOf(
            "outputSchema",
            Schema.Field.of("message", Schema.of(Schema.Type.STRING)),
            Schema.Field.of("alert", Schema.nullableOf(Schema.of(Schema.Type.STRING))),
            Schema.Field.of("tcaMessageType", Schema.of(Schema.Type.STRING))
    );

    @BeforeClass
    public static void setupTest() throws Exception {

        setupBatchArtifacts(DATAPIPELINE_ARTIFACT_ID, DataPipelineApp.class);


        // Enable the below code if you want to run the test in Intelli IDEA editor
        // addPluginArtifact(NamespaceId.DEFAULT.artifact("spark-plugins", "1.0.0"), DATAPIPELINE_ARTIFACT_ID,
        //        SimpleTCAPlugin.class, SimpleTCAPluginConfig.class);

        // Enable the below code if you want to run the test via command line
        ArtifactId dcaeAnalyticsCdapPluginsArtifact = NamespaceId.DEFAULT.artifact(
                CDAP_PLUGIN_ARTIFACT_NAME, CDAP_PLUGIN_VERSION);

        addPluginArtifact(dcaeAnalyticsCdapPluginsArtifact, DATAPIPELINE_ARTIFACT_ID,
                ImmutableSet.of(getSimpleTCAPluginClass()), SimpleTCAPlugin.class, SimpleTCAPluginConfig.class,
                CDAPAppSettingsValidator.class, DCAEValidator.class);
    }

    private static PluginClass getSimpleTCAPluginClass() {
        final HashMap<String, PluginPropertyField> properties = new HashMap<>();
        properties.put("vesMessageFieldName", new PluginPropertyField("vesMessageFieldName", "",
                "string", false, false));
        properties.put("referenceName", new PluginPropertyField("referenceName", "",
                "string", false, false));
        properties.put("policyJson", new PluginPropertyField("policyJson", "", "string", false, false));
        properties.put("alertFieldName", new PluginPropertyField("alertFieldName", "", "string", false, false));
        properties.put("messageTypeFieldName", new PluginPropertyField(
                "messageTypeFieldName", "", "string", false, false));
        properties.put("enableAlertCEFFormat", new PluginPropertyField(
                "enableAlertCEFFormat", "", "string", false, false));
        properties.put("schema", new PluginPropertyField(
                "schema", "", "string", false, false));

        return new PluginClass("sparkcompute", "SimpleTCAPlugin", "", SimpleTCAPlugin.class.getName(),
                "pluginConfig", properties);
    }


    @AfterClass
    public static void cleanup() {
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testTransform() throws Exception {

        LOG.info("Starting Test Transform");

        final String policyString = getFileContentAsString("/data/json/policy/tca_policy.json");
        final String cefMessage = getFileContentAsString("/data/json/cef/cef_message.json");

        final Map<String, String> tcaProperties = new ImmutableMap.Builder<String, String>()
                .put("vesMessageFieldName", "message")
                .put("referenceName", "SimpleTcaPlugin")
                .put("policyJson", policyString)
                .put("alertFieldName", "alert")
                .put("messageTypeFieldName", "tcaMessageType")
                .put("enableAlertCEFFormat", "true")
                .put("schema", outputSchema.toString())
                .build();

        final ETLPlugin mockSourcePlugin = MockSource.getPlugin("messages", sourceSchema);
        final ETLPlugin tcaPlugin =
                new ETLPlugin("SimpleTCAPlugin", SparkCompute.PLUGIN_TYPE, tcaProperties, null);
        final ETLPlugin mockSink = MockSink.getPlugin("tcaOutput");

        final ETLBatchConfig etlBatchConfig = ETLBatchConfig.builder("* * * * *")
                .addStage(new ETLStage("source", mockSourcePlugin))
                .addStage(new ETLStage("simpleTCAPlugin", tcaPlugin))
                .addStage(new ETLStage("sink", mockSink))
                .addConnection("source", "simpleTCAPlugin")
                .addConnection("simpleTCAPlugin", "sink")
                .build();

        AppRequest<ETLBatchConfig> appRequest = new AppRequest<>(DATAPIPELINE_ARTIFACT, etlBatchConfig);
        ApplicationId appId = NamespaceId.DEFAULT.app("TestSimpleTCAPlugin");
        ApplicationManager appManager = deployApplication(appId.toId(), appRequest);

        List<StructuredRecord> sourceMessages = new ArrayList<>();
        StructuredRecord.Builder builder = StructuredRecord.builder(sourceSchema);
        builder.set("message", cefMessage);
        sourceMessages.add(builder.build());

        // write records to source
        DataSetManager<Table> inputManager = getDataset(NamespaceId.DEFAULT.dataset("messages"));
        MockSource.writeInput(inputManager, sourceMessages);

        // manually trigger the pipeline
        WorkflowManager workflowManager = appManager.getWorkflowManager(SmartWorkflow.NAME);
        workflowManager.start();
        workflowManager.waitForFinish(5, TimeUnit.MINUTES);

        final DataSetManager<Table> outputManager = getDataset("tcaOutput");

        Tasks.waitFor(
                TCACalculatorMessageType.COMPLIANT.name(),
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        outputManager.flush();
                        List<String> tcaOutputMessageType = new LinkedList<>();
                        for (StructuredRecord outputRecord : MockSink.readOutput(outputManager)) {
                            tcaOutputMessageType.add(outputRecord.get("tcaMessageType").toString());
                            final List<Schema.Field> fields = outputRecord.getSchema().getFields();
                            LOG.debug("====>> Printing output Structured Record Contents: {}", outputRecord);
                            for (Schema.Field field : fields) {
                                LOG.debug("Field Name: {} - Field Type: {}  ---> Field Value: {}",
                                        field.getName(), field.getSchema().getType(),
                                        outputRecord.get(field.getName()));
                            }

                        }
                        return tcaOutputMessageType.get(0);
                    }
                },
                4,
                TimeUnit.MINUTES);

    }

    private static String getFileContentAsString(final String fileLocation) throws Exception {
        final URI tcaPolicyURI =
                SimpleTCAPluginCDAPIT.class.getResource(fileLocation).toURI();
        List<String> lines = Files.readAllLines(Paths.get(tcaPolicyURI), Charset.defaultCharset());
        return Joiner.on("").join(lines);
    }

}
