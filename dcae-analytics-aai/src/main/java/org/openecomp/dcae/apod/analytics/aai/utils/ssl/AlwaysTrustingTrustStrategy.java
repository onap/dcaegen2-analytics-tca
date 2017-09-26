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

package org.openecomp.dcae.apod.analytics.aai.utils.ssl;

import org.apache.http.ssl.TrustStrategy;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * An implementation of SSL Trust Strategy which does no SSL certificate validation effectively
 * bypassing any SSL certificate related issues
 *
 * @author Rajiv Singla . Creation Date: 9/19/2017.
 */
public class AlwaysTrustingTrustStrategy implements TrustStrategy {
    /**
     * Determines whether the certificate chain can be trusted without consulting the trust manager
     * configured in the actual SSL context. This method can be used to override the standard JSSE
     * certificate verification process.
     * <p>
     * Please note that, if this method returns {@code false}, the trust manager configured
     * in the actual SSL context can still clear the certificate as trusted.
     *
     * @param chain the peer certificate chain
     * @param authType the authentication type based on the client certificate
     *
     * @return {@code true} if the certificate can be trusted without verification by
     * the trust manager, {@code false} otherwise.
     *
     * @throws CertificateException thrown if the certificate is not trusted or invalid.
     */
    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        return true;
    }
}
