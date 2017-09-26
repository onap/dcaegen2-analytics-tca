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
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 1/25/2017.
 */
public class DMaaPSourceOutputSchemaTest extends BaseAnalyticsCDAPPluginsUnitTest {


    @Test
    public void testGetSchemaColumnName() throws Exception {
        assertThat(DMaaPSourceOutputSchema.TIMESTAMP.getSchemaColumnName(), is("ts"));
        assertThat(DMaaPSourceOutputSchema.RESPONSE_CODE.getSchemaColumnName(), is("responseCode"));
        assertThat(DMaaPSourceOutputSchema.RESPONSE_MESSAGE.getSchemaColumnName(), is("responseMessage"));
        assertThat(DMaaPSourceOutputSchema.FETCHED_MESSAGE.getSchemaColumnName(), is("message"));
    }

    @Test
    public void testGetSchema() throws Exception {
        final Schema schema = DMaaPSourceOutputSchema.getSchema();
        final List<Schema.Field> fields = schema.getFields();
        final List<String> fieldNames = new LinkedList<>();
        for (Schema.Field field : fields) {
            fieldNames.add(field.getName());
        }
        assertThat(fieldNames, hasItems(
                DMaaPSourceOutputSchema.TIMESTAMP.getSchemaColumnName(),
                DMaaPSourceOutputSchema.RESPONSE_CODE.getSchemaColumnName(),
                DMaaPSourceOutputSchema.RESPONSE_MESSAGE.getSchemaColumnName(),
                DMaaPSourceOutputSchema.FETCHED_MESSAGE.getSchemaColumnName()));
    }

}
