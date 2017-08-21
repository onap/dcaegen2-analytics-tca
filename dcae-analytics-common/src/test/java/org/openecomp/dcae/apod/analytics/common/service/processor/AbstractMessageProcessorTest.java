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

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.common.BaseAnalyticsCommonUnitTest;
import org.openecomp.dcae.apod.analytics.common.exception.MessageProcessingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Rajiv Singla . Creation Date: 12/12/2016.
 */
public class AbstractMessageProcessorTest extends BaseAnalyticsCommonUnitTest {


    @Test
    public void testPreProcessorWhenProcessingContextFlagIsTrue() throws Exception {
        TestMessageProcessor1 messageProcessor1 = new TestMessageProcessor1();
        final TestProcessorContext processorContext =
                new TestProcessorContext(TEST_MESSAGE_PROCESSOR_MESSAGE, true);
        final TestProcessorContext testProcessorContext = messageProcessor1.preProcessor(processorContext);
        assertThat("Processing flag must be true",
                testProcessorContext.canProcessingContinue(), is(true));
    }

    @Test(expected = MessageProcessingException.class)
    public void testPreProcessorWhenProcessingContextFlagIsFalse() throws Exception {
        TestMessageProcessor1 messageProcessor1 = new TestMessageProcessor1();
        final TestProcessorContext testProcessorContext =
                new TestProcessorContext(TEST_MESSAGE_PROCESSOR_MESSAGE, false);
        messageProcessor1.preProcessor(testProcessorContext);
    }

    @Test
    public void testPostProcessorWhenProcessingStateIsNotFinishedSuccessfully() throws Exception {
        TestMessageProcessor1 messageProcessor1 = new TestMessageProcessor1();
        final ProcessingState processingState = messageProcessor1.getProcessingState();
        assertTrue("Processing state is not processing finished successfully",
                processingState != ProcessingState.PROCESSING_FINISHED_SUCCESSFULLY);
        final TestProcessorContext processorContext =
                new TestProcessorContext(TEST_MESSAGE_PROCESSOR_MESSAGE, true);
        final TestProcessorContext testProcessorContext = messageProcessor1.postProcessor(processorContext);
        assertThat("Processing flag must be false",
                testProcessorContext.canProcessingContinue(), is(false));
    }

}
