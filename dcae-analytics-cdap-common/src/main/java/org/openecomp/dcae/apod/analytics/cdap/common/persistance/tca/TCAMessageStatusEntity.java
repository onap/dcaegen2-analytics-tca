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

package org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Direction;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFJsonProcessor;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFPolicyDomainFilter;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFPolicyEventNameFilter;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFPolicyThresholdsProcessor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * TCA Message Status is an Entity which is used to persist TCA VES Message status information in Message Status Table
 *
 * @author Rajiv Singla . Creation Date: 11/16/2016.
 */
public class TCAMessageStatusEntity implements Writable, Serializable {

    private static final long serialVersionUID = 1L;

    private long creationTS;
    private int instanceId;
    private String messageType;
    private String vesMessage;
    private String domain;
    private String eventName;
    private String thresholdPath;
    private String thresholdSeverity;
    private String thresholdDirection;
    private Long thresholdValue;
    private String jsonProcessorStatus;
    private String jsonProcessorMessage;
    private String domainFilterStatus;
    private String domainFilterMessage;
    private String eventNameFilterStatus;
    private String eventNameFilterMessage;
    private String thresholdCalculatorStatus;
    private String thresholdCalculatorMessage;
    private String alertMessage;

    /**
     * No Arg constructor required for Jackson Json Serialization / Deserialization
     */
    public TCAMessageStatusEntity() {
        // no argument constructor required for json serialization / deserialization
    }

    /**
     * Create new Instance of {@link TCAMessageStatusEntity}
     *
     * @param creationTS creation Timestamp
     * @param instanceId CDAP flowlet instance ID
     * @param messageType {@link TCACalculatorMessageType}
     * @param vesMessage incoming VES message from collector
     * @param domain VES message domain if present
     * @param eventName VES message functional role if present
     */
    public TCAMessageStatusEntity(final long creationTS, final int instanceId, final String messageType,
                                  final String vesMessage, final String domain, final String eventName) {
        this(creationTS, instanceId, messageType, vesMessage, domain, eventName, null, null, null, null,
                null, null, null, null, null, null, null, null, null);
    }


    /**
     * Create new Instance of {@link TCAMessageStatusEntity}
     *
     * @param creationTS creation Timestamp
     * @param instanceId CDAP flowlet instance ID
     * @param messageType {@link TCACalculatorMessageType}
     * @param vesMessage incoming VES message from collector
     * @param domain VES message domain if present
     * @param eventName VES message event name if present
     * @param thresholdPath Violated threshold path
     * @param thresholdSeverity Violated threshold Severity if any
     * @param thresholdDirection Violated threshold Direction if any
     * @param thresholdValue Violated threshold value if any
     * @param jsonProcessorStatus {@link TCACEFJsonProcessor} status
     * @param jsonProcessorMessage {@link TCACEFJsonProcessor} message
     * @param domainFilterStatus {@link TCACEFPolicyDomainFilter} status
     * @param domainFilterMessage {@link TCACEFPolicyDomainFilter} message
     * @param eventNameFilterStatus {@link TCACEFPolicyEventNameFilter} status
     * @param eventNameFilterMessage {@link TCACEFPolicyEventNameFilter} message
     * @param thresholdCalculatorStatus {@link TCACEFPolicyThresholdsProcessor} status
     * @param thresholdCalculatorMessage {@link TCACEFPolicyThresholdsProcessor} message
     * @param alertMessage alert message that will be sent out in case of threshold violation
     */
    public TCAMessageStatusEntity(long creationTS, int instanceId, String messageType, String vesMessage,
                                  String domain, String eventName,
                                  String thresholdPath, String thresholdSeverity, String thresholdDirection,
                                  Long thresholdValue,
                                  String jsonProcessorStatus, String jsonProcessorMessage,
                                  String domainFilterStatus, String domainFilterMessage,
                                  String eventNameFilterStatus, String eventNameFilterMessage,
                                  String thresholdCalculatorStatus, String thresholdCalculatorMessage,
                                  String alertMessage) {
        this.creationTS = creationTS;
        this.instanceId = instanceId;
        this.messageType = messageType;
        this.vesMessage = vesMessage;
        this.domain = domain;
        this.eventName = eventName;
        this.thresholdPath = thresholdPath;
        this.thresholdSeverity = thresholdSeverity;
        this.thresholdDirection = thresholdDirection;
        this.thresholdValue = thresholdValue;
        this.jsonProcessorStatus = jsonProcessorStatus;
        this.jsonProcessorMessage = jsonProcessorMessage;
        this.domainFilterStatus = domainFilterStatus;
        this.domainFilterMessage = domainFilterMessage;
        this.eventNameFilterStatus = eventNameFilterStatus;
        this.eventNameFilterMessage = eventNameFilterMessage;
        this.thresholdCalculatorStatus = thresholdCalculatorStatus;
        this.thresholdCalculatorMessage = thresholdCalculatorMessage;
        this.alertMessage = alertMessage;
    }

