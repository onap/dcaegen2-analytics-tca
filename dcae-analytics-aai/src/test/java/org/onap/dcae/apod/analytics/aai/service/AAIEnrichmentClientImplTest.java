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

package org.onap.dcae.apod.analytics.aai.service;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.Test;
import org.onap.dcae.apod.analytics.aai.BaseAnalyticsAAIUnitTest;
import org.onap.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfig;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 9/25/2017.
 */
public class AAIEnrichmentClientImplTest extends BaseAnalyticsAAIUnitTest {


    @Test
    public void testGetEnrichmentDetails() throws Exception {

        final String enrichmentResponseJson = "{}";

        final String vnfName = "vCPEInfraVNF13";
        final AAIHttpClientConfig aaiHttpClientTestConfig = getAAIHttpClientTestConfig(true, PROXY_URL);
        Map<String, String> queryParams = ImmutableMap.of("vnf-name", vnfName);
        final AAIHttpClientFactory aaiHttpClientFactory = mock(AAIHttpClientFactory.class);
        final CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class);
        final AAIHttpClient aaiHttpClient = mock(AAIHttpClient.class);
        when(aaiHttpClientFactory.create(any(AAIHttpClientConfig.class))).thenReturn(aaiHttpClient);
        when(aaiHttpClient.getAAIHttpClient()).thenReturn(closeableHttpClient);
        when(closeableHttpClient.execute(any(HttpGet.class), any(ResponseHandler.class)))
                .thenReturn(Optional.of(enrichmentResponseJson));
        final AAIEnrichmentClientImpl aaiEnrichmentClient = new AAIEnrichmentClientImpl(
                aaiHttpClientTestConfig, aaiHttpClientFactory);

        final String enrichmentDetails = aaiEnrichmentClient.getEnrichmentDetails(AAI_VNF_ENRICHMENT_PATH,
                queryParams, AAI_HEADERS);
        assertThat("Enrichment response is same", enrichmentDetails, is(enrichmentResponseJson));

    }

    @Test
    public void testAAiResponseHandler() throws Exception {
        final ResponseHandler<Optional<String>> aaiResponseHandler =
                AAIEnrichmentClientImpl.aaiResponseHandler();
        final HttpResponse httpResponse = mock(HttpResponse.class);
        final HttpEntity httpEntity = mock(HttpEntity.class);
        final String response = "{}";
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        when(httpEntity.getContent()).thenReturn(inputStream);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        final StatusLine statusLine = mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        final Optional<String> result = aaiResponseHandler.handleResponse(httpResponse);
        assertThat("Response must match", result.get(), is(response));
    }

}
