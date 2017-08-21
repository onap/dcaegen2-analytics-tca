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

import org.apache.hadoop.conf.Configuration;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class DMaaPSinkConfigMapperTest extends BaseAnalyticsCDAPPluginsUnitTest {


    @Test
    public void testMapToPublisherConfig() throws Exception {

        final Configuration testConfiguration = getTestConfiguration();
        final DMaaPMRPublisherConfig publisherConfig = DMaaPSinkConfigMapper.map(testConfiguration);

        assertNotNull(publisherConfig);
        assertThat(publisherConfig.getHostName(), is(DMAAP_MR_SINK_PLUGIN_HOST_NAME));
        assertThat(publisherConfig.getTopicName(), is(DMAAP_MR_SINK_PLUGIN_TOPIC_NAME));
        assertThat(publisherConfig.getPortNumber(), is(DMAAP_MR_SINK_PLUGIN_PORT_NUMBER));
        assertThat(publisherConfig.getProtocol(), is(DMAAP_MR_SINK_PLUGIN_PROTOCOL));
        assertThat(publisherConfig.getUserName(), is(DMAAP_MR_SINK_PLUGIN_USERNAME));
        assertThat(publisherConfig.getUserPassword(), is(DMAAP_MR_SINK_PLUGIN_PASSWORD));
        assertThat(publisherConfig.getContentType(), is(DMAAP_MR_SINK_PLUGIN_CONTENT_TYPE));
        assertThat(publisherConfig.getMaxBatchSize(), is(DMAAP_MR_SINK_PLUGIN_MAX_BATCH_SIZE));
        assertThat(publisherConfig.getMaxRecoveryQueueSize(), is(DMAAP_MR_SINK_PLUGIN_MAX_RECOVERY_QUEUE_SIZE));

    }

}
