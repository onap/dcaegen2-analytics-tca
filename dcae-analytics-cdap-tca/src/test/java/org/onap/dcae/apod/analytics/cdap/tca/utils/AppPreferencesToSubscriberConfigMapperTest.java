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

import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCATestAppPreferences;
import org.onap.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Manjesh Gowda. Creation Date: 11/21/2016.
 */
public class AppPreferencesToSubscriberConfigMapperTest extends BaseAnalyticsCDAPTCAUnitTest {

    @Test
    public void testMapTCAConfigToSubscriberConfigFunctionGood() {
        DMaaPMRSubscriberConfig dMaaPMRSubscriberConfig =
                (new AppPreferencesToSubscriberConfigMapper()).apply(getTCATestAppPreferences());
        assertEquals(dMaaPMRSubscriberConfig.getHostName(), "SUBSCRIBER_HOST_NAME");
    }

    @Test
    public void testMapTCAConfigToSubscriberConfigFunctionMap() {
        DMaaPMRSubscriberConfig dMaaPMRSubscriberConfig =
                AppPreferencesToSubscriberConfigMapper.map(getTCATestAppPreferences());
        assertEquals(dMaaPMRSubscriberConfig.getHostName(), "SUBSCRIBER_HOST_NAME");
    }

    @Test
    public void testMapTCAConfigToSubscriberConfigFunction() {
        final TCATestAppPreferences tcaAppPreferences = new TCATestAppPreferences();
        final String subscriberHostname = "subscriberHostname";
        tcaAppPreferences.setSubscriberHostName(subscriberHostname);
        final String subscriberTopicName = "subscriberTopicName";
        tcaAppPreferences.setSubscriberTopicName(subscriberTopicName);
        DMaaPMRSubscriberConfig dMaaPMRSubscriberConfig =
                (new AppPreferencesToSubscriberConfigMapper()).apply(tcaAppPreferences);
        assertTrue(subscriberHostname.equals(dMaaPMRSubscriberConfig.getHostName()));
        assertTrue(subscriberTopicName.equals(dMaaPMRSubscriberConfig.getTopicName()));
    }
}
