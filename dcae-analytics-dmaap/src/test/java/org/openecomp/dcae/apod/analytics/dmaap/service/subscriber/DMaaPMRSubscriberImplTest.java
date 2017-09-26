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

package org.openecomp.dcae.apod.analytics.dmaap.service.subscriber;

import com.jayway.jsonassert.impl.matcher.IsCollectionWithSize;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.BaseAnalyticsDMaaPUnitTest;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;

/**
 * @author Rajiv Singla . Creation Date: 10/21/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class DMaaPMRSubscriberImplTest extends BaseAnalyticsDMaaPUnitTest {

    @Mock
    private CloseableHttpClient closeableHttpClient;

    private String consumerGroup, consumerId;

    @Before
    public void setUp() throws Exception {
        Random random = new Random(10000L);
        consumerGroup = "Test-Consumer-Group" + Long.toString(random.nextLong());
        consumerId = UUID.randomUUID().toString();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSubscriberSuccessfullyReceiveDmaapMessage() throws Exception {

        String testMessages = "[{\"message\":\"I'm Object 1 Message\"}," +
                "{\"message\":\"I'm Object 2 Message\"}]";
        Mockito.when(
                closeableHttpClient.execute(Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(200, testMessages));

        DMaaPMRSubscriberImpl dmaapMRSubscriberImpl = new DMaaPMRSubscriberImpl(
                getSubscriberConfig(consumerId, consumerGroup), closeableHttpClient);
        DMaaPMRSubscriberResponse dmaapMRSubscriberResponse = dmaapMRSubscriberImpl.fetchMessages();
        assertThat(dmaapMRSubscriberResponse.getResponseCode(), is(200));
        assertThat(dmaapMRSubscriberResponse.getFetchedMessages(), IsCollectionWithSize.hasSize(2));
    }

    @Test
    public void testSubscriberSuccessfullyReceiveDmaapMessageWithNoUsername() throws Exception {

        DMaaPMRSubscriberConfig dmaapMRSubscriberConfig = new DMaaPMRSubscriberConfig.Builder(HOST_NAME, TOPIC_NAME)
                .setPortNumber(PORT_NUMBER)
                .setProtocol(HTTP_PROTOCOL)
                .setContentType(CONTENT_TYPE)
                .setConsumerGroup(consumerGroup != null ? consumerGroup : SUBSCRIBER_CONSUMER_GROUP_NAME)
                .setConsumerId(consumerId != null ? consumerId : SUBSCRIBER_CONSUMER_ID)
                .setTimeoutMS(SUBSCRIBER_TIMEOUT_MS)
                .setMessageLimit(SUBSCRIBER_MESSAGE_LIMIT).build();

        String testMessages = "[{\"message\":\"I'm Object 1 Message\"}," +
                "{\"message\":\"I'm Object 2 Message\"}]";
        Mockito.when(
                closeableHttpClient.execute(Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(200, testMessages));

        DMaaPMRSubscriberImpl dmaapMRSubscriberImpl = new DMaaPMRSubscriberImpl(
                dmaapMRSubscriberConfig, closeableHttpClient);
        DMaaPMRSubscriberResponse dmaapMRSubscriberResponse = dmaapMRSubscriberImpl.fetchMessages();
        assertThat(dmaapMRSubscriberResponse.getResponseCode(), is(200));
        assertThat(dmaapMRSubscriberResponse.getFetchedMessages(), IsCollectionWithSize.hasSize(2));
    }

    @Test
    public void testSubscriberSuccessfullyReceiveNoDmaapMessage() throws Exception {
        Mockito.when(
                closeableHttpClient.execute(Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(200, null));

        DMaaPMRSubscriberImpl dmaapMRSubscriberImpl = new DMaaPMRSubscriberImpl(
                getSubscriberConfig(consumerId, consumerGroup), closeableHttpClient);
        DMaaPMRSubscriberResponse dmaapMRSubscriberResponse = dmaapMRSubscriberImpl.fetchMessages();
        assertThat(dmaapMRSubscriberResponse.getResponseCode(), is(200));
        assertThat(dmaapMRSubscriberResponse.getFetchedMessages(), IsCollectionWithSize.hasSize(0));
    }

    @Test
    public void testSubscriberSuccessfullyReceiveErrorMessage() throws Exception {
        Mockito.when(
                closeableHttpClient.execute(Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(400, "Bad Request"));

        DMaaPMRSubscriberImpl dmaapMRSubscriberImpl = new DMaaPMRSubscriberImpl(
                getSubscriberConfig(consumerId, consumerGroup), closeableHttpClient);
        DMaaPMRSubscriberResponse dmaapMRSubscriberResponse = dmaapMRSubscriberImpl.fetchMessages();
        assertThat(dmaapMRSubscriberResponse.getResponseCode(), is(400));
        assertThat(dmaapMRSubscriberResponse.getFetchedMessages(), IsCollectionWithSize.hasSize(0));
    }

    @Rule
    public ExpectedException httpIOException = ExpectedException.none();

    @Test
    public void testSubscriberSuccessfullyReceiveException() throws Exception {

        httpIOException.expect(DCAEAnalyticsRuntimeException.class);
        httpIOException.expectCause(isA(IOException.class));

        given(closeableHttpClient.execute(
                Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class))).willThrow(IOException.class);

        DMaaPMRSubscriberImpl dmaapMRSubscriberImpl = new DMaaPMRSubscriberImpl(
                getSubscriberConfig(consumerId, consumerGroup), closeableHttpClient);
        dmaapMRSubscriberImpl.fetchMessages();
    }

}
