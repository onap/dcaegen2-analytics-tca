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

package org.onap.dcae.apod.analytics.dmaap.service.subscriber;

import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponse;
import org.onap.dcae.apod.analytics.dmaap.domain.response.DMaaPMRSubscriberResponseImpl;

import java.util.Date;

/**
 * @author Rajiv Singla . Creation Date: 10/21/2016.
 */
public class DMaaPMRSubscriberMockImpl implements DMaaPMRSubscriber {

    @Override
    public DMaaPMRSubscriberResponse fetchMessages() throws DCAEAnalyticsRuntimeException {
        return new DMaaPMRSubscriberResponseImpl(102, "Mock Response", null);
    }

    @Override
    public Date getSubscriberCreationTime() {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
