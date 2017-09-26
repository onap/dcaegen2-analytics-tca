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

package org.openecomp.dcae.apod.analytics.dmaap.domain.response;

/**
 * <p>
 *      Contract for all DMaaPMR Publisher Response
 * <p>
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public interface DMaaPMRPublisherResponse extends DMaaPMRResponse {


    /**
     * Gets number of pending messages
     *
     * @return pending messages in the batch queue
     */
    int getPendingMessagesCount();
}
