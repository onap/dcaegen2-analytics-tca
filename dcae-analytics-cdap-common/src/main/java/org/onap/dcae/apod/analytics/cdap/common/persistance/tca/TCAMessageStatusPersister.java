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

package org.onap.dcae.apod.analytics.cdap.common.persistance.tca;

import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.api.data.schema.UnsupportedTypeException;
import co.cask.cdap.api.dataset.DatasetProperties;
import co.cask.cdap.api.dataset.lib.IndexedTable;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.dataset.lib.ObjectMappedTableProperties;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.onap.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.onap.dcae.apod.analytics.common.service.processor.MessageProcessor;
import org.onap.dcae.apod.analytics.common.service.processor.ProcessorContext;
import org.onap.dcae.apod.analytics.common.utils.PersistenceUtils;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.onap.dcae.apod.analytics.tca.processor.TCACEFJsonProcessor;
import org.onap.dcae.apod.analytics.tca.processor.TCACEFPolicyDomainFilter;
import org.onap.dcae.apod.analytics.tca.processor.TCACEFPolicyEventNameFilter;
import org.onap.dcae.apod.analytics.tca.processor.TCACEFPolicyThresholdsProcessor;
import org.onap.dcae.apod.analytics.tca.processor.TCACEFProcessorContext;
import org.onap.dcae.apod.analytics.tca.utils.TCAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import static org.onap.dcae.apod.analytics.common.utils.PersistenceUtils.TABLE_ROW_KEY_COLUMN_NAME;

/**
 *
 *
 * @author Rajiv Singla . Creation Date: 11/15/2016.
 */
public abstract class TCAMessageStatusPersister {

    private static final Logger LOG = LoggerFactory.getLogger(TCAMessageStatusPersister.class);

    private TCAMessageStatusPersister() {

    }

    /**
     * Saves Message Status in Table. Assumes no alert was generated
     *
     * @param processorContext processor Context
     * @param instanceId Instance Id
     * @param calculatorMessageType Calculation Message Type
     * @param messageStatusTable Message Status Table
     */
    public static void persist(final TCACEFProcessorContext processorContext,
                               final int instanceId,
                               final TCACalculatorMessageType calculatorMessageType,
                               final ObjectMappedTable<TCAMessageStatusEntity> messageStatusTable) {
        persist(processorContext, instanceId, calculatorMessageType, messageStatusTable, null);
    }

    /**
     * Saves Message Status in Table. Sets up alert message aslo
     *
     * @param processorContext processor Context
     * @param instanceId Instance Id
     * @param calculatorMessageType Calculation Message Type
     * @param messageStatusTable Message Status Table
     * @param alertMessage Alert message
     */
    public static void persist(final TCACEFProcessorContext processorContext,
                               final int instanceId,
                               final TCACalculatorMessageType calculatorMessageType,
                               final ObjectMappedTable<TCAMessageStatusEntity> messageStatusTable,
                               @Nullable final String alertMessage) {

        final String rowKey = createKey(calculatorMessageType);

        final Long currentTS = new Date().getTime();
        final String vesMessage = StringEscapeUtils.unescapeJson(processorContext.getMessage());

        // Find Functional Role and domain
        final Pair<String, String> domainAndEventName = TCAUtils.getDomainAndEventName(processorContext);
        final String domain = domainAndEventName.getLeft();
        final String eventName = domainAndEventName.getRight();

        final TCAMessageStatusEntity tcaMessageStatusEntity = new TCAMessageStatusEntity(currentTS,
                instanceId, calculatorMessageType.name(), vesMessage, domain, eventName);

        // add threshold violation fields
        addViolatedThreshold(tcaMessageStatusEntity, processorContext);
        // add processor status and messages
        addMessageProcessorMessages(tcaMessageStatusEntity, processorContext);
        // add Alert message
        tcaMessageStatusEntity.setAlertMessage(
                alertMessage == null ? null : StringEscapeUtils.unescapeJson(alertMessage)
        );

        messageStatusTable.write(rowKey, tcaMessageStatusEntity);

        LOG.debug("Finished persisting VES Status Message with rowKey: {} in Message Status Table.", rowKey);

    }


