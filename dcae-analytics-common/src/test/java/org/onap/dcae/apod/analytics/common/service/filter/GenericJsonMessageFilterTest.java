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

package org.onap.dcae.apod.analytics.common.service.filter;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onap.dcae.apod.analytics.common.BaseAnalyticsCommonUnitTest;
import org.onap.dcae.apod.analytics.common.service.processor.GenericMessageChainProcessor;

import static org.junit.Assert.assertEquals;

/**
 * @author Rajiv Singla . Creation Date: 2/10/2017.
 */
public class GenericJsonMessageFilterTest extends BaseAnalyticsCommonUnitTest {

    private String jsonMessage;

    @Before
    public void before() throws Exception {
        jsonMessage = fromStream(CEF_MESSAGE_FILE_PATH);

    }

    @Test
    public void testJsonMessageFilterWhenAllFiltersPassed() throws Exception {

        final JsonMessageFilterProcessorContext finalMessageProcessorContext =
                processJsonMessageFilterChain(jsonMessage,
                        "domainFilter", "$.event.commonEventHeader.domain", "measurementsForVfScaling",
                        "eventNameFilter", "$.event.commonEventHeader.eventName", "Mfvs_eNodeB_RANKPI");

        assertJsonMessageAssertions(jsonMessage, finalMessageProcessorContext, true, true, 2);

    }

    @Test
    public void testJsonMessageFilterWhenJsonPathValueIsANumber() throws Exception {

        final JsonMessageFilterProcessorContext finalMessageProcessorContext =
                processJsonMessageFilterChain(jsonMessage,
                        "domainFilter", "$.event.commonEventHeader.sequence", "0",
                        "eventNameFilter", "$.event.commonEventHeader.eventName", "Mfvs_eNodeB_RANKPI");

        assertJsonMessageAssertions(jsonMessage, finalMessageProcessorContext, true, true, 2);
    }

    @Test
    public void testJsonMessageFilterWhenOneFilterDoesNotMatch() throws Exception {

        final JsonMessageFilterProcessorContext finalMessageProcessorContext =
                processJsonMessageFilterChain(jsonMessage,
                        "domainFilter", "$.event.commonEventHeader.domain", "xxxxxxxxxxx",
                        "functionalRoleFilter", "$.event.commonEventHeader.eventName", "vFirewall");

        assertJsonMessageAssertions(jsonMessage, finalMessageProcessorContext, false, false, 1);
    }

    @Test
    public void testJsonMessageFilterWhenJsonPathDoesNotExist() throws Exception {

        final JsonMessageFilterProcessorContext finalMessageProcessorContext =
                processJsonMessageFilterChain(jsonMessage,
                        "domainFilter", "$.event.commonEventHeader.xxxxxxx", "measurementsForVfScaling",
                        "functionalRoleFilter", "$.event.commonEventHeader.eventName", "vFirewall");

        assertJsonMessageAssertions(jsonMessage, finalMessageProcessorContext, false, false, 1);
    }

    @Test
    public void testJsonMessageFilterWhenIncomingMessageIsBlank() throws Exception {

        final JsonMessageFilterProcessorContext finalMessageProcessorContext =
                processJsonMessageFilterChain("",
                        "domainFilter", "$.event.commonEventHeader.domain", "measurementsForVfScaling",
                        "functionalRoleFilter", "$.event.commonEventHeader.eventName", "vFirewall");

        assertJsonMessageAssertions("", finalMessageProcessorContext, false, null, 1);

    }

    @Test
    public void testJsonMessageFilterWhenIncomingMessageIsNotValidJson() throws Exception {

        final JsonMessageFilterProcessorContext finalMessageProcessorContext =
                processJsonMessageFilterChain("invalidJson",
                        "domainFilter", "$.event.commonEventHeader.domain", "measurementsForVfScaling",
                        "functionalRoleFilter", "$.event.commonEventHeader.eventName", "vFirewall");

        assertJsonMessageAssertions("invalidJson", finalMessageProcessorContext, false, null, 1);

    }


    private static void assertJsonMessageAssertions(
            final String jsonMessage, final JsonMessageFilterProcessorContext finalMessageProcessorContext,
            final Boolean canProcessingContinueFlag, final Boolean matchedFlag,
            final int messageProcessorCount) throws Exception {

        assertJson(jsonMessage, finalMessageProcessorContext.getMessage());
        Assert.assertEquals(canProcessingContinueFlag, finalMessageProcessorContext.canProcessingContinue());
        assertEquals(matchedFlag, finalMessageProcessorContext.getMatched());
        Assert.assertEquals(finalMessageProcessorContext.getMessageProcessors().size(), messageProcessorCount);

    }


    private static JsonMessageFilterProcessorContext processJsonMessageFilterChain(
            final String jsonMessage,
            final String firstFilterName, final String firstFilterPath, final String firstFilterValue,
            final String secondFilterName, final String secondFilterPath, final String secondFilterValue) {

        // create processors
        final GenericJsonMessageFilter firstFilter = new GenericJsonMessageFilter(firstFilterName, firstFilterPath,
                firstFilterValue);
        final GenericJsonMessageFilter secondFilter = new GenericJsonMessageFilter(secondFilterName,
                secondFilterPath, secondFilterValue);

        // create initial processor context containing the json message that need to be processed
        final JsonMessageFilterProcessorContext initialProcessorContext =
                new JsonMessageFilterProcessorContext(jsonMessage);

        // create a generic message chain processor and feed it list of processors and initialProcessor context
        final GenericMessageChainProcessor<JsonMessageFilterProcessorContext> messageChainProcessor =
                new GenericMessageChainProcessor<>(ImmutableList.of(firstFilter, secondFilter),
                        initialProcessorContext);

        // process the generic message chain
        return messageChainProcessor.processChain();
    }
}
