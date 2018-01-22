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
import org.onap.dcae.apod.analytics.common.exception.MessageProcessingException;
import org.onap.dcae.apod.analytics.tca.BaseAnalyticsTCAUnitTest;

/**
 * @author Rajiv Singla . Creation Date: 12/16/2016.
 */
public class AbstractTCAECEFPolicyProcessorTest extends BaseAnalyticsTCAUnitTest {

    private class DummyAbstractTCAECEFPolicyProcessor extends AbstractTCAECEFPolicyProcessor {

        @Override
        public String getProcessorDescription() {
            return "dummy";
        }

        @Override
        public TCACEFProcessorContext processMessage(TCACEFProcessorContext processorContext) {
            return processorContext;
        }
    }


    @Test(expected = MessageProcessingException.class)
    public void preProcessorWhenThereIsNoCEFMessage() throws Exception {
        DummyAbstractTCAECEFPolicyProcessor dummyAbstractTCAECEFPolicyProcessor = new
                DummyAbstractTCAECEFPolicyProcessor();

        final TCACEFProcessorContext processorContext = new TCACEFProcessorContext(null, getSampleTCAPolicy());
        processorContext.setCEFEventListener(null);
        dummyAbstractTCAECEFPolicyProcessor.preProcessor(processorContext);
    }

}
