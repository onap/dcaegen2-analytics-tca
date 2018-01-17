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

package org.onap.dcae.apod.analytics.cdap.tca.utils;

import co.cask.cdap.api.RuntimeContext;
import co.cask.cdap.api.app.ApplicationSpecification;
import org.junit.Test;
import org.mockito.Mockito;
import org.onap.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public class CDAPTCAUtilsTest extends BaseAnalyticsCDAPTCAUnitTest {

    @Test
    public void testGetValidatedTCAAppPreferences() throws Exception {
        RuntimeContext runtimeContext = mock(RuntimeContext.class);
        final Map<String, String> preferenceMap = getPreferenceMap();
        preferenceMap.remove("subscriberHostName");
        preferenceMap.remove("publisherHostName");
        when(runtimeContext.getRuntimeArguments()).thenReturn(preferenceMap);
        ApplicationSpecification mockApplicationSpecification = Mockito.mock(ApplicationSpecification.class);
        when(mockApplicationSpecification.getConfiguration()).thenReturn(fromStream(TCA_APP_CONFIG_FILE_LOCATION));
        when(runtimeContext.getApplicationSpecification()).thenReturn(mockApplicationSpecification);
        TCAAppPreferences validatedTCAAppPreferences = CDAPTCAUtils.getValidatedTCAAppPreferences(runtimeContext);
        assertEquals(validatedTCAAppPreferences.getSubscriberHostName(), "HOSTNAME");
    }

    @Test
    public void testConvertRuntimeContextToTCAPolicy() throws Exception {

        final TCAPolicy tcaPolicy =
                CDAPTCAUtils.getValidatedTCAPolicyPreferences(getTestFlowletContextWithValidPolicy());
        assertThat("Policy Domain must be measurementsForVfScaling",
                tcaPolicy.getDomain(), is("measurementsForVfScaling"));
        assertThat("Policy must have 2 metrics per functional roles",
                tcaPolicy.getMetricsPerEventName().size(), is(2));
    }

    @Test
    public void testConvertRuntimeContextToTCAPolicyFromJSON() throws Exception {

        final TCAPolicy tcaPolicy =
                CDAPTCAUtils.getValidatedTCAPolicyPreferences(getTestFlowletContextWithValidPolicyFromJSON());
        assertThat("Policy Domain must be measurementsForVfScaling",
                tcaPolicy.getDomain(), is("measurementsForVfScaling"));
        assertThat("Policy must have 2 metrics per functional roles",
                tcaPolicy.getMetricsPerEventName().size(), is(2));
    }

}
