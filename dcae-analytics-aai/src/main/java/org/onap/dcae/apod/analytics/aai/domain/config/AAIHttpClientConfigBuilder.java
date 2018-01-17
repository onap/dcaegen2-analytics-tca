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

package org.onap.dcae.apod.analytics.aai.domain.config;

import org.onap.dcae.apod.analytics.common.AnalyticsConstants;

import java.io.Serializable;
import java.net.URL;

/**
 * A&AI Http Client Config Builder used to create immutable instance of {@link AAIHttpClientConfig}
 *
 * @author Rajiv Singla . Creation Date: 9/21/2017.
 */
public class AAIHttpClientConfigBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private String aaiHost;
    private Integer aaiHostPortNumber;
    private String aaiProtocol;
    private String aaiUserName;
    private String aaiUserPassword;
    private URL aaiProxyURL;
    private boolean aaiIgnoreSSLCertificateErrors;

    public AAIHttpClientConfigBuilder(final String aaiHost) {
        this.aaiHost = aaiHost;
        this.aaiHostPortNumber = AnalyticsConstants.DEFAULT_PORT_NUMBER;
        this.aaiProtocol = AnalyticsConstants.DEFAULT_PROTOCOL;
        this.aaiUserName = AnalyticsConstants.DEFAULT_USER_NAME;
        this.aaiUserPassword = AnalyticsConstants.DEFAULT_USER_PASSWORD;
        this.aaiIgnoreSSLCertificateErrors = AnalyticsConstants
                .TCA_DEFAULT_AAI_ENRICHMENT_IGNORE_SSL_CERTIFICATE_ERRORS;
    }

    public AAIHttpClientConfigBuilder setAaiHostPortNumber(final Integer aaiHostPortNumber) {
        this.aaiHostPortNumber = aaiHostPortNumber;
        return this;
    }

    public AAIHttpClientConfigBuilder setAaiProtocol(final String aaiProtocol) {
        this.aaiProtocol = aaiProtocol;
        return this;
    }

    public AAIHttpClientConfigBuilder setAaiUserName(final String aaiUserName) {
        this.aaiUserName = aaiUserName;
        return this;
    }

    public AAIHttpClientConfigBuilder setAaiUserPassword(final String aaiUserPassword) {
        this.aaiUserPassword = aaiUserPassword;
        return this;
    }

    public AAIHttpClientConfigBuilder setAaiProxyURL(final URL aaiProxyURL) {
        this.aaiProxyURL = aaiProxyURL;
        return this;
    }

    public AAIHttpClientConfigBuilder setAaiIgnoreSSLCertificateErrors(final boolean aaiIgnoreSSLCertificateErrors) {
        this.aaiIgnoreSSLCertificateErrors = aaiIgnoreSSLCertificateErrors;
        return this;
    }

    public AAIHttpClientConfig build() {
        return new AAIHttpClientConfig(aaiHost, aaiHostPortNumber, aaiProtocol, aaiUserName, aaiUserPassword,
                aaiProxyURL, aaiIgnoreSSLCertificateErrors);
    }
}
