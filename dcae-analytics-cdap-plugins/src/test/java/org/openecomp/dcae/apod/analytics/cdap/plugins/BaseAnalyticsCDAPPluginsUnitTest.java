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

package org.openecomp.dcae.apod.analytics.cdap.plugins;

import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.etl.api.StageMetrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import org.apache.hadoop.conf.Configuration;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPPluginConstants;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSinkPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.filter.TestJsonPathFilterPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.tca.TestSimpleTCAPluginConfig;
import org.openecomp.dcae.apod.analytics.model.util.json.AnalyticsModelObjectMapperSupplier;
import org.openecomp.dcae.apod.analytics.test.BaseDCAEAnalyticsUnitTest;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Rajiv Singla . Creation Date: 1/23/2017.
 */
public abstract class BaseAnalyticsCDAPPluginsUnitTest extends BaseDCAEAnalyticsUnitTest {

    protected static final ObjectMapper ANALYTICS_MODEL_OBJECT_MAPPER =
            Suppliers.memoize(new AnalyticsModelObjectMapperSupplier()).get();

    protected static final String TCA_POLICY_JSON_FILE_LOCATION = "data/json/policy/tca_policy.json";
    protected static final String CEF_MESSAGE_JSON_FILE_LOCATION = "data/json/cef/cef_message.json";
    protected static final String CEF_NON_COMPLIANT_MESSAGE_JSON_FILE_LOCATION =
            "data/json/cef/non_compliant_cef_message.json";


    protected static final String DMAAP_MR_SOURCE_PLUGIN_REFERENCE_NAME = "testDMaaPMRSource";
    protected static final String DMAAP_MR_SOURCE_PLUGIN_HOST_NAME = "dcae-msrt-mtl1-ftl.homer.com";
    protected static final Integer DMAAP_MR_SOURCE_PLUGIN_PORT_NUMBER = 3905;
    protected static final String DMAAP_MR_SOURCE_PLUGIN_TOPIC_NAME = "com.dcae.dmaap.FTL.DcaeTestVESSub";
    protected static final Integer DMAAP_MR_SOURCE_PLUGIN_POLLING_INTERVAL = 1000;
    protected static final String DMAAP_MR_SOURCE_PLUGIN_PROTOCOL = "https";
    protected static final String DMAAP_MR_SOURCE_PLUGIN_USERNAME = "username";
    protected static final String DMAAP_MR_SOURCE_PLUGIN_PASSWORD = "password";
    protected static final String DMAAP_MR_SOURCE_PLUGIN_CONTENT_TYPE = "application/json";
    protected static final String DMAAP_MR_SOURCE_PLUGIN_CONSUMER_GROUP = "G1";
    protected static final String DMAAP_MR_SOURCE_PLUGIN_CONSUMER_ID = "C1";
    protected static final Integer DMAAP_MR_SOURCE_PLUGIN_MESSAGE_LIMIT = 100;
    protected static final Integer DMAAP_MR_SOURCE_PLUGIN_TIMEOUT = 10000;


    protected static final String DMAAP_MR_SINK_PLUGIN_REFERENCE_NAME = "testDMaaPMRSINK";
    protected static final String DMAAP_MR_SINK_PLUGIN_HOST_NAME = "dcae-msrt-mtl1-ftl.homer.com";
    protected static final Integer DMAAP_MR_SINK_PLUGIN_PORT_NUMBER = 3905;
    protected static final String DMAAP_MR_SINK_PLUGIN_TOPIC_NAME = "com.dcae.dmaap.FTL.DcaeTestVESPub";
    protected static final String DMAAP_MR_SINK_PLUGIN_PROTOCOL = "https";
    protected static final String DMAAP_MR_SINK_PLUGIN_USERNAME = "username";
    protected static final String DMAAP_MR_SINK_PLUGIN_PASSWORD = "password";
    protected static final String DMAAP_MR_SINK_PLUGIN_CONTENT_TYPE = "application/json";
    protected static final String DMAAP_MR_SINK_MESSAGE_COLUMN_NAME = "message";
    protected static final Integer DMAAP_MR_SINK_PLUGIN_MAX_BATCH_SIZE = 10;
    protected static final Integer DMAAP_MR_SINK_PLUGIN_MAX_RECOVERY_QUEUE_SIZE = 100;

    protected static final String VES_MESSAGE_FIELD_NAME = "message";
    protected static final String TCA_PLUGIN_ALERT_FIELD_NAME = "alert";
    protected static final String TCA_PLUGIN_MESSAGE_TYPE_FIELD_NAME = "tcaMessageType";


