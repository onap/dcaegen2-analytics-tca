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

package org.openecomp.dcae.apod.analytics.aai.domain.config;

import com.google.common.base.Objects;

import java.net.URL;

/**
 * Contains parameters required to create an instance of A&AI Http Client
 *
 * @author Rajiv Singla . Creation Date: 9/21/2017.
 */
public class AAIHttpClientConfig implements AAIEnrichmentConfig {

    private static final long serialVersionUID = 1L;

    private final String aaiHost;
    private final Integer aaiHostPortNumber;
    private final String aaiProtocol;
    private final String aaiUserName;
    private final String aaiUserPassword;
    private final URL aaiProxyURL;
    private final boolean aaiIgnoreSSLCertificateErrors;
    
    AAIHttpClientConfig(final String aaiHost, final Integer aaiHostPortNumber, final String aaiProtocol,
                        final String aaiUserName, final String aaiUserPassword, final URL aaiProxyURL,
                        final boolean aaiIgnoreSSLCertificateErrors) {
        this.aaiHost = aaiHost;
        this.aaiHostPortNumber = aaiHostPortNumber;
        this.aaiProtocol = aaiProtocol;
        this.aaiUserName = aaiUserName;
        this.aaiUserPassword = aaiUserPassword;
        this.aaiProxyURL = aaiProxyURL;
        this.aaiIgnoreSSLCertificateErrors = aaiIgnoreSSLCertificateErrors;
    }

    /**
     * Provides A&AI Http Client Host
     *
     * @return A&AI Http Client Host
     */
    public String getAaiHost() {
        return aaiHost;
    }

    /**
     * Provides A&AI Http Client Host Port Number
     *
     * @return A&AI Http Client Host Port Number
     */
    public Integer getAaiHostPortNumber() {
        return aaiHostPortNumber;
    }

    /**
     * Provides A&AI Http Client Protocol
     *
     * @return A&AI Http Client Protocol
     */
    public String getAaiProtocol() {
        return aaiProtocol;
    }

    /**
     * Provides A&AI Http Client UserName
     *
     * @return A&AI Http Client UserName
     */
    public String getAaiUserName() {
        return aaiUserName;
    }

    /**
     * Provides A&AI Http Client UserPassword
     *
     * @return A&AI Http Client UserPassword
     */
    public String getAaiUserPassword() {
        return aaiUserPassword;
    }

    /**
     * Returns A&AI Proxy url
     *
     * @return A&AI Proxy url
     */
    public URL getAaiProxyURL() {
        return aaiProxyURL;
    }

    /**
     * Returns true if SSL Certificate errors can be ignored for A&AI Http client
     *
     * @return true if SSL Certificate errors can be ignored for A&AI Http client
     */
    public boolean isAaiIgnoreSSLCertificateErrors() {
        return aaiIgnoreSSLCertificateErrors;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("aaiHost", aaiHost)
                .add("aaiHostPortNumber", aaiHostPortNumber)
                .add("aaiProtocol", aaiProtocol)
                .add("aaiUserName", aaiUserName)
                .add("aaiProxyHost", aaiProxyURL == null ? null : aaiProxyURL.getHost())
                .add("aaiIgnoreSSLCertificateErrors", aaiIgnoreSSLCertificateErrors)
                .toString();
    }
}
