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

package org.openecomp.dcae.apod.analytics.common.service.processor;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.common.BaseAnalyticsCommonUnitTest;
import org.openecomp.dcae.apod.analytics.common.exception.MessageProcessingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;


/**
 *
 * @author Rajiv Singla . Creation Date: 11/8/2016.
 */
public class GenericMessageChainProcessorTest extends BaseAnalyticsCommonUnitTest {


    @Test
    public void testProcessChainWhenProcessChainHasNoEarlyTermination() throws Exception {

        final TestMessageProcessor1 testMessageProcessor1 = getTestMessageProcessor1();
        final TestMessageProcessor2 testMessageProcessor2 = getTestMessageProcessor2();
        final ImmutableList<? extends MessageProcessor<TestProcessorContext>> testMessageChain =
                ImmutableList.of(testMessageProcessor1, testMessageProcessor2);

        final TestProcessorContext testProcessorContext = new TestProcessorContext("Hello", true);

        final GenericMessageChainProcessor<TestProcessorContext> genericMessageChainProcessor =
                new GenericMessageChainProcessor<>(testMessageChain, testProcessorContext);

        final TestProcessorContext finalProcessorContext = genericMessageChainProcessor.processChain();

        final String result = finalProcessorContext.getResult();
        assertThat("Final Result must be Hello World! Again", result, is("Hello World! Again"));
        assertThat("TestProcessor1 state is correct", testMessageProcessor1.getProcessingState(),
                is(ProcessingState.PROCESSING_FINISHED_SUCCESSFULLY));
        assertThat("TestProcessor2 state is correct", testMessageProcessor2.getProcessingState(),
                is(ProcessingState.PROCESSING_FINISHED_SUCCESSFULLY));
    }


    @Test
    public void testProcessChainWhenProcessChainEarlyTermination() throws Exception {

        final TestEarlyTerminatingProcessor testEarlyTerminatingProcessor = getTestEarlyTerminationProcessor();
        final ImmutableList<? extends MessageProcessor<TestProcessorContext>> testMessageChain =
                ImmutableList.of(testEarlyTerminatingProcessor, getTestMessageProcessor2());
        final TestProcessorContext testProcessorContext = new TestProcessorContext("Hello", true);

        final GenericMessageChainProcessor<TestProcessorContext> genericMessageChainProcessor =
                new GenericMessageChainProcessor<>(testMessageChain, testProcessorContext);

        final TestProcessorContext finalProcessorContext = genericMessageChainProcessor.processChain();
        final String result = finalProcessorContext.getResult();
        assertNull("Final Result must be null", result);
        assertThat("TestEarlyTerminatingProcessor state is correct",
                testEarlyTerminatingProcessor.getProcessingState(), is(ProcessingState.PROCESSING_TERMINATED_EARLY));
    }

    @Test(expected = MessageProcessingException.class)
    public void testProcessChainWhenIncomingMessageContextIsNull() throws Exception {

        final TestEarlyTerminatingProcessor testEarlyTerminatingProcessor = getTestEarlyTerminationProcessor();
        final ImmutableList<? extends MessageProcessor<TestProcessorContext>> testMessageChain =
                ImmutableList.of(testEarlyTerminatingProcessor, getTestMessageProcessor2());
        final TestProcessorContext testProcessorContext = null;

        final GenericMessageChainProcessor<TestProcessorContext> genericMessageChainProcessor =
                new GenericMessageChainProcessor<>(testMessageChain, testProcessorContext);

       genericMessageChainProcessor.processChain();
    }

}