    protected static final String JSON_PATH_FILTER_PLUGIN_REFERENCE_NAME = "JsonPathFilter";
    protected static final String JSON_PATH_FILTER_PLUGIN_INCOMING_JSON_FIELD_NAME = "message";
    protected static final String JSON_PATH_FILTER_PLUGIN_OUTPUT_SCHEMA_FILED_NAME = "filterMatched";
    protected static final String JSON_PATH_FILTER_PLUGIN_JSON_FILTER_MAPPINGS =
            "$.event.commonEventHeader.domain:measurementsForVfScaling," +
                    "$.event.commonEventHeader.functionalRole:vLoadBalancer;vFirewall";
    protected static final String JSON_PATH_FILTER_PLUGIN_JSON_FILTER_OUTPUT_SCHEMA =
            "{\"type\":\"record\"," +
                    "\"name\":\"etlSchemaBody\",\"fields\":" +
                    "[" +
                    "{\"name\":\"ts\",\"type\":\"long\"}," +
                    "{\"name\":\"filterMatched\",\"type\":[\"boolean\",\"null\"]}," +
                    "{\"name\":\"responseCode\",\"type\":\"int\"}," +
                    "{\"name\":\"responseMessage\",\"type\":\"string\"}," +
                    "{\"name\":\"message\",\"type\":\"string\"}" +
                    "]" +
                    "}";

    protected static class MockStageMetrics implements StageMetrics, Serializable {

        @Override
        public void count(String metricName, int delta) {
            LOG.debug("Mocking metric count, MetricName: {}, Delta: {}", metricName, delta);
        }

        @Override
        public void gauge(String metricName, long value) {
            LOG.debug("Mocking metric guage, MetricName: {}, Value: {}", metricName, value);
        }

        @Override
        public void pipelineCount(String metricName, int delta) {
            LOG.debug("Mocking metric pipelineCount, MetricName: {}, Delta: {}", metricName, delta);
        }

        @Override
        public void pipelineGauge(String metricName, long value) {
            LOG.debug("Mocking metric guage, pipelineGauge: {}, Value: {}", metricName, value);
        }
    }

    protected static TestDMaaPMRSourcePluginConfig getTestDMaaPMRSourcePluginConfig() {
        final TestDMaaPMRSourcePluginConfig sourcePluginConfig = new TestDMaaPMRSourcePluginConfig();
        sourcePluginConfig.setReferenceName(DMAAP_MR_SOURCE_PLUGIN_REFERENCE_NAME);
        sourcePluginConfig.setHostName(DMAAP_MR_SOURCE_PLUGIN_HOST_NAME);
        sourcePluginConfig.setPortNumber(DMAAP_MR_SOURCE_PLUGIN_PORT_NUMBER);
        sourcePluginConfig.setTopicName(DMAAP_MR_SOURCE_PLUGIN_TOPIC_NAME);
        sourcePluginConfig.setPollingInterval(DMAAP_MR_SOURCE_PLUGIN_POLLING_INTERVAL);
        sourcePluginConfig.setProtocol(DMAAP_MR_SOURCE_PLUGIN_PROTOCOL);
        sourcePluginConfig.setUserName(DMAAP_MR_SOURCE_PLUGIN_USERNAME);
        sourcePluginConfig.setUserPassword(DMAAP_MR_SOURCE_PLUGIN_PASSWORD);
        sourcePluginConfig.setContentType(DMAAP_MR_SOURCE_PLUGIN_CONTENT_TYPE);
        sourcePluginConfig.setConsumerGroup(DMAAP_MR_SOURCE_PLUGIN_CONSUMER_GROUP);
        sourcePluginConfig.setConsumerId(DMAAP_MR_SOURCE_PLUGIN_CONSUMER_ID);
        sourcePluginConfig.setMessageLimit(DMAAP_MR_SOURCE_PLUGIN_MESSAGE_LIMIT);
        sourcePluginConfig.setTimeoutMS(DMAAP_MR_SOURCE_PLUGIN_TIMEOUT);
        return sourcePluginConfig;
    }

    protected static TestDMaaPMRSinkPluginConfig getTestDMaaPMRSinkPluginConfig() {
        final TestDMaaPMRSinkPluginConfig sinkPluginConfig = new TestDMaaPMRSinkPluginConfig();
        sinkPluginConfig.setReferenceName(DMAAP_MR_SINK_PLUGIN_REFERENCE_NAME);
        sinkPluginConfig.setHostName(DMAAP_MR_SINK_PLUGIN_HOST_NAME);
        sinkPluginConfig.setPortNumber(DMAAP_MR_SINK_PLUGIN_PORT_NUMBER);
        sinkPluginConfig.setTopicName(DMAAP_MR_SINK_PLUGIN_TOPIC_NAME);
        sinkPluginConfig.setProtocol(DMAAP_MR_SINK_PLUGIN_PROTOCOL);
        sinkPluginConfig.setUserName(DMAAP_MR_SINK_PLUGIN_USERNAME);
        sinkPluginConfig.setUserPassword(DMAAP_MR_SINK_PLUGIN_PASSWORD);
        sinkPluginConfig.setContentType(DMAAP_MR_SINK_PLUGIN_CONTENT_TYPE);
        sinkPluginConfig.setMessageColumnName(DMAAP_MR_SINK_MESSAGE_COLUMN_NAME);
        sinkPluginConfig.setMaxBatchSize(DMAAP_MR_SINK_PLUGIN_MAX_BATCH_SIZE);
        sinkPluginConfig.setMaxRecoveryQueueSize(DMAAP_MR_SINK_PLUGIN_MAX_RECOVERY_QUEUE_SIZE);
        return sinkPluginConfig;
    }


