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

package org.onap.dcae.apod.analytics.cdap.plugins.utils;

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.etl.api.PipelineConfigurer;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.onap.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.onap.dcae.apod.analytics.cdap.plugins.domain.schema.dmaap.DMaaPSourceOutputSchema;
import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.onap.dcae.apod.analytics.model.util.AnalyticsModelJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Rajiv Singla . Creation Date: 1/26/2017.
 */
public abstract class CDAPPluginUtils extends AnalyticsModelJsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CDAPPluginUtils.class);

    public static final Function<Schema, Schema.Type> SCHEMA_TO_TYPE_FUNCTION = new Function<Schema, Schema.Type>() {
        @Override
        public Schema.Type apply(@Nonnull Schema schema) {
            return schema.getType();
        }
    };



    private CDAPPluginUtils() {
        // private constructor
    }

    /**
     * Validates if CDAP Schema contains expected fields
     *
     * @param schema schema that need to be validated
     * @param expectedFields fields that are expected to be in the schema
     */

    public static void validateSchemaContainsFields(@Nullable final Schema schema, final String... expectedFields) {

        LOG.debug("Validating schema:{} contains expected fields:{}", schema, Arrays.toString(expectedFields));

        if (schema == null) {
            // If input schema is null then no validation possible
            LOG.warn("Input Schema is null. No validation possible");
        } else {
            // Check if expected fields are indeed present in the schema
            for (String expectedField : expectedFields) {
                final Schema.Field schemaField = schema.getField(expectedField);
                if (schemaField == null) {
                    final String errorMessage = String.format(
                            "Unable to find expected field: %s, in schema: %s", expectedField, schema);
                    throw new CDAPSettingsException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
                }
            }
            LOG.debug("Successfully validated schema:{}, contains expected fields:{}", schema,
                    Arrays.toString(expectedFields));
        }
    }


    /**
     * Creates a new Structured Record containing DMaaP MR fetched message
     *
     * @param message DMaaP MR fetch message
     *
     * @return Structured record containing DMaaP MR Message
     */
    public static StructuredRecord createDMaaPMRResponseStructuredRecord(final String message) {
        StructuredRecord.Builder recordBuilder = StructuredRecord.builder(DMaaPSourceOutputSchema.getSchema());
        recordBuilder
                .set(DMaaPSourceOutputSchema.TIMESTAMP.getSchemaColumnName(), System.nanoTime())
                .set(DMaaPSourceOutputSchema.RESPONSE_CODE.getSchemaColumnName(), 200)
                .set(DMaaPSourceOutputSchema.RESPONSE_MESSAGE.getSchemaColumnName(), "OK")
                .set(DMaaPSourceOutputSchema.FETCHED_MESSAGE.getSchemaColumnName(), message);
        return recordBuilder.build();
    }


    /**
     * Creates output StructuredRecord Builder which has copied values from input StructuredRecord
     *
     * @param outputSchema output Schema
     * @param inputStructuredRecord input Structured Record
     *
     * @return output Structured Record builder with pre populated values from input structured record
     */
    public static StructuredRecord.Builder createOutputStructuredRecordBuilder(
            @Nonnull final Schema outputSchema,
            @Nonnull final StructuredRecord inputStructuredRecord) {

        // Get input structured Record Schema
        final Schema inputSchema = inputStructuredRecord.getSchema();
        // Create new instance of output Structured Record Builder from output Schema
        final StructuredRecord.Builder outputStructuredRecordBuilder = StructuredRecord.builder(outputSchema);

        // iterate over input fields and if output schema has field with same name copy the value to out record builder
        for (Schema.Field inputField : inputSchema.getFields()) {
            final String inputFieldName = inputField.getName();
            if (outputSchema.getField(inputFieldName) != null) {
                outputStructuredRecordBuilder.set(inputFieldName, inputStructuredRecord.get(inputFieldName));
            }
        }

        return outputStructuredRecordBuilder;
    }


    /**
     * Adds Field value to StructuredRecord Builder if schema contains that field Name
     *
     * @param structuredRecordBuilder structured record builder
     * @param structuredRecordSchema schema for structured record builder
     * @param fieldName field name
     * @param fieldValue field value
     *
     * @return structured record builder with populated field name and value if schema contains field name
     */
    public static StructuredRecord.Builder addFieldValueToStructuredRecordBuilder(
            @Nonnull final StructuredRecord.Builder structuredRecordBuilder,
            @Nonnull final Schema structuredRecordSchema,
            @Nonnull final String fieldName,
            final Object fieldValue) {

        // check if schema contains field Name
        if (structuredRecordSchema.getField(fieldName) != null) {
            structuredRecordBuilder.set(fieldName, fieldValue);
        } else {
            LOG.info("Unable to populate value for field Name: {} with field value: {}. " +
                            "Schema Fields: {} does not contain field name: {}",
                    fieldName, fieldValue, structuredRecordSchema.getFields(), fieldName);
        }

        return structuredRecordBuilder;
    }


    /**
     * Validates that given schema String has fieldName of expected type. If field does not exist in given schema
     * then validation will pass with warning. If field does exist in given schema then this validation will return
     * true if field type is same as expected type else false
     *
     * @param schemaString CDAP Plugin output or input schema string
     * @param fieldName field name
     * @param expectedFieldType expected schema field type
     *
     * @return true if field type matches expected field type else false. If field does not exist in
     * give schema validation will pass but will generate a warning message
     */
    public static boolean validateSchemaFieldType(@Nonnull final String schemaString,
                                                  @Nonnull final String fieldName,
                                                  @Nonnull final Schema.Type expectedFieldType) {

        try {
            // parse given schema String
            final Schema outputSchema = Schema.parseJson(schemaString);
            final Schema.Field schemaField = outputSchema.getField(fieldName);

            // if given schema does contain field then validated fieldName type
            if (schemaField != null) {

                final List<Schema> schemas = new LinkedList<>();

                // if it is a union type then grab all union schemas
                if (outputSchema.getField(fieldName).getSchema().getType() == Schema.Type.UNION) {
                    final List<Schema> unionFieldSchemas =
                            outputSchema.getField(fieldName).getSchema().getUnionSchemas();
                    schemas.addAll(unionFieldSchemas);
                } else {
                    // if not union type the just get the field schema
                    final Schema fieldSchema = outputSchema.getField(fieldName).getSchema();
                    schemas.add(fieldSchema);
                }

                // get all schema types
                final List<Schema.Type> fieldTypes =
                        Lists.transform(schemas, CDAPPluginUtils.SCHEMA_TO_TYPE_FUNCTION);

                // if all schema types does not contain expected field type then return false
                if (!fieldTypes.contains(expectedFieldType)) {
                    LOG.error("Validation failed for fieldName: {} is NOT of expected Type: {} in schema: {}",
                            fieldName, expectedFieldType, outputSchema);
                    return false;
                }

                // field type validation passed
                LOG.debug("Successfully validated fieldName: {} is of expected Type: {}",
                        fieldName, expectedFieldType);

                return true;

            } else {

                // if field does not exist then the validation will pass but will generate warning message
                LOG.warn("Validation of field type not possible. Field name: {} does not exist in schema: {}",
                        fieldName, outputSchema);
                return true;
            }

        } catch (IOException e) {
            final String errorMessage =
                    String.format("Unable to parse schema: %s for field type validation. " +
                                    "Field Name: %s, Expected Field Type: %s Exception: %s",
                            schemaString, fieldName, expectedFieldType, e);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }

    }


    /**
     * Parses provided schema String as Schema object and set it as output Schema format
     *
     * @param pipelineConfigurer plugin pipeline configurer
     * @param schemaString schema String to be set as output schema
     */
    public static void setOutputSchema(final PipelineConfigurer pipelineConfigurer, final String schemaString) {
        try {
            final Schema outputSchema = Schema.parseJson(schemaString);
            pipelineConfigurer.getStageConfigurer().setOutputSchema(outputSchema);
        } catch (IOException e) {
            final String errorMessage = String.format(
                    "Schema specified is not a valid JSON. Schema String: %s, Exception: %s", schemaString, e);
            throw new CDAPSettingsException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        }
    }


    /**
     * Parses incoming plugin config mapping to key value map. If any of the key value map is blank an Illegal Argument
     * exception will be thrown
     *
     * @param mappingFieldString field Mapping String
     *
     * @return map containing mapping key values
     */
    public static Map<String, String> extractFieldMappings(final String mappingFieldString) {
        final Map<String, String> fieldMappings = Maps.newHashMap();
        if (StringUtils.isNotBlank(mappingFieldString)) {
            final Splitter commaSplitter = Splitter.on(",");
            for (String fieldMapping : commaSplitter.split(mappingFieldString)) {
                final String[] keyValueMappings = fieldMapping.split(":");
                if (keyValueMappings.length != 2 ||
                        StringUtils.isBlank(keyValueMappings[0]) ||
                        StringUtils.isBlank(keyValueMappings[1])) {
                    final String errorMessage = "Field Mapping key or value is Blank. All field mappings must " +
                            "be present in mappings: " + mappingFieldString;
                    throw new DCAEAnalyticsRuntimeException(
                            errorMessage, LOG, new IllegalArgumentException(errorMessage));
                }
                fieldMappings.put(keyValueMappings[0].trim(), keyValueMappings[1].trim());
            }
        }
        return fieldMappings;
    }




}
