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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.ssl.SSLContextBuilder;
import org.openecomp.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfig;
import org.openecomp.dcae.apod.analytics.aai.utils.ssl.AlwaysTrustingTrustStrategy;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * A concrete implementation of {@link AAIHttpClient} which provides Apache {@link CloseableHttpClient} for
 * making rest calls to A&AI Enrichment API.
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 9/19/2017.
 */
public class AAIHttpClientImpl implements AAIHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(AAIHttpClientImpl.class);

    private final AAIHttpClientConfig aaiHttpClientConfig;

    @Inject
    public AAIHttpClientImpl(@Assisted final AAIHttpClientConfig aaiHttpClientConfig) {
        this.aaiHttpClientConfig = aaiHttpClientConfig;
    }

    /**
     * Provides an instance of {@link CloseableHttpClient} used to make REST calls to A&AI Enrichment API
     *
     * @return An instance of Closeable HTTP Client used to make A&AI API Rest calls
     */
    @Override
    public CloseableHttpClient getAAIHttpClient() {

        final HttpClientBuilder httpClientBuilder = HttpClients.custom().useSystemProperties();
        final boolean aaiIgnoreSSLCertificateErrors = aaiHttpClientConfig.isAaiIgnoreSSLCertificateErrors();

        // Setup SSL Context to ignore SSL certificate issues if ignoreSSLCertificateErrors is true
        LOG.info("SSL Certificate Errors attributed is set to : {}", aaiIgnoreSSLCertificateErrors);

        if (aaiIgnoreSSLCertificateErrors) {
            LOG.warn("SSL Certificate Errors will be ignored for this A&AI Http Client Instance");
            try {
                SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
                sslContextBuilder.loadTrustMaterial(null, new AlwaysTrustingTrustStrategy());
                httpClientBuilder.setSSLContext(sslContextBuilder.build());
            } catch (NoSuchAlgorithmException e) {
                final String errorMessage = "NoSuchAlgorithmException while setting SSL Context for AAI HTTP Client.";
                throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
            } catch (KeyStoreException e) {
                final String errorMessage = "KeyStoreException while setting SSL Context for AAI HTTP Client.";
                throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
            } catch (KeyManagementException e) {
                final String errorMessage = "KeyManagementException while setting SSL Context for AAI HTTP Client.";
                throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
            }

            httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);

        } else {
            LOG.info("SSL Certification Errors will be enforced for A&AI Http Client instance");
        }

        // Setup credentials and proxy
        final String aaiUserName = aaiHttpClientConfig.getAaiUserName();

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        if (aaiUserName != null) {
            final String aaiHost = aaiHttpClientConfig.getAaiHost();
            final Integer aaiHostPortNumber = aaiHttpClientConfig.getAaiHostPortNumber();
            final String aaiUserPassword = aaiHttpClientConfig.getAaiUserPassword();
            LOG.info("Setting A&AI host credentials for AAI Host: {}", aaiHost);
            final AuthScope aaiHostPortAuthScope = new AuthScope(aaiHost, aaiHostPortNumber);
            final Credentials aaiCredentials = new UsernamePasswordCredentials(aaiUserName, aaiUserPassword);
            credentialsProvider.setCredentials(aaiHostPortAuthScope, aaiCredentials);
        } else {
            LOG.warn("A&AI userName not present. No credentials set for A&AI authentication");
        }

        final URL aaiProxyURL = aaiHttpClientConfig.getAaiProxyURL();

        if (aaiProxyURL != null) {
            final String aaiProxyHost = aaiProxyURL.getHost();
            final Integer aaiProxyPortNumber = aaiProxyURL.getPort();
            final String aaiProxyProtocol = aaiProxyURL.getProtocol();
            final HttpHost proxy = new HttpHost(aaiProxyHost, aaiProxyPortNumber, aaiProxyProtocol);
            LOG.info("Setting A&AI Http Client default proxy as: {}", proxy);
            final DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            httpClientBuilder.setRoutePlanner(routePlanner);

            final String userInfo = aaiProxyURL.getUserInfo();
            if (StringUtils.isNotBlank(userInfo)) {
                final String[] userInfoArray = userInfo.split(":");
                final String aaiProxyUsername = userInfoArray[0];
                String aaiProxyPassword = null;
                if (userInfoArray.length > 1) {
                    aaiProxyPassword = userInfoArray[1];
                }
                LOG.info("Setting A&AI Http Client proxy credentials with username: {}", aaiProxyUsername);
                final AuthScope aaiProxyAuthScope = new AuthScope(aaiProxyHost, aaiProxyPortNumber);
                final Credentials aaiProxyCredentials = new UsernamePasswordCredentials(aaiProxyUsername,
                        aaiProxyPassword);
                credentialsProvider.setCredentials(aaiProxyAuthScope, aaiProxyCredentials);
            } else {
                LOG.debug("NO A&AI Proxy Username present.Bypassing setting up A&AI Proxy authentication credentials");
            }
        } else {
            LOG.debug("A&AI proxy not Enabled - bypassing setting A&AI Proxy settings");
        }

        // setup credentials provider
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        return httpClientBuilder.build();
    }

}
