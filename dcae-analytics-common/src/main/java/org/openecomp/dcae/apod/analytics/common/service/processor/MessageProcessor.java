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

import com.google.common.base.Function;
import com.google.common.base.Optional;

import java.io.Serializable;

/**
 * <p>
 *     A message processor can be used to process incoming messages.
 *     It uses implementations of {@link ProcessorContext} as input and output
 * </p>
 *
 * @param <P> Message Processor Context implementations
 *
 * @author Rajiv Singla . Creation Date: 11/7/2016.
 */
public interface MessageProcessor<P extends ProcessorContext> extends Function<P, P>, Serializable {

    /**
     * Returns processor information
     *
     * @return processor Information
     */
    ProcessorInfo getProcessorInfo();


    /**
     * Does pre-processing of {@link ProcessorContext} e.g. validate input conditions and return
     * pre processed context
     *
     * @param processorContext incoming Processor Context
     * @return Pre processed Processor Context
     */
    P preProcessor(P processorContext);


    /**
     * Return processing state of a processor
     *
     * @return Processing State
     */
    ProcessingState getProcessingState();


    /**
     * May return a message from a processor which indicates the reason for {@link ProcessingState} especially if
     * there was some failure in processing
     *
     * @return processing Message
     */
    Optional<String> getProcessingMessage();


    /**
     * Does post-processing of {@link ProcessorContext}
     *
     * @param processorContext incoming Processor Context
     * @return processor Context after post processing is finished
     */
    P postProcessor(P processorContext);


}
