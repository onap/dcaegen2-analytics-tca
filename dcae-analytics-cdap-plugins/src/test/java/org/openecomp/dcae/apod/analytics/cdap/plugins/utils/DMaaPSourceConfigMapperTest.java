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

package org.openecomp.dcae.apod.analytics.cdap.plugins.utils;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 1/24/2017.
 */
public class DMaaPSourceConfigMapperTest extends BaseAnalyticsCDAPPluginsUnitTest {

    @Test
    public void testMapToSubscriberConfig() throws Exception {

        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        final DMaaPMRSubscriberConfig subscriberConfig = DMaaPSourceConfigMapper.map(testDMaaPMRSourcePluginConfig);

        assertNotNull(subscriberConfig);
        assertThat(subscriberConfig.getHostName(), is(DMAAP_MR_SOURCE_PLUGIN_HOST_NAME));
        assertThat(subscriberConfig.getTopicName(), is(DMAAP_MR_SOURCE_PLUGIN_TOPIC_NAME));
        assertThat(subscriberConfig.getPortNumber(), is(DMAAP_MR_SOURCE_PLUGIN_PORT_NUMBER));
        assertThat(subscriberConfig.getProtocol(), is(DMAAP_MR_SOURCE_PLUGIN_PROTOCOL));
        assertThat(subscriberConfig.getUserName(), is(DMAAP_MR_SOURCE_PLUGIN_USERNAME));
        assertThat(subscriberConfig.getUserPassword(), is(DMAAP_MR_SOURCE_PLUGIN_PASSWORD));
        assertThat(subscriberConfig.getContentType(), is(DMAAP_MR_SOURCE_PLUGIN_CONTENT_TYPE));
        assertThat(subscriberConfig.getConsumerGroup(), is(DMAAP_MR_SOURCE_PLUGIN_CONSUMER_GROUP));
        assertThat(subscriberConfig.getConsumerId(), is(DMAAP_MR_SOURCE_PLUGIN_CONSUMER_ID));
        assertThat(subscriberConfig.getMessageLimit(), is(DMAAP_MR_SOURCE_PLUGIN_MESSAGE_LIMIT));
        assertThat(subscriberConfig.getTimeoutMS(), is(DMAAP_MR_SOURCE_PLUGIN_TIMEOUT));
    }

    @Test(expected = IllegalStateException.class)
    public void testMapToSubscriberConfigWhenSubscriberHostNameIsEmpty() throws Exception {
        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        testDMaaPMRSourcePluginConfig.setHostName(null);
        DMaaPSourceConfigMapper.map(testDMaaPMRSourcePluginConfig);

    }
}
