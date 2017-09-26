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

import com.google.common.base.Optional;
import org.openecomp.dcae.apod.analytics.common.exception.MessageProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import static java.lang.String.format;

/**
 * An abstract Message Processor which can be extended by {@link MessageProcessor} implementations
 * to get default behavior for Message Processors
 *
 * @param <P> Processor Context sub classes
 *
 * @author Rajiv Singla . Creation Date: 11/8/2016.
 */
public abstract class AbstractMessageProcessor<P extends ProcessorContext> implements MessageProcessor<P> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMessageProcessor.class);

    /**
     * By Default there is no processing message
     */
    private String processingMessage = null;

    /**
     * By Default Processing State is set to not required - subclasses must
     * set processing state to {@link ProcessingState#PROCESSING_FINISHED_SUCCESSFULLY} on successful processing
     * or {@link ProcessingState#PROCESSING_TERMINATED_EARLY} if processing fails
     */
    protected ProcessingState processingState = ProcessingState.PROCESSING_NOT_REQUIRED;

    /**
     * Sub classes must provide a description of a processor
     *
     * @return description of processor
     *
     */
    public abstract String getProcessorDescription();


    /**
     * Sub classes must provide implementation to process Message
     *
     * @param processorContext incoming {@link ProcessorContext}
     * @return outgoing {@link ProcessorContext}
     */
    public abstract P processMessage(P processorContext);

    @Override
    public ProcessorInfo getProcessorInfo() {
        // by default the class of the Processor is assigned as Processor Name
        final String processorClassName = getClass().getSimpleName();
        return new GenericProcessorInfo(processorClassName, getProcessorDescription());
    }

    @Override
    public P preProcessor(P processorContext) {
        LOG.debug("Processing Started for Processor: {}", getProcessorInfo().getProcessorName());
        // by default check to see if continue processing Flag is not false
        final boolean okToContinue = processorContext.canProcessingContinue();
        if (!okToContinue) {
            final String errorMessage =
                    format("Processor: %s. Processing Context flag okToContinue is false. Unable to proceed...",
                            getProcessorInfo().getProcessorName());
            throw new MessageProcessingException(errorMessage, LOG, new IllegalStateException(errorMessage));
        }
        processingState = ProcessingState.PROCESSING_STARTED;
        return processorContext;
    }

    @Override
    public ProcessingState getProcessingState() {
        return processingState;
    }

    @Override
    public Optional<String> getProcessingMessage() {
        return Optional.fromNullable(processingMessage);
    }

    @Override
    public P postProcessor(P processorContext) {
        // Default implementation updates the post processing flag if processing did not
        // completed successfully
        if (processingState != ProcessingState.PROCESSING_FINISHED_SUCCESSFULLY) {
            LOG.debug("Processor: {}, Update Process Context State to stop Processing.",
                    getProcessorInfo().getProcessorName());
            processorContext.setProcessingContinueFlag(false);
        }
        // attaches itself to message processor context
        processorContext.getMessageProcessors().add(this);
        LOG.debug("Processing Completed for Processor: {}", getProcessorInfo());
        return processorContext;
    }


    @Override
    public final P apply(@Nonnull P processorContext) {
        final P preProcessedProcessorContext = preProcessor(processorContext);
        final P processedProcessorContext = processMessage(preProcessedProcessorContext);
        return postProcessor(processedProcessorContext);
    }


    /**
     * Helper method that updates processing state in case of early termination, logs the processing
     * termination reason, updates Processor processing state as Terminated and sets it processing message
     *
     * @param terminatingMessage error Message
     * @param processorContext message processor context
     */
    protected void setTerminatingProcessingMessage(final String terminatingMessage,
                                                   final P processorContext) {

        final String message = processorContext.getMessage();
        this.processingState = ProcessingState.PROCESSING_TERMINATED_EARLY;
        this.processingMessage = terminatingMessage;
        LOG.debug("Processor: {}, Early Terminating Message: {}, Incoming Message: {}",
                getProcessorInfo().getProcessorName(), terminatingMessage, message);
    }

    /**
     * Helper method that updates Processing state and logs completion message
     * passed
     *
     * @param processorPassingMessage Processor passing message
     * @param processorContext message processor context
     */
    protected void setFinishedProcessingMessage(final String processorPassingMessage, P processorContext) {
        final String message = processorContext.getMessage();
        processingState = ProcessingState.PROCESSING_FINISHED_SUCCESSFULLY;
        this.processingMessage = processorPassingMessage;
        LOG.debug("Processor: {}, Successful Completion Message: {}, Incoming Message: {}",
                getProcessorInfo().getProcessorName(), processorPassingMessage, message);
    }


}
