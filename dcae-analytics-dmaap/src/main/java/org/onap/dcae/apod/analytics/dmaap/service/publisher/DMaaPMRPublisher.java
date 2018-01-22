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

package org.onap.dcae.apod.analytics.dmaap.service.publisher;

import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.onap.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *     DMaaP MR Publisher can be used to publish messages to DMaaP MR Topics.
 * <p>
 *
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public interface DMaaPMRPublisher extends AutoCloseable {


    /**
     * <p>
     *     Adds collection of messages to DMaaP MR Topic Publishing Queue.
     * <p>
     *     Note: Invoking this method may or may not cause publishing immediately
     *     as publishing in done is batch mode by default. Parameter maxBatchSize
     *     in {@link DMaaPMRPublisherConfig} is used to determine max batch queue size.
     *     If the maxBatchSize is reached all message will be published automatically
     *     during subsequent call.
     * </p>
     *
     * @param messages messages to publish to DMaaP MR Publisher
     * @return response which may contain Http Response code 202 (Accepted) as publishing
     * will proceed when max batch size is reached. Throws {@link DCAEAnalyticsRuntimeException}
     * if publishing fails
     */
    DMaaPMRPublisherResponse publish(List<String> messages);


    /**
     * <p>
     *     Forces publishing of messages to DMaaP MR Topic and returns {@link DMaaPMRPublisherResponse}
     *     which can be inspected for HTTP status code of publishing call to DMaaP MR Topic.
     * </p>
     *
     * @param messages messages to publish to DMaaP MR Publisher
     * @return DMaaP Message Router Publisher Response. Throws {@link DCAEAnalyticsRuntimeException}
     * if force publishing fails
     *
     */
    DMaaPMRPublisherResponse forcePublish(List<String> messages);


    /**
     * <p>
     *     Forces publishing of messages in Publisher queue to DMaaP MR Topic and returns
     *     {@link DMaaPMRPublisherResponse}.If there are no messages were in the queue to
     *     be flushed response code 304 (Not Modified) will be returned
     * </p>
     *
     * @return DMaaP Message Router Publisher Response
     */
    DMaaPMRPublisherResponse flush();


    /**
     * <p>
     *     Returns the creation time when Publisher instance was created.
     * <p>
     *
     * @return creation time of Subscriber instance
     */
    Date getPublisherCreationTime();


}
