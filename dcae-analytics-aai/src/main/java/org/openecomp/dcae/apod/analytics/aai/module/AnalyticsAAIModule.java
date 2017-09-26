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

package org.openecomp.dcae.apod.analytics.aai.module;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.openecomp.dcae.apod.analytics.aai.service.AAIEnrichmentClient;
import org.openecomp.dcae.apod.analytics.aai.service.AAIEnrichmentClientFactory;
import org.openecomp.dcae.apod.analytics.aai.service.AAIEnrichmentClientImpl;
import org.openecomp.dcae.apod.analytics.aai.service.AAIHttpClient;
import org.openecomp.dcae.apod.analytics.aai.service.AAIHttpClientFactory;
import org.openecomp.dcae.apod.analytics.aai.service.AAIHttpClientImpl;

/**
 * <p>
 * Guice Module to bind concrete implementation of interfaces used in Analytics A&AI API
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 9/18/2017.
 */
public class AnalyticsAAIModule extends AbstractModule {

    /**
     * Configures A&AI API guice modules
     */
    @Override
    protected void configure() {

        install(new FactoryModuleBuilder().implement(AAIHttpClient.class, AAIHttpClientImpl.class)
                .build(AAIHttpClientFactory.class));

        install(new FactoryModuleBuilder().implement(AAIEnrichmentClient.class, AAIEnrichmentClientImpl.class)
                .build(AAIEnrichmentClientFactory.class));

    }
}
