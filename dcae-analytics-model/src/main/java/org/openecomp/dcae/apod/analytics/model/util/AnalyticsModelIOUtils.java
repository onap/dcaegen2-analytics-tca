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

package org.openecomp.dcae.apod.analytics.model.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class containing methods for IO related operations
 * <p>
 * @author Rajiv Singla . Creation Date: 10/17/2016.
 */
public abstract class AnalyticsModelIOUtils extends AnalyticsModelJsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsModelIOUtils.class);

    /**
     * Parses given valid JSON file Location to object of given binding class type.
     *
     * @param fileLocation valid JSON File Location
     * @param bindingClass class Type of Binding object
     *
     * @param <T>  binding Class Type
     *
     * @return binding Class Object which properties populated from JSON File Location
     * @throws IOException when fails to do IO operations
     */
    public static final <T> T convertToJsonObject(String fileLocation, Class<T> bindingClass) throws IOException {

        // Load Resource from give path
        final InputStream resourceAsStream = loadResourceAsStream(fileLocation);

        // If resource is null throw an exception
        if (resourceAsStream == null) {
            final String errorMessage = String.format("Invalid File location: %s", fileLocation);
            throw new IOException(errorMessage, new FileNotFoundException(errorMessage));
        }

        // Parse input stream
        try (InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream, Charset.forName("UTF-8"))) {

            return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(inputStreamReader, bindingClass);
        } catch (JsonMappingException | JsonParseException e) {

            // If parsing fails due to Invalid Json or Json IO Issues throw an exception
            final String errorMessage = String.format("Json parsing error while parsing Json File location: %s",
                fileLocation);

            LOG.error(errorMessage);
            throw new IOException(errorMessage, e);
        } catch (IOException e) {

            // If parsing fails due to IO Issues throw an exception
            final String errorMessage = String.format("IO Error while parsing Json File location: %s", fileLocation);
            LOG.error(errorMessage);
            throw new IOException(errorMessage, e);
        }
    }

    /**
     * Loads properties from a given file location. Throws {@link RuntimeException} if file location is invalid
     * or there were exception when loading properties
     *
     * @param propertiesFileLocation path string for properties file
     * @param properties properties object that needs to be populated with give file properties
     *
     * @return properties object with populated properties from properties file
     *
     */
    public static Properties loadPropertiesFile(String propertiesFileLocation, final Properties properties) {

        // Load Resource from give properties file path
        final InputStream propertiesFileInputStream = loadResourceAsStream(propertiesFileLocation);

        // If properties file is not present throw an exception
        if (propertiesFileInputStream == null) {
            final String errorMessage = String.format("Invalid Properties File at location: %s",
                propertiesFileLocation);
            //TODO: discuss and change this excpeiton as well.
            throw new RuntimeException(errorMessage, new FileNotFoundException(errorMessage));
        }

        try {
            properties.load(propertiesFileInputStream);
        } catch (IOException e) {
            final String errorMessage = String.format("IO Exception while reading Properties File at location: %s",
                propertiesFileLocation);
            throw new RuntimeException(errorMessage, e);
        }

        return properties;
    }

    /**
     * Loads Input file from the given classpath file location and returns file InputStream
     *
     * @param fileLocation classpath file location
     *
     * @return {@link InputStream} for classpath file
     */
    public static InputStream loadResourceAsStream(String fileLocation) {
        // Load Resource from give path
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileLocation);
    }
}
