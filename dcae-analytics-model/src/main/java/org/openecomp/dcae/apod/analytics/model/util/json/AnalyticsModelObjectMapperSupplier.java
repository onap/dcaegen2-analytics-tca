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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.EnumSet;
import java.util.Set;

/**
 *<p>
 *     {@link Supplier} that can be used by clients to get Object Mapper which specializes
 *     in serialize and deserialize - DCAE Analytics Model JSON Objects. Clients can
 *     choose to memoize this Supplier for performance enhancements
 *     <br>
 *     NOTE: This supplier also setups up {@link JsonPath} default
 *     config to make use of this Supplier object mapper
 *</p>
 * @author Rajiv Singla . Creation Date: 11/10/2016.
 */
@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
public class AnalyticsModelObjectMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {

        final ObjectMapper objectMapper = new ObjectMapper();

        // Serialize null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Don't fail on unknown properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Register Common Event Format Module
        objectMapper.registerModule(new CommonEventFormatModule());
        // Register TCA Policy Module
        objectMapper.registerModule(new TCAPolicyModule());
        // Register TCA Facade Module
        objectMapper.registerModule(new TCAFacadeModelModule());
        // Register TCA Controller App Config Module
        objectMapper.registerModule(new TCAControllerConfigModule());


        // Setup JsonPath default config
        setupJsonPathDefaultConfig(objectMapper);

        return objectMapper;
    }


    /**
     * Setups up default Config for {@link JsonPath}
     *
     * @param objectMapper Jackson object mapper
     */
    private void setupJsonPathDefaultConfig(final ObjectMapper objectMapper) {

        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
            private final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {

                // Json Path exceptions are suppressed, also missing properties are tolerated
                return EnumSet.of(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);
            }
        });


    }

}