    protected static Configuration getTestConfiguration() {
        final Configuration configuration = new Configuration();
        final Map<String, String> sinkConfigurationMap = createSinkConfigurationMap();
        for (Map.Entry<String, String> property : sinkConfigurationMap.entrySet()) {
            configuration.set(property.getKey(), property.getValue());
        }
        return configuration;
    }

    protected static Map<String, String> createSinkConfigurationMap() {

        Map<String, String> sinkConfig = new LinkedHashMap<>();
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.HOST_NAME, DMAAP_MR_SINK_PLUGIN_HOST_NAME);
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.TOPIC_NAME, DMAAP_MR_SINK_PLUGIN_TOPIC_NAME);
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.PORT_NUMBER,
                DMAAP_MR_SINK_PLUGIN_PORT_NUMBER.toString());
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.PROTOCOL, DMAAP_MR_SINK_PLUGIN_PROTOCOL);
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.USER_NAME, DMAAP_MR_SINK_PLUGIN_USERNAME);
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.USER_PASS, DMAAP_MR_SINK_PLUGIN_PASSWORD);
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.CONTENT_TYPE,
                DMAAP_MR_SINK_PLUGIN_CONTENT_TYPE);
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.MAX_BATCH_SIZE,
                DMAAP_MR_SINK_PLUGIN_MAX_BATCH_SIZE.toString());
        sinkConfig.put(CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields.MAX_RECOVER_QUEUE_SIZE,
                DMAAP_MR_SINK_PLUGIN_MAX_RECOVERY_QUEUE_SIZE.toString());
        return sinkConfig;
    }

    protected static Schema getDMaaPMRSinkTestSchema() {
        return Schema.recordOf(
                "DMaaPMRSinkTestSchema",
                Schema.Field.of("message", Schema.of(Schema.Type.STRING)),
                Schema.Field.of("field1", Schema.of(Schema.Type.STRING))
        );
    }


    protected static TestSimpleTCAPluginConfig getTestSimpleTCAPluginConfig() {
        final String policyJson;
        try {
            policyJson = fromStream(TCA_POLICY_JSON_FILE_LOCATION);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing policy", e);
        }
        return new TestSimpleTCAPluginConfig(VES_MESSAGE_FIELD_NAME, policyJson, TCA_PLUGIN_ALERT_FIELD_NAME,
                TCA_PLUGIN_MESSAGE_TYPE_FIELD_NAME, getSimpleTCAPluginInputSchema().toString(), false);
    }

    protected static Schema getSimpleTCAPluginInputSchema() {
        return Schema.recordOf(
                "TestSimpleTCAPluginInputSchema",
                Schema.Field.of("message", Schema.of(Schema.Type.STRING)),
                Schema.Field.of("inputField1", Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                Schema.Field.of("inputField2", Schema.nullableOf(Schema.of(Schema.Type.STRING)))
        );
    }

    protected static Schema getJsonFilterPluginInputSchema() {
        return Schema.recordOf(
                "TestJsonFilterInputSchema",
                Schema.Field.of("ts", Schema.of(Schema.Type.LONG)),
                Schema.Field.of("responseCode", Schema.of(Schema.Type.INT)),
                Schema.Field.of("responseMessage", Schema.of(Schema.Type.STRING)),
                Schema.Field.of("message", Schema.of(Schema.Type.STRING))
        );
    }

    protected static TestJsonPathFilterPluginConfig getJsonPathFilterPluginConfig() {
        return new TestJsonPathFilterPluginConfig(JSON_PATH_FILTER_PLUGIN_REFERENCE_NAME,
                JSON_PATH_FILTER_PLUGIN_INCOMING_JSON_FIELD_NAME,
                JSON_PATH_FILTER_PLUGIN_OUTPUT_SCHEMA_FILED_NAME,
                JSON_PATH_FILTER_PLUGIN_JSON_FILTER_MAPPINGS,
                JSON_PATH_FILTER_PLUGIN_JSON_FILTER_OUTPUT_SCHEMA);
    }

}
