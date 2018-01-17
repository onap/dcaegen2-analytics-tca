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

package org.onap.dcae.apod.analytics.it.plugins;

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.dataset.table.Table;
import co.cask.cdap.api.plugin.PluginClass;
import co.cask.cdap.api.plugin.PluginPropertyField;
import co.cask.cdap.common.utils.Tasks;
import co.cask.cdap.datastreams.DataStreamsApp;
import co.cask.cdap.datastreams.DataStreamsSparkLauncher;
import co.cask.cdap.etl.api.streaming.StreamingSource;
import co.cask.cdap.etl.mock.batch.MockSink;
import co.cask.cdap.etl.proto.v2.DataStreamsConfig;
import co.cask.cdap.etl.proto.v2.ETLPlugin;
import co.cask.cdap.etl.proto.v2.ETLStage;
import co.cask.cdap.proto.artifact.AppRequest;
import co.cask.cdap.proto.artifact.ArtifactSummary;
import co.cask.cdap.proto.id.ApplicationId;
import co.cask.cdap.proto.id.ArtifactId;
import co.cask.cdap.proto.id.NamespaceId;
import co.cask.cdap.test.ApplicationManager;
import co.cask.cdap.test.DataSetManager;
import co.cask.cdap.test.SparkManager;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;
import org.onap.dcae.apod.analytics.cdap.plugins.streaming.dmaap.DMaaPMRReceiver;
import org.onap.dcae.apod.analytics.cdap.plugins.streaming.dmaap.DMaaPMRSource;
import org.onap.dcae.apod.analytics.it.dmaap.DMaaPMRCreator;
import org.onap.dcae.apod.analytics.it.module.AnalyticsITInjectorSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Performs integration testing on DMaaP source plugin , where 2 sample messages are posted and verified
 * <p/>
 * @author Manjesh Gowda. Creation Date: 2/3/2017.
 */
