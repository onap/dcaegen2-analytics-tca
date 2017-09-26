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

import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;

import java.util.Date;

/**
 * <p>
 *     DMaaP MR Subscriber can be used to subscribe messages from DMaaP MR Topics.
 * <p>
 *
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public interface DMaaPMRSubscriber extends AutoCloseable {

    /**
     * Fetches Messages from DMaaP MR Topic. {@link DMaaPMRPublisherConfig} settings parameters
     * for messageLimit and message timeout are used
     *
     * @return DMaaP Message Router Subscriber Response
     */
    DMaaPMRSubscriberResponse fetchMessages();


    /**
     * Returns the Subscriber instance creation time
     * <p>
     * NOTE: Due to DMaaP API Design - Subscribers can only fetch messages which
     * are published to the topic after the creation of the Subscriber.
     *
     * @return creation time of Subscriber instance
     */
    Date getSubscriberCreationTime();


}