    /**
     * Provides Creation Timestamp
     *
     * @return creation timestamp long value
     */
    public long getCreationTS() {
        return creationTS;
    }

    /**
     * Sets Creations Timestamp
     *
     * @param creationTS creation timestamp long value
     */
    public void setCreationTS(long creationTS) {
        this.creationTS = creationTS;
    }


    /**
     * Provides CDAP Flowlet instance ID
     *
     * @return cdap flowlet instance ID
     */
    public int getInstanceId() {
        return instanceId;
    }

    /**
     * Sets CDAP Flowlet instance ID
     *
     * @param instanceId flowlet instance ID
     */
    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * Provides Message Calculator Type {@link TCACalculatorMessageType}
     *
     * @return calculator message type
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets Calculator message Type {@link TCACalculatorMessageType}
     *
     * @param messageType calculator message type
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * Provides incoming VES Message
     *
     * @return ves message
     */
    public String getVesMessage() {
        return vesMessage;
    }

    /**
     * Set new value for VES message
     *
     * @param vesMessage ves message
     */
    public void setVesMessage(String vesMessage) {
        this.vesMessage = vesMessage;
    }

    /**
     * Provides VES message Domain
     *
     * @return ves message domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets VES Message Domain
     *
     * @param domain ves message domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Provides VES Message Event Name
     *
     * @return ves message Event Name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets VES Message Functional Role
     *
     * @param eventName ves message Functional Role
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Violated Threshold Path as extracted from {@link TCAPolicy}
     *
     * @return violated threshold path
     */
    public String getThresholdPath() {
        return thresholdPath;
    }

    /**
     * Sets value for Violated Threshold Path
     *
     * @param thresholdPath violated threshold path
     */
    public void setThresholdPath(String thresholdPath) {
        this.thresholdPath = thresholdPath;
    }

    /**
     * Violated threshold Event Severity
     *
     * @return event severity
     */
    public String getThresholdSeverity() {
        return thresholdSeverity;
    }

    /**
     * Violated Threshold Severity
     *
     * @param thresholdSeverity violated threshold severity
     */
    public void setThresholdSeverity(String thresholdSeverity) {
        this.thresholdSeverity = thresholdSeverity;
    }

    /**
     * Violated Threshold {@link Direction}
     *
     * @return violated threshold Direction
     */
    public String getThresholdDirection() {
        return thresholdDirection;
    }

    /**
     * Sets Violated Threshold Direction
     *
     * @param thresholdDirection violated threshold direction
     */
    public void setThresholdDirection(String thresholdDirection) {
        this.thresholdDirection = thresholdDirection;
    }

    /**
     * Provides Violated Threshold Value
     *
     * @return violated Threshold value
     */
    public Long getThresholdValue() {
        return thresholdValue;
    }