public class DMaaPMRSourcePluginIT extends BaseAnalyticsPluginsIT {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRSourcePluginIT.class);
    protected static final ArtifactId DATASTREAMS_ARTIFACT_ID = NamespaceId.DEFAULT.artifact("data-streams", "3.2.0");
    protected static final ArtifactSummary DATASTREAMS_ARTIFACT = new ArtifactSummary("data-streams", "3.2.0");

    /**
     * Streaming artifacts are added to the hydrator pipeline. Important. Make sure you explicitly add all the custom
     * class that you have written in the plugin artifact, if not you will get incompatible type error
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setupTest() throws Exception {
        setupStreamingArtifacts(DATASTREAMS_ARTIFACT_ID, DataStreamsApp.class);

//        Set<ArtifactRange> parents = ImmutableSet.of(
//                new ArtifactRange(NamespaceId.DEFAULT.toId(), DATASTREAMS_ARTIFACT_ID.getArtifact(),
//                        new ArtifactVersion(DATASTREAMS_ARTIFACT_ID.getVersion()), true,
//                        new ArtifactVersion(DATASTREAMS_ARTIFACT_ID.getVersion()), true)
//        );

        ArtifactId dcaeAnalyticsCdapPluginsArtifact = NamespaceId.DEFAULT.artifact(
                "dcae-analytics-cdap-plugins", "2.0-SNAPSHOT");
        addPluginArtifact(dcaeAnalyticsCdapPluginsArtifact, DATASTREAMS_ARTIFACT_ID, ImmutableSet.of(getPluginClass()),
                DMaaPMRSource.class, DMaaPMRSourcePluginConfig.class, DMaaPMRReceiver.class);

//        addPluginArtifact(NamespaceId.DEFAULT.artifact("spark-plugins", "1.0.0"), parents,
//                DMaaPMRSource.class, DMaaPMRReceiver.class, DMaaPMRSourcePluginConfig.class);
    }

    private static PluginClass getPluginClass() {
        HashMap<String, PluginPropertyField> properties = new HashMap<>();
        properties.put("referenceName", new PluginPropertyField("referenceName", "", "string", false, false));
        properties.put("hostName", new PluginPropertyField("hostName", "", "string", false, false));
        properties.put("topicName", new PluginPropertyField("topicName", "", "string", false, false));
        properties.put("protocol", new PluginPropertyField("protocol", "", "string", false, false));
        properties.put("userName", new PluginPropertyField("userName", "", "string", false, false));
        properties.put("userPassword", new PluginPropertyField("userPassword", "", "string", false, false));
        properties.put("contentType", new PluginPropertyField("contentType", "", "string", false, false));
        properties.put("consumerId", new PluginPropertyField("consumerId", "", "string", false, false));
        properties.put("consumerGroup", new PluginPropertyField("consumerGroup", "", "string", false, false));

        properties.put("portNumber", new PluginPropertyField("portNumber", "", "long", false, false));
        properties.put("timeoutMS", new PluginPropertyField("timeoutMS", "", "long", false, false));
        properties.put("messageLimit", new PluginPropertyField("messageLimit", "", "long", false, false));
        properties.put("pollingInterval", new PluginPropertyField("pollingInterval", "", "long", false, false));

        return new PluginClass("streamingsource", "DMaaPMRSource", "", DMaaPMRSource.class.getName(),
                "pluginConfig", properties);
    }

    @AfterClass
    public static void cleanup() {
    }

    /**
     * Build a pipeline with a mock-sink. After that publish coupe of messages to the subscriber topic, and verify in
     * the mock sink
     *
     * @throws Exception
     */
    @Test
    public void testDMaaPMRSourcePlugin() throws Exception {
        AnalyticsITInjectorSource analyticsITInjectorSource = new AnalyticsITInjectorSource();

        final DMaaPMRCreator dMaaPMRCreator = analyticsITInjectorSource.getInjector().getInstance(DMaaPMRCreator.class);
        Map<String, String> dmaapSourceProperties = dMaaPMRCreator.getDMaaPMRSubscriberConfig();
        dmaapSourceProperties.put("consumerId", UUID.randomUUID().toString().replace("-", ""));
        dmaapSourceProperties.put("consumerGroup", UUID.randomUUID().toString().replace("-", ""));
        final String subscriberTopicName = dmaapSourceProperties.get("topicName");

        DataStreamsConfig dmaaPMRSourcePipeline = DataStreamsConfig.builder()
                .addStage(new ETLStage("source", new ETLPlugin(
                        "DMaaPMRSource", StreamingSource.PLUGIN_TYPE, dmaapSourceProperties, null)))
                .addStage(new ETLStage("sink", MockSink.getPlugin("dmaapOutput")))
                .addConnection("source", "sink")
                .setBatchInterval("20s")
                .build();

        AppRequest<DataStreamsConfig> appRequest = new AppRequest<>(DATASTREAMS_ARTIFACT, dmaaPMRSourcePipeline);
        ApplicationId appId = NamespaceId.DEFAULT.app("DMaaPMRSourceIntegrationTestingApp");
        ApplicationManager appManager = deployApplication(appId.toId(), appRequest);

        SparkManager sparkManager = appManager.getSparkManager(DataStreamsSparkLauncher.NAME);
        sparkManager.start();
        sparkManager.waitForStatus(true, 1, 100);

        final DataSetManager<Table> outputManager = getDataset("dmaapOutput");
        final List<String> dmaapContents = new LinkedList<>();

        // Publish message to source

        new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(30000);
                    dMaaPMRCreator.getDMaaPMRPublisherWithTopicName(subscriberTopicName).publish(getTwoSampleMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        try {
            Tasks.waitFor(true, new Callable<Boolean>() {
                        boolean initialized = false;

                        @Override
                        public Boolean call() throws Exception {
                            try {
                                outputManager.flush();
                                for (StructuredRecord record : MockSink.readOutput(outputManager)) {
                                    dmaapContents.add((String) record.get("message"));
                                }
                                return dmaapContents.size() >= 2;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                    },
                    90, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sparkManager.stop();

        Assert.assertTrue(dmaapContents.size() == 2);
        String allMessages = Joiner.on(",").join(dmaapContents);
        Assert.assertTrue(allMessages.contains("Message 1"));
        Assert.assertTrue(allMessages.contains("Message 2"));
    }
}
