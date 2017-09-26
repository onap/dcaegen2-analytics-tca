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

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.data.schema.Schema;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class CDAPPluginUtilsTest extends BaseAnalyticsCDAPPluginsUnitTest {


    @Test
    public void testValidateSchemaContainsFieldsWhenSchemaIsNotNull() throws Exception {
        final Schema dMaaPMRSinkTestSchema = getDMaaPMRSinkTestSchema();
        CDAPPluginUtils.validateSchemaContainsFields(dMaaPMRSinkTestSchema, "message");
    }

    @Test
    public void testValidateSchemaContainsFieldsWhenInputSchemaIsNull() throws Exception {
        CDAPPluginUtils.validateSchemaContainsFields(null, "message");
    }

    @Test
    public void testCreateStructuredRecord() throws Exception {
        final StructuredRecord testMessage = CDAPPluginUtils.createDMaaPMRResponseStructuredRecord("testMessage");
        assertNotNull(testMessage);
    }


    @Test
    public void testCreateOutputStructuredRecordBuilder() throws Exception {

        final String messageFieldName = "message";
        final String firstInputFieldName = "inputField1";
        final String secondInputFieldName = "inputField2";


        final Schema inputSchema = Schema.recordOf(
                "inputSchema",
                Schema.Field.of(messageFieldName, Schema.of(Schema.Type.STRING)),
                Schema.Field.of(firstInputFieldName, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                Schema.Field.of(secondInputFieldName, Schema.nullableOf(Schema.of(Schema.Type.STRING)))
        );

        final String addedFieldName = "addedField";
        final Schema outputSchema = Schema.recordOf(
                "outputSchema",
                Schema.Field.of(messageFieldName, Schema.of(Schema.Type.STRING)),
                Schema.Field.of(firstInputFieldName, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                Schema.Field.of(addedFieldName, Schema.nullableOf(Schema.of(Schema.Type.STRING))) // added field
                // missing second Input Field
        );

        // input structured record
        final String messageFieldValue = "Message String";
        final String firstFieldValue = "Input Field 1";
        final String secondFieldValue = "Input Field 2";
        final StructuredRecord inputSR = StructuredRecord.builder(inputSchema)
                .set(messageFieldName, messageFieldValue)
                .set(firstInputFieldName, firstFieldValue)
                .set(secondInputFieldName, secondFieldValue)
                .build();

        final StructuredRecord.Builder outputStructuredRecordBuilder =
                CDAPPluginUtils.createOutputStructuredRecordBuilder(outputSchema, inputSR);

        final String addedFieldValue = "Added Field Value";
        final StructuredRecord outputSR = outputStructuredRecordBuilder
                .set(addedFieldName, addedFieldValue)
                .build();

        assertThat("Added Field field value copied correctly",
                outputSR.get(addedFieldName).toString(), is(addedFieldValue));

        assertThat("Output SR has message field copied correctly",
                outputSR.get(messageFieldName).toString(), is(messageFieldValue));

        assertThat("First Field value copied correctly",
                outputSR.get(firstInputFieldName).toString(), is(firstFieldValue));

        assertNull("Second Field value is null as output schema does not have the field",
                outputSR.get(secondInputFieldName));

    }


    @Test
    public void testAddFieldValueToStructuredRecordBuilder() throws Exception {

        final String messageFieldName = "message";
        final String firstInputFieldName = "inputField1";
        final String addedFieldName = "addedField";
        final String firstFieldValue = "Input Field 1";
        final String addedFieldValue = "Added Field Value";
        final Schema outputSchema = Schema.recordOf(
                "outputSchema",
                Schema.Field.of(messageFieldName, Schema.of(Schema.Type.STRING)),
                Schema.Field.of(firstInputFieldName, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                Schema.Field.of(addedFieldName, Schema.nullableOf(Schema.of(Schema.Type.STRING))) // added field
        );

        final StructuredRecord.Builder outputSRBuilder = StructuredRecord.builder(outputSchema)
                .set(messageFieldName, "Some message")
                .set(firstInputFieldName, firstFieldValue);

        final StructuredRecord.Builder addedFieldSRBuilder = CDAPPluginUtils.addFieldValueToStructuredRecordBuilder(
                outputSRBuilder, outputSchema, addedFieldName, addedFieldValue);

        // Try adding field to output Structured record that is not in output schema
        final String nonExistentFieldName = "fieldNotInOutputSchema";
        final String nonExistentFieldValue = "Some Value";
        final StructuredRecord outputSR = CDAPPluginUtils.addFieldValueToStructuredRecordBuilder(
                addedFieldSRBuilder, outputSchema, nonExistentFieldName, nonExistentFieldValue).build();

        assertThat("Output SR must contain added Field which is in output schema",
                outputSR.get(addedFieldName).toString(), is(addedFieldValue));
        assertNull("Output SR must not contain field that is not in output schema",
                outputSR.get(nonExistentFieldName));

    }

    @Test(expected = DCAEAnalyticsRuntimeException.class)
    public void testValidateSchemaFieldTypeWhenInputSchemaIsNotValidJson() throws Exception {
        CDAPPluginUtils.validateSchemaFieldType("Invalid Schema", "field1", Schema.Type.STRING);
    }

    @Test(expected = DCAEAnalyticsRuntimeException.class)
    public void testSetOutputSchemaWhenOutputSchemaIsNotValidJson() throws Exception {
        CDAPPluginUtils.setOutputSchema(null, "Invalid output Schema");
    }

    @Test(expected = DCAEAnalyticsRuntimeException.class)
    public void testExtractFieldMappingsWhenFieldMappingValueIsEmpty() throws Exception {
        CDAPPluginUtils.extractFieldMappings("path1:,path2:value2");
    }

    @Test(expected = DCAEAnalyticsRuntimeException.class)
    public void testExtractFieldMappingsWhenFieldMappingAreBlank() throws Exception {
        CDAPPluginUtils.extractFieldMappings("path1: ,path2:value2");
    }


}
