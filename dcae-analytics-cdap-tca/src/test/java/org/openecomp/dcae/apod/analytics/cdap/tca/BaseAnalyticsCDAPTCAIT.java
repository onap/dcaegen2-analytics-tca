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

package org.openecomp.dcae.apod.analytics.cdap.tca;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Suppliers;
import org.junit.BeforeClass;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCATestAppConfig;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCATestAppPreferences;
import org.openecomp.dcae.apod.analytics.model.util.AnalyticsModelIOUtils;
import org.openecomp.dcae.apod.analytics.model.util.json.AnalyticsModelObjectMapperSupplier;
import org.openecomp.dcae.apod.analytics.test.BaseDCAEAnalyticsIT;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 *
 * @author Rajiv Singla . Creation Date: 10/25/2016.
 */
public abstract class BaseAnalyticsCDAPTCAIT extends BaseDCAEAnalyticsIT {

    protected static ObjectMapper objectMapper;

    @BeforeClass
    public static void beforeClass() {
        final AnalyticsModelObjectMapperSupplier analyticsModelObjectMapperSupplier =
                new AnalyticsModelObjectMapperSupplier();
        objectMapper = Suppliers.memoize(analyticsModelObjectMapperSupplier).get();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    protected static final String TCA_CONTROLLER_POLICY_FILE_LOCATION =
            "data/properties/tca_controller_policy.properties";

    // App Settings
    protected static final String DCAE_ANALYTICS_TCA_TEST_APP_NAME = "dcae-tca";
    protected static final String DCAE_ANALYTICS_TCA_TEST_APP_DESC =
            "DCAE Analytics Threshold Crossing Alert Application";
    

    protected static TCATestAppConfig getTCATestAppConfig() {
        final TCATestAppConfig tcaTestAppConfig = new TCATestAppConfig();
        tcaTestAppConfig.setAppName(DCAE_ANALYTICS_TCA_TEST_APP_NAME);
        tcaTestAppConfig.setAppDescription(DCAE_ANALYTICS_TCA_TEST_APP_DESC);
        return tcaTestAppConfig;
    }

    protected static TCATestAppPreferences getTCATestAppPreferences() {
        final TCATestAppPreferences tcaTestAppPreferences = new TCATestAppPreferences(getTCAPolicyPreferences());
        tcaTestAppPreferences.setSubscriberPollingInterval(null);
        tcaTestAppPreferences.setPublisherMaxBatchSize(null);
        tcaTestAppPreferences.setPublisherMaxRecoveryQueueSize(null);
        tcaTestAppPreferences.setEnableAlertCEFFormat(null);
        tcaTestAppPreferences.setPublisherPollingInterval(null);
        return tcaTestAppPreferences;
    }


    protected static Map<String, String> getTCAPolicyPreferences() {
        final Map<String, String> policyPreferences = new TreeMap<>();
        final Properties policyPreferencesProps =
                AnalyticsModelIOUtils.loadPropertiesFile(TCA_CONTROLLER_POLICY_FILE_LOCATION, new Properties());
        for (Map.Entry<Object, Object> propEntry : policyPreferencesProps.entrySet()) {
            policyPreferences.put(propEntry.getKey().toString(), propEntry.getValue().toString());
        }

        return policyPreferences;
    }

    protected static String serializeModelToJson(Object model) throws JsonProcessingException {
        return objectMapper.writeValueAsString(model);
    }
}
