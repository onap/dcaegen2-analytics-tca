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

import com.google.common.base.Objects;

import javax.annotation.Nonnull;

/**
 * <p>
 *      An simple implementation of {@link DMaaPMRPublisherResponse}
 * <p>
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public class DMaaPMRPublisherResponseImpl implements DMaaPMRPublisherResponse {

    private final Integer responseCode;
    private final String responseMessage;
    private final int pendingMessagesCount;

    public DMaaPMRPublisherResponseImpl(@Nonnull Integer responseCode,
                                        @Nonnull String responseMessage,
                                        int pendingMessagesCount) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.pendingMessagesCount = pendingMessagesCount;
    }

    @Override
    public Integer getResponseCode() {
        return responseCode;
    }

    @Override
    public String getResponseMessage() {
        return responseMessage;
    }

    @Override
    public int getPendingMessagesCount() {
        return pendingMessagesCount;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("responseCode", responseCode)
                .add("responseMessage", responseMessage)
                .add("pendingMessagesCount", pendingMessagesCount)
                .toString();
    }
}
