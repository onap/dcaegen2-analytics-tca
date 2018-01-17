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

package org.onap.dcae.apod.analytics.cdap.tca;

import co.cask.cdap.api.flow.flowlet.AbstractFlowlet;
import co.cask.cdap.api.flow.flowlet.FlowletContext;
import co.cask.cdap.internal.flow.DefaultFlowletConfigurer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import org.junit.Assert;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAPolicyPreferences;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCATestAppConfig;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCATestAppPreferences;
import org.onap.dcae.apod.analytics.model.domain.cef.EventListener;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.onap.dcae.apod.analytics.model.util.AnalyticsModelIOUtils;
import org.onap.dcae.apod.analytics.model.util.json.AnalyticsModelObjectMapperSupplier;
import org.onap.dcae.apod.analytics.test.BaseDCAEAnalyticsUnitTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 10/25/2016.
 */
public abstract class BaseAnalyticsCDAPTCAUnitTest extends BaseDCAEAnalyticsUnitTest {

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
    protected static final String TCA_APP_CONFIG_FILE_LOCATION = "data/json/config/controller_app_config.json";
    protected static final String TCA_ALERT_JSON_FILE_LOCATION = "data/json/facade/tca_ves_cef_response.json";


    protected static final String TCA_CONTROLLER_POLICY_FILE_LOCATION =
            "data/properties/tca_controller_policy.properties";

    protected static final String TCA_CONTROLLER_POLICY_FROM_JSON_FILE_LOCATION =
            "data/properties/tca_controller_policy_from_json.properties";


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
    protected static TCAPolicy getSampleTCAPolicy() {
        return deserializeJsonFileToModel(TCA_POLICY_JSON_FILE_LOCATION, TCAPolicy.class);
    }

    /**
     * Provides TCA Policy that can be used for testing
     *
     * @return test {@link TCAPolicyPreferences}
     */
    protected static TCAPolicyPreferences getSampleTCAPolicyPreferences() {
        return deserializeJsonFileToModel(TCA_POLICY_JSON_FILE_LOCATION, TCAPolicyPreferences.class);
    }

