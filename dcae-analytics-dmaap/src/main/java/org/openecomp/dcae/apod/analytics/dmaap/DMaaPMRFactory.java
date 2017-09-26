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

package org.openecomp.dcae.apod.analytics.dmaap;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;
import org.openecomp.dcae.apod.analytics.dmaap.module.AnalyticsDMaaPModule;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisherFactory;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriberFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Creates pre injected implementations for {@link DMaaPMRPublisher} and {@link DMaaPMRSubscriber}
 * <p>
 *     Usage:
 *     <p>Create an instance of DMaaP MR Factory</p>
 *     <pre>
 *        DMaaPFactory dmaapFactory = DMaaPFactory.initalize()
 *     </pre>
 *     <p>Create a new DMaaP MR Publisher</p>
 *     <pre>
 *         DMaaPMRPublisher publisher = dmaapFactory.createPublisher(publisherConfig)
 *     </pre>
 *     <p>Create new DMaaP MR Subscriber</p>
 *     <pre>
 *         DMaaPMRSubscriber subscriber = dmaapFactory.createSubscriber(subscriberConfig)
 *     </pre>
 * <p>
 * <strong>All Clients must use this Factory to initalize DMaaP Message Router Publishers and Subscribers</strong>
 * </p>
 * <p>
 * @author Rajiv Singla . Creation Date: 10/20/2016.
 */
public class DMaaPMRFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRFactory.class);

    private final Injector injector;

    public DMaaPMRFactory(AbstractModule guiceModule) {
        injector = Guice.createInjector(guiceModule);
    }

    /**
     * Returns configured instance of {@link DMaaPMRPublisher}
     *
     * @param publisherConfig Publisher Config
     * @return configured instance of DMaaP MR Publisher
     */
    public DMaaPMRPublisher createPublisher(@Nonnull DMaaPMRPublisherConfig publisherConfig) {
        final DMaaPMRPublisherFactory publisherFactory = injector.getInstance(DMaaPMRPublisherFactory.class);
        LOG.debug("Creating new DMaaP MR Publisher Instance with configuration: {}", publisherConfig);
        final DMaaPMRPublisher dMaaPMRPublisher = publisherFactory.create(publisherConfig);
        LOG.info("Created new DMaaP MR Publisher Instance. Publisher creation time: {}",
                dMaaPMRPublisher.getPublisherCreationTime());
        return dMaaPMRPublisher;
    }

    /**
     * Returns configured instance of {@link DMaaPMRSubscriber}
     *
     * @param subscriberConfig Subscriber Config
     * @return configured instance of DMaaP MR Subscriber
     */
    public DMaaPMRSubscriber createSubscriber(@Nonnull DMaaPMRSubscriberConfig subscriberConfig) {
        final DMaaPMRSubscriberFactory subscriberFactory = injector.getInstance(DMaaPMRSubscriberFactory.class);
        LOG.debug("Creating new DMaaP MR Subscriber Instance with configuration: {}", subscriberConfig);
        final DMaaPMRSubscriber dMaaPMRSubscriber = subscriberFactory.create(subscriberConfig);
        LOG.info("Created new DMaaP MR Subscriber Instance. Subscriber creation time: {}",
                dMaaPMRSubscriber.getSubscriberCreationTime());
        return dMaaPMRSubscriber;
    }

    /**
     * Creates an instance of {@link DMaaPMRFactory}
     *
     * @return {@link DMaaPMRFactory} factory instance
     */
    public static DMaaPMRFactory create() {
        final DMaaPMRFactory dMaaPMRFactory = new DMaaPMRFactory(new AnalyticsDMaaPModule());
        LOG.info("Created new instance of DMaaP MR Factory");
        return dMaaPMRFactory;
    }


}
