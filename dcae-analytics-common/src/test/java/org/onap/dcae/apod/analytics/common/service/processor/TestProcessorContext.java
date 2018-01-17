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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/8/2016.
 */
public class TestProcessorContext implements ProcessorContext {

    private String message;
    private boolean continueProcessingFlag;
    private String result;
    private List<? super MessageProcessor<? extends ProcessorContext>> messageProcessors;

    public TestProcessorContext(String message, boolean continueProcessingFlag) {
        this.message = message;
        this.continueProcessingFlag = continueProcessingFlag;
        this.messageProcessors = new LinkedList<>();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean canProcessingContinue() {
        return continueProcessingFlag;
    }

    @Override
    public void setProcessingContinueFlag(boolean canProcessingContinue) {
        this.continueProcessingFlag = canProcessingContinue;
    }

    @Override
    public List<? super MessageProcessor<? extends ProcessorContext>> getMessageProcessors() {
        return messageProcessors;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isContinueProcessingFlag() {
        return continueProcessingFlag;
    }

    public void setContinueProcessingFlag(boolean continueProcessingFlag) {
        this.continueProcessingFlag = continueProcessingFlag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
