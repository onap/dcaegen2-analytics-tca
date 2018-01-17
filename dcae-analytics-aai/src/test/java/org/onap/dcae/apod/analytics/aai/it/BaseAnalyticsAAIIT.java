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

package org.onap.dcae.apod.analytics.aai.it;

import org.onap.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfig;
import org.onap.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfigBuilder;
import org.onap.dcae.apod.analytics.test.BaseDCAEAnalyticsIT;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Rajiv Singla . Creation Date: 9/18/2017.
 */
public abstract class BaseAnalyticsAAIIT extends BaseDCAEAnalyticsIT {

    protected static final String AAI_HOST_NAME = "1.2.3.4";
    protected static final Integer AAI_HOST_PORT_NUMBER = 1234;
    protected static final String AAI_HOST_PROTOCOL = "https";
    protected static final String AAI_VNF_ENRICHMENT_PATH = "/aai/v11/network/generic-vnfs/generic-vnf";
    protected static final String AAI_VSERVER_NODE_QUERY_PATH = "/aai/v11/search/nodes-query";
    protected static final Map<String, String> AAI_HEADERS = new LinkedHashMap<>();


    protected static final String AAI_VSERVER_QUERY_RESPONSE_LOCATION = "data/json/aai/aai_vserver_resource_data.json";

    static {
        AAI_HEADERS.put("X-FromAppId", "vv-temp");
        AAI_HEADERS.put("X-TransactionId", "vv-temp");
        AAI_HEADERS.put("Accept", "application/json");
        AAI_HEADERS.put("Real-Time", "true");
        AAI_HEADERS.put("Content-Type", "application/json");
    }

    protected static final String AAI_USER_NAME = "DCAE";
    protected static final String AAI_USER_PASSWORD = "DCAE";
    protected static final boolean IGNORE_SSL_CERTIFICATE_ERRORS = true;


    protected AAIHttpClientConfig getAAIHttpClientConfig() {
        try {
            URL proxyURL = new URL("http://username:password@proxy.att.com:80");
            return new AAIHttpClientConfigBuilder(AAI_HOST_NAME)
                    .setAaiProtocol(AAI_HOST_PROTOCOL)
                    .setAaiHostPortNumber(AAI_HOST_PORT_NUMBER)
                    .setAaiUserName(AAI_USER_NAME)
                    .setAaiUserPassword(AAI_USER_PASSWORD)
                    .setAaiProxyURL(proxyURL)
                    .setAaiIgnoreSSLCertificateErrors(IGNORE_SSL_CERTIFICATE_ERRORS).build();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
