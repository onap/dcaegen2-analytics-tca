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

import org.junit.Test;
import org.onap.dcae.apod.analytics.common.service.processor.ProcessingState;
import org.onap.dcae.apod.analytics.model.domain.cef.EventListener;
import org.onap.dcae.apod.analytics.tca.BaseAnalyticsTCAUnitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public class TCACEFPolicyThresholdsProcessorTest extends BaseAnalyticsTCAUnitTest {

    @Test
    public void testCEFPolicyThresholdProcessorWithNoThresholdViolation() throws Exception {

        final String cefMessageString = fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
        final TCACEFProcessorContext tcacefProcessorContext = new TCACEFProcessorContext(cefMessageString,
                getSampleTCAPolicy());
        tcacefProcessorContext.setCEFEventListener(getCEFEventListener());

        AbstractTCAECEFPolicyProcessor policyThresholdsProcessor = new TCACEFPolicyThresholdsProcessor();
        final TCACEFProcessorContext finalProcessorContext = policyThresholdsProcessor.apply(tcacefProcessorContext);

        assertFalse("Process Context can Processing Continue flag should be false", finalProcessorContext
                .canProcessingContinue());
        assertThat("Policy Threshold Processor State must be terminated early",
                policyThresholdsProcessor.getProcessingState(), is(ProcessingState.PROCESSING_TERMINATED_EARLY));
        assertEquals("Policy must not change", getSampleTCAPolicy(), finalProcessorContext.getTCAPolicy());

    }

    @Test
    public void testCEFPolicyThresholdProcessorWithThresholdViolation() throws Exception {

        final String cefMessageString = fromStream(CEF_MESSAGE_WITH_THRESHOLD_VIOLATION_JSON_FILE_LOCATION);
        final TCACEFProcessorContext tcacefProcessorContext = new TCACEFProcessorContext(cefMessageString,
                getSampleTCAPolicy());

        final EventListener eventListener = ANALYTICS_MODEL_OBJECT_MAPPER.readValue(cefMessageString,
                EventListener.class);
        tcacefProcessorContext.setCEFEventListener(eventListener);

        AbstractTCAECEFPolicyProcessor policyThresholdsProcessor = new TCACEFPolicyThresholdsProcessor();
        final TCACEFProcessorContext finalProcessorContext = policyThresholdsProcessor.apply(tcacefProcessorContext);

        assertTrue("Process Context can Processing Continue flag should be true", finalProcessorContext
                .canProcessingContinue());
        assertThat("Policy Threshold Processor State must be successful",
                policyThresholdsProcessor.getProcessingState(), is(ProcessingState.PROCESSING_FINISHED_SUCCESSFULLY));
        assertEquals("Policy must not change", getSampleTCAPolicy(), finalProcessorContext.getTCAPolicy());

    }

}
