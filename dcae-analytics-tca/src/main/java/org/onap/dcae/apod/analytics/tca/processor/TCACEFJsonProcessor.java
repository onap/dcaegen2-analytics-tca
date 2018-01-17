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

import org.apache.commons.lang3.StringUtils;
import org.onap.dcae.apod.analytics.common.exception.MessageProcessingException;
import org.onap.dcae.apod.analytics.common.service.processor.AbstractMessageProcessor;
import org.onap.dcae.apod.analytics.model.domain.cef.EventListener;
import org.onap.dcae.apod.analytics.tca.utils.TCAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *<p>
 *    Processor that converts incoming presumed JSON string CEF message to {@link EventListener} object
 *    <br>
 *    Pre Conditions: None
 *</p>
 *
 *  @author Rajiv Singla . Creation Date: 11/5/2016.
 */
public class TCACEFJsonProcessor extends AbstractMessageProcessor<TCACEFProcessorContext> {


    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(TCACEFJsonProcessor.class);


    @Override
    public String getProcessorDescription() {
        return "Converts incoming TCA CEF Message to Event Listener object";
    }

    @Override
    public TCACEFProcessorContext processMessage(TCACEFProcessorContext processorContext) {

        final String cefMessage = processorContext.getMessage();

        // If CEF Message is null then processor should stop processing
        if (cefMessage == null) {
            String errorMessage = "Null CEF message cannot be converted to CEF Event Listener Object";
            throw new MessageProcessingException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        }

        // If CEF Message is blank then processor stop processing
        if (StringUtils.isBlank(cefMessage)) {
            setTerminatingProcessingMessage("Blank CEF message cannot be converted to CEF Event Listener Object",
                    processorContext);
            return processorContext;
        }

        // trim cef message
        final String trimmedCEFMessage = cefMessage.trim();

        // if message does not start with curly brace and ends with curly brace, it is not a valid cef message
        // processor will stop processing
        if (!(trimmedCEFMessage.startsWith("{") && trimmedCEFMessage.endsWith("}"))) {
            setTerminatingProcessingMessage("CEF Message must start with curly brace and must end with curly brace",
                    processorContext);
            return processorContext;
        }

        // try parsing the cef message
        try {
            final EventListener eventListener = TCAUtils.readValue(trimmedCEFMessage, EventListener.class);
            setFinishedProcessingMessage("CEF JSON to Event Listener Conversion Successful", processorContext);
            // set new Event Listener in the Processor Context
            processorContext.setCEFEventListener(eventListener);
            return processorContext;
        } catch (IOException e) {
            final String errorMessage = String.format("Parsing Failed for CEF Message: %s, Error: %s", cefMessage, e);
            // If parsing fails throw an exception
            throw new MessageProcessingException(errorMessage, LOG, e);
        }

    }
}
