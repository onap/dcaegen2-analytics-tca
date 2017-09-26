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

package org.openecomp.dcae.apod.analytics.cdap.common.domain.tca;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Simple POJO emitted by threshold calculator
 *
 * @author Rajiv Singla . Creation Date: 9/11/2017.
 */
public class ThresholdCalculatorOutput implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String cefMessage;
    protected String tcaPolicy;
    protected String violatedMetricsPerEventName;
    protected String alertMessage;

    public ThresholdCalculatorOutput() {
        // no arg constructor
    }

    public ThresholdCalculatorOutput(String cefMessage, String tcaPolicy,
                                     String violatedMetricsPerEventName, String alertMessage) {
        this.cefMessage = cefMessage;
        this.tcaPolicy = tcaPolicy;
        this.violatedMetricsPerEventName = violatedMetricsPerEventName;
        this.alertMessage = alertMessage;
    }

    public String getCefMessage() {
        return cefMessage;
    }

    public void setCefMessage(String cefMessage) {
        this.cefMessage = cefMessage;
    }

    public String getTcaPolicy() {
        return tcaPolicy;
    }

    public void setTcaPolicy(String tcaPolicy) {
        this.tcaPolicy = tcaPolicy;
    }

    public String getViolatedMetricsPerEventName() {
        return violatedMetricsPerEventName;
    }

    public void setViolatedMetricsPerEventName(String violatedMetricsPerEventName) {
        this.violatedMetricsPerEventName = violatedMetricsPerEventName;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("cefMessage", cefMessage)
                .add("tcaPolicy", tcaPolicy)
                .add("violatedMetricsPerEventName", violatedMetricsPerEventName)
                .add("alertMessage", alertMessage)
                .toString();
    }
}
