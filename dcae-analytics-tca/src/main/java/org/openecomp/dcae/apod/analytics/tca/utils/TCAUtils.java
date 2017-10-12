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

package org.openecomp.dcae.apod.analytics.tca.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openecomp.dcae.apod.analytics.aai.service.AAIEnrichmentClient;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.common.exception.MessageProcessingException;
import org.openecomp.dcae.apod.analytics.common.service.processor.AbstractMessageProcessor;
import org.openecomp.dcae.apod.analytics.common.service.processor.GenericMessageChainProcessor;
import org.openecomp.dcae.apod.analytics.model.domain.cef.AlertAction;
import org.openecomp.dcae.apod.analytics.model.domain.cef.AlertType;
import org.openecomp.dcae.apod.analytics.model.domain.cef.CommonEventHeader;
import org.openecomp.dcae.apod.analytics.model.domain.cef.Criticality;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity;
import org.openecomp.dcae.apod.analytics.model.domain.cef.PerformanceCounter;
import org.openecomp.dcae.apod.analytics.model.domain.cef.ThresholdCrossingAlertFields;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ClosedLoopEventStatus;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ControlLoopSchemaType;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Direction;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.openecomp.dcae.apod.analytics.model.facade.tca.AAI;
import org.openecomp.dcae.apod.analytics.model.facade.tca.TCAVESResponse;
import org.openecomp.dcae.apod.analytics.model.util.AnalyticsModelIOUtils;
import org.openecomp.dcae.apod.analytics.model.util.AnalyticsModelJsonUtils;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFJsonProcessor;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFPolicyDomainFilter;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFPolicyEventNameFilter;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFPolicyThresholdsProcessor;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFProcessorContext;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.time.DateFormatUtils.SMTP_DATETIME_FORMAT;

/**
 * Utility Helper methods for TCA sub module only. Extends {@link AnalyticsModelJsonUtils} to get
 * pre configured Json Object Mapper understand serialization and deserialization of CEF Message
 * and TCA Policy
 *
 * @author Rajiv Singla . Creation Date: 10/24/2016.
 */
