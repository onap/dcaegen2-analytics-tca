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

package org.openecomp.dcae.apod.analytics.aai.service;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.openecomp.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfig;
import org.openecomp.dcae.apod.analytics.common.utils.HTTPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;


/**
 * A concrete implementation for {@link AAIEnrichmentClient} which uses A&AI REST API to get A&AI Enrichment details
 *
 * @author Rajiv Singla . Creation Date: 9/18/2017.
 */
public class AAIEnrichmentClientImpl implements AAIEnrichmentClient {

    private static final Logger LOG = LoggerFactory.getLogger(AAIEnrichmentClientImpl.class);

    private final CloseableHttpClient closeableHttpClient;
    private final String aaiProtocol;
    private final String aaiHost;
    private final Integer aaiHostPortNumber;

    @Inject
    public AAIEnrichmentClientImpl(@Assisted final AAIHttpClientConfig aaiHttpClientConfig,
                                   final AAIHttpClientFactory aaiHttpClientFactory) {
        final AAIHttpClient aaiHttpClient = aaiHttpClientFactory.create(aaiHttpClientConfig);
        closeableHttpClient = aaiHttpClient.getAAIHttpClient();
        aaiProtocol = aaiHttpClientConfig.getAaiProtocol();
        aaiHost = aaiHttpClientConfig.getAaiHost();
        aaiHostPortNumber = aaiHttpClientConfig.getAaiHostPortNumber();
    }


    /**
     * Provides enrichment details from A&AI API and returns them as string. If no enrichment lookup fails returns null
     *
     * @param aaiAPIPath A&AI API Path
     * @param queryParams A&AI Query Params map
     * @param headers A&AI HTTP Headers
     *
     * @return Enrichment details from A&AI API and returns them as string. If enrichment lookup fails returns null
     */
    @Override
    public String getEnrichmentDetails(final String aaiAPIPath, final Map<String, String> queryParams,
                                       final Map<String, String> headers) {

        final URI enrichmentURI =
                createAAIEnrichmentURI(aaiProtocol, aaiHost, aaiHostPortNumber, aaiAPIPath, queryParams);

        if (enrichmentURI == null) {
            return null;
        }

        // create new get request
        final HttpGet getRequest = new HttpGet(enrichmentURI);
        // add http headers
        for (Map.Entry<String, String> headersEntry : headers.entrySet()) {
            getRequest.addHeader(headersEntry.getKey(), headersEntry.getValue());
        }

        Optional<String> enrichmentDetails = Optional.absent();
        // execute http get request
        try {
            enrichmentDetails = closeableHttpClient.execute(getRequest, aaiResponseHandler());
        } catch (IOException ex) {
            LOG.error("Failed to get A&AI Enrichment Details for A&AI Enrichment URI: {} A&AI Error: {}",
                    enrichmentURI, ex);
        }

        // return response
        if (enrichmentDetails.isPresent()) {
            return enrichmentDetails.get();
        } else {
            return null;
        }
    }

    /**
     * Create A&AI API Enrichment URI. If invalid URI - null will be returned
     *
     * @param protocol A&AI API protocol
     * @param hostName A&AI API hostname
     * @param portNumber A&AI API port number
     * @param path A&AI API path
     * @param queryParams A&AI API query parameters
     *
     * @return A&AI API Enrichment URI
     */
    private URI createAAIEnrichmentURI(final String protocol, final String hostName,
                                       final Integer portNumber, final String path,
                                       Map<String, String> queryParams) {

        final URIBuilder uriBuilder = new URIBuilder().setScheme(protocol).setHost(hostName).setPort(portNumber)
                .setPath(path);

        // creates custom query string which is not encoded
        final String customQuery = createCustomQuery(queryParams);
        if (StringUtils.isNoneBlank(customQuery)) {
            uriBuilder.setCustomQuery(customQuery);
        }

        URI enrichmentURI = null;
        try {
            enrichmentURI = uriBuilder.build();
        } catch (URISyntaxException e) {
            LOG.error("URI Syntax Exception when creating A&AI Enrichment URI. " +
                            "Protocol: {}, HostName: {}, Port: {}, Path: {}, Custom Query String: {}, Exception: {}",
                    protocol, hostName, portNumber, path, customQuery, e);
        }

        LOG.trace("Created A&AI Enrichment URI: {}", enrichmentURI);
        return enrichmentURI;
    }

    /**
     * Creates Custom Query string to be used for A&AI API URI as A&AI currently does not expect encoded
     * query params.
     *
     * @param queryParams query param map
     *
     * @return custom query string which does not encode query param values
     */
    private static String createCustomQuery(@Nonnull final Map<String, String> queryParams) {
        final StringBuilder queryStringBuilder = new StringBuilder("");
        final Iterator<Map.Entry<String, String>> queryParamIterator = queryParams.entrySet().iterator();
        while (queryParamIterator.hasNext()) {
            final Map.Entry<String, String> queryParamsEntry = queryParamIterator.next();
            queryStringBuilder.append(queryParamsEntry.getKey());
            queryStringBuilder.append("=");
            queryStringBuilder.append(queryParamsEntry.getValue());
            if (queryParamIterator.hasNext()) {
                queryStringBuilder.append("&");
            }
        }
        return queryStringBuilder.toString();
    }

    /**
     * Response Handler for A&AI Enrichment API
     *
     * @return Response Handler that
     */
    static ResponseHandler<Optional<String>> aaiResponseHandler() {
        return new ResponseHandler<Optional<String>>() {
            @Override
            public Optional<String> handleResponse(final HttpResponse response) throws IOException {
                final int responseCode = response.getStatusLine().getStatusCode();
                final HttpEntity responseEntity = response.getEntity();
                if (HTTPUtils.isSuccessfulResponseCode(responseCode) && null != responseEntity) {
                    final String aaiResponse = EntityUtils.toString(responseEntity);
                    return Optional.of(aaiResponse);
                } else {
                    String aaiResponse = responseEntity != null ? EntityUtils.toString(responseEntity) : "";
                    LOG.error("Unable to fetch response from A&AI API. A&AI Response Code: {}, " +
                            "A&AI Response Message: {}", responseCode, aaiResponse);
                    return Optional.absent();
                }
            }
        };
    }

}

