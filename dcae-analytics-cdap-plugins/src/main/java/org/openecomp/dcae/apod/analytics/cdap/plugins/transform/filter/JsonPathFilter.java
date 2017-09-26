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

package org.openecomp.dcae.apod.analytics.cdap.plugins.transform.filter;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.etl.api.Emitter;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.Transform;
import co.cask.cdap.etl.api.TransformContext;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.filter.JsonPathFilterPluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.CDAPPluginUtils;
import org.openecomp.dcae.apod.analytics.cdap.plugins.validator.JsonPathFilterPluginConfigValidator;
import org.openecomp.dcae.apod.analytics.common.service.filter.JsonMessageFilterProcessorContext;
import org.openecomp.dcae.apod.analytics.common.utils.MessageProcessorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * Json Path filter Plugin filters incoming schema field based of given json path expected values
 * <p>
 * @author Rajiv Singla . Creation Date: 3/2/2017.
 */

@Plugin(type = Transform.PLUGIN_TYPE)
@Name("JsonPathFilter")
@Description("Filters incoming schema field based of given json path expected values")
public class JsonPathFilter extends Transform<StructuredRecord, StructuredRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonPathFilter.class);

    private final JsonPathFilterPluginConfig pluginConfig;
    private final Map<String, Set<String>> jsonFilterPathMappings;

    public JsonPathFilter(final JsonPathFilterPluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
        jsonFilterPathMappings = Maps.newHashMap();
        LOG.info("Created instance of Json Path Filter Plugin with plugin config: {}", pluginConfig);
    }


    @Override
    public void initialize(final TransformContext context) throws Exception {
        super.initialize(context);
        populateJsonFilterMapping();
    }

    @Override
    public void configurePipeline(final PipelineConfigurer pipelineConfigurer) {
        super.configurePipeline(pipelineConfigurer);
        ValidationUtils.validateSettings(pluginConfig, new JsonPathFilterPluginConfigValidator());
        final Schema inputSchema = pipelineConfigurer.getStageConfigurer().getInputSchema();
        CDAPPluginUtils.validateSchemaContainsFields(inputSchema, pluginConfig.getIncomingJsonFieldName());
        populateJsonFilterMapping();
        CDAPPluginUtils.setOutputSchema(pipelineConfigurer, pluginConfig.getSchema());
    }

    @Override
    public void transform(final StructuredRecord inputStructuredRecord, final Emitter<StructuredRecord> emitter)
            throws Exception {

        // get input json message
        final String jsonMessage = inputStructuredRecord.get(pluginConfig.getIncomingJsonFieldName());

        // process Json Filter Mappings
        final JsonMessageFilterProcessorContext jsonMessageFilterProcessorContext =
                MessageProcessorUtils.processJsonFilterMappings(jsonMessage, jsonFilterPathMappings);

        // create new output record builder and copy any input Structured record values to output record builder
        final Schema outputSchema = Schema.parseJson(pluginConfig.getSchema());
        final StructuredRecord.Builder outputRecordBuilder =
                CDAPPluginUtils.createOutputStructuredRecordBuilder(outputSchema, inputStructuredRecord);

        // add json filter matched field
        final StructuredRecord.Builder outputRecordBuilderWithMatchedField =
                CDAPPluginUtils.addFieldValueToStructuredRecordBuilder(outputRecordBuilder,
                        outputSchema, pluginConfig.getOutputSchemaFieldName(),
                        jsonMessageFilterProcessorContext.getMatched());

        // emit structured record with filtering matched field
        final StructuredRecord outputStructuredRecord = outputRecordBuilderWithMatchedField.build();

        LOG.debug("Incoming Json Message: {}.Json Path Filter Output Matched Field: {}", jsonMessage,
                outputStructuredRecord.get(pluginConfig.getOutputSchemaFieldName()));

        emitter.emit(outputStructuredRecord);

    }

    /**
     * Populates Json Filter Mapping
     */
    private void populateJsonFilterMapping() {
        final Map<String, String> fieldMappings =
                CDAPPluginUtils.extractFieldMappings(pluginConfig.getJsonFilterMappings());
        if (fieldMappings.isEmpty()) {
            throw new IllegalArgumentException("No Field Mapping found. Invalid Filter mapping configuration");
        }
        final Splitter semiColonSplitter = Splitter.on(";");
        for (Map.Entry<String, String> fieldMappingEntry : fieldMappings.entrySet()) {
            jsonFilterPathMappings.put(fieldMappingEntry.getKey(),
                    Sets.newLinkedHashSet(semiColonSplitter.split(fieldMappingEntry.getValue())));
        }
        LOG.info("Input Json Filter Mappings: {}", jsonFilterPathMappings);
    }
}
