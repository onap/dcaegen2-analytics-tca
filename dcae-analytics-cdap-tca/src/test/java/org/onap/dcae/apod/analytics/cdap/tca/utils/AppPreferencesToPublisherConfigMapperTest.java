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
import org.onap.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Manjesh Gowda. Creation Date: 11/21/2016.
 */
public class AppPreferencesToPublisherConfigMapperTest extends BaseAnalyticsCDAPTCAUnitTest {
    @Test
    public void testMapTCAConfigToPublisherConfigFunctionGood() {
        DMaaPMRPublisherConfig dMaaPMRPublisherConfig =
                (new AppPreferencesToPublisherConfigMapper()).apply(getTCATestAppPreferences());
        assertEquals(dMaaPMRPublisherConfig.getHostName(), "PUBLISHER_HOST_NAME");
    }

    @Test
    public void testMapTCAConfigToPublisherConfigFunctionMap() {
        DMaaPMRPublisherConfig dMaaPMRPublisherConfig = AppPreferencesToPublisherConfigMapper.map(
                getTCATestAppPreferences());
        assertEquals(dMaaPMRPublisherConfig.getHostName(), "PUBLISHER_HOST_NAME");
    }

    @Test
    public void testMapTCAConfigToPublisherConfigFunction() {
        final TCATestAppPreferences tcaAppPreferences = new TCATestAppPreferences();
        final String publisherHostName = "publisherHostName";
        final String publisherTopicName = "publisherTopicName";
        tcaAppPreferences.setPublisherHostName(publisherHostName);
        tcaAppPreferences.setPublisherTopicName(publisherTopicName);
        DMaaPMRPublisherConfig dMaaPMRPublisherConfig =
                (new AppPreferencesToPublisherConfigMapper()).apply(tcaAppPreferences);
        assertTrue(publisherHostName.equals(dMaaPMRPublisherConfig.getHostName()));
        assertTrue(publisherTopicName.equals(dMaaPMRPublisherConfig.getTopicName()));

    }
}