    /**
     * Provides list containing 350 CEF messages
     *
     * @return CEF Test Message
     *
     * @throws Exception Exception
     */
    protected static List<EventListener> getCEFMessages() throws Exception {
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
     *
     * @throws Exception Exception
     */
    protected static String getValidCEFMessage() throws Exception {
        return fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
    }


    /**
     * Provides single CEF Test Message
     *
     * @return CEF Test Message
     *
     * @throws Exception Exception
     */
    protected static EventListener getCEFEventListener() throws Exception {
        final String cefMessageAsString = fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
        return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(cefMessageAsString, EventListener.class);
    }

    /**
     * Deserialize given Json file location to given model class and returns it back without any validation check
     *
     * @param jsonFileLocation Classpath location of the json file
     * @param modelClass Model Class type
     * @param <T> Json Model Type
     *
     * @return Json model object
     */
    public static <T> T deserializeJsonFileToModel(String jsonFileLocation, Class<T> modelClass) {
        final InputStream jsonFileInputStream =
                BaseDCAEAnalyticsUnitTest.class.getClassLoader().getResourceAsStream(jsonFileLocation);
        Assert.assertNotNull("Json File Location must be valid", jsonFileInputStream);
        try {
            return ANALYTICS_MODEL_OBJECT_MAPPER.readValue(jsonFileInputStream, modelClass);
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

    protected static TCATestAppConfig getTCATestAppConfig() {
        final TCATestAppConfig tcaAppConfig = new TCATestAppConfig();
        tcaAppConfig.setAppName(TCA_TEST_APP_CONFIG_NAME);
        tcaAppConfig.setAppDescription(TCA_TEST_APP_CONFIG_DESCRIPTION);
        tcaAppConfig.setTcaSubscriberOutputStreamName(TCA_TEST_APP_CONFIG_SUBSCRIBER_OUTPUT_STREAM_NAME);
        tcaAppConfig.setTcaVESAlertsTableName(TCA_TEST_APP_CONFIG_VES_ALERT_TABLE_NAME);
        tcaAppConfig.setTcaVESMessageStatusTableName(TCA_TEST_APP_CONFIG_VES_MESSAGE_STATUS_TABLE_NAME);
        return tcaAppConfig;
    }

    /**
     * Provides a test application preference for unit testing
     *
     * @return tca app preferences
     */
    protected static TCATestAppPreferences getTCATestAppPreferences() {
        final TCATestAppPreferences tcaTestAppPreferences = new TCATestAppPreferences();
        tcaTestAppPreferences.setSubscriberHostName("SUBSCRIBER_HOST_NAME");
        tcaTestAppPreferences.setSubscriberHostPortNumber(10000);
        tcaTestAppPreferences.setSubscriberTopicName("SUBSCRIBER_TOPIC_NAME");
        tcaTestAppPreferences.setSubscriberUserName("SUBSCRIBER_USERNAME");
        tcaTestAppPreferences.setSubscriberUserPassword("SUBSCRIBER_PASSWORD");
        tcaTestAppPreferences.setSubscriberProtocol("https");
        tcaTestAppPreferences.setSubscriberContentType("application/json");
        tcaTestAppPreferences.setSubscriberConsumerId("SUBSCRIBER_CONSUMER_ID");
        tcaTestAppPreferences.setSubscriberConsumerGroup("SUBSCRIBER_CONSUMER_GROUP_NAME");
        tcaTestAppPreferences.setSubscriberTimeoutMS(10);
        tcaTestAppPreferences.setSubscriberMessageLimit(100);
        tcaTestAppPreferences.setSubscriberPollingInterval(1000);

        tcaTestAppPreferences.setPublisherHostName("PUBLISHER_HOST_NAME");
        tcaTestAppPreferences.setPublisherHostPort(1234);
        tcaTestAppPreferences.setPublisherTopicName("PUBLISHER_TOPIC_NAME");
        tcaTestAppPreferences.setPublisherUserName("PUBLISHER_USERNAME");
        tcaTestAppPreferences.setPublisherUserPassword("PUBLISHER_PASSWORD");
        tcaTestAppPreferences.setPublisherProtocol("https");
        tcaTestAppPreferences.setPublisherContentType("application/json");
        tcaTestAppPreferences.setPublisherMaxBatchSize(100);
        tcaTestAppPreferences.setPublisherMaxRecoveryQueueSize(100);
        tcaTestAppPreferences.setPublisherPollingInterval(6000);

        tcaTestAppPreferences.setEnableAAIEnrichment(true);
        tcaTestAppPreferences.setAaiEnrichmentHost("AAI_ENRICHMENT_HOST");
        tcaTestAppPreferences.setAaiEnrichmentPortNumber(8443);
        tcaTestAppPreferences.setAaiEnrichmentProtocol("https");
        tcaTestAppPreferences.setAaiEnrichmentUserName("AAI_USERNAME");
        tcaTestAppPreferences.setAaiEnrichmentUserPassword("AAI_USERPASSWORD");
        tcaTestAppPreferences.setAaiEnrichmentIgnoreSSLCertificateErrors(true);
        tcaTestAppPreferences.setAaiVMEnrichmentAPIPath("VM_ENRICHMENT_PATH");
        tcaTestAppPreferences.setAaiVNFEnrichmentAPIPath("VNF_ENRICHMENT_PATH");
        return tcaTestAppPreferences;
    }

    protected static Map<String, String> getPreferenceMap() {
        Map<String, String> preference = new HashMap<>();
        preference.put("subscriberHostName", "mrlocal-mtnjftle01.homer.com");
        preference.put("subscriberHostPort", "3905");
        preference.put("subscriberTopicName", "com.dcae.dmaap.mtnje2.DcaeTestVESPub");
        preference.put("subscriberProtocol", "https");
        preference.put("subscriberUserName", "USER");
        preference.put("subscriberUserPassword", "PASSWORD");
        preference.put("subscriberContentType", "application/json");
        preference.put("subscriberConsumerId", "123");
        preference.put("subscriberConsumerGroup", "testTCAConsumerName-123");
        preference.put("subscriberTimeoutMS", "-1");
        preference.put("subscriberMessageLimit", "-1");
        preference.put("subscriberPollingInterval", "30000");

        preference.put("publisherHostName", "publisherHostName");
        preference.put("publisherHostPort", "3905");
        preference.put("publisherTopicName", "publisherTopicName");
        preference.put("publisherProtocol", "https");
        preference.put("publisherUserName", "publisherUserName");
        preference.put("publisherContentType", "application/json");
        preference.put("publisherMaxBatchSize", "1000");
        preference.put("publisherMaxRecoveryQueueSize", "100");
        preference.put("publisherPollingInterval", "6000");
        return preference;
    }

    protected static <T extends AbstractFlowlet> void assertFlowletNameAndDescription(
            final String expectedName, final String expectedDescription, final T flowlet) {
        final DefaultFlowletConfigurer defaultFlowletConfigurer =
                new DefaultFlowletConfigurer(flowlet);
        flowlet.configure(defaultFlowletConfigurer);

        final String flowletName = getPrivateFiledValue(defaultFlowletConfigurer, "name", String.class);
        final String flowletDescription =
                getPrivateFiledValue(defaultFlowletConfigurer, "description", String.class);

        assertThat("Flowlet name must match with CDAPComponentsConstants",
                flowletName, is(expectedName));

        assertThat("Flowlet description must match with CDAPComponentsConstants",
                flowletDescription, is(expectedDescription));

    }

    protected static FlowletContext getTestFlowletContextWithValidPolicy() {
        return createNewFlowletContextFromPropertiesFile(TCA_CONTROLLER_POLICY_FILE_LOCATION);
    }

    protected static FlowletContext getTestFlowletContextWithValidPolicyFromJSON() {
        return createNewFlowletContextFromPropertiesFile(TCA_CONTROLLER_POLICY_FROM_JSON_FILE_LOCATION);
    }

    private static FlowletContext createNewFlowletContextFromPropertiesFile(final String propertyFileLocation) {
        final Properties controllerProperties =
                AnalyticsModelIOUtils.loadPropertiesFile(propertyFileLocation, new Properties());

        Map<String, String> runtimeArgs = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> property : controllerProperties.entrySet()) {
            runtimeArgs.put(property.getKey().toString(), property.getValue().toString());
        }

        final FlowletContext flowletContext = mock(FlowletContext.class);
        when(flowletContext.getRuntimeArguments()).thenReturn(runtimeArgs);
        return flowletContext;
    }

}
