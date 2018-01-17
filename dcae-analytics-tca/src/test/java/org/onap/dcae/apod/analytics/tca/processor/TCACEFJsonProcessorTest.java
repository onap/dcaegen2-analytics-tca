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
import org.onap.dcae.apod.analytics.model.domain.cef.EventListener;
import org.onap.dcae.apod.analytics.tca.BaseAnalyticsTCAUnitTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public class TCACEFJsonProcessorTest extends BaseAnalyticsTCAUnitTest {


    // A valid CEF Message
    @Test
    public void testCEFJsonProcessorWithValidCEFMessage() throws Exception {

        final String cefMessageString = fromStream(CEF_MESSAGE_JSON_FILE_LOCATION);
        final TCACEFProcessorContext tcacefProcessorContext =
                new TCACEFProcessorContext(cefMessageString, getSampleTCAPolicy());

        TCACEFJsonProcessor tcacefJsonProcessor = new TCACEFJsonProcessor();
        final TCACEFProcessorContext finalProcessorContext = tcacefJsonProcessor.apply(tcacefProcessorContext);

        final EventListener cefEventListener = finalProcessorContext.getCEFEventListener();

        assertNotNull("CEF Event Listener must be present", cefEventListener);

    }

    // Even if message is not a valid CEF format but still a Json - Json Processor will parse it
    @Test
    public void testCEFJsonProcessorWithValidJson() throws Exception {

        final TCACEFProcessorContext tcacefProcessorContext = new TCACEFProcessorContext(
                " { \"key\" : \"value\" } ", getSampleTCAPolicy());

        TCACEFJsonProcessor tcacefJsonProcessor = new TCACEFJsonProcessor();
        final TCACEFProcessorContext finalProcessorContext = tcacefJsonProcessor.apply(tcacefProcessorContext);
        final EventListener cefEventListener = finalProcessorContext.getCEFEventListener();

        assertNotNull("Even if message is not a valid CEF format but a valid Json.Json Processor must be able to " +
                        "parse it",
                cefEventListener);
    }

    @Test(expected = MessageProcessingException.class)
    public void testCEFJsonProcessorWithCEFMessageAsNull() throws Exception {

        final TCACEFProcessorContext tcacefProcessorContext = new TCACEFProcessorContext(null, getSampleTCAPolicy());

        TCACEFJsonProcessor tcacefJsonProcessor = new TCACEFJsonProcessor();
        tcacefJsonProcessor.apply(tcacefProcessorContext);

    }

    @Test
    public void testCEFJsonProcessorWithCEFMessageIsBlank() throws Exception {

        final TCACEFProcessorContext tcacefProcessorContext = new TCACEFProcessorContext("   ", getSampleTCAPolicy());

        TCACEFJsonProcessor tcacefJsonProcessor = new TCACEFJsonProcessor();
        final TCACEFProcessorContext finalProcessorContext = tcacefJsonProcessor.apply(tcacefProcessorContext);
        assertFalse("Blank message must terminate processing of message chain", finalProcessorContext
                .canProcessingContinue());
    }


    @Test
    public void testCEFJsonProcessorWithCEFMessageWhichIsNotValidMessage() throws Exception {

        final TCACEFProcessorContext tcacefProcessorContext = new TCACEFProcessorContext(" Invalid Message ",
                getSampleTCAPolicy());

        TCACEFJsonProcessor tcacefJsonProcessor = new TCACEFJsonProcessor();
        final TCACEFProcessorContext finalProcessorContext = tcacefJsonProcessor.apply(tcacefProcessorContext);
        assertFalse("Invalid message must terminate processing of message chain", finalProcessorContext
                .canProcessingContinue());
    }


    @Test(expected = MessageProcessingException.class)
    public void testCEFJsonProcessorWithCEFMessageWhichIsNotValidJson() throws Exception {

        final TCACEFProcessorContext tcacefProcessorContext = new TCACEFProcessorContext(
                " { \"Invalid Event Listener Json\" } ", getSampleTCAPolicy());

        TCACEFJsonProcessor tcacefJsonProcessor = new TCACEFJsonProcessor();
        tcacefJsonProcessor.apply(tcacefProcessorContext);
    }


}
