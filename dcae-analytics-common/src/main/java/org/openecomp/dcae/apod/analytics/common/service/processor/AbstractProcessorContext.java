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

import com.google.common.base.Objects;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *      An abstract implementation for {@link ProcessorContext} which other DCAE Analytics Modules
 *      can extend to add module specific functionality
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/7/2016.
 */
public abstract class AbstractProcessorContext implements ProcessorContext {

    private final String message;
    private List<? super MessageProcessor<? extends ProcessorContext>> messageProcessors;
    private boolean canProcessingContinue;

    public AbstractProcessorContext(final String message,
                                    boolean canProcessingContinue) {
        this.message = message;
        this.canProcessingContinue = canProcessingContinue;
        this.messageProcessors = new LinkedList<>();
    }

    /**
     * Returns JSON String of incoming CEF Message that needs to be processed
     *
     * @return incoming CEF message that needs to be processed
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Sets if it is ok to continue processing normally
     *
     * @return boolean which determines if it is ok to continue processing normally
     */
    @Override
    public boolean canProcessingContinue() {
        return canProcessingContinue;
    }


    /**
     * Set if it is ok to continue processing normally
     *
     * @param canProcessingContinue sets boolean which determines if it is ok to continue processing normally
     */
    @Override
    public void setProcessingContinueFlag(boolean canProcessingContinue) {
        this.canProcessingContinue = canProcessingContinue;
    }

    /**
     * Provides List of message processors which were used in processing CEF message
     *
     * @return List of message processors which were used in processing CEF message
     */
    @Override
    public List<? super MessageProcessor<? extends ProcessorContext>> getMessageProcessors() {
        return messageProcessors;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("canProcessingContinue", canProcessingContinue)
                .toString();
    }
}
