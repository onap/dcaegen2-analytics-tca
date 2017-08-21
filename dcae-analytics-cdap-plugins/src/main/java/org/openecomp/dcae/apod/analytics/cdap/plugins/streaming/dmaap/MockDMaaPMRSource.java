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

package org.openecomp.dcae.apod.analytics.cdap.plugins.streaming.dmaap;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.streaming.StreamingContext;
import co.cask.cdap.etl.api.streaming.StreamingSource;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.openecomp.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A mock implementation of DMaaP MR Receiver which sends mock ves messages
 * <p>
 * @author Rajiv Singla . Creation Date: 2/15/2017.
 */
@Plugin(type = StreamingSource.PLUGIN_TYPE)
@Name("MockDMaaPMRSource")
@Description("Fetches DMaaP MR Messages at regular intervals")
public class MockDMaaPMRSource extends StreamingSource<StructuredRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(MockDMaaPMRSource.class);
    private static final long serialVersionUID = 1L;

    private final DMaaPMRSourcePluginConfig pluginConfig;

    public MockDMaaPMRSource(final DMaaPMRSourcePluginConfig pluginConfig) {
        LOG.debug("Creating DMaaP MR Source plugin with plugin Config: {}", pluginConfig);
        this.pluginConfig = pluginConfig;
    }

    @Override
    public void configurePipeline(PipelineConfigurer pipelineConfigurer) {
        final Integer pollingInterval = pluginConfig.getPollingInterval();
        if (pollingInterval == null) {
            final String errorMessage = "Polling Interval field must be present";
            throw new CDAPSettingsException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        } else {
            LOG.info("Mock Message will be send every ms: {}", pollingInterval);
        }
    }

    @Override
    public JavaDStream<StructuredRecord> getStream(final StreamingContext streamingContext) throws Exception {
        return streamingContext.getSparkStreamingContext().receiverStream(
                new MockDMaaPMRReceiver(StorageLevel.MEMORY_ONLY(), pluginConfig));
    }
}
