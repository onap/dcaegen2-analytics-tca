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

package org.openecomp.dcae.apod.analytics.tca;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ControlLoopEventStatus;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Direction;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.openecomp.dcae.apod.analytics.model.util.AnalyticsModelIOUtils;
import org.openecomp.dcae.apod.analytics.model.util.json.AnalyticsModelObjectMapperSupplier;
import org.openecomp.dcae.apod.analytics.test.BaseDCAEAnalyticsUnitTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Rajiv Singla . Creation Date: 10/25/2016.
 */
public abstract class BaseAnalyticsTCAUnitTest extends BaseDCAEAnalyticsUnitTest {

    /**
     * Object mapper to be used for all TCA Json Parsing
     */
    protected static final ObjectMapper ANALYTICS_MODEL_OBJECT_MAPPER =
            Suppliers.memoize(new AnalyticsModelObjectMapperSupplier()).get();

    protected static final String TCA_POLICY_JSON_FILE_LOCATION = "data/json/policy/tca_policy.json";
    protected static final String CEF_MESSAGES_JSON_FILE_LOCATION = "data/json/cef/cef_messages.json";
    protected static final String CEF_MESSAGE_JSON_FILE_LOCATION = "data/json/cef/cef_message.json";
    protected static final String CEF_MESSAGE_WITH_THRESHOLD_VIOLATION_JSON_FILE_LOCATION =
            "data/json/cef/cef_message_with_threshold_violation.json";

    protected static final String TCA_CONTROLLER_POLICY_FILE_LOCATION =
            "data/properties/tca_controller_policy.properties";

    protected static final String TCA_TEST_APP_CONFIG_NAME = "testTCAAppName";
    protected static final String TCA_TEST_APP_CONFIG_DESCRIPTION = "testTCAAppDescription";
    protected static final String TCA_TEST_APP_CONFIG_SUBSCRIBER_OUTPUT_STREAM_NAME =
            "testTcaSubscriberOutputStreamName";
    protected static final String TCA_TEST_APP_CONFIG_VES_ALERT_TABLE_NAME = "testTcaVESAlertsTableName";
    protected static final String TCA_TEST_APP_CONFIG_VES_MESSAGE_STATUS_TABLE_NAME =
            "testTcaVESMessageStatusTableName";


    /**
     * Provides TCA Policy that can be used for testing
     *
     * @return test TCA Policy Object
     */
    protected TCAPolicy getSampleTCAPolicy() {
        try {
            return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(fromStream(TCA_POLICY_JSON_FILE_LOCATION), TCAPolicy.class);
        } catch (IOException e) {
            LOG.error("Error while parsing policy: {}", e);
            throw new RuntimeException("Error while parsing policy", e);
        }
    }

    /**
     * Provides list containing 350 CEF messages
     *
     * @return CEF Test Message
     * @throws Exception Exception
     */
    protected List<EventListener> getCEFMessages() throws Exception {
        final String cefMessageAsString = fromStream(CEF_MESSAGES_JSON_FILE_LOCATION);
        final TypeReference<List<EventListener>> eventListenerListTypeReference =
                new TypeReference<List<EventListener>>() {
                };
        return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(cefMessageAsString, eventListenerListTypeReference);
    }

    /**
     * Provides 1 valid CEF messages which does not violate Threshold as String
     *
     * @return CEF Test Message String
     * @throws Exception Exception
     */
    protected String getValidCEFMessage() throws Exception {
        return fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
    }


    /**
     * Provides single CEF Test Message
     *
     * @return CEF Test Message
     * @throws Exception Exception
     */
    protected EventListener getCEFEventListener() throws Exception {
        final String cefMessageAsString = fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
        return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(cefMessageAsString, EventListener.class);
    }

    protected static List<Threshold> getThresholds() {
        Threshold majorThreshold = new Threshold();
        majorThreshold.setClosedLoopControlName("CL-LBAL-LOW-TRAFFIC-SIG-FB480F95-A453-6F24-B767-FD703241AB1A");
        majorThreshold.setFieldPath("$.event.measurementsForVfScalingFields.vNicUsageArray[*].packetsIn");
        majorThreshold.setVersion("Test Version");
        majorThreshold.setThresholdValue(500L);
        majorThreshold.setClosedLoopEventStatus(ControlLoopEventStatus.ONSET);
        majorThreshold.setDirection(Direction.LESS_OR_EQUAL);

        Threshold criticalThreshold = new Threshold();
        criticalThreshold.setClosedLoopControlName("CL-LBAL-LOW-TRAFFIC-SIG-FB480F95-A453-6F24-B767-FD703241AB1A");
        criticalThreshold.setThresholdValue(5000L);
        criticalThreshold.setFieldPath("$.event.measurementsForVfScalingFields.vNicUsageArray[*].packetsIn");
        criticalThreshold.setClosedLoopEventStatus(ControlLoopEventStatus.ONSET);
        criticalThreshold.setDirection(Direction.GREATER_OR_EQUAL);
        return Arrays.asList(majorThreshold, criticalThreshold);
    }

    protected static Threshold getCriticalThreshold() {
        Threshold criticalThreshold = new Threshold();
        criticalThreshold.setClosedLoopControlName("CL-LBAL-LOW-TRAFFIC-SIG-FB480F95-A453-6F24-B767-FD703241AB1A");
        criticalThreshold.setThresholdValue(5000L);
        criticalThreshold.setFieldPath("$.event.measurementsForVfScalingFields.vNicUsageArray[*].packetsIn");
        criticalThreshold.setDirection(Direction.GREATER_OR_EQUAL);
        return criticalThreshold;
    }

    protected static Map<String, String> getControllerRuntimeArguments() {
        final Properties controllerProperties =
                AnalyticsModelIOUtils.loadPropertiesFile(TCA_CONTROLLER_POLICY_FILE_LOCATION, new Properties());

        final Map<String, String> runtimeArgs = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> property : controllerProperties.entrySet()) {
            runtimeArgs.put(property.getKey().toString(), property.getValue().toString());
        }

        return runtimeArgs;
    }

}
