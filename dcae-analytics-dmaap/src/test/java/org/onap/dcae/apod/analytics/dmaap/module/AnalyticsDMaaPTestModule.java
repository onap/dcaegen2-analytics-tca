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

package org.onap.dcae.apod.analytics.dmaap.module;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisherFactory;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisherMockImpl;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisherQueue;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisherQueueFactory;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisherQueueImpl;
import org.onap.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.onap.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriberFactory;
import org.onap.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriberMockImpl;

/**
 * DMaaP Guice Test Module
 * <p>
 * @author Rajiv Singla . Creation Date: 10/20/2016.
 */
public class AnalyticsDMaaPTestModule extends AbstractModule {


    @Override
    protected void configure() {
// Bind Http Client
        bind(CloseableHttpClient.class).toInstance(HttpClients.createDefault());

        // Bind Publishing queue
        install(new FactoryModuleBuilder().implement(DMaaPMRPublisherQueue.class, DMaaPMRPublisherQueueImpl.class)
                .build(DMaaPMRPublisherQueueFactory.class));

        install(new FactoryModuleBuilder().implement(DMaaPMRPublisher.class, DMaaPMRPublisherMockImpl.class)
                .build(DMaaPMRPublisherFactory.class));

        install(new FactoryModuleBuilder().implement(DMaaPMRSubscriber.class, DMaaPMRSubscriberMockImpl.class)
                .build(DMaaPMRSubscriberFactory.class));
    }
}
