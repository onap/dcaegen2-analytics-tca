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

package org.onap.dcae.apod.analytics.dmaap.domain.response;

import java.util.List;

/**
 * <p>
 *     Contract for all DMaaP MR Subscriber Responses
 * </p>
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public interface DMaaPMRSubscriberResponse extends DMaaPMRResponse {

    /**
     * Returns message fetched from DMaaP MR Topic
     *
     * @return collection of actual message retrieved from DMaaP MR Topic
     */
    List<String> getFetchedMessages();

}
