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

package org.openecomp.dcae.apod.analytics.cdap.tca.it;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAIT;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCATestAppConfig;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCATestAppConfigHolder;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCATestAppPreferences;

/**
 * @author Rajiv Singla . Creation Date: 10/25/2016.
 */
public class TCAnalyticsAppConfigIT extends BaseAnalyticsCDAPTCAIT {


    @Test
    public void createTestAppConfigJson() throws Exception {
        final TCATestAppConfig tcaTestAppConfig = getTCATestAppConfig();
        final TCATestAppConfigHolder appConfigHolder = new TCATestAppConfigHolder(tcaTestAppConfig);
        final String appConfigJson = serializeModelToJson(appConfigHolder);
        LOG.info("AppConfigJson: \n{}", appConfigJson);
        writeToOutputTextFile("appSettings/tca_app_config.json", appConfigJson, TCAnalyticsAppConfigIT.class);
    }

    @Test
    public void createTestAppPreferencesJson() throws Exception {
        final TCATestAppPreferences tcaTestAppPreferences = getTCATestAppPreferences();
        final String appPreferencesJson = serializeModelToJson(tcaTestAppPreferences);
        LOG.info("AppPreferences: \n{}", appPreferencesJson);
        writeToOutputTextFile("appSettings/tca_app_preferences.json",
                appPreferencesJson, TCAnalyticsAppConfigIT.class);
    }
}
