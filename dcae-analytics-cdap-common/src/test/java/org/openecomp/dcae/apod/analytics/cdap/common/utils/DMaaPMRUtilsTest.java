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

package org.openecomp.dcae.apod.analytics.cdap.common.utils;

import co.cask.cdap.api.metrics.Metrics;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.BaseAnalyticsCDAPCommonUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPMetricsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 2/6/2017.
 */
public class DMaaPMRUtilsTest extends BaseAnalyticsCDAPCommonUnitTest {

    private DMaaPMRSubscriber subscriber;
    private Metrics metrics;


    @Before
    public void before() throws Exception {
        metrics = mock(Metrics.class);
        doNothing().when(metrics).count(anyString(), anyInt());
        subscriber = mock(DMaaPMRSubscriber.class);
    }

    @Test
    public void testGetSubscriberMessagesWhenMessagesAreFound() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(200);
        when(subscriberResponse.getResponseMessage()).thenReturn("testMessage");
        when(subscriberResponse.getFetchedMessages()).thenReturn(ImmutableList.of("testMessage1", "testMessage1"));
        when(subscriber.fetchMessages()).thenReturn(subscriberResponse);
        DMaaPMRUtils.getSubscriberMessages(subscriber, metrics);
        verify(metrics, Mockito.times(1)).count(ArgumentMatchers.eq(CDAPMetricsConstants
                .DMAAP_MR_SUBSCRIBER_TOTAL_MESSAGES_PROCESSED_METRIC), eq(2));
    }

    @Test
    public void testSubscriberMessagesWhenSubscriberResponseCodeIsNull() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(null);
        when(subscriber.fetchMessages()).thenReturn(subscriberResponse);
        DMaaPMRUtils.getSubscriberMessages(subscriber, metrics);
    }

    @Test
    public void testSubscriberMessagesWhenNoMessagesFound() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(200);
        when(subscriberResponse.getResponseMessage()).thenReturn("no messages");
        when(subscriberResponse.getFetchedMessages()).thenReturn(Collections.<String>emptyList());
        when(subscriber.fetchMessages()).thenReturn(subscriberResponse);
        DMaaPMRUtils.getSubscriberMessages(subscriber, metrics);
        verify(metrics, Mockito.times(1)).count(eq(CDAPMetricsConstants
                .DMAAP_MR_SUBSCRIBER_RESPONSES_WITH_NO_MESSAGES_METRIC), eq(1));
    }


    @Test
    public void testWhenSubscriberReturnNonSuccessfulReturnCode() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(500);
        when(subscriber.fetchMessages()).thenReturn(subscriberResponse);
        DMaaPMRUtils.getSubscriberMessages(subscriber, metrics);
        verify(metrics, Mockito.times(1)).count(eq(CDAPMetricsConstants
                .DMAAP_MR_SUBSCRIBER_UNSUCCESSFUL_RESPONSES_METRIC), eq(1));
    }

    @Test
    public void testWhenSubscriberThrowsException() throws Exception {
        final DMaaPMRSubscriberResponse subscriberResponse = mock(DMaaPMRSubscriberResponse.class);
        when(subscriberResponse.getResponseCode()).thenReturn(500);
        when(subscriber.fetchMessages()).thenThrow(DCAEAnalyticsRuntimeException.class);
        DMaaPMRUtils.getSubscriberMessages(subscriber, metrics);
    }

}
