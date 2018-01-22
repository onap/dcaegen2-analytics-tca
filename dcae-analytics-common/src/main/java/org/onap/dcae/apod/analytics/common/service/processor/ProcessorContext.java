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

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     A Processor Context is used a an input and output to a {@link MessageProcessor}
 *     <br>
 *     DCAE Analytics sub projects should extend this interface and add specific fields
 *     required for input and output
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/7/2016.
 */
public interface ProcessorContext extends Serializable {

    /**
     * Returns Processor Context message that will be processed by Chain of Processors
     *
     * @return message that need to be processed by processors
     */
    String getMessage();

    /**
     * Processing Context flag which determines if Processing can continue in a processing
     * chain
     *
     * @return true if ok to continue processing normally
     */
    boolean canProcessingContinue();


    /**
     * Sets new value for ProcessingContinue flag which will cause early termination of processing in chain if
     * set to false
     *
     * @param canProcessingContinue set new value for canProcessing Continue flag
     */
    void setProcessingContinueFlag(boolean canProcessingContinue);


    /**
     * Provides a List of previous processors which have completed processing
     *
     * @return list of previous processors
     */
    List<? super MessageProcessor<? extends ProcessorContext>> getMessageProcessors();

}
