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

package org.onap.dcae.apod.analytics.cdap.plugins.sparkcompute.tca;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.format.StructuredRecord.Builder;
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.StageMetrics;
import co.cask.cdap.etl.api.batch.SparkCompute;
import co.cask.cdap.etl.api.batch.SparkExecutionPluginContext;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.onap.dcae.apod.analytics.cdap.common.CDAPMetricsConstants;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCACalculatorMessageType;
import org.onap.dcae.apod.analytics.cdap.common.utils.ValidationUtils;
import org.onap.dcae.apod.analytics.cdap.plugins.domain.config.tca.SimpleTCAPluginConfig;
import org.onap.dcae.apod.analytics.cdap.plugins.utils.CDAPPluginUtils;
import org.onap.dcae.apod.analytics.cdap.plugins.validator.SimpleTCAPluginConfigValidator;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.onap.dcae.apod.analytics.tca.processor.TCACEFJsonProcessor;
import org.onap.dcae.apod.analytics.tca.processor.TCACEFProcessorContext;
import org.onap.dcae.apod.analytics.tca.utils.TCAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rajiv Singla . Creation Date: 2/13/2017.
 */

@Plugin(type = SparkCompute.PLUGIN_TYPE)
@Name("SimpleTCAPlugin")
@Description("Used to create TCA (Threshold Crossing Alert) based on given Policy")
@SuppressFBWarnings("SE_INNER_CLASS")
public class SimpleTCAPlugin extends SparkCompute<StructuredRecord, StructuredRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleTCAPlugin.class);
    private static final long serialVersionUID = 1L;

    private final SimpleTCAPluginConfig pluginConfig;

    /**
     * Create an instance of Simple TCA Plugin with give Simple TCA Plugin Config
     *
     * @param pluginConfig Simple TCA Plugin Config
     */
    public SimpleTCAPlugin(SimpleTCAPluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
        LOG.info("Creating instance of Simple TCA Plugin with plugin config: {}", pluginConfig);
    }

    @Override
    public void configurePipeline(PipelineConfigurer pipelineConfigurer) {
        super.configurePipeline(pipelineConfigurer);
        ValidationUtils.validateSettings(pluginConfig, new SimpleTCAPluginConfigValidator());
        final Schema inputSchema = pipelineConfigurer.getStageConfigurer().getInputSchema();
        CDAPPluginUtils.validateSchemaContainsFields(inputSchema, pluginConfig.getVesMessageFieldName());
        CDAPPluginUtils.setOutputSchema(pipelineConfigurer, pluginConfig.getSchema());
    }

    @Override
    public JavaRDD<StructuredRecord> transform(final SparkExecutionPluginContext context,
                                               final JavaRDD<StructuredRecord> input) throws Exception {
        final StageMetrics metrics = context.getMetrics();

        LOG.debug("Invoking Spark Transform for Simple TCA Plugin");
        return input.map(new Function<StructuredRecord, StructuredRecord>() {

            @Override
            public StructuredRecord call(StructuredRecord inputStructuredRecord) throws Exception {
                TCACalculatorMessageType calculatorMessageType;
                String alertMessage = null;

                // Get input structured record
                final String cefMessage = inputStructuredRecord.get(pluginConfig.getVesMessageFieldName());

                // Get TCA Policy
                final TCAPolicy tcaPolicy = CDAPPluginUtils.readValue(pluginConfig.getPolicyJson(), TCAPolicy.class);

                // create initial processor context
                final TCACEFProcessorContext initialProcessorContext =
                        new TCACEFProcessorContext(cefMessage, tcaPolicy);

                final TCACEFJsonProcessor jsonProcessor = new TCACEFJsonProcessor();
                final TCACEFProcessorContext jsonProcessorContext =
                        jsonProcessor.processMessage(initialProcessorContext);

                if (jsonProcessorContext.getCEFEventListener() != null) {

                    LOG.debug("Json to CEF parsing successful. Parsed object {}",
                            jsonProcessorContext.getCEFEventListener());

                    // compute violations
                    final TCACEFProcessorContext processorContextWithViolations =
                            TCAUtils.computeThresholdViolations(jsonProcessorContext);

                    // if violation are found then create alert message
                    if (processorContextWithViolations.canProcessingContinue()) {

                        alertMessage = TCAUtils.createTCAAlertString(processorContextWithViolations,
                                pluginConfig.getReferenceName(), pluginConfig.getEnableAlertCEFFormat());
                        calculatorMessageType = TCACalculatorMessageType.NON_COMPLIANT;

                        LOG.debug("VES Threshold Violation Detected.An alert message is be generated: {}",
                                alertMessage);

                        final MetricsPerEventName metricsPerEventName =
                                processorContextWithViolations.getMetricsPerEventName();
                        if (metricsPerEventName != null
                                && metricsPerEventName.getThresholds() != null
                                && metricsPerEventName.getThresholds().get(0) != null) {
                            final Threshold violatedThreshold = metricsPerEventName.getThresholds().get(0);
                            LOG.debug("CEF Message: {}, Violated Threshold: {}", cefMessage, violatedThreshold);
                        }

                        metrics.count(CDAPMetricsConstants.TCA_VES_NON_COMPLIANT_MESSAGES_METRIC, 1);

                    } else {
                        LOG.debug("No Threshold Violation Detected. No alert will be generated.");
                        calculatorMessageType = TCACalculatorMessageType.COMPLIANT;
                        metrics.count(CDAPMetricsConstants.TCA_VES_COMPLIANT_MESSAGES_METRIC, 1);
                    }

                } else {
                    LOG.info("Unable to parse provided json message to CEF format. Invalid message: {}", cefMessage);
                    calculatorMessageType = TCACalculatorMessageType.INAPPLICABLE;
                }

                LOG.debug("Calculator message type: {} for message: {}", calculatorMessageType, cefMessage);

                final Schema outputSchema = Schema.parseJson(pluginConfig.getSchema());

                // create new output record builder and copy any input record values to output record builder
                final Builder outputRecordBuilder =
                        CDAPPluginUtils.createOutputStructuredRecordBuilder(outputSchema, inputStructuredRecord);

                // add alert field
                final Builder outputRecordBuilderWithAlertField =
                        CDAPPluginUtils.addFieldValueToStructuredRecordBuilder(outputRecordBuilder,
                                outputSchema, pluginConfig.getAlertFieldName(), alertMessage);

                // add message field type
                final Builder outRecordBuilderWithMessageTypeField =
                        CDAPPluginUtils.addFieldValueToStructuredRecordBuilder(outputRecordBuilderWithAlertField,
                                outputSchema, pluginConfig.getMessageTypeFieldName(), calculatorMessageType.toString());

                return outRecordBuilderWithMessageTypeField.build();
            }
        });
    }
}
