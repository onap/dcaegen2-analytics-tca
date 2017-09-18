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

package org.openecomp.dcae.apod.analytics.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.openecomp.dcae.apod.analytics.model.util.json.AnalyticsModelObjectMapperSupplier;
import org.openecomp.dcae.apod.analytics.test.BaseDCAEAnalyticsUnitTest;

/**
 * @author Rajiv Singla . Creation Date: 10/17/2016.
 */
public abstract class BaseAnalyticsModelUnitTest extends BaseDCAEAnalyticsUnitTest {


    protected static ObjectMapper objectMapper;

    @BeforeClass
    public static void beforeClass() {
        final AnalyticsModelObjectMapperSupplier analyticsModelObjectMapperSupplier =
            new AnalyticsModelObjectMapperSupplier();
        objectMapper = Suppliers.memoize(analyticsModelObjectMapperSupplier).get();
    }


    /**
     * Deserialize given Json file location to given model class and returns it back without any validation check
     *
     * @param jsonFileLocation Classpath location of the json file
     * @param modelClass       Model Class type
     * @param <T>              Json Model Type
     * @return Deserialized Model Object
     */
    public static <T> T deserializeJsonFileToModel(String jsonFileLocation, Class<T> modelClass) {
        final InputStream jsonFileInputStream =
            BaseDCAEAnalyticsUnitTest.class.getClassLoader().getResourceAsStream(jsonFileLocation);
        Assert.assertNotNull("Json File Location must be valid", jsonFileInputStream);
        try {
            return objectMapper.readValue(jsonFileInputStream, modelClass);
        } catch (IOException ex) {
            LOG.error("Error while doing assert Json for fileLocation: {}, modelClass: {}, Exception {}",
                jsonFileLocation, modelClass, ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                jsonFileInputStream.close();
            } catch (IOException e) {
                LOG.error("Error while closing input stream at file location: {}", jsonFileLocation);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Deserialize given Json file location to given model class and then validates deserialization by comparing it
     * with given expected Object
     *
     * @param jsonFileLocation   Classpath location of the json file
     * @param modelClass         Model Class type
     * @param expectedJsonObject Expected Json Object
     * @param <T>                Json Model Type
     * @return deserialized actual value if expected Json Object matches deserialized object
     */
    public static <T> T assertJsonDeserialization(String jsonFileLocation, Class<T> modelClass, T expectedJsonObject) {
        final T actualValue = deserializeJsonFileToModel(jsonFileLocation, modelClass);
        assertThat(actualValue, is(expectedJsonObject));
        return actualValue;
    }

    public static String serializeModelToJson(Object model) throws JsonProcessingException {
        return objectMapper.writeValueAsString(model);
    }

    /**
     * Converts given model to json string and compare it with json present at given file location
     *
     * @param model                    Model which needs to be compared
     * @param expectedJsonFileLocation Location of file containing expected json string
     *
     * @return If assertion passes returns the input model
     */
    public static <T> T assertJsonSerialization(T model, String expectedJsonFileLocation) {
        try {
            final String actualModelString = serializeModelToJson(model);
            final String expectedModelString = fromStream(expectedJsonFileLocation);
            assertJson(expectedModelString, actualModelString);
            return model;
        } catch (IOException | JSONException ex) {
            LOG.error("Error while doing assert Json serialization Assertion: model: {}, " +
                "expected Json File Location: {}, Exception {}", model, expectedJsonFileLocation, ex);
            throw new RuntimeException(ex);
        }
    }


    /**
     * Checks both serialization and deserialization.
     *
     * First checks deserialization and then serialize the deserialized object back to json
     * and check if matches the given json file location string
     *
     * @param jsonFileLocation   Classpath location of the json file
     * @param modelClass         Class type
     * @param <T>                Json Model Type
     *
     * @return If assertion passes, returns deserialized object
     */

    public static <T> T assertJsonConversions(String jsonFileLocation, Class<T> modelClass) {
        //first check deserialization
        final T actualValue = deserializeJsonFileToModel(jsonFileLocation, modelClass);
        //then check serialization
        assertJsonSerialization(actualValue, jsonFileLocation);

        return actualValue;
    }
}
