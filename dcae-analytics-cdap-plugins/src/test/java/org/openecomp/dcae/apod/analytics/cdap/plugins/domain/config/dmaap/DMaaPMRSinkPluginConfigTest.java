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

package org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 1/23/2017.
 */
public class DMaaPMRSinkPluginConfigTest extends BaseAnalyticsCDAPPluginsUnitTest {

    @Test
    public void testDMaaPMRSinkPluginConfigDefaults() throws Exception {
        final DMaaPMRSinkPluginConfig sinkPluginConfig = new DMaaPMRSinkPluginConfig
                (DMAAP_MR_SINK_PLUGIN_REFERENCE_NAME, DMAAP_MR_SINK_PLUGIN_HOST_NAME,
                        DMAAP_MR_SINK_PLUGIN_TOPIC_NAME, DMAAP_MR_SINK_MESSAGE_COLUMN_NAME);

        assertThat(sinkPluginConfig.getReferenceName(), is(DMAAP_MR_SINK_PLUGIN_REFERENCE_NAME));
        assertThat(sinkPluginConfig.getHostName(), is(DMAAP_MR_SINK_PLUGIN_HOST_NAME));
        assertThat(sinkPluginConfig.getTopicName(), is(DMAAP_MR_SINK_PLUGIN_TOPIC_NAME));
        assertThat(sinkPluginConfig.getMessageColumnName(), is(DMAAP_MR_SINK_MESSAGE_COLUMN_NAME));
        assertNull(sinkPluginConfig.getPortNumber());
        assertNull(sinkPluginConfig.getProtocol());
        assertNull(sinkPluginConfig.getUserName());
        assertNull(sinkPluginConfig.getUserPassword());
        assertNull(sinkPluginConfig.getContentType());
        assertNull(sinkPluginConfig.getMaxBatchSize());
        assertNull(sinkPluginConfig.getMaxRecoveryQueueSize());
    }

    @Test
    public void testDMaaPMRSinkPluginConfigCustom() throws Exception {
        final DMaaPMRSinkPluginConfig sinkPluginConfig = getTestDMaaPMRSinkPluginConfig();
        assertThat(sinkPluginConfig.getReferenceName(), is(DMAAP_MR_SINK_PLUGIN_REFERENCE_NAME));
        assertThat(sinkPluginConfig.getHostName(), is(DMAAP_MR_SINK_PLUGIN_HOST_NAME));
        assertThat(sinkPluginConfig.getTopicName(), is(DMAAP_MR_SINK_PLUGIN_TOPIC_NAME));
        assertThat(sinkPluginConfig.getPortNumber(), is(DMAAP_MR_SINK_PLUGIN_PORT_NUMBER));
        assertThat(sinkPluginConfig.getProtocol(), is(DMAAP_MR_SINK_PLUGIN_PROTOCOL));
        assertThat(sinkPluginConfig.getUserName(), is(DMAAP_MR_SINK_PLUGIN_USERNAME));
        assertThat(sinkPluginConfig.getUserPassword(), is(DMAAP_MR_SINK_PLUGIN_PASSWORD));
        assertThat(sinkPluginConfig.getContentType(), is(DMAAP_MR_SINK_PLUGIN_CONTENT_TYPE));
        assertThat(sinkPluginConfig.getMessageColumnName(), is(DMAAP_MR_SINK_MESSAGE_COLUMN_NAME));
        assertThat(sinkPluginConfig.getMaxBatchSize(), is(DMAAP_MR_SINK_PLUGIN_MAX_BATCH_SIZE));
        assertThat(sinkPluginConfig.getMaxRecoveryQueueSize(), is(DMAAP_MR_SINK_PLUGIN_MAX_RECOVERY_QUEUE_SIZE));
    }

    @Test
    public void testValidToString() throws Exception {
        final TestDMaaPMRSinkPluginConfig sinkPluginConfig = getTestDMaaPMRSinkPluginConfig();
        assertNotNull(sinkPluginConfig.toString());
        assertTrue(sinkPluginConfig.toString().contains(DMAAP_MR_SINK_PLUGIN_HOST_NAME));
    }


}
