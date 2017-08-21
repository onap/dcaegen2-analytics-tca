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

package org.openecomp.dcae.apod.analytics.cdap.plugins.streaming.dmaap;

import com.google.common.collect.ImmutableList;
import org.apache.spark.storage.StorageLevel;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.TestDMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 1/24/2017.
 */
public class DMaaPMRReceiverTest extends BaseAnalyticsCDAPPluginsUnitTest {


    @Test
    public void testStoreStructuredRecords() throws Exception {

        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        final TestDMaaPMRReceiver dMaaPMRReceiver =
                new TestDMaaPMRReceiver(StorageLevel.MEMORY_ONLY(), testDMaaPMRSourcePluginConfig);

        final DMaaPMRSubscriber dMaaPMRSubscriber = Mockito.mock(DMaaPMRSubscriber.class);
        final DMaaPMRSubscriberResponse subscriberResponse = Mockito.mock(DMaaPMRSubscriberResponse.class);
        when(dMaaPMRSubscriber.fetchMessages()).thenReturn(subscriberResponse);
        when(subscriberResponse.getFetchedMessages()).thenReturn(ImmutableList.of("Test Message"));
        when(subscriberResponse.getResponseCode()).thenReturn(200);
        when(subscriberResponse.getResponseMessage()).thenReturn("OK");
        dMaaPMRReceiver.storeStructuredRecords(dMaaPMRSubscriber);
        verify(dMaaPMRSubscriber, times(1)).fetchMessages();
        verify(subscriberResponse, times(1)).getFetchedMessages();
    }

    @Test
    public void testStoreStructuredRecordsWhenSubscriberThrowsException() throws Exception {

        final TestDMaaPMRSourcePluginConfig testDMaaPMRSourcePluginConfig = getTestDMaaPMRSourcePluginConfig();
        final TestDMaaPMRReceiver dMaaPMRReceiver =
                new TestDMaaPMRReceiver(StorageLevel.MEMORY_ONLY(), testDMaaPMRSourcePluginConfig);

        final DMaaPMRSubscriber dMaaPMRSubscriber = Mockito.mock(DMaaPMRSubscriber.class);
        final DMaaPMRSubscriberResponse subscriberResponse = Mockito.mock(DMaaPMRSubscriberResponse.class);
        when(dMaaPMRSubscriber.fetchMessages()).thenThrow(DCAEAnalyticsRuntimeException.class);
        dMaaPMRReceiver.storeStructuredRecords(dMaaPMRSubscriber);
        verify(dMaaPMRSubscriber, times(1)).fetchMessages();
        verify(subscriberResponse, times(0)).getFetchedMessages();
    }
}
