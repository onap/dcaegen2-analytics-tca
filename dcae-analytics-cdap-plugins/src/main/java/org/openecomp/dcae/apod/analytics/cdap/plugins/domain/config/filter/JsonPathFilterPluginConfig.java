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

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import co.cask.cdap.api.annotation.Name;
import com.google.common.base.Objects;
import org.openecomp.dcae.apod.analytics.cdap.common.settings.CDAPBasePluginConfig;

/**
 * Configuration for Json Path Filter Plugin
 *
 * @author Rajiv Singla . Creation Date: 3/2/2017.
 */
public class JsonPathFilterPluginConfig extends CDAPBasePluginConfig {

    private static final long serialVersionUID = 1L;

    @Name("incomingJsonFieldName")
    @Description("Input schema field name that contain JSON used for filtering")
    @Macro
    protected String incomingJsonFieldName;


    @Name("outputSchemaFieldName")
    @Description("Name of the nullable boolean schema field name that will contain result of the filter matching")
    @Macro
    protected String outputSchemaFieldName;


    @Name("jsonFilterMappings")
    @Macro
    @Description("Filters incoming JSON based on given filter mappings - in terms of JSON path and expected values." +
            "Right hand side contains JSON path. Left hand side contains semicolon (';') separated expected values " +
            "for that JSON Path. If all provided JSON Path mappings and corresponding values matches - " +
            "output schema field will be marked as true")
    protected String jsonFilterMappings;


    @Name("schema")
    @Description("Output Schema")
    protected String schema;


    public JsonPathFilterPluginConfig(final String referenceName, final String incomingJsonFieldName,
                                      final String outputSchemaFieldName, final String jsonFilterMappings,
                                      final String schema) {
        this.referenceName = referenceName;
        this.incomingJsonFieldName = incomingJsonFieldName;
        this.outputSchemaFieldName = outputSchemaFieldName;
        this.jsonFilterMappings = jsonFilterMappings;
        this.schema = schema;
    }

    /**
     * Provides incoming plugin schema field name which contains json used to apply filter
     *
     * @return  name of incoming schema field containing JSON to be filtered
     */
    public String getIncomingJsonFieldName() {
        return incomingJsonFieldName;
    }

    /**
     * Provides plugin output schema filed name that will contain result of filter application
     * It must be nullable and boolean type
     *
     * @return name of outgoing schema filed name that will contain filtering result
     */
    public String getOutputSchemaFieldName() {
        return outputSchemaFieldName;
    }

    /**
     * Provides JSON filter mappings. LHS contains JSON path value and RHS contains expected
     * values separated by semicolon
     *
     *
     * @return String for JSON filter mappings
     */
    public String getJsonFilterMappings() {
        return jsonFilterMappings;
    }

    /**
     * Output Schema
     *
     * @return output schema string
     */
    public String getSchema() {
        return schema;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("referenceName", referenceName)
                .add("incomingJsonFieldName", incomingJsonFieldName)
                .add("outputSchemaFieldName", outputSchemaFieldName)
                .add("jsonFilterMappings", jsonFilterMappings)
                .add("schema", schema)
                .toString();
    }

}
