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

package org.onap.dcae.apod.analytics.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import org.junit.BeforeClass;
import org.onap.dcae.apod.analytics.common.service.processor.TestEarlyTerminatingProcessor;
import org.onap.dcae.apod.analytics.common.service.processor.TestMessageProcessor1;
import org.onap.dcae.apod.analytics.common.service.processor.TestMessageProcessor2;
import org.onap.dcae.apod.analytics.model.util.json.AnalyticsModelObjectMapperSupplier;
import org.onap.dcae.apod.analytics.test.BaseDCAEAnalyticsUnitTest;

/**
 * Base class from all DCEA Analytics Common Module Unit Tests
 * <p>
 * @author Rajiv Singla . Creation Date: 10/6/2016.
 */
public abstract class BaseAnalyticsCommonUnitTest extends BaseDCAEAnalyticsUnitTest {


    protected static final String TEST_MESSAGE_PROCESSOR_MESSAGE = "Test Processor Message";

    protected static final String CEF_MESSAGE_FILE_PATH = "data/json/cef/cef_message.json";

    protected static ObjectMapper objectMapper;

    /**
     * Before running test cases need to assign object mapper.
     */
    @BeforeClass
    public static void beforeClass() {
        final AnalyticsModelObjectMapperSupplier analyticsModelObjectMapperSupplier =
                new AnalyticsModelObjectMapperSupplier();
        objectMapper = Suppliers.memoize(analyticsModelObjectMapperSupplier).get();
    }

    /*
     * Test implementation for {@link CDAPAppSettings}
     */
    protected class CDAPTestAppSettings {

        private String settingsField;

        public String getSettingsField() {
            return settingsField;
        }

        public void setSettingsField(String settingsField) {
            this.settingsField = settingsField;
        }
    }

    protected TestMessageProcessor1 getTestMessageProcessor1() {
         return new TestMessageProcessor1();
    }
    protected TestMessageProcessor2 getTestMessageProcessor2() {
        return new TestMessageProcessor2();
    }
    protected TestEarlyTerminatingProcessor getTestEarlyTerminationProcessor() {
        return new TestEarlyTerminatingProcessor();
    }
}
