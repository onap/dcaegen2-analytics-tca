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

package org.openecomp.dcae.apod.analytics.dmaap.service.subscriber;

import org.openecomp.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRSubscriberConfig;

/**
 * Factory to initialize instance of {@link DMaaPMRSubscriber} for Guice DI injection purposes.
 * <p>
 *     <strong>
 *          NOTE: Client should not use this Factory to initialize {@link DMaaPMRSubscriber} unless they
 *              are wiring dependencies using Guice. Client must use {@link DMaaPMRFactory} to initialize
 *              guice injected Subscriber instances
 *      </strong>
 * <p>
 * @author Rajiv Singla . Creation Date: 10/20/2016.
 */
public interface DMaaPMRSubscriberFactory {

    /**
     * Guice Factory to create DMaaP MR Subscriber Instance
     *
     * @param subscriberConfig subscriber config
     *
     * @return DMaaP MR Subscriber instance
     */
    DMaaPMRSubscriber create(DMaaPMRSubscriberConfig subscriberConfig);
}
