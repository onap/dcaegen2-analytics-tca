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

package org.openecomp.dcae.apod.analytics.dmaap.service.publisher;

import com.google.inject.assistedinject.Assisted;

/**
 * <p>
 *     Factory to initialize instance of {@link DMaaPMRPublisherQueue} for Guice DI injection purposes.
 * <p>
 *
 * @author Rajiv Singla . Creation Date: 11/1/2016.
 */
public interface DMaaPMRPublisherQueueFactory {

    /**
     * Guice Factory to create DMaaP MR Publisher Queue
     *
     * @param batchQueueSize batch queue size
     * @param recoveryQueueSize recovery queue size
     *
     * @return instance of DMaaP MR Publisher Queue
     */
    DMaaPMRPublisherQueue create(@Assisted("batchQueueSize") int batchQueueSize,
                                 @Assisted("recoveryQueueSize") int recoveryQueueSize);

}
