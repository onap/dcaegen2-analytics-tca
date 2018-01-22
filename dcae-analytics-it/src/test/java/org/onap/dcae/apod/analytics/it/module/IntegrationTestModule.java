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

package org.onap.dcae.apod.analytics.it.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.onap.dcae.apod.analytics.it.dmaap.DMaaPMRCreator;
import org.onap.dcae.apod.analytics.it.dmaap.DMaaPMRCreatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author Rajiv Singla . Creation Date: 2/1/2017.
 */
public class IntegrationTestModule implements Module {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationTestModule.class);

    public static final String ANALYTICS_SYSTEM_VARIABLE_KEY_NAME = "analytics.it.env";
    public static final String DEFAULT_ENVIRONMENT = "dev";
    public static final String ENVIRONMENT_PROPERTIES_FILE_LOCATION = "env";

    @Override
    public void configure(Binder binder) {
        final Properties envProperties = loadPropertiesFile();
        Names.bindProperties(binder, envProperties);
        binder.bind(DMaaPMRCreator.class).to(DMaaPMRCreatorImpl.class);
    }


    /**
     * Load environment specific properties file
     *
     * @return environment properties
     */
    private Properties loadPropertiesFile() {
        final String currentEnvironment = getCurrentEnvironment().toLowerCase();
        final String envPropertiesFileName = currentEnvironment + ".properties";
        final Properties envProperties = new Properties();
        final String fileLocation = ENVIRONMENT_PROPERTIES_FILE_LOCATION + "/" + envPropertiesFileName;
        LOG.info("===>>> EFFECTIVE ENV: {}, EFFECTIVE PROPERTIES FILE: {} <<<====", currentEnvironment, fileLocation);
        try {
            final InputStream propertiesFileInputStream =
                    IntegrationTestModule.class.getClassLoader().getResourceAsStream(fileLocation);
            envProperties.load(propertiesFileInputStream);
        } catch (FileNotFoundException e) {
            final String errorMessage = String.format("Unable to find env properties file: %s.", fileLocation);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        } catch (IOException e) {
            final String errorMessage = String.format("I/O Exception during loading env properties file: %s",
                    fileLocation);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }

        final Properties systemProperties = System.getProperties();
        for (Object envProperty : envProperties.keySet()) {
            final String systemPropertyValue = systemProperties.getProperty(envProperty.toString());
            if (systemPropertyValue !=  null) {
                LOG.info("Overriding System property name: {} with env property value: {}",
                        envProperty.toString(), systemPropertyValue);
                envProperties.setProperty(envProperty.toString(), systemPropertyValue);
            }
        }

        LOG.info("Printing Effective Environment Properties =============== >>>");
        for (Map.Entry<Object, Object> envPropertyEntry : envProperties.entrySet()) {
            LOG.info("{}={}", envPropertyEntry.getKey(), envPropertyEntry.getValue());
        }

        return envProperties;
    }


    private static String getCurrentEnvironment() {
        // First look in environment variables
        LOG.info("Looking for IT variable name: {} in Environment variables", ANALYTICS_SYSTEM_VARIABLE_KEY_NAME);
        final String itEnvironmentVariable = System.getenv(ANALYTICS_SYSTEM_VARIABLE_KEY_NAME);
        if (itEnvironmentVariable != null) {
            LOG.info("Found value in Environment variables: {} for IT Environment variable", itEnvironmentVariable);
            return itEnvironmentVariable;
        } else {
            LOG.info("Unable to find IT variable name: {} in Environment variable", ANALYTICS_SYSTEM_VARIABLE_KEY_NAME);
        }

        // Second look inside system properties
        LOG.info("Looking for IT variable name: {} in System variables", ANALYTICS_SYSTEM_VARIABLE_KEY_NAME);

        final String itSystemProperty = System.getProperty(ANALYTICS_SYSTEM_VARIABLE_KEY_NAME);
        if (itSystemProperty != null) {
            LOG.info("Found value for System variables: {} in System variable", itSystemProperty);
            return itSystemProperty;
        } else {
            LOG.info("Unable to find IT variable name: {} in System variable", ANALYTICS_SYSTEM_VARIABLE_KEY_NAME);
        }

        // return default enviroment
        LOG.warn("Unable to find IT environment variable. Choosing default environment: {}", DEFAULT_ENVIRONMENT);
        return DEFAULT_ENVIRONMENT;
    }
}
