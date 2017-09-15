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

package org.openecomp.dcae.apod.analytics.dmaap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.BaseAnalyticsDMaaPUnitTest;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisherQueue;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Manjesh Gowda. Creation Date: 11/4/2016.
 */
public class BaseDMaaPMRComponentTest extends BaseAnalyticsDMaaPUnitTest {

    @Test
    public void testGetAuthHeaderWithGoodValues() {
        String expectedEncodedString = "Basic bTAwNTAyQHRjYS5hZi5kY2FlLmNvbTpUZTUwMjFhYmM=";
        Optional<String> actualOutput = BaseDMaaPMRComponent.getAuthHeader("USER", "PASSWORD");
        assertTrue(" Authentication Header has value ", actualOutput.isPresent());
//        assertEquals(" Authentication Header has value ", expectedEncodedString, actualOutput.get());
    }

    @Test
    public void testGetAuthHeaderWithNullValues() {
        Optional<String> actualOutput = BaseDMaaPMRComponent.getAuthHeader(null, null);
        assertFalse(" Authentication Header has value ", actualOutput.isPresent());
    }

    @Test
    public void testGetAuthHeaderWithUserNullValue() {
        Optional<String> actualOutput = BaseDMaaPMRComponent.getAuthHeader("USER", null);
        assertFalse(" Authentication Header has value ", actualOutput.isPresent());
    }

    @Test
    public void testGetAuthHeaderWithPasswordNullValue() {
        Optional<String> actualOutput = BaseDMaaPMRComponent.getAuthHeader(null, "PASSWORD");
        assertFalse(" Authentication Header has value ", actualOutput.isPresent());
    }

    @Test
    public void testCreatePublishURIWithGoodValues() {
        URI actualURI = BaseDMaaPMRComponent.createPublisherURI(getPublisherConfig());
        String test = actualURI.toString();
        assertEquals("Generated Publisher URL is correct",
                "https://testHostName:8080/events/testTopicName", actualURI.toString());
    }

    @Test(expected = DCAEAnalyticsRuntimeException.class)
    public void testCreatePublishURIWithURISyntaxException() {
        DMaaPMRPublisherConfig badPublisherConfig = new DMaaPMRPublisherConfig
                .Builder(" dav /gh. ss/ asd ", "///@$%#-htps:<>!##")
                .setPortNumber(0)
                .setProtocol("https").build();

      BaseDMaaPMRComponent.createPublisherURI(badPublisherConfig);
    }

    @Test
    public void testCreateSubscribeURIWithGoodValues() {
        URI actualURI = BaseDMaaPMRComponent.createSubscriberURI(
                getSubscriberConfig("test-consumer-group", "test-consumer-id"));
        assertEquals("Generated Subscriber URL is correct",
                "https://testHostName:8080/events/testTopicName/" +
                        "test-consumer-id/test-consumer-group?timeout=2000&limit=20",
                actualURI.toString());
    }

    @Test(expected = DCAEAnalyticsRuntimeException.class)
    public void testCreateSubscribeURIWithURISyntaxException() {
        DMaaPMRSubscriberConfig badSubscriberConfig = new DMaaPMRSubscriberConfig
                .Builder(" dav /gh. ss/ asd ", "")
                .setPortNumber(PORT_NUMBER)
                .setProtocol(HTTP_PROTOCOL)
                .setContentType(CONTENT_TYPE).build();

        URI actualURI = BaseDMaaPMRComponent.createSubscriberURI(badSubscriberConfig);
    }

    @Test
    public void testConvertToJsonStringGoodJsonStringList() {
        List<String> jsonMessage = Arrays.asList(
                "{\"message\":\"I'm Object 1 Message\"}",
                "{\"message\":\"I'm Object 2 Message\"}");

        String actualJSONMsg = BaseDMaaPMRComponent.convertToJsonString(jsonMessage);

        String expectedJSONMsg = "[{\"message\":\"I'm Object 1 Message\"}," +
                "{\"message\":\"I'm Object 2 Message\"}]";
        assertEquals("Convert a List of Strings to JSON is working fine", expectedJSONMsg, actualJSONMsg);

    }

    @Rule
    public ExpectedException expectedJsonProcessingException = ExpectedException.none();

    @Test
    public void testConvertToJsonStringBadJsonStringList() {
        expectedJsonProcessingException.expect(DCAEAnalyticsRuntimeException.class);
        expectedJsonProcessingException.expectCause(isA(JsonProcessingException.class));

        List<String> jsonMessage = Arrays.asList(
                "{\"message\":\"I'm Object 1 Message\"",
                "\"message\":\"I'm Object 2 Message\"");

        BaseDMaaPMRComponent.convertToJsonString(jsonMessage);
    }

    @Test
    public void testConvertToJsonStringWithEmptyList() {
        List<String> jsonMessage = Arrays.asList();
        String actualJSONMsg = BaseDMaaPMRComponent.convertToJsonString(jsonMessage);
        String expectedJSONMsg = "[]";
        assertEquals("Convert a List of Strings to JSON is working fine", expectedJSONMsg, actualJSONMsg);
    }

    @Test
    public void testConvertToJsonStringWithNullList() {
        String actualJSONMsg = BaseDMaaPMRComponent.convertToJsonString(null);
        String expectedJSONMsg = "[]";
        assertEquals("Convert a List of Strings to JSON is working fine", expectedJSONMsg, actualJSONMsg);
    }


