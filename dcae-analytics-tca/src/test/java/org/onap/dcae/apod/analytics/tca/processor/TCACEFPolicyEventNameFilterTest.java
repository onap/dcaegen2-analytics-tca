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

package org.onap.dcae.apod.analytics.tca.processor;

import org.junit.Before;
import org.junit.Test;
import org.onap.dcae.apod.analytics.common.service.processor.ProcessingState;
import org.onap.dcae.apod.analytics.model.domain.cef.EventListener;
import org.onap.dcae.apod.analytics.tca.BaseAnalyticsTCAUnitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 12/19/2016.
 */
public class TCACEFPolicyEventNameFilterTest extends BaseAnalyticsTCAUnitTest {

    private TCACEFPolicyEventNameFilter tcacefPolicyEventNameFilter;
    private TCACEFProcessorContext processorContext;
    private EventListener cefEventListener;

    @Before
    public void before() throws Exception {
        tcacefPolicyEventNameFilter = new TCACEFPolicyEventNameFilter();
        processorContext = new TCACEFProcessorContext("", getSampleTCAPolicy());
        cefEventListener = getCEFEventListener();
        processorContext.setCEFEventListener(cefEventListener);
    }

    @Test
    public void testProcessMessageWhenMessageIsValid() throws Exception {
        tcacefPolicyEventNameFilter.processMessage(processorContext);
        assertThat("Processing must finish successfully",
                tcacefPolicyEventNameFilter.getProcessingState(),
                is(ProcessingState.PROCESSING_FINISHED_SUCCESSFULLY));
    }

    @Test
    public void testProcessMessageWhenCEFEventIsNull() throws Exception {
        cefEventListener.setEvent(null);
        processorContext.setCEFEventListener(cefEventListener);
        tcacefPolicyEventNameFilter.processMessage(processorContext);
        assertThat("Processing must terminate early",
                tcacefPolicyEventNameFilter.getProcessingState(), is(ProcessingState.PROCESSING_TERMINATED_EARLY));
    }

    @Test
    public void testProcessMessageWhenPolicyEventNameDoesNotMatchMessageEventName() throws Exception {
        cefEventListener.getEvent().getCommonEventHeader().setEventName("someNonPolicyEventName");
        tcacefPolicyEventNameFilter.processMessage(processorContext);
        assertThat("Processing must terminate early",
                tcacefPolicyEventNameFilter.getProcessingState(), is(ProcessingState.PROCESSING_TERMINATED_EARLY));
    }


}
