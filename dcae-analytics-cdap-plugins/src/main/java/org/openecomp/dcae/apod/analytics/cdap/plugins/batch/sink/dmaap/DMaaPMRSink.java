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

package org.openecomp.dcae.apod.analytics.cdap.plugins.batch.sink.dmaap;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.api.data.batch.Output;
import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.api.dataset.lib.KeyValue;
import co.cask.cdap.etl.api.Emitter;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.batch.BatchSink;
import co.cask.cdap.etl.api.batch.BatchSinkContext;
import org.apache.hadoop.io.NullWritable;
import org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSinkPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.CDAPPluginUtils;
import org.openecomp.dcae.apod.analytics.cdap.plugins.validator.DMaaPMRSinkPluginConfigValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rajiv Singla . Creation Date: 1/26/2017.
 */
@Plugin(type = BatchSink.PLUGIN_TYPE)
@Name("DMaaPMRSink")
@Description("A batch sink Plugin that publishes messages to DMaaP MR Topic.")
public class DMaaPMRSink extends BatchSink<StructuredRecord, String, NullWritable> {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRSink.class);

    private final DMaaPMRSinkPluginConfig pluginConfig;

    public DMaaPMRSink(final DMaaPMRSinkPluginConfig pluginConfig) {
        LOG.debug("Creating DMaaP MR Sink Plugin with plugin Config: {}", pluginConfig);
        this.pluginConfig = pluginConfig;
    }

    @Override
    public void configurePipeline(final PipelineConfigurer pipelineConfigurer) {
        super.configurePipeline(pipelineConfigurer);
        ValidationUtils.validateSettings(pluginConfig, new DMaaPMRSinkPluginConfigValidator());
        // validates that input schema contains the field provided in Sink Message Column Name property
        final Schema inputSchema = pipelineConfigurer.getStageConfigurer().getInputSchema();
        CDAPPluginUtils.validateSchemaContainsFields(inputSchema, pluginConfig.getMessageColumnName());
    }


    @Override
    public void prepareRun(BatchSinkContext context) throws Exception {
        context.addOutput(Output.of(pluginConfig.getReferenceName(), new DMaaPMROutputFormatProvider(pluginConfig)));
    }

    @Override
    public void transform(StructuredRecord structuredRecord,
                          Emitter<KeyValue<String, NullWritable>> emitter) throws Exception {
        // get incoming message from structured record
        final String incomingMessage = structuredRecord.get(pluginConfig.getMessageColumnName());

        // if incoming messages does not have message column name log warning as it should not happen
        if (incomingMessage == null) {
            LOG.warn("Column Name: {}, contains no message.Skipped for DMaaP MR Publishing....",
                    pluginConfig.getMessageColumnName());
        } else {

            // emit the messages as key
            emitter.emit(new KeyValue<String, NullWritable>(incomingMessage, null));
        }
    }
}