    /**
     * Create TCA VES Message Status Table Properties
     *
     * @param timeToLiveSeconds Message Status Table time to live in seconds
     *
     * @return Message Status table properties
     */
    public static DatasetProperties getDatasetProperties(final int timeToLiveSeconds) {

        try {
            return ObjectMappedTableProperties.builder()
                    .setType(TCAMessageStatusEntity.class)
                    .setRowKeyExploreName(TABLE_ROW_KEY_COLUMN_NAME)
                    .setRowKeyExploreType(Schema.Type.STRING)
                    .add(IndexedTable.PROPERTY_TTL, timeToLiveSeconds)
                    .setDescription(CDAPComponentsConstants.TCA_FIXED_VES_MESSAGE_STATUS_DESCRIPTION_TABLE)
                    .build();
        } catch (UnsupportedTypeException e) {
            final String errorMessage = "Unable to convert TCAMessageStatusEntity class to Schema";
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }

    }


    /**
     * Adds Violated Threshold Parameter values to {@link TCAMessageStatusEntity}
     *
     * @param tcaMessageStatusEntity message entity that needs to be populated with threshold fields
     * @param processorContext processor context
     *
     */
    private static void addViolatedThreshold(final TCAMessageStatusEntity tcaMessageStatusEntity,
                                                               final TCACEFProcessorContext processorContext) {

        final MetricsPerEventName metricsPerEventName = processorContext.getMetricsPerEventName();

        if (metricsPerEventName != null
                && metricsPerEventName.getThresholds() != null
                && metricsPerEventName.getThresholds().get(0) != null) {

            final Threshold threshold = metricsPerEventName.getThresholds().get(0);
            tcaMessageStatusEntity.setThresholdPath(threshold.getFieldPath());
            tcaMessageStatusEntity.setThresholdSeverity(threshold.getSeverity().name());
            tcaMessageStatusEntity.setThresholdDirection(threshold.getDirection().name());
            tcaMessageStatusEntity.setThresholdValue(threshold.getThresholdValue());
        }

    }


    /**
     * Add TCA CEF Message Processor status information
     *
     * @param tcaMessageStatusEntity message entity that needs to be populated with message processor fields
     * @param processorContext processor context
     *
     */
    @SuppressWarnings("unchecked")
    private static void addMessageProcessorMessages(
            final TCAMessageStatusEntity tcaMessageStatusEntity, final TCACEFProcessorContext processorContext) {
        final List<? super MessageProcessor<? extends ProcessorContext>> messageProcessors = processorContext
                .getMessageProcessors();

        if (messageProcessors != null && !messageProcessors.isEmpty()) {
            for (Object messageProcessor : messageProcessors) {
                final MessageProcessor<TCACEFProcessorContext> tcaMessageProcessor =
                        (MessageProcessor<TCACEFProcessorContext>) messageProcessor;

                final String processingState = tcaMessageProcessor.getProcessingState().name();
                final String processingMessage = tcaMessageProcessor.getProcessingMessage().orNull();

                if (messageProcessor.getClass().equals(TCACEFJsonProcessor.class)) {
                    tcaMessageStatusEntity.setJsonProcessorStatus(processingState);
                    tcaMessageStatusEntity.setJsonProcessorMessage(processingMessage);
                }

                if (messageProcessor.getClass().equals(TCACEFPolicyDomainFilter.class)) {
                    tcaMessageStatusEntity.setDomainFilterStatus(processingState);
                    tcaMessageStatusEntity.setDomainFilterMessage(processingMessage);
                }

                if (messageProcessor.getClass().equals(TCACEFPolicyEventNameFilter.class)) {
                    tcaMessageStatusEntity.setEventNameFilterStatus(processingState);
                    tcaMessageStatusEntity.setEventNameFilterMessage(processingMessage);
                }

                if (messageProcessor.getClass().equals(TCACEFPolicyThresholdsProcessor.class)) {
                    tcaMessageStatusEntity.setThresholdCalculatorStatus(processingState);
                    tcaMessageStatusEntity.setThresholdCalculatorMessage(processingMessage);
                }

            }
        }
    }

    /**
     * Creates Row Key for TCA VES Message Status table
     *
     * Row Key = (Message Type + Decreasing Value)
     *
     * @param calculatorMessageType calculator message type
     *
     * @return row key string
     */
    private static String createKey(final TCACalculatorMessageType calculatorMessageType) {

        final List<String> keyList = new LinkedList<>();
        keyList.add(calculatorMessageType.name());
        keyList.add(PersistenceUtils.getCurrentTimeReverseSubKey());
        return Joiner.on(PersistenceUtils.ROW_KEY_DELIMITER).join(keyList);
    }

}
