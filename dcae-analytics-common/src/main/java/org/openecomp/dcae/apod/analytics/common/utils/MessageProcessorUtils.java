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

package org.openecomp.dcae.apod.analytics.common.utils;

import com.google.common.base.Preconditions;
import org.openecomp.dcae.apod.analytics.common.exception.MessageProcessingException;
import org.openecomp.dcae.apod.analytics.common.service.filter.GenericJsonMessageFilter;
import org.openecomp.dcae.apod.analytics.common.service.filter.JsonMessageFilterProcessorContext;
import org.openecomp.dcae.apod.analytics.common.service.processor.GenericMessageChainProcessor;
import org.openecomp.dcae.apod.analytics.common.service.processor.MessageProcessor;
import org.openecomp.dcae.apod.analytics.common.service.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;


/**
 *
 * @author Rajiv Singla . Creation Date: 11/8/2016.
 */
public abstract class MessageProcessorUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorUtils.class);

    /**
     * Provides an abstraction how to apply {@link ProcessorContext} to next {@link MessageProcessor}
     * in the message processor chain
     *
     * @param <P> Sub classes of Processor Context
     */
    public interface MessageProcessorFunction<P extends ProcessorContext> {

        /**
         * Method which provides accumulated {@link ProcessorContext} from previous processors and a reference
         * to next processor in the chain
         *
         * @param p accumulated {@link ProcessorContext} from previous processors
         * @param m current {@link MessageProcessor} in the chain
         * @param <M> Message processor sub classes
         *
         * @return processing context after computing the current Message Processor
         */
        <M extends MessageProcessor<P>> P apply(P p, M m);
    }


    /**
     * Provides an abstraction to compute a chain of {@link MessageProcessor}
     *
     * @param messageProcessors An iterable containing one or more {@link MessageProcessor}s
     * @param initialProcessorContext An initial processing Context
     * @param messageProcessorFunction messageProcessor Function
     * @param <P> Sub classes for Processor Context
     *
     * @return processing context which results after computing the whole chain
     */
    public static <P extends ProcessorContext> P computeMessageProcessorChain(
            final Iterable<? extends MessageProcessor<P>> messageProcessors,
            final P initialProcessorContext,
            final MessageProcessorFunction<P> messageProcessorFunction) {

        // Get message processor iterator
        final Iterator<? extends MessageProcessor<P>> processorIterator = messageProcessors.iterator();

        // If no next message processor - return initial processor context
        if (!processorIterator.hasNext()) {
            return initialProcessorContext;
        }

        // An accumulator for processor Context
        P processorContextAccumulator = initialProcessorContext;

        while (processorIterator.hasNext()) {

            final MessageProcessor<P> nextProcessor = processorIterator.next();

            // If Initial Processor Context is null
            if (processorContextAccumulator == null) {
                final String errorMessage =
                        String.format("Processor Context must not be null for Message Process: %s",
                                nextProcessor.getProcessorInfo().getProcessorName());
                throw new MessageProcessingException(errorMessage, LOG, new IllegalStateException(errorMessage));
            }


            if (!processorContextAccumulator.canProcessingContinue()) {
                LOG.debug("Triggering Early Termination, before Message Processor: {}, Incoming Message: {}",
                        nextProcessor.getProcessorInfo().getProcessorName(), processorContextAccumulator.getMessage());
                break;
            }
            processorContextAccumulator = messageProcessorFunction.apply(processorContextAccumulator, nextProcessor);
        }

        return processorContextAccumulator;
    }


    /**
     * Utility method to process Json Filter Mappings. Processes incoming json message and applies a list of json
     * filter mappings and returns the resulting {@link JsonMessageFilterProcessorContext}
     *
     * @param jsonMessage json message to which filter mappings will be applies
     * @param jsonFilterMappings Filter mappings contains a Map containing keys as filter json path
     * and values as set of expected value corresponding to filter path
     *
     * @return json message processor context which contains the {@link JsonMessageFilterProcessorContext#isMatched}
     * status after applying all filter mappings
     */
    public static JsonMessageFilterProcessorContext processJsonFilterMappings(
            final String jsonMessage, @Nonnull final Map<String, Set<String>> jsonFilterMappings) {

        Preconditions.checkState(jsonFilterMappings.size() > 0, "Json Filter Mappings must not be empty");

        // create initial processor context containing the json message that need to be processed
        final JsonMessageFilterProcessorContext initialProcessorContext =
                new JsonMessageFilterProcessorContext(jsonMessage);

        // Create Json Message Filters
        final List<GenericJsonMessageFilter> jsonMessageFilters = new LinkedList<>();

        int i = 0;
        for (Map.Entry<String, Set<String>> jsonFilterMapping : jsonFilterMappings.entrySet()) {
            jsonMessageFilters.add(new GenericJsonMessageFilter("Filter-" + i, jsonFilterMapping.getKey(),
                    jsonFilterMapping.getValue()));
            i++;
        }

        // Create Generic Message Chain Processor
        final GenericMessageChainProcessor<JsonMessageFilterProcessorContext> messageChainProcessor =
                new GenericMessageChainProcessor<>(jsonMessageFilters, initialProcessorContext);

        // Process chain and return resulting json Message Filter Processor Context
        return messageChainProcessor.processChain();
    }


}