public abstract class TCAUtils extends AnalyticsModelJsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TCAUtils.class);

    /**
     * Threshold Comparator which is used to order thresholds based on their severity e.g. ( CRITICAL, MAJOR, MINOR,
     * WARNING )
     */
    private static final Comparator<Threshold> THRESHOLD_COMPARATOR = new Comparator<Threshold>() {
        @Override
        public int compare(Threshold threshold1, Threshold threshold2) {
            return threshold1.getSeverity().compareTo(threshold2.getSeverity());
        }
    };

    /**
     * {@link Function} that extracts {@link TCAPolicy#getMetricsPerEventName()} from {@link TCAPolicy}
     *
     * @return TCA Policy Metrics Per Event Name list
     */
    public static Function<TCAPolicy, List<MetricsPerEventName>> tcaPolicyMetricsExtractorFunction() {
        return new Function<TCAPolicy, List<MetricsPerEventName>>() {
            @Nullable
            @Override
            public List<MetricsPerEventName> apply(@Nonnull TCAPolicy tcaPolicy) {
                return tcaPolicy.getMetricsPerEventName();
            }
        };
    }

    /**
     * {@link Function} that extracts {@link MetricsPerEventName#getEventName()} from
     * {@link MetricsPerEventName}
     *
     * @return Event Names or a Metrics Per Event Name object
     */
    public static Function<MetricsPerEventName, String> tcaEventNameExtractorFunction() {
        return new Function<MetricsPerEventName, String>() {
            @Override
            public String apply(@Nonnull MetricsPerEventName metricsPerEventName) {
                return metricsPerEventName.getEventName();
            }
        };
    }


    /**
     * Extracts {@link TCAPolicy} Event Names
     *
     * @param tcaPolicy TCA Policy
     * @return List of event names in the TCA Policy
     */
    public static List<String> getPolicyEventNames(@Nonnull final TCAPolicy tcaPolicy) {
        final List<MetricsPerEventName> metricsPerEventNames =
                tcaPolicyMetricsExtractorFunction().apply(tcaPolicy);

        return Lists.transform(metricsPerEventNames, tcaEventNameExtractorFunction());
    }

    /**
     * A {@link Supplier} which caches {@link TCAPolicy} Event names as they are not expected to
     * change during runtime
     *
     * @param tcaPolicy TCA Policy
     * @return a Supplier that memoize the TCA Policy event names
     */
    public static Supplier<List<String>> getPolicyEventNamesSupplier(@Nonnull final TCAPolicy tcaPolicy) {
        return Suppliers.memoize(new Supplier<List<String>>() {
            @Override
            public List<String> get() {
                return getPolicyEventNames(tcaPolicy);
            }
        });
    }


    /**
     * Creates a Table to lookup thresholds of a {@link TCAPolicy} by its Event Name and Threshold Field path
     *
     * @param tcaPolicy TCA Policy
     * @return A table with Keys of event name and field path containing List of threshold as values
     */
    public static Table<String, String, List<Threshold>> getPolicyEventNameThresholdsTable(final TCAPolicy tcaPolicy) {
        final Table<String, String, List<Threshold>> domainFRTable = HashBasedTable.create();
        for (MetricsPerEventName metricsPerEventName : tcaPolicy.getMetricsPerEventName()) {
            final String eventName = metricsPerEventName.getEventName();
            final List<Threshold> thresholds = metricsPerEventName.getThresholds();
            for (Threshold threshold : thresholds) {
                final List<Threshold> existingThresholds = domainFRTable.get(eventName, threshold.getFieldPath());
                if (existingThresholds == null) {
                    final LinkedList<Threshold> newThresholdList = new LinkedList<>();
                    newThresholdList.add(threshold);
                    domainFRTable.put(eventName, threshold.getFieldPath(), newThresholdList);
                } else {
                    domainFRTable.get(eventName, threshold.getFieldPath()).add(threshold);
                }
            }
        }
        return domainFRTable;
    }


    /**
     * A {@link Supplier} which caches Policy Event Name and Threshold Field Path Thresholds lookup table
     *
     * @param tcaPolicy TCA Policy
     * @return Cached Supplier for table with Keys of event Name and field path containing thresholds as values
     */
    public static Supplier<Table<String, String, List<Threshold>>> getPolicyEventNameThresholdsTableSupplier
    (final TCAPolicy tcaPolicy) {
        return Suppliers.memoize(new Supplier<Table<String, String, List<Threshold>>>() {
            @Override
            public Table<String, String, List<Threshold>> get() {
                return getPolicyEventNameThresholdsTable(tcaPolicy);
            }
        });
    }


    /**
     * Creates a {@link GenericMessageChainProcessor} of {@link TCACEFJsonProcessor},
     * {@link TCACEFPolicyDomainFilter} and {@link TCACEFPolicyEventNameFilter}s to
     * filter out messages which does not match policy domain or event Name
     *
     * @param cefMessage CEF Message
     * @param tcaPolicy TCA Policy
     * @return Message Process Context after processing filter chain
     */
    public static TCACEFProcessorContext filterCEFMessage(@Nullable final String cefMessage,
                                                          @Nonnull final TCAPolicy tcaPolicy) {

        final TCACEFJsonProcessor jsonProcessor = new TCACEFJsonProcessor();
        final TCACEFPolicyDomainFilter domainFilter = new TCACEFPolicyDomainFilter();
        final TCACEFPolicyEventNameFilter eventNameFilter = new TCACEFPolicyEventNameFilter();
        // Create a list of message processors
        final ImmutableList<AbstractMessageProcessor<TCACEFProcessorContext>> messageProcessors =
                ImmutableList.of(jsonProcessor, domainFilter, eventNameFilter);
        final TCACEFProcessorContext processorContext = new TCACEFProcessorContext(cefMessage, tcaPolicy);
        // Create a message processors chain
        final GenericMessageChainProcessor<TCACEFProcessorContext> tcaProcessingChain =
                new GenericMessageChainProcessor<>(messageProcessors, processorContext);
        // process chain
        return tcaProcessingChain.processChain();
    }


    /**
     * Extracts json path values for given json Field Paths from using Json path notation. Assumes
     * that values extracted are always long
     *
     * @param message CEF Message
     * @param jsonFieldPaths Json Field Paths
     * @return Map containing key as json path and values as values associated with that json path
     */
    public static Map<String, List<BigDecimal>> getJsonPathValue(@Nonnull String message, @Nonnull Set<String>
            jsonFieldPaths) {

        final Map<String, List<BigDecimal>> jsonFieldPathMap = new HashMap<>();
        final DocumentContext documentContext = JsonPath.parse(message);

        for (String jsonFieldPath : jsonFieldPaths) {
            List<BigDecimal> jsonFieldValues = null;

            try {
                jsonFieldValues = documentContext.read(jsonFieldPath, new TypeRef<List<BigDecimal>>() {
                });
            } catch (Exception e) {
                final String errorMessage = String.format(
                        "Unable to convert jsonFieldPath: %s value to valid number. " +
                                "Json Path value is not in a valid number format. Incoming message: %s",
                        jsonFieldPath, message);
                throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
            }
            // If Json Field Values are not or empty
            if (jsonFieldValues != null && !jsonFieldValues.isEmpty()) {
                // Filter out all null values in the filed values list
                final List<BigDecimal> nonNullValues = Lists.newLinkedList(Iterables.filter(jsonFieldValues,
                        Predicates.<BigDecimal>notNull()));
                // If there are non null values put them in the map
                if (!nonNullValues.isEmpty()) {
                    jsonFieldPathMap.put(jsonFieldPath, nonNullValues);
                }
            }
        }

        return jsonFieldPathMap;
    }

    /**
     * Computes if any CEF Message Fields have violated any Policy Thresholds. For the same policy field path
     * it applies threshold in order of their severity and record the first threshold per message field path
     *
     * @param messageFieldValues Field Path Values extracted from CEF Message
     * @param fieldThresholds Policy Thresholds for Field Path
     * @return Optional of violated threshold for a field path
     */
    public static Optional<Threshold> thresholdCalculator(final List<BigDecimal> messageFieldValues, final
    List<Threshold>
            fieldThresholds) {
        // order thresholds by severity
        Collections.sort(fieldThresholds, THRESHOLD_COMPARATOR);
        // Now apply each threshold to field values
        for (Threshold fieldThreshold : fieldThresholds) {
            for (BigDecimal messageFieldValue : messageFieldValues) {
                final Boolean isThresholdViolated =
                        fieldThreshold.getDirection().operate(messageFieldValue, new BigDecimal(fieldThreshold
                                .getThresholdValue()));
                if (isThresholdViolated) {
                    final Threshold violatedThreshold = Threshold.copy(fieldThreshold);
                    violatedThreshold.setActualFieldValue(messageFieldValue);
                    return Optional.of(violatedThreshold);
                }
            }
        }
        return Optional.absent();
    }

    /**
     * Prioritize Threshold to be reported in case there was multiple TCA violations in a single CEF message.
     * Grabs first highest priority violated threshold
     *
     * @param violatedThresholdsMap Map containing field Path and associated violated Thresholds
     * @return First Highest priority violated threshold
     */
    public static Threshold prioritizeThresholdViolations(final Map<String, Threshold> violatedThresholdsMap) {

        final List<Threshold> violatedThresholds = newArrayList(violatedThresholdsMap.values());

        if (violatedThresholds.size() == 1) {
            return violatedThresholds.get(0);
        }
        Collections.sort(violatedThresholds, THRESHOLD_COMPARATOR);
        // Just grab the first violated threshold with highest priority
        return violatedThresholds.get(0);
    }


    /**
     * Creates {@link MetricsPerEventName} object which contains violated thresholds
     *
     * @param tcaPolicy TCA Policy
     * @param violatedThreshold Violated thresholds
     * @param eventName Event Name
     *
     * @return MetricsPerEventName object containing one highest severity violated threshold
     */
    public static MetricsPerEventName createViolatedMetrics(@Nonnull final TCAPolicy tcaPolicy,
                                                            @Nonnull final Threshold violatedThreshold,
                                                            @Nonnull final String eventName) {

        final ArrayList<MetricsPerEventName> metricsPerEventNames = newArrayList(
                Iterables.filter(tcaPolicy.getMetricsPerEventName(), new Predicate<MetricsPerEventName>() {
                    @Override
                    public boolean apply(@Nonnull MetricsPerEventName metricsPerEventName) {
                        return metricsPerEventName.getEventName().equals(eventName);
                    }
                }));
        // TCA policy must have only one metrics per event Name
        if (metricsPerEventNames.size() == 1) {
            final MetricsPerEventName violatedMetrics =
                    MetricsPerEventName.copy(metricsPerEventNames.get(0));
            violatedMetrics.setThresholds(ImmutableList.of(Threshold.copy(violatedThreshold)));
            return violatedMetrics;
        } else {
            final String errorMessage = String.format("TCA Policy must contain eventName: %s", eventName);
            throw new MessageProcessingException(errorMessage, LOG, new IllegalStateException(errorMessage));
        }
    }

    /**
     * Computes threshold violations
     *
     * @param processorContext Filtered processor Context
     * @return processor context with any threshold violations
     */
    public static TCACEFProcessorContext computeThresholdViolations(final TCACEFProcessorContext processorContext) {
        final TCACEFPolicyThresholdsProcessor policyThresholdsProcessor = new TCACEFPolicyThresholdsProcessor();
        return policyThresholdsProcessor.apply(processorContext);
    }


    /**
     * Creates TCA Alert String - Alert String is created in both {@link EventListener} or {@link TCAVESResponse}
     * formats
     *
     * @param processorContextWithViolations processor context which has TCA violations
     * @param tcaAppName tca app name
     * @param isAlertInCEFFormat determines if output alert is in CEF format
     *
     * @return TCA Alert String
     *
     * @throws JsonProcessingException If alert cannot be parsed into JSON String
     */
    public static String createTCAAlertString(final TCACEFProcessorContext processorContextWithViolations,
                                              final String tcaAppName,
                                              final Boolean isAlertInCEFFormat) throws JsonProcessingException {
        if (isAlertInCEFFormat != null && isAlertInCEFFormat) {
            final EventListener eventListenerWithViolations =
                    addThresholdViolationFields(processorContextWithViolations);
            final String alertString = writeValueAsString(eventListenerWithViolations);
            LOG.debug("Created alert in CEF Format: {}", alertString);
            return alertString;
        } else {
            final TCAVESResponse newTCAVESResponse =
                    createNewTCAVESResponse(processorContextWithViolations, tcaAppName);
            final String alertString = writeValueAsString(newTCAVESResponse);
            LOG.debug("Created alert in Non CEF Format: {}", alertString);
            return alertString;
        }
    }

    /**
     * Adds threshold violation fields to {@link EventListener}
     *
     * @param processorContextWithViolations processor context that contains violations
     * @return event listener with threshold crossing alert fields populated
     */
    public static EventListener addThresholdViolationFields(
            final TCACEFProcessorContext processorContextWithViolations) {

        final MetricsPerEventName metricsPerEventName =
                processorContextWithViolations.getMetricsPerEventName();
        // confirm violations are indeed present
        if (metricsPerEventName == null) {
            final String errorMessage = "No violations metrics. Unable to add Threshold Violation Fields";
            throw new MessageProcessingException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        }

        // get violated threshold
        final Threshold violatedThreshold = metricsPerEventName.getThresholds().get(0);
        final EventListener eventListener = processorContextWithViolations.getCEFEventListener();
        final CommonEventHeader commonEventHeader = eventListener.getEvent().getCommonEventHeader();

        // create new threshold crossing alert fields
        final ThresholdCrossingAlertFields thresholdCrossingAlertFields = new ThresholdCrossingAlertFields();
        thresholdCrossingAlertFields.setEventStartTimestamp(commonEventHeader.getStartEpochMicrosec().toString());
        thresholdCrossingAlertFields.setEventSeverity(violatedThreshold.getSeverity());
        thresholdCrossingAlertFields.setCollectionTimestamp(SMTP_DATETIME_FORMAT.format(new Date()));
        thresholdCrossingAlertFields.setAlertAction(AlertAction.SET);
        thresholdCrossingAlertFields.setAlertType(AlertType.INTERFACE_ANOMALY);
        thresholdCrossingAlertFields.setAlertDescription(violatedThreshold.getDirection().toString());
        thresholdCrossingAlertFields.setInterfaceName(commonEventHeader.getReportingEntityName());
        thresholdCrossingAlertFields.setElementType(commonEventHeader.getEventName());

        // create new performance count
        final PerformanceCounter performanceCounter = new PerformanceCounter();
        performanceCounter.setCriticality(convertSeverityToCriticality(violatedThreshold.getSeverity()));
        performanceCounter.setName(violatedThreshold.getFieldPath());
        performanceCounter.setValue(violatedThreshold.getActualFieldValue().toString());
        performanceCounter.setThresholdCrossed(violatedThreshold.getThresholdValue().toString());

        // set additional parameters for threshold crossing alert fields
        thresholdCrossingAlertFields.setAdditionalParameters(ImmutableList.of(performanceCounter));

        // add threshold crossing fields to existing event listener
        eventListener.getEvent().setThresholdCrossingAlertFields(thresholdCrossingAlertFields);

        return eventListener;
    }

    /**
     * Converts {@link EventSeverity} to {@link Criticality}
     *
     * @param eventSeverity event severity
     *
     * @return performance counter criticality
     */
    private static Criticality convertSeverityToCriticality(final EventSeverity eventSeverity) {
        switch (eventSeverity) {
            case CRITICAL:
                return Criticality.CRIT;
            case MAJOR:
                return Criticality.MAJ;
            default:
                return Criticality.UNKNOWN;
        }
    }

    /**
     * Creates {@link TCAVESResponse} object
     *
     * @param processorContext processor Context with violations
     * @param tcaAppName TCA App Name
     *
     * @return TCA VES Response Message
     */
    public static TCAVESResponse createNewTCAVESResponse(final TCACEFProcessorContext processorContext,
                                                         final String tcaAppName) {

        final MetricsPerEventName metricsPerEventName = processorContext.getMetricsPerEventName();
        // confirm violations are indeed present
        if (metricsPerEventName == null) {
            final String errorMessage = "No violations metrics. Unable to create VES Response";
            throw new MessageProcessingException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        }

        final Threshold violatedThreshold = metricsPerEventName.getThresholds().get(0);
        final EventListener eventListener = processorContext.getCEFEventListener();
        final CommonEventHeader commonEventHeader = eventListener.getEvent().getCommonEventHeader();

        final TCAVESResponse tcavesResponse = new TCAVESResponse();
        // ClosedLoopControlName included in the DCAE configuration Policy
        tcavesResponse.setClosedLoopControlName(violatedThreshold.getClosedLoopControlName());
        // version included in the DCAE configuration Policy
        tcavesResponse.setVersion(violatedThreshold.getVersion());
        // Generate a UUID for this output message
        tcavesResponse.setRequestID(UUID.randomUUID().toString());
        // commonEventHeader.startEpochMicrosec from the received VES message
        tcavesResponse.setClosedLoopAlarmStart(commonEventHeader.getStartEpochMicrosec());
        // commonEventHeader.lastEpochMicrosec from the received VES message for abated alerts
        if (violatedThreshold.getClosedLoopEventStatus() == ClosedLoopEventStatus.ABATED) {
            tcavesResponse.setClosedLoopAlarmEnd(commonEventHeader.getLastEpochMicrosec());
        }
        // Concatenate name of this DCAE instance and name for this TCA instance, separated by dot
        tcavesResponse.setClosedLoopEventClient("DCAE_INSTANCE_ID." + tcaAppName);

        final AAI aai = new AAI();
        tcavesResponse.setAai(aai);

        // VM specific settings
        if (metricsPerEventName.getControlLoopSchemaType() == ControlLoopSchemaType.VM) {
            // Hard Coded - "VM"
            tcavesResponse.setTargetType(AnalyticsConstants.TCA_VES_RESPONSE_VM_TARGET_TYPE);
            // Hard Coded - "vserver.vserver-name"
            tcavesResponse.setTarget(AnalyticsConstants.TCA_VES_RESPONSE_VM_TARGET);
            // commonEventHeader.sourceName from the received VES message
            aai.setGenericServerId(commonEventHeader.getSourceName());
        } else {
            // VNF specific settings
            // Hard Coded - "VNF"
            tcavesResponse.setTargetType(AnalyticsConstants.TCA_VES_RESPONSE_VNF_TARGET_TYPE);
            // Hard Coded - "generic-vnf.vnf-id"
            tcavesResponse.setTarget(AnalyticsConstants.TCA_VES_RESPONSE_VNF_TARGET);
            // commonEventHeader.sourceName from the received VES message
            aai.setGenericVNFId(commonEventHeader.getSourceName());
        }

        // Hard Coded - "DCAE"
        tcavesResponse.setFrom(AnalyticsConstants.TCA_VES_RESPONSE_FROM);
        // policyScope included in the DCAE configuration Policy
        tcavesResponse.setPolicyScope(metricsPerEventName.getPolicyScope());
        // policyName included in the DCAE configuration Policy
        tcavesResponse.setPolicyName(metricsPerEventName.getPolicyName());
        // policyVersion included in the DCAE configuration Policy
        tcavesResponse.setPolicyVersion(metricsPerEventName.getPolicyVersion());
        // Extracted from violated threshold
        tcavesResponse.setClosedLoopEventStatus(violatedThreshold.getClosedLoopEventStatus().name());

        return tcavesResponse;
    }


    /**
     * Determines {@link ControlLoopSchemaType} for given {@link TCAVESResponse} alert
     *
     * @param tcavesResponse alert
     *
     * @return control Loop Schema Type
     */
    public static ControlLoopSchemaType determineControlLoopSchemaType(final TCAVESResponse tcavesResponse) {
        final AAI aai = tcavesResponse.getAai();
        if (aai.getGenericServerId() != null) {
            return ControlLoopSchemaType.VM;
        } else {
            return ControlLoopSchemaType.VNF;
        }
    }

    /**
     * Determines {@link ControlLoopSchemaType} for given {@link TCAVESResponse} alert
     *
     * @param tcavesResponse {@link TCAVESResponse} TCA alert
     *
     * @return Source name
     */
    public static String determineSourceName(final TCAVESResponse tcavesResponse) {
        final AAI aai = tcavesResponse.getAai();
        if (aai.getGenericServerId() != null) {
            return aai.getGenericServerId();
        } else {
            return aai.getGenericVNFId();
        }
    }


    /**
     * Extract Domain and Event Name from processor context if present
     *
     * @param processorContext processor context
     * @return Tuple of domain and event Name
     */
    public static Pair<String, String> getDomainAndEventName(
            @Nullable final TCACEFProcessorContext processorContext) {

        String domain = null;
        String eventName = null;

        if (processorContext != null &&
                processorContext.getCEFEventListener() != null &&
                processorContext.getCEFEventListener().getEvent() != null &&
                processorContext.getCEFEventListener().getEvent().getCommonEventHeader() != null) {
            final CommonEventHeader commonEventHeader = processorContext.getCEFEventListener().getEvent()
                    .getCommonEventHeader();

            if (commonEventHeader.getDomain() != null) {
                domain = commonEventHeader.getDomain().name();
            }

            if (commonEventHeader.getEventName() != null) {
                eventName = commonEventHeader.getEventName();
            }

        }

        return new ImmutablePair<>(domain, eventName);

    }

    /**
     * Creates {@link TCAPolicy} Metrics per Event Name list
     *
     * @param eventNamesMap Map containing event Name as key and corresponding values
     *
     * @return List of {@link MetricsPerEventName}
     */
    public static List<MetricsPerEventName> createTCAPolicyMetricsPerEventNameList(
            final Map<String, Map<String, String>> eventNamesMap) {

        // create a new metrics per event Name list
        final List<MetricsPerEventName> metricsPerEventNames = new LinkedList<>();

        for (Map.Entry<String, Map<String, String>> eventNamesEntry : eventNamesMap.entrySet()) {

            // create new metrics per event Name instance
            final MetricsPerEventName newMetricsPerEventName =
                    createNewMetricsPerEventName(eventNamesEntry);
            metricsPerEventNames.add(newMetricsPerEventName);

            // determine all threshold related values
            final Map<String, String> thresholdsValuesMaps =
                    filterMapByKeyNamePrefix(eventNamesEntry.getValue(),
                            AnalyticsConstants.TCA_POLICY_THRESHOLDS_PATH_POSTFIX);

            // create a map of all threshold values
            final Map<String, Map<String, String>> thresholdsMap =
                    extractSubTree(thresholdsValuesMaps, 1, 2,
                            AnalyticsConstants.TCA_POLICY_DELIMITER);

            // add thresholds to nmetrics per event Names threshold list
            for (Map<String, String> thresholdMap : thresholdsMap.values()) {
                newMetricsPerEventName.getThresholds().add(createNewThreshold(thresholdMap));
            }

        }

        return metricsPerEventNames;
    }

    /**
     * Creates new instance of TCA Policy {@link Threshold} with values extracted from thresholdMap
     *
     * @param thresholdMap threshold map with threshold values
     *
     * @return new instance of TCA Policy Threshold
     */
    public static Threshold createNewThreshold(final Map<String, String> thresholdMap) {
        final Threshold threshold = new Threshold();
        threshold.setClosedLoopControlName(thresholdMap.get("policy.closedLoopControlName"));
        threshold.setVersion(thresholdMap.get("policy.version"));
        threshold.setFieldPath(thresholdMap.get("policy.fieldPath"));
        threshold.setDirection(Direction.valueOf(thresholdMap.get("policy.direction")));
        threshold.setSeverity(EventSeverity.valueOf(thresholdMap.get("policy.severity")));
        threshold.setThresholdValue(Long.valueOf(thresholdMap.get("policy.thresholdValue")));
        threshold.setClosedLoopEventStatus(
                ClosedLoopEventStatus.valueOf(thresholdMap.get("policy.closedLoopEventStatus")));
        return threshold;
    }

    /**
     * Create new {@link MetricsPerEventName} instance with policy Name, policy Version and policy Scope
     * extracted from given eventNamesEntry
     *
     * @param eventNamesEntry Event Names Entry
     *
     * @return new instance of MetricsPerEventName
     */
    public static MetricsPerEventName createNewMetricsPerEventName(
            final Map.Entry<String, Map<String, String>> eventNamesEntry) {
        // determine event Name
        final String eventName = eventNamesEntry.getKey();
        // determine event Name thresholds
        final Map<String, String> metricsPerEventNameThresholdsMap = eventNamesEntry.getValue();
        final MetricsPerEventName metricsPerEventName = new MetricsPerEventName();
        final List<Threshold> thresholds = new LinkedList<>();
        metricsPerEventName.setThresholds(thresholds);
        metricsPerEventName.setEventName(eventName);
        // bind policyName, policyVersion, policyScope and closedLoopControlName
        metricsPerEventName.setPolicyName(metricsPerEventNameThresholdsMap.get("policyName"));
        metricsPerEventName.setPolicyVersion(metricsPerEventNameThresholdsMap.get("policyVersion"));
        metricsPerEventName.setPolicyScope(metricsPerEventNameThresholdsMap.get("policyScope"));
        metricsPerEventName.setControlLoopSchemaType(ControlLoopSchemaType.valueOf(
                metricsPerEventNameThresholdsMap.get("controlLoopSchemaType")));
        return metricsPerEventName;
    }

    /**
     * Converts a flattened key/value map which has keys delimited by a given delimiter.
     * The start Index and end index extract the sub-key value and returns a new map containing
     * sub-keys and values.
     *
     * @param actualMap actual Map
     * @param startIndex start index
     * @param endIndex end index
     * @param delimiter delimiter
     *
     * @return Map with new sub tree map
     */
    public static Map<String, Map<String, String>> extractSubTree(
            final Map<String, String> actualMap, int startIndex, int endIndex, String delimiter) {

        final SortedMap<String, Map<String, String>> subTreeMap = new TreeMap<>();

        // iterate over actual map entries
        for (Map.Entry<String, String> actualMapEntry : actualMap.entrySet()) {
            final String actualMapKey = actualMapEntry.getKey();
            final String actualMapValue = actualMapEntry.getValue();

            // determine delimiter start and end index
            final int keyStartIndex = StringUtils.ordinalIndexOf(actualMapKey, delimiter, startIndex);
            final int keyEndIndex = StringUtils.ordinalIndexOf(actualMapKey, delimiter, endIndex);
            final int keyLength = actualMapKey.length();

            // extract sub-tree map
            if (keyStartIndex != -1 && keyEndIndex != -1 && keyEndIndex > keyStartIndex && keyLength > keyEndIndex) {
                final String thresholdKey = actualMapKey.substring(keyStartIndex + 1, keyEndIndex);
                final Map<String, String> existingThresholdMap = subTreeMap.get(thresholdKey);
                final String subMapKey = actualMapKey.substring(keyEndIndex + 1, keyLength);
                if (existingThresholdMap == null) {
                    Map<String, String> newThresholdMap = new LinkedHashMap<>();
                    newThresholdMap.put(subMapKey, actualMapValue);
                    subTreeMap.put(thresholdKey, newThresholdMap);
                } else {
                    existingThresholdMap.put(subMapKey, actualMapValue);
                }

            }
        }

        return subTreeMap;

    }


    /**
     * Provides a view of underlying map that filters out entries with keys starting with give prefix
     *
     * @param actualMap Target map that needs to be filtered
     * @param keyNamePrefix key prefix
     *
     * @return a view of actual map which only show entries which have give prefix
     */
    public static Map<String, String> filterMapByKeyNamePrefix(final Map<String, String> actualMap,
                                                               final String keyNamePrefix) {
        return Maps.filterKeys(actualMap,
                new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable String key) {
                        return key != null && key.startsWith(keyNamePrefix);
                    }
                });
    }


    /**
     * Creates Quartz Scheduler
     *
     * @param pollingIntervalMS polling interval
     * @param stdSchedulerFactory Quartz standard schedule factory instance
     * @param quartzPublisherPropertiesFileName quartz properties file name
     * @param jobDataMap job Data map
     * @param quartzJobClass Quartz Job Class
     * @param quartzJobName Quartz Job Name
     * @param quartzTriggerName Quartz Trigger name
     *
     * @param <T> An implementation of Quartz {@link Job} interface
     * @return Configured Quartz Scheduler
     *
     * @throws SchedulerException exception if unable to create to Quartz Scheduler
     */
    public static <T extends Job> Scheduler createQuartzScheduler(final Integer pollingIntervalMS,
            final StdSchedulerFactory stdSchedulerFactory, final String quartzPublisherPropertiesFileName,
            final JobDataMap jobDataMap, final Class<T> quartzJobClass, final String quartzJobName,
            final String quartzTriggerName) throws SchedulerException {

        // Initialize a new Quartz Standard scheduler
        LOG.debug("Configuring quartz scheduler for Quartz Job: {} with properties file: {}",
                quartzJobClass.getSimpleName(), quartzPublisherPropertiesFileName);
        final Properties quartzProperties = AnalyticsModelIOUtils.loadPropertiesFile(
                quartzPublisherPropertiesFileName, new Properties());
        stdSchedulerFactory.initialize(quartzProperties);
        final Scheduler scheduler = stdSchedulerFactory.getScheduler();

        // Create a new job detail
        final JobDetail jobDetail = JobBuilder.newJob(quartzJobClass).withIdentity(quartzJobName,
                AnalyticsConstants.TCA_QUARTZ_GROUP_NAME).usingJobData(jobDataMap).build();

        // Create a new scheduling builder
        final SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(pollingIntervalMS) // job will use custom polling schedule
                .repeatForever(); // repeats while worker is running

        // Create a trigger for the TCA Publisher Job
        final SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzTriggerName, AnalyticsConstants.TCA_QUARTZ_GROUP_NAME)
                .startNow() // job starts right away
                .withSchedule(simpleScheduleBuilder).build();

        scheduler.scheduleJob(jobDetail, simpleTrigger);
        LOG.info("Scheduler Initialized successfully for JobName: {}", quartzJobClass.getSimpleName());
        return scheduler;
    }


    /**
     * Does A&AI Enrichment for VM
     *
     * @param tcavesResponse Outgoing alert object
     * @param aaiEnrichmentClient A&AI Enrichment client
     * @param aaiVMEnrichmentAPIPath A&AI VM Enrichment API Path
     * @param alertString alert String
     * @param vmSourceName vm source name
     */
    public static void doAAIVMEnrichment(final TCAVESResponse tcavesResponse,
                                         final AAIEnrichmentClient aaiEnrichmentClient,
                                         final String aaiVMEnrichmentAPIPath,
                                         final String alertString,
                                         final String vmSourceName) {

        final String filterString = "vserver-name:EQUALS:" + vmSourceName;
        final ImmutableMap<String, String> queryParams = ImmutableMap.of(
                "search-node-type", "vserver", "filter", filterString);

        // fetch vm object resource Link from A&AI
        final String vmAAIResourceLinkDetails = aaiEnrichmentClient.getEnrichmentDetails(
                aaiVMEnrichmentAPIPath, queryParams, createAAIEnrichmentHeaders());
        final String vmObjectResourceLink = getVMObjectResourceLink(vmAAIResourceLinkDetails);

        if (vmObjectResourceLink == null) {
            LOG.warn("No A&AI Enrichment possible for alert message: {}.VM Object resource Link cannot be " +
                    "determined for vmSourceName: {}.", alertString, vmSourceName);
        } else {

            LOG.debug("Fetching VM A&AI Enrichment Details for VM Source Name: {}, Object resource Link: {}",
                    vmSourceName, vmObjectResourceLink);

            // fetch vm A&AI Enrichment
            final String vmEnrichmentDetails = aaiEnrichmentClient.getEnrichmentDetails(
                    vmObjectResourceLink, Collections.<String, String>emptyMap(), createAAIEnrichmentHeaders());

            // enrich AAI
            enrichAAI(tcavesResponse.getAai(), vmEnrichmentDetails, alertString,
                    AnalyticsConstants.AAI_VSERVER_KEY_PREFIX);
        }


    }


    /**
     * Does A&AI Enrichment for VNF
     *
     * @param tcavesResponse Outgoing alert object
     * @param aaiEnrichmentClient A&AI Enrichment client
     * @param aaiVNFEnrichmentAPIPath A&AI VNF Enrichment API Path
     * @param alertString alert String
     * @param vnfSourceName vnf source name
     */
    public static void doAAIVNFEnrichment(final TCAVESResponse tcavesResponse,
                                          final AAIEnrichmentClient aaiEnrichmentClient,
                                          final String aaiVNFEnrichmentAPIPath,
                                          final String alertString,
                                          final String vnfSourceName) {
        final ImmutableMap<String, String> queryParams = ImmutableMap.of("vnf-name", vnfSourceName);

        // fetch vnf A&AI Enrichment
        final String vnfEnrichmentDetails = aaiEnrichmentClient.getEnrichmentDetails(
                aaiVNFEnrichmentAPIPath, queryParams, createAAIEnrichmentHeaders());

        // enrich alert AAI
        enrichAAI(tcavesResponse.getAai(), vnfEnrichmentDetails, alertString, AnalyticsConstants.AAI_VNF_KEY_PREFIX);
    }

    /**
     * Fetches VM Object Resource Link from A&AI Resource Link Json
     *
     * @param vmAAIResourceLinkDetails VM Object Resource Link from A&AI Resource Link Json
     *
     * @return object resource link String
     */
    private static String getVMObjectResourceLink(final String vmAAIResourceLinkDetails) {
        if (StringUtils.isNotBlank(vmAAIResourceLinkDetails)) {
            try {
                final JsonNode jsonNode = ANALYTICS_MODEL_OBJECT_MAPPER.readTree(vmAAIResourceLinkDetails);
                final JsonNode resourceLinkJsonNode = jsonNode.findPath("resource-link");
                if (!resourceLinkJsonNode.isMissingNode()) {
                    return resourceLinkJsonNode.asText();
                }
            } catch (IOException e) {
                LOG.warn("Unable to determine VM Object link inside AAI Resource Link Response JSON: {}. Exception: {}",
                        vmAAIResourceLinkDetails, e);
            }
        }
        return null;
    }

    /**
     * Creates Http Headers for A&AI Enrichment client
     *
     * @return Http Headers Map for A&AI Enrichment client
     */
    private static Map<String, String> createAAIEnrichmentHeaders() {
        final Map<String, String> aaiEnrichmentHeaders = new LinkedHashMap<>();
        final String transactionId = Long.toString(new Date().getTime());
        aaiEnrichmentHeaders.put("X-FromAppId", "dcae-analytics-tca");
        aaiEnrichmentHeaders.put("X-TransactionId", transactionId);
        aaiEnrichmentHeaders.put("Accept", "application/json");
        aaiEnrichmentHeaders.put("Real-Time", "true");
        aaiEnrichmentHeaders.put("Content-Type", "application/json");
        return aaiEnrichmentHeaders;
    }


    /**
     * Populates A&AI details retrieved from A&AI Enrichment API into Alerts A&AI Object
     *
     * @param preEnrichmentAAI A&AI Alert object which needs to be populated with A&AI Enrichment Details
     * @param aaiEnrichmentDetails A&AI Enrichment API fetched JSON String
     * @param alertString Alert String
     * @param keyPrefix Key prefix that needs to be added to each fetched A&AI Enrichment record
     */
    private static void enrichAAI(final AAI preEnrichmentAAI, final String aaiEnrichmentDetails,
                                  final String alertString, final String keyPrefix) {

        if (aaiEnrichmentDetails == null) {
            LOG.warn("No A&AI Enrichment possible for AAI: {}. A&AI Enrichment details are absent." +
                    "Skipping Enrichment for alert message:{}", preEnrichmentAAI, alertString);

        } else {

            final AAI enrichmentDetailsAAI = getEnrichmentDetailsAAI(aaiEnrichmentDetails);

            if (enrichmentDetailsAAI != null) {
                final Set<Map.Entry<String, Object>> enrichedAAIEntrySet =
                        enrichmentDetailsAAI.getDynamicProperties().entrySet();
                final Map<String, Object> preEnrichmentAAIDynamicProperties = preEnrichmentAAI.getDynamicProperties();

                // populate A&AI Enrichment details and add prefix to key
                for (Map.Entry<String, Object> enrichedAAIEntry : enrichedAAIEntrySet) {
                    preEnrichmentAAIDynamicProperties.put(keyPrefix + enrichedAAIEntry.getKey(),
                            enrichedAAIEntry.getValue());
                }

                LOG.debug("A&AI Enrichment was completed successfully for alert message: {}. Enriched AAI: {}",
                        alertString, preEnrichmentAAI);
            } else {
                LOG.warn("No A&AI Enrichment possible for AAI: {}. Invalid A&AI Response: {}." +
                                "Skipping Enrichment for alert message: {}",
                        preEnrichmentAAI, aaiEnrichmentDetails, alertString);
            }
        }

    }

    /**
     * Creates a new A&AI object with only top level A&AI Enrichment details
     *
     * @param aaiEnrichmentDetails A&AI Enrichment details
     *
     * @return new A&AI with only top level A&AI Enrichment details
     */
    private static AAI getEnrichmentDetailsAAI(final String aaiEnrichmentDetails) {
        try {
            final JsonNode rootNode = ANALYTICS_MODEL_OBJECT_MAPPER.readTree(aaiEnrichmentDetails);
            final Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            while (fieldsIterator.hasNext()) {
                final Map.Entry<String, JsonNode> fieldEntry = fieldsIterator.next();
                final JsonNode jsonNode = fieldEntry.getValue();
                // remove all arrays, objects from A&AI Enrichment Json
                if (jsonNode.isPojo() || jsonNode.isObject() || jsonNode.isArray()) {
                    fieldsIterator.remove();
                }
            }
            return ANALYTICS_MODEL_OBJECT_MAPPER.treeToValue(rootNode, AAI.class);
        } catch (IOException e) {
            LOG.error("Failed to Parse AAI Enrichment Details from JSON: {}, Exception: {}.", aaiEnrichmentDetails, e);
        }
        return null;
    }

}
