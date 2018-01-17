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
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponse;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRPublisherResponseImpl;

import java.util.Date;
import java.util.List;

/**
 * @author Rajiv Singla . Creation Date: 10/21/2016.
 */
public class DMaaPMRPublisherMockImpl implements  DMaaPMRPublisher {

    @Override
    public DMaaPMRPublisherResponse publish(List<String> messages) throws DCAEAnalyticsRuntimeException {
        return new DMaaPMRPublisherResponseImpl(102, "Mock Response", 100);
    }

    @Override
    public DMaaPMRPublisherResponse forcePublish(List<String> messages) throws DCAEAnalyticsRuntimeException {
        return null;
    }

    @Override
    public DMaaPMRPublisherResponse flush() {
        return null;
    }

    @Override
    public Date getPublisherCreationTime() {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
