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

import org.onap.dcae.apod.analytics.common.exception.MessageProcessingException;
import org.onap.dcae.apod.analytics.common.service.processor.AbstractMessageProcessor;
import org.onap.dcae.apod.analytics.model.domain.cef.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * <p>
 *     Encapsulates common functionality for all TCA CEF Policy Processors
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public abstract class AbstractTCAECEFPolicyProcessor extends AbstractMessageProcessor<TCACEFProcessorContext> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTCAECEFPolicyProcessor.class);

    /**
     * For all TCA Policy Processor the pre processor ensures that {@link EventListener} object is
     * present
     *
     * @param processorContext incoming Processor Context
     * @return Pre processed Processor Context
     */
    @Override
    public TCACEFProcessorContext preProcessor(@Nonnull TCACEFProcessorContext processorContext) {
        // validates CEF Event Listener is Present
        final EventListener cefEventListener = processorContext.getCEFEventListener();
        if (cefEventListener == null) {
            final String errorMessage = String.format(
                    "CEF Event Listener is not Present.Invalid use of Processor: %s. CEF Message: %s",
                    getProcessorInfo().getProcessorName(), processorContext.getMessage());
            throw new MessageProcessingException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        }
        return super.preProcessor(processorContext);
    }
}
