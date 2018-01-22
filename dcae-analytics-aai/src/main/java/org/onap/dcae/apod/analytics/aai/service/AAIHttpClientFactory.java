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

import org.onap.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfig;

/**
 * Factory to initialize instance of {@link AAIHttpClient} for Guice DI injection purposes.
 *
 * @author Rajiv Singla . Creation Date: 9/22/2017.
 */
public interface AAIHttpClientFactory {

    /**
     * Provides an instance of A&AI HTTP Client
     *
     * @param aaiHttpClientConfig A&AI HTTP Client Config
     *
     * @return An instance of A&AI HTTP Client
     */
    AAIHttpClient create(AAIHttpClientConfig aaiHttpClientConfig);

}
