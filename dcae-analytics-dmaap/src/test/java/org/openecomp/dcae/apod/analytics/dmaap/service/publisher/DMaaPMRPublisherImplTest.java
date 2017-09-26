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

package org.openecomp.dcae.apod.analytics.dmaap.service.publisher;

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
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Rajiv Singla . Creation Date: 10/21/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class DMaaPMRPublisherImplTest extends BaseAnalyticsDMaaPUnitTest {

    @Mock
    private DMaaPMRPublisherQueueFactory dmaapMRPublisherQueueFactory;
    @Mock
    private CloseableHttpClient closeableHttpClient;
    @Mock
    private DMaaPMRPublisherQueue dmaapMRPublisherQueue;

    @Before
    public void setUp() throws Exception {
        given(dmaapMRPublisherQueueFactory.create(Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(dmaapMRPublisherQueue);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPublishSmallMessageList() throws Exception {
        given(dmaapMRPublisherQueue.getBatchQueueRemainingSize()).willReturn(10);
        given(dmaapMRPublisherQueue.addBatchMessages(Mockito.<String>anyList())).willReturn(2);

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                getPublisherConfig(), dmaapMRPublisherQueueFactory, closeableHttpClient);

        DMaaPMRPublisherResponse dmaapMRPublisherResponse = dmaapMRPublisherImpl.publish(getTwoSampleMessages());

        assertThat(dmaapMRPublisherResponse.getResponseCode(), is(202));
        assertThat(dmaapMRPublisherResponse.getPendingMessagesCount(), is(2));
        assertThat(dmaapMRPublisherResponse.getResponseMessage(),
                is("Accepted - Messages queued for batch publishing to MR Topic"));
    }

    @Test
    public void testPublishBigMessageList() throws Exception {

        given(dmaapMRPublisherQueue.getBatchQueueRemainingSize()).willReturn(0);
        given(dmaapMRPublisherQueue.getMessageForPublishing()).willReturn(getTwoSampleMessages());
        Mockito.when(
                closeableHttpClient.execute(Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(200, "Message successfully posted"));

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                getPublisherConfig(), dmaapMRPublisherQueueFactory, closeableHttpClient);

        DMaaPMRPublisherResponse dmaapMRPublisherResponse = dmaapMRPublisherImpl.publish(getTwoSampleMessages());

        assertThat(dmaapMRPublisherResponse.getResponseCode(), is(200));
        assertThat(dmaapMRPublisherResponse.getPendingMessagesCount(), is(200));
        assertThat(dmaapMRPublisherResponse.getResponseMessage(), is("Message successfully posted"));
    }

    @Test
    public void testForcePublishSuccessful() throws Exception {
        DMaaPMRPublisherConfig dmaapMRPublisherConfig = new
                DMaaPMRPublisherConfig.Builder(HOST_NAME, TOPIC_NAME)
                .setPortNumber(PORT_NUMBER)
                .setProtocol(HTTP_PROTOCOL)
                .setContentType(CONTENT_TYPE)
                .setMaxRecoveryQueueSize(PUBLISHER_MAX_RECOVERY_QUEUE_SIZE)
                .setMaxBatchSize(PUBLISHER_MAX_BATCH_QUEUE_SIZE).build();

        Mockito.when(closeableHttpClient.execute(
                Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(200, "Message successfully posted"));

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                dmaapMRPublisherConfig, dmaapMRPublisherQueueFactory, closeableHttpClient);
        DMaaPMRPublisherResponse response = dmaapMRPublisherImpl.forcePublish(getTwoSampleMessages());
        assertThat(response.getResponseCode(), is(200));
    }

    @Test
    public void testForcePublishFailure() throws Exception {
        Mockito.when(closeableHttpClient.execute(
                Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(503, "Message successfully posted"));

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                getPublisherConfig(), dmaapMRPublisherQueueFactory, closeableHttpClient);
        DMaaPMRPublisherResponse response = dmaapMRPublisherImpl.forcePublish(getTwoSampleMessages());
        assertThat(response.getResponseCode(), is(503));
    }

    @Rule
    public ExpectedException httpIOException = ExpectedException.none();

    @Test
    public void testForcePublishHttpFailure() throws Exception {

        httpIOException.expect(DCAEAnalyticsRuntimeException.class);
        httpIOException.expectCause(isA(IOException.class));

        given(closeableHttpClient.execute(
                Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class))).willThrow(IOException.class);

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                getPublisherConfig(), dmaapMRPublisherQueueFactory, closeableHttpClient);
        dmaapMRPublisherImpl.forcePublish(getTwoSampleMessages());
    }

    @Test
    public void testFlushSuccessful() throws Exception {
        Mockito.when(closeableHttpClient.execute(
                Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(200, "Message successfully posted"));

        Mockito.when(dmaapMRPublisherQueue.getMessageForPublishing()).thenReturn(getTwoSampleMessages());

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                getPublisherConfig(), dmaapMRPublisherQueueFactory, closeableHttpClient);
        DMaaPMRPublisherResponse response = dmaapMRPublisherImpl.flush();
        assertThat(response.getResponseCode(), is(200));
    }

    @Test
    public void testFlushEmptyList() throws Exception {
        Mockito.when(dmaapMRPublisherQueue.getMessageForPublishing()).thenReturn(new ArrayList<String>());

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                getPublisherConfig(), dmaapMRPublisherQueueFactory, closeableHttpClient);
        DMaaPMRPublisherResponse response = dmaapMRPublisherImpl.flush();
        assertThat(response.getResponseCode(), is(204));
    }

    @Test
    public void testClose() throws Exception {
        Mockito.when(dmaapMRPublisherQueue.getMessageForPublishing()).thenReturn(new ArrayList<String>());
        Mockito.when(closeableHttpClient.execute(
                Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(200, "Message successfully posted"));
        Mockito.when(dmaapMRPublisherQueue.getMessageForPublishing()).thenReturn(getTwoSampleMessages());

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                getPublisherConfig(), dmaapMRPublisherQueueFactory, closeableHttpClient);
        dmaapMRPublisherImpl.close();
        verify(closeableHttpClient).execute(Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class));
    }

    @Test
    public void testCloseUnsuccessful() throws Exception {
        Mockito.when(dmaapMRPublisherQueue.getMessageForPublishing()).thenReturn(new ArrayList<String>());
        Mockito.when(closeableHttpClient.execute(
                Mockito.any(HttpUriRequest.class), Mockito.any(ResponseHandler.class)))
                .thenReturn(new ImmutablePair<>(400, "Message successfully posted"));
        Mockito.when(dmaapMRPublisherQueue.getMessageForPublishing()).thenReturn(getTwoSampleMessages());

        DMaaPMRPublisherImpl dmaapMRPublisherImpl = new DMaaPMRPublisherImpl(
                getPublisherConfig(), dmaapMRPublisherQueueFactory, closeableHttpClient);
        dmaapMRPublisherImpl.close();
        verify(closeableHttpClient, times(6)).execute(Mockito.any(HttpUriRequest.class),
                Mockito.any(ResponseHandler.class));
    }
}
