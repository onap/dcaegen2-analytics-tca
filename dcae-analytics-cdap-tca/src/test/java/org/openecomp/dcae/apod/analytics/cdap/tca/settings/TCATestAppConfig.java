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

package org.openecomp.dcae.apod.analytics.cdap.tca.settings;

/**
 * TCA Test App Config is used for testing purposes only
 *
 * @author Rajiv Singla . Creation Date: 11/3/2016.
 */
public class TCATestAppConfig extends TCAAppConfig {

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public void setTcaSubscriberOutputStreamName(String tcaSubscriberOutputStreamName) {
        this.tcaSubscriberOutputStreamName = tcaSubscriberOutputStreamName;
    }

    public void setThresholdCalculatorFlowletInstances(Integer thresholdCalculatorFlowletInstances) {
        this.thresholdCalculatorFlowletInstances = thresholdCalculatorFlowletInstances;
    }

    public void setTcaVESMessageStatusTableName(String tcaVESMessageStatusTableName) {
        this.tcaVESMessageStatusTableName = tcaVESMessageStatusTableName;
    }

    public void setTcaVESMessageStatusTableTTLSeconds(Integer tcaVESMessageStatusTableTTLSeconds) {
        this.tcaVESMessageStatusTableTTLSeconds = tcaVESMessageStatusTableTTLSeconds;
    }

    public void setTcaVESAlertsTableName(String tcaVESAlertsTableName) {
        this.tcaVESAlertsTableName = tcaVESAlertsTableName;
    }

    public void setTcaVESAlertsTableTTLSeconds(Integer tcaVESAlertsTableTTLSeconds) {
        this.tcaVESAlertsTableTTLSeconds = tcaVESAlertsTableTTLSeconds;
    }

}
