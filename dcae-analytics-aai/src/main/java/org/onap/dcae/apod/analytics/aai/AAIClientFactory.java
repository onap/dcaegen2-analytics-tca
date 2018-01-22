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

package org.onap.dcae.apod.analytics.aai;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.onap.dcae.apod.analytics.aai.domain.config.AAIHttpClientConfig;
import org.onap.dcae.apod.analytics.aai.module.AnalyticsAAIModule;
import org.onap.dcae.apod.analytics.aai.service.AAIEnrichmentClient;
import org.onap.dcae.apod.analytics.aai.service.AAIEnrichmentClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create A&AI API Client.
 *
 * @author Rajiv Singla . Creation Date: 9/18/2017.
 */
public class AAIClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AAIClientFactory.class);

    private final Injector injector;

    public AAIClientFactory(final AbstractModule guiceModule) {
        LOG.info("Creating instance of AAI Client Factory with Module: {}", guiceModule.getClass().getSimpleName());
        this.injector = Guice.createInjector(guiceModule);
    }

    /**
     * Creates an instance of {@link AAIEnrichmentClient}.
     *
     * @param aaiHttpClientConfig A&AI Http Client Config
     *
     * @return An instance of A&AI Enrichment Client to fetch enrichment details from A&AI API.
     */
    public AAIEnrichmentClient getEnrichmentClient(final AAIHttpClientConfig aaiHttpClientConfig) {
        LOG.info("Creating instance of A&AI Enrichment Client with A&AI HttpClientConfig: {}", aaiHttpClientConfig);
        final AAIEnrichmentClientFactory aaiEnrichmentClientFactory =
                injector.getInstance(AAIEnrichmentClientFactory.class);
        return aaiEnrichmentClientFactory.create(aaiHttpClientConfig);
    }


    /**
     * Static method used to create an instance of {@link AAIClientFactory} itself using default
     * guice {@link AnalyticsAAIModule}
     *
     * @return An instance of AAI Client Factory with {@link AnalyticsAAIModule} guice module configuration
     */
    public static AAIClientFactory create() {
        return new AAIClientFactory(new AnalyticsAAIModule());
    }

}
