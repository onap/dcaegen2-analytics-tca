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

package org.onap.dcae.apod.analytics.cdap.plugins.batch.sink.dmaap;

import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.common.CDAPPluginConstants.DMaaPMRSinkHadoopConfigFields;
import org.onap.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.onap.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSinkPluginConfig;
import org.onap.dcae.apod.analytics.common.AnalyticsConstants;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class DMaaPMROutputFormatProviderTest extends BaseAnalyticsCDAPPluginsUnitTest {


    @Test
    public void testDMaaPMROutputFormatProviderWhenConfigIsMissingNonRequiredValues() throws Exception {
        final TestDMaaPMRSinkPluginConfig sinkPluginConfig = new TestDMaaPMRSinkPluginConfig();
        sinkPluginConfig.setHostName(DMAAP_MR_SINK_PLUGIN_HOST_NAME);
        sinkPluginConfig.setTopicName(DMAAP_MR_SINK_PLUGIN_TOPIC_NAME);
        final DMaaPMROutputFormatProvider dMaaPMROutputFormatProvider =
                new DMaaPMROutputFormatProvider(sinkPluginConfig);
        final Map<String, String> outputFormatConfiguration =
                dMaaPMROutputFormatProvider.getOutputFormatConfiguration();
        final String hostName = outputFormatConfiguration.get(DMaaPMRSinkHadoopConfigFields.HOST_NAME);
        assertTrue(hostName.equals(DMAAP_MR_SINK_PLUGIN_HOST_NAME));
        final String topicName = outputFormatConfiguration.get(DMaaPMRSinkHadoopConfigFields.TOPIC_NAME);
        assertTrue(topicName.equals(DMAAP_MR_SINK_PLUGIN_TOPIC_NAME));
        final String portNumber = outputFormatConfiguration.get(DMaaPMRSinkHadoopConfigFields.PORT_NUMBER);
        assertTrue(portNumber.equals(AnalyticsConstants.DEFAULT_PORT_NUMBER.toString()));
        final String protocol = outputFormatConfiguration.get(DMaaPMRSinkHadoopConfigFields.PROTOCOL);
        assertTrue(protocol.equals(AnalyticsConstants.DEFAULT_PROTOCOL));
    }

    @Test
    public void testGetOutputFormatClassName() throws Exception {
        final DMaaPMROutputFormatProvider dMaaPMROutputFormatProvider =
                new DMaaPMROutputFormatProvider(getTestDMaaPMRSinkPluginConfig());
        final String outputFormatClassName = dMaaPMROutputFormatProvider.getOutputFormatClassName();
        assertTrue(outputFormatClassName.equals(DMaaPMROutputFormat.class.getName()));
    }

    @Test
    public void testGetOutputFormatConfiguration() throws Exception {
        final TestDMaaPMRSinkPluginConfig testDMaaPMRSinkPluginConfig = getTestDMaaPMRSinkPluginConfig();
        final DMaaPMROutputFormatProvider dMaaPMROutputFormatProvider =
                new DMaaPMROutputFormatProvider(testDMaaPMRSinkPluginConfig);
        final Map<String, String> outputFormatConfiguration =
                dMaaPMROutputFormatProvider.getOutputFormatConfiguration();
        assertTrue(outputFormatConfiguration.size() == 9);

    }

}
