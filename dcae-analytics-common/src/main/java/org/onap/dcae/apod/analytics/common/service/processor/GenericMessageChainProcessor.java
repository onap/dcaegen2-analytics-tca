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

import org.onap.dcae.apod.analytics.common.utils.MessageProcessorUtils.MessageProcessorFunction;

import java.util.List;

import static org.onap.dcae.apod.analytics.common.utils.MessageProcessorUtils.computeMessageProcessorChain;

/**
 * <p>
 *     A Generic Message Processor which passes the {@link ProcessorContext} from first to second
 *     {@link MessageProcessor}
 * </p>
 *
 * @param <P> Processor Context sub classes
 *
 * @author Rajiv Singla . Creation Date: 11/8/2016.
 */
public class GenericMessageChainProcessor<P extends ProcessorContext> {

    private final List<? extends MessageProcessor<P>> messageProcessors;
    private final P initialProcessorContext;

    public GenericMessageChainProcessor(List<? extends MessageProcessor<P>> messageProcessors,
                                        P initialProcessorContext) {
        this.messageProcessors = messageProcessors;
        this.initialProcessorContext = initialProcessorContext;
    }

    /**
     * Process a processor chain
     *
     * @return Processor Context after processing the processor chain
     */
    public P processChain() {

        final MessageProcessorFunction<P> messageProcessorFunction =
                new MessageProcessorFunction<P>() {
                    @Override
                    public <M extends MessageProcessor<P>> P apply(P context, M processor) {
                        return processor.apply(context);
                    }
                };

        return computeMessageProcessorChain(messageProcessors, initialProcessorContext,
                messageProcessorFunction);

    }

}
