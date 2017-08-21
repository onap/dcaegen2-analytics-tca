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
public class DMaaPMRSourcePluginConfigTest extends BaseAnalyticsCDAPPluginsUnitTest {

    @Test
    public void testDMaaPMRSourcePluginConfigDefaults() throws Exception {
        final DMaaPMRSourcePluginConfig sourcePluginConfig = new DMaaPMRSourcePluginConfig
                (DMAAP_MR_SOURCE_PLUGIN_REFERENCE_NAME, DMAAP_MR_SOURCE_PLUGIN_HOST_NAME,
                        DMAAP_MR_SOURCE_PLUGIN_TOPIC_NAME, DMAAP_MR_SOURCE_PLUGIN_POLLING_INTERVAL);

        assertThat(sourcePluginConfig.getReferenceName(), is(DMAAP_MR_SOURCE_PLUGIN_REFERENCE_NAME));
        assertThat(sourcePluginConfig.getHostName(), is(DMAAP_MR_SOURCE_PLUGIN_HOST_NAME));
        assertThat(sourcePluginConfig.getTopicName(), is(DMAAP_MR_SOURCE_PLUGIN_TOPIC_NAME));
        assertThat(sourcePluginConfig.getPollingInterval(), is(DMAAP_MR_SOURCE_PLUGIN_POLLING_INTERVAL));
        assertNull(sourcePluginConfig.getPortNumber());
        assertNull(sourcePluginConfig.getProtocol());
        assertNull(sourcePluginConfig.getUserName());
        assertNull(sourcePluginConfig.getUserPassword());
        assertNull(sourcePluginConfig.getContentType());
        assertNull(sourcePluginConfig.getConsumerGroup());
        assertNull(sourcePluginConfig.getConsumerId());
        assertNull(sourcePluginConfig.getMessageLimit());
        assertNull(sourcePluginConfig.getTimeoutMS());
    }

    @Test
    public void testDMaaPMRSourcePluginConfigCustom() throws Exception {
        final TestDMaaPMRSourcePluginConfig sourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        assertThat(sourcePluginConfig.getReferenceName(), is(DMAAP_MR_SOURCE_PLUGIN_REFERENCE_NAME));
        assertThat(sourcePluginConfig.getHostName(), is(DMAAP_MR_SOURCE_PLUGIN_HOST_NAME));
        assertThat(sourcePluginConfig.getTopicName(), is(DMAAP_MR_SOURCE_PLUGIN_TOPIC_NAME));
        assertThat(sourcePluginConfig.getPollingInterval(), is(DMAAP_MR_SOURCE_PLUGIN_POLLING_INTERVAL));
        assertThat(sourcePluginConfig.getPortNumber(), is(DMAAP_MR_SOURCE_PLUGIN_PORT_NUMBER));
        assertThat(sourcePluginConfig.getProtocol(), is(DMAAP_MR_SOURCE_PLUGIN_PROTOCOL));
        assertThat(sourcePluginConfig.getUserName(), is(DMAAP_MR_SOURCE_PLUGIN_USERNAME));
        assertThat(sourcePluginConfig.getUserPassword(), is(DMAAP_MR_SOURCE_PLUGIN_PASSWORD));
        assertThat(sourcePluginConfig.getContentType(), is(DMAAP_MR_SOURCE_PLUGIN_CONTENT_TYPE));
        assertThat(sourcePluginConfig.getConsumerGroup(), is(DMAAP_MR_SOURCE_PLUGIN_CONSUMER_GROUP));
        assertThat(sourcePluginConfig.getConsumerId(), is(DMAAP_MR_SOURCE_PLUGIN_CONSUMER_ID));
        assertThat(sourcePluginConfig.getMessageLimit(), is(DMAAP_MR_SOURCE_PLUGIN_MESSAGE_LIMIT));
        assertThat(sourcePluginConfig.getTimeoutMS(), is(DMAAP_MR_SOURCE_PLUGIN_TIMEOUT));
    }

    @Test
    public void testValidToString() throws Exception {
        final TestDMaaPMRSourcePluginConfig sourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        assertNotNull(sourcePluginConfig.toString());
        assertTrue(sourcePluginConfig.toString().contains(DMAAP_MR_SOURCE_PLUGIN_HOST_NAME));
    }

}
