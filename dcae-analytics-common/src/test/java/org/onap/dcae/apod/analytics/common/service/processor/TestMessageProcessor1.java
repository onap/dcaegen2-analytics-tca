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

package org.onap.dcae.apod.analytics.common.service.processor;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/8/2016.
 */
public class TestMessageProcessor1 extends  AbstractMessageProcessor<TestProcessorContext> {

    @Override
    public String getProcessorDescription() {
        return "Appends \" World!\" to the message string and set it to result string";
    }

    @Override
    public TestProcessorContext processMessage(TestProcessorContext processorContext) {
        final String message = processorContext.getMessage();
        processorContext.setResult(message + " World!");
        setFinishedProcessingMessage("Finished Appending world", processorContext);
        return processorContext;
    }
}
