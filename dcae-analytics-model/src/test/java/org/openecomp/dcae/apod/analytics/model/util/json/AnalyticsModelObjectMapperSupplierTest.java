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

package org.openecomp.dcae.apod.analytics.model.util.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;


/**
 * @author Rajiv Singla . Creation Date: 12/16/2016.
 */
public class AnalyticsModelObjectMapperSupplierTest extends BaseAnalyticsModelUnitTest {


    @Test
    public void testJsonPathSettings() throws Exception {
        final Configuration configuration = JsonPath.parse("{\"test\": \"test\"}").configuration();

        final JsonProvider jsonProvider = configuration.jsonProvider();
        final String jsonProviderClassName = jsonProvider.getClass().getSimpleName();
        assertThat("Json Provider cass name must be JacksonJsonProvider",
                jsonProviderClassName, is("JacksonJsonProvider"));

        final MappingProvider mappingProvider = configuration.mappingProvider();
        final String mappingProviderClassName = mappingProvider.getClass().getSimpleName();
        assertThat("Mapping Provider cass name must be JacksonMappingProvider",
                mappingProviderClassName, is("JacksonMappingProvider"));

        final Set<Option> configurationOptions = configuration.getOptions();
        assertThat(configurationOptions,
                containsInAnyOrder(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS,
                        Option.ALWAYS_RETURN_LIST));
    }

}
