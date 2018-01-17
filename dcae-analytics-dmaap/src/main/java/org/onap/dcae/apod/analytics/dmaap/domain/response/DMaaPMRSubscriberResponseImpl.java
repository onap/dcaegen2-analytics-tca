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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Collections.unmodifiableList;

/**
 * <p>
 *      A simple implementation for {@link DMaaPMRSubscriberResponse}
 * <p>
 * @author Rajiv Singla . Creation Date: 10/13/2016.
 */
public class DMaaPMRSubscriberResponseImpl implements DMaaPMRSubscriberResponse {

    private final Integer responseCode;
    private final String responseMessage;
    private final List<String> fetchedMessages;

    public DMaaPMRSubscriberResponseImpl(@Nonnull Integer responseCode,
                                         @Nonnull String responseMessage,
                                         @Nullable List<String> fetchedMessages) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.fetchedMessages = fetchedMessages != null ? fetchedMessages : ImmutableList.<String>of();
    }

    public DMaaPMRSubscriberResponseImpl(Integer responseCode, String responseMessage) {
        this(responseCode, responseMessage, null);
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
    public List<String> getFetchedMessages() {
        return unmodifiableList(fetchedMessages);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("responseCode", responseCode)
                .add("responseMessage", responseMessage)
                .add("fetchedMessages(size)", fetchedMessages.size())
                .toString();
    }
}
