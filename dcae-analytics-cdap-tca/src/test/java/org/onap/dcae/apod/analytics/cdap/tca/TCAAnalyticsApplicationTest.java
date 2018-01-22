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

import co.cask.cdap.app.DefaultApplicationContext;
import co.cask.cdap.app.MockAppConfigurer;
import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAAppConfig;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 1/12/2017.
 */
public class TCAAnalyticsApplicationTest extends BaseAnalyticsCDAPTCAUnitTest {

    @Test
    public void testConfigure() throws Exception {
        final TCAAnalyticsApplication tcaAnalyticsApplication = new TCAAnalyticsApplication();
        MockAppConfigurer mockAppConfigurer = new MockAppConfigurer(tcaAnalyticsApplication);
        final DefaultApplicationContext<TCAAppConfig> applicationContext =
                new DefaultApplicationContext<TCAAppConfig>(getTCATestAppConfig());
        tcaAnalyticsApplication.configure(mockAppConfigurer, applicationContext);
        assertThat(TCA_TEST_APP_CONFIG_NAME, is(mockAppConfigurer.getName()));
    }


}
