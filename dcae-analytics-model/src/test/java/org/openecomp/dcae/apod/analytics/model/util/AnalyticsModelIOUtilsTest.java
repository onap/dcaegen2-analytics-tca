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


import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

/**
 * @author Rajiv Singla . Creation Date: 10/17/2016.
 */
public class AnalyticsModelIOUtilsTest extends BaseAnalyticsModelUnitTest {

    private static final String TEST_CONFIG_FILE_LOCATION = "data/json/config/testAppConfig.json";
    private static final String INVALID_TEST_CONFIG_FILE_LOCATION = "data/json/config/invalidJsonConfig.json";
    private static final String TEST_PROPERTIES_FILE_LOCATION = "data/testApp.properties";

    @Test
    public void testConvertToJsonObjectWhenFileLocationIsValid() throws Exception {
        ConfigHolder configHolder =
                AnalyticsModelIOUtils.convertToJsonObject(TEST_CONFIG_FILE_LOCATION, ConfigHolder.class);
        String appName = configHolder.getConfig().getAppName();
        assertEquals("App Name must match with json settings file value", "TestAppName", appName);
        String appDescription = configHolder.getConfig().getAppDescription();
        assertEquals("App Description much with json settings file value", "Test App Description", appDescription);
    }

    @Test(expected = RuntimeException.class)
    public void testConvertToJsonObjectWhenFileLocationIsInvValid() throws Exception {
        AnalyticsModelIOUtils.convertToJsonObject("InvalidFileLocation", ConfigHolder.class);
    }

    @Test(expected = RuntimeException.class)
    public void testConvertToJsonObjectWhenJsonFileHasInvalidJson() throws Exception {
        AnalyticsModelIOUtils.convertToJsonObject(INVALID_TEST_CONFIG_FILE_LOCATION, ConfigHolder.class);
    }


    @Test
    public void testValidPropertiesFileLoading() throws Exception {
        final Properties properties =
                AnalyticsModelIOUtils.loadPropertiesFile(TEST_PROPERTIES_FILE_LOCATION, new Properties());
        assertThat("Properties File must contain 2 properties", properties.size(), is(2));
    }

    @Test(expected = RuntimeException.class)
    public void testNonExistingPropertiesFileLoading() throws Exception {
        AnalyticsModelIOUtils.loadPropertiesFile("InvalidPropertiesFileLocation", new Properties());
    }

    @Test(expected = RuntimeException.class)
    public void testLoadPropertiesFileWhenIOException() throws Exception {
        final Properties mockProperties = Mockito.mock(Properties.class);
        doThrow(new IOException()).when(mockProperties).load(any(InputStream.class));
        AnalyticsModelIOUtils.loadPropertiesFile(TEST_PROPERTIES_FILE_LOCATION, mockProperties);

    }
    
}

