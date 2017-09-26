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

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 12/12/2016.
 */
public class AbstractProcessorContextTest extends BaseAnalyticsCommonUnitTest {

    class TestAbstractMessageProcessorContext extends AbstractProcessorContext {

        public TestAbstractMessageProcessorContext(String message, boolean canProcessingContinue) {
            super(message, canProcessingContinue);
        }
    }


    @Test
    public void testGetMessage() throws Exception {
        TestAbstractMessageProcessorContext testProcessorContext =
                new TestAbstractMessageProcessorContext(TEST_MESSAGE_PROCESSOR_MESSAGE, true);
        final String message = testProcessorContext.getMessage();
        assertThat("Message Processor message must match", message, is(TEST_MESSAGE_PROCESSOR_MESSAGE));
    }

    @Test
    public void testCanProcessingContinue() throws Exception {
        TestAbstractMessageProcessorContext testProcessorContext =
                new TestAbstractMessageProcessorContext(TEST_MESSAGE_PROCESSOR_MESSAGE, true);
        final boolean canProcessingContinue = testProcessorContext.canProcessingContinue();
        assertThat("Message Can Processing flag must be true", canProcessingContinue, is(true));
    }

    @Test
    public void testSetProcessingContinueFlag() throws Exception {
        TestAbstractMessageProcessorContext testProcessorContext =
                new TestAbstractMessageProcessorContext(TEST_MESSAGE_PROCESSOR_MESSAGE, true);
        testProcessorContext.setProcessingContinueFlag(false);
        assertThat("Message Can processing flag must be false",
                testProcessorContext.canProcessingContinue(), is(false));

    }

    @Test
    public void testGetMessageProcessors() throws Exception {
        TestAbstractMessageProcessorContext testProcessorContext =
                new TestAbstractMessageProcessorContext(TEST_MESSAGE_PROCESSOR_MESSAGE, true);
        final List<? super MessageProcessor<? extends ProcessorContext>> messageProcessors =
                testProcessorContext.getMessageProcessors();
        assertThat("Message processor processing message must match", messageProcessors.size(), is(0));
    }

}
