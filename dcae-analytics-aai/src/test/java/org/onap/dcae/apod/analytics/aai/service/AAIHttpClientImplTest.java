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

import org.apache.http.HttpHost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.DefaultHttpClientConnectionOperator;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.junit.Assert;
import org.junit.Test;
import org.onap.dcae.apod.analytics.aai.BaseAnalyticsAAIUnitTest;
import org.onap.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfig;
import sun.security.ssl.SSLContextImpl;
import sun.security.ssl.SSLSocketFactoryImpl;

import javax.net.ssl.HostnameVerifier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 9/25/2017.
 */
public class AAIHttpClientImplTest extends BaseAnalyticsAAIUnitTest {

    @Test
    public void getAAIHttpClientWithIgnoredSSLErrorsAndProxySettings() throws Exception {

        final AAIHttpClientConfig aaiHttpClientTestConfig =
                getAAIHttpClientTestConfig(true, PROXY_URL);
        final AAIHttpClientImpl aaiHttpClient = new AAIHttpClientImpl(aaiHttpClientTestConfig);
        final CloseableHttpClient closeableHttpClient = aaiHttpClient.getAAIHttpClient();

        final HostnameVerifier hostnameVerifier = getHostNameVerifier(closeableHttpClient);

        assertTrue("HostName Verifier must be NoOpHostnameVerifier",
                NoopHostnameVerifier.INSTANCE.getClass().equals(hostnameVerifier.getClass()));

        final DefaultProxyRoutePlanner routePlanner = getPrivateFiledValue(closeableHttpClient, "routePlanner",
                DefaultProxyRoutePlanner.class);

        final HttpHost proxyHost = getPrivateFiledValue(routePlanner, "proxy", HttpHost.class);

        final String hostName = proxyHost.getHostName();
        final int port = proxyHost.getPort();

        assertThat("Proxy Host name must match", hostName, is(PROXY_HOST));
        assertThat("Proxy Port number must match", port, is(PROXY_PORT));

    }

    @Test
    public void getAAIHttpClientWithNoIgnoredSSLErrorsAndProxySettings() throws Exception {

        final AAIHttpClientConfig aaiHttpClientTestConfig =
                getAAIHttpClientTestConfig(false, null);
        final AAIHttpClientImpl aaiHttpClient = new AAIHttpClientImpl(aaiHttpClientTestConfig);
        final CloseableHttpClient closeableHttpClient = aaiHttpClient.getAAIHttpClient();
        final HostnameVerifier hostnameVerifier = getHostNameVerifier(closeableHttpClient);

        assertTrue("HostName Verifier must be DefaultHostNameVerifier",
                hostnameVerifier instanceof DefaultHostnameVerifier);

        getPrivateFiledValue(closeableHttpClient, "routePlanner",
                SystemDefaultRoutePlanner.class);
    }


    private HostnameVerifier getHostNameVerifier(final CloseableHttpClient closeableHttpClient) {
        final PoolingHttpClientConnectionManager connManager =
                getPrivateFiledValue(closeableHttpClient, "connManager",
                        PoolingHttpClientConnectionManager.class);
        final DefaultHttpClientConnectionOperator connectionOperator = getPrivateFiledValue(connManager,
                "connectionOperator", DefaultHttpClientConnectionOperator.class);
        final Registry socketFactoryRegistry = getPrivateFiledValue(connectionOperator, "socketFactoryRegistry",
                Registry.class);
        final SSLConnectionSocketFactory sslConnectionSocketFactory = (SSLConnectionSocketFactory)
                socketFactoryRegistry.lookup("https");
        final HostnameVerifier hostnameVerifier = getPrivateFiledValue(sslConnectionSocketFactory,
                "hostnameVerifier", HostnameVerifier.class);
        return hostnameVerifier;
    }
}