    @Test
    public void testConvertJsonToStringMessagesGoodValues() {
        String inputJSONMsg = "[{\"message\":\"I'm Object 1 Message\"}," +
                "{\"message\":\"I'm Object 2 Message\"}]";
        List<String> actualList = BaseDMaaPMRComponent.convertJsonToStringMessages(inputJSONMsg);
        assertThat(actualList, hasSize(2));
        assertThat(actualList, containsInAnyOrder(
                "{\"message\":\"I'm Object 1 Message\"}",
                "{\"message\":\"I'm Object 2 Message\"}"
        ));
    }

    @Test
    public void testConvertJsonToStringMessagesNoValues() {
        String inputJSONMsg = "[]";
        List<String> actualList = BaseDMaaPMRComponent.convertJsonToStringMessages(inputJSONMsg);
        assertThat(actualList, hasSize(0));
    }

    @Test
    public void testConvertJsonToStringMessagesNullValues() {
        List<String> actualList = BaseDMaaPMRComponent.convertJsonToStringMessages(null);
        assertThat(actualList, hasSize(0));
    }

    @Test
    public void testConvertJsonToStringMessagesEmptyValues() {
        List<String> actualList = BaseDMaaPMRComponent.convertJsonToStringMessages("  ");
        assertThat(actualList, hasSize(0));
    }

    @Rule
    public ExpectedException convertToJSONIOException = ExpectedException.none();

    @Test
    public void testConvertJsonToStringMessagesException() {
        convertToJSONIOException.expect(DCAEAnalyticsRuntimeException.class);
        convertToJSONIOException.expectCause(isA(IOException.class));

        String inputJSONMsg = "[\"{\"message\":\"I'm Object 1 Message\"}\"," +
                "\"{\"message\":\"I'm Object 2 Message\"}\"]";
        List<String> actualList = BaseDMaaPMRComponent.convertJsonToStringMessages(inputJSONMsg);
        assertThat(actualList, hasSize(2));
        assertThat(actualList, containsInAnyOrder(
                "{\"message\":\"I'm Object 1 Message\"}",
                "{\"message\":\"I'm Object 2 Message\"}"
        ));
    }

    @Test
    public void testAddMessagesToRecoveryQueueAllGood() {
        DMaaPMRPublisherQueue dmaapMRPublisherQueue = mock(DMaaPMRPublisherQueue.class);
        given(dmaapMRPublisherQueue.addRecoverableMessages(Mockito.<String>anyList())).willReturn(0);
        given(dmaapMRPublisherQueue.getBatchQueueRemainingSize()).willReturn(0);
        List<String> messages = new ArrayList<String>();
        BaseDMaaPMRComponent.addMessagesToRecoveryQueue(dmaapMRPublisherQueue, messages);
    }

    @Rule
    public ExpectedException addQueueIllegalException = ExpectedException.none();

    @Test
    public void testAddMessagesToRecoveryQueueException() {
        addQueueIllegalException.expect(isA(DCAEAnalyticsRuntimeException.class));
        addQueueIllegalException.expectCause(isA(IllegalStateException.class));

        DMaaPMRPublisherQueue dmaapMRPublisherQueue = mock(DMaaPMRPublisherQueue.class);

        given(dmaapMRPublisherQueue.addRecoverableMessages(Mockito.<String>anyList()))
                .willThrow(IllegalStateException.class);
        List<String> messages = new ArrayList<String>();

        BaseDMaaPMRComponent.addMessagesToRecoveryQueue(dmaapMRPublisherQueue, messages);
    }


    @Test
    public void testResponseHandler() {
        HttpResponse mockHttpResponse = mock(HttpResponse.class);
        StatusLine mockStatusLine = mock(StatusLine.class);
        HttpEntity mockHttpEntity = mock(HttpEntity.class);
        // Could not mock EntityUtils as it's final class
        //EntityUtils mockEntityUtils = mock(EntityUtils.class);

        given(mockHttpResponse.getStatusLine()).willReturn(mockStatusLine);
        given(mockStatusLine.getStatusCode()).willReturn(200);
        given(mockHttpResponse.getEntity()).willReturn(null);
        //given(mockEntityUtils.toString()).willReturn("Test value");

        ResponseHandler<Pair<Integer, String>> responseHandler = BaseDMaaPMRComponent.responseHandler();
        try {
            Pair<Integer, String> mappedResponse = responseHandler.handleResponse(mockHttpResponse);
            assertTrue("Http response code returned properly ", mappedResponse.getLeft().equals(200));
            assertTrue("Http response body returned properly ", mappedResponse.getRight().equals(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateSubscriberResponse() {
        DMaaPMRSubscriberResponse dmaapMRSubscriberResponse =
                BaseDMaaPMRComponent.createSubscriberResponse(200, "Test Message", getTwoSampleMessages());

        assertThat(dmaapMRSubscriberResponse.getResponseCode(), is(200));
        assertEquals(dmaapMRSubscriberResponse.getResponseMessage(), "Test Message");
        assertThat(dmaapMRSubscriberResponse.getFetchedMessages().size(), is(2));

    }

    @Test
    public void testCreateSubscriberResponse_no_message() {
        DMaaPMRSubscriberResponse dmaapMRSubscriberResponse =
                BaseDMaaPMRComponent.createSubscriberResponse(200, "Test Message", null);

        assertThat(dmaapMRSubscriberResponse.getResponseCode(), is(200));
        assertEquals(dmaapMRSubscriberResponse.getResponseMessage(), "Test Message");
        assertThat(dmaapMRSubscriberResponse.getFetchedMessages().size(), is(0));

    }

}