    /**
     * Sets Violated Threshold Value
     *
     * @param thresholdValue violated threshold value
     */
    public void setThresholdValue(Long thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    /**
     * Provides {@link TCACEFJsonProcessor} status
     *
     * @return json processor status
     */
    public String getJsonProcessorStatus() {
        return jsonProcessorStatus;
    }

    /**
     * Sets Json Processor status
     *
     * @param jsonProcessorStatus json processor status
     */
    public void setJsonProcessorStatus(String jsonProcessorStatus) {
        this.jsonProcessorStatus = jsonProcessorStatus;
    }

    /**
     * Provides {@link TCACEFJsonProcessor} message
     *
     * @return json processor message
     */
    public String getJsonProcessorMessage() {
        return jsonProcessorMessage;
    }

    /**
     * Sets Json Processor Message
     *
     * @param jsonProcessorMessage json processor message
     */
    public void setJsonProcessorMessage(String jsonProcessorMessage) {
        this.jsonProcessorMessage = jsonProcessorMessage;
    }

    /**
     * Provides {@link TCACEFPolicyDomainFilter} status
     *
     * @return domain filter status
     */
    public String getDomainFilterStatus() {
        return domainFilterStatus;
    }

    /**
     * Sets Domain Filter status
     *
     * @param domainFilterStatus domain filter status
     */
    public void setDomainFilterStatus(String domainFilterStatus) {
        this.domainFilterStatus = domainFilterStatus;
    }

    /**
     * Provides {@link TCACEFPolicyDomainFilter} message
     *
     * @return domain filter message
     */
    public String getDomainFilterMessage() {
        return domainFilterMessage;
    }

    /**
     * Sets Domain filter message
     *
     * @param domainFilterMessage domain filter message
     */
    public void setDomainFilterMessage(String domainFilterMessage) {
        this.domainFilterMessage = domainFilterMessage;
    }

    public String getEventNameFilterStatus() {
        return eventNameFilterStatus;
    }

    /**
     * Provides {@link TCACEFPolicyEventNameFilter} status
     *
     * @param eventNameFilterStatus functional Role filter status
     */
    public void setEventNameFilterStatus(String eventNameFilterStatus) {
        this.eventNameFilterStatus = eventNameFilterStatus;
    }

    /**
     * Provides {@link TCACEFPolicyEventNameFilter} message
     *
     * @return functional role filter message
     */
    public String getEventNameFilterMessage() {
        return eventNameFilterMessage;
    }

    /**
     * Sets Functional Role filter message
     *
     * @param eventNameFilterMessage functional role filter message
     */
    public void setEventNameFilterMessage(String eventNameFilterMessage) {
        this.eventNameFilterMessage = eventNameFilterMessage;
    }

    /**
     * Provides {@link TCACEFPolicyThresholdsProcessor} status
     *
     * @return threshold processor status
     */
    public String getThresholdCalculatorStatus() {
        return thresholdCalculatorStatus;
    }

    /**
     * Sets threshold calculator status
     *
     * @param thresholdCalculatorStatus threshold calculator status
     */
    public void setThresholdCalculatorStatus(String thresholdCalculatorStatus) {
        this.thresholdCalculatorStatus = thresholdCalculatorStatus;
    }

    /**
     * Provides {@link TCACEFPolicyThresholdsProcessor} message
     *
     * @return threshold processor message
     */
    public String getThresholdCalculatorMessage() {
        return thresholdCalculatorMessage;
    }

    /**
     * Sets Threshold Calculator Processor Message
     *
     * @param thresholdCalculatorMessage threshold calculator message
     */
    public void setThresholdCalculatorMessage(String thresholdCalculatorMessage) {
        this.thresholdCalculatorMessage = thresholdCalculatorMessage;
    }

    /**
     * Provides generated alert message
     *
     * @return generated alert message
     */
    public String getAlertMessage() {
        return alertMessage;
    }

    /**
     * Sets alert message
     *
     * @param alertMessage alert message
     */
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    /**
     * Write entity to Table
     *
     * @param dataOutput data output
     *
     * @throws IOException io exception
     */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        WritableUtils.writeVLong(dataOutput, creationTS);
        WritableUtils.writeVInt(dataOutput, instanceId);
        WritableUtils.writeString(dataOutput, messageType);
        WritableUtils.writeString(dataOutput, vesMessage);

        WritableUtils.writeString(dataOutput, domain);
        WritableUtils.writeString(dataOutput, eventName);

        WritableUtils.writeString(dataOutput, thresholdPath);
        WritableUtils.writeString(dataOutput, thresholdSeverity);
        WritableUtils.writeString(dataOutput, thresholdDirection);
        WritableUtils.writeVLong(dataOutput, thresholdValue);

        WritableUtils.writeString(dataOutput, jsonProcessorStatus);
        WritableUtils.writeString(dataOutput, jsonProcessorMessage);
        WritableUtils.writeString(dataOutput, domainFilterStatus);
        WritableUtils.writeString(dataOutput, domainFilterMessage);
        WritableUtils.writeString(dataOutput, eventNameFilterStatus);
        WritableUtils.writeString(dataOutput, eventNameFilterMessage);
        WritableUtils.writeString(dataOutput, thresholdCalculatorStatus);
        WritableUtils.writeString(dataOutput, thresholdCalculatorMessage);

        WritableUtils.writeString(dataOutput, alertMessage);

    }

    /**
     * Read entity from table
     *
     * @param dataInput data input
     * @throws IOException io exception
     */
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        creationTS = WritableUtils.readVLong(dataInput);
        instanceId = WritableUtils.readVInt(dataInput);
        messageType = WritableUtils.readString(dataInput);
        vesMessage = WritableUtils.readString(dataInput);

        domain = WritableUtils.readString(dataInput);
        eventName = WritableUtils.readString(dataInput);

        thresholdPath = WritableUtils.readString(dataInput);
        thresholdSeverity = WritableUtils.readString(dataInput);
        thresholdDirection = WritableUtils.readString(dataInput);
        thresholdValue = WritableUtils.readVLong(dataInput);

        jsonProcessorStatus = WritableUtils.readString(dataInput);
        jsonProcessorMessage = WritableUtils.readString(dataInput);
        domainFilterStatus = WritableUtils.readString(dataInput);
        domainFilterMessage = WritableUtils.readString(dataInput);
        eventNameFilterStatus = WritableUtils.readString(dataInput);
        eventNameFilterMessage = WritableUtils.readString(dataInput);
        thresholdCalculatorStatus = WritableUtils.readString(dataInput);
        thresholdCalculatorMessage = WritableUtils.readString(dataInput);

        alertMessage = WritableUtils.readString(dataInput);

    }

}
