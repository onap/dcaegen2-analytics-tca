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

package org.onap.dcae.apod.analytics.model.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import org.onap.dcae.apod.analytics.model.util.json.AnalyticsModelObjectMapperSupplier;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/7/2016.
 */
public abstract class AnalyticsModelJsonUtils {

    /**
     * Object mapper to be used for all TCA Json Parsing
     */
    protected static final ObjectMapper ANALYTICS_MODEL_OBJECT_MAPPER =
            Suppliers.memoize(new AnalyticsModelObjectMapperSupplier()).get();


    /**
     * Converts Input Stream to given type reference object
     *
     * @param inputStream input stream
     * @param valueTypeRef type reference
     * @param <T> type of type reference
     *
     * @return parsed json object
     *
     * @throws IOException IO Exception
     */
    public static <T> T readValue(InputStream inputStream, TypeReference<T> valueTypeRef) throws IOException {
        return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(inputStream, valueTypeRef);
    }


    /**
     * Converts Input Stream to given target class object
     *
     * @param inputStream input stream
     * @param targetClass target class type
     * @param <T> type of class
     *
     * @return parsed json object
     *
     * @throws IOException IO Exception
     */
    public static <T> T readValue(InputStream inputStream, Class<T> targetClass) throws IOException {
        return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(inputStream, targetClass);
    }


    /**
     * Converts given object to JSON string
     *
     * @param value object that needs to converted to json string
     *
     * @return json string
     * @throws JsonProcessingException Json Processing exception
     */
    public static String writeValueAsString(Object value) throws JsonProcessingException {
        return ANALYTICS_MODEL_OBJECT_MAPPER.writeValueAsString(value);
    }


    /**
     *  Method to deserialize JSON content from given JSON content String.
     *
     * @param jsonString JSON String
     * @param objectClass target object class
     * @param <T> object class type
     *
     * @return converted Object from JSON String
     *
     * @throws IOException IO Exception
     */
    public static <T> T readValue(final String jsonString, final Class<T> objectClass) throws IOException {
        return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(jsonString, objectClass);
    }
}
