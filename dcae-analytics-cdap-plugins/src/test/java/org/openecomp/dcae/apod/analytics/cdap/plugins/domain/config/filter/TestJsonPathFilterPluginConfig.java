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

package org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.filter;

/**
 * @author Rajiv Singla . Creation Date: 3/3/2017.
 */
public class TestJsonPathFilterPluginConfig extends JsonPathFilterPluginConfig {

    public TestJsonPathFilterPluginConfig(final String referenceName, final String incomingJsonFieldName,
                                          final String outputSchemaFieldName, final String jsonFilterMappings,
                                          final String schema) {
        super(referenceName, incomingJsonFieldName, outputSchemaFieldName, jsonFilterMappings, schema);
    }


    public void setIncomingJsonFieldName(String incomingJsonFieldName) {
        this.incomingJsonFieldName = incomingJsonFieldName;
    }

    public void setOutputSchemaFieldName(String outputSchemaFieldName) {
        this.outputSchemaFieldName = outputSchemaFieldName;
    }

    public void setJsonFilterMappings(String jsonFilterMappings) {
        this.jsonFilterMappings = jsonFilterMappings;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
