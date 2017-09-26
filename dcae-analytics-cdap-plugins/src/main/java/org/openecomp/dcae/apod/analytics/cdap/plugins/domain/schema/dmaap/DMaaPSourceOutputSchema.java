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

package org.openecomp.dcae.apod.analytics.cdap.plugins.domain.schema.dmaap;

import co.cask.cdap.api.data.schema.Schema;
import org.openecomp.dcae.apod.analytics.cdap.plugins.common.PluginSchema;

/**
 * Output Schema for DMaaP MR Source Plugin
 *
 * @author Rajiv Singla . Creation Date: 1/25/2017.
 */
public enum DMaaPSourceOutputSchema implements PluginSchema {

    TIMESTAMP("ts"),
    RESPONSE_CODE("responseCode"),
    RESPONSE_MESSAGE("responseMessage"),
    FETCHED_MESSAGE("message");

    private String schemaColumnName;

    DMaaPSourceOutputSchema(String schemaColumnName) {
        this.schemaColumnName = schemaColumnName;
    }

    @Override
    public String getSchemaColumnName() {
        return schemaColumnName;
    }

    public static Schema getSchema() {
        return Schema.recordOf(
                "DMaaPMRSourcePluginResponse",
                Schema.Field.of(TIMESTAMP.getSchemaColumnName(), Schema.of(Schema.Type.LONG)),
                Schema.Field.of(RESPONSE_CODE.getSchemaColumnName(), Schema.of(Schema.Type.INT)),
                Schema.Field.of(RESPONSE_MESSAGE.getSchemaColumnName(), Schema.of(Schema.Type.STRING)),
                Schema.Field.of(FETCHED_MESSAGE.getSchemaColumnName(), Schema.of(Schema.Type.STRING))
        );
    }

}
