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

import com.google.common.base.Objects;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.settings.CDAPBaseAppConfig;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;


/**
 * Contains CDAP App Config Settings for TCA Application
 *
 * @author Rajiv Singla . Creation Date: 11/2/2016.
 */
public class TCAAppConfig extends CDAPBaseAppConfig {


    private static final long serialVersionUID = 1L;

    protected String tcaSubscriberOutputStreamName;
    protected Integer thresholdCalculatorFlowletInstances;

    protected String tcaVESMessageStatusTableName;
    protected Integer tcaVESMessageStatusTableTTLSeconds;
    protected String tcaVESAlertsTableName;
    protected Integer tcaVESAlertsTableTTLSeconds;


    public TCAAppConfig() {
        appName = CDAPComponentsConstants.TCA_DEFAULT_NAME_APP;
        appDescription = CDAPComponentsConstants.TCA_DEFAULT_DESCRIPTION_APP;
        tcaSubscriberOutputStreamName = CDAPComponentsConstants.TCA_DEFAULT_SUBSCRIBER_OUTPUT_NAME_STREAM;
        thresholdCalculatorFlowletInstances = AnalyticsConstants.TCA_DEFAULT_THRESHOLD_CALCULATOR_FLOWLET_INSTANCES;
        tcaVESMessageStatusTableName = CDAPComponentsConstants.TCA_DEFAULT_VES_MESSAGE_STATUS_NAME_TABLE;
        tcaVESMessageStatusTableTTLSeconds = AnalyticsConstants.TCA_DEFAULT_VES_MESSAGE_STATUS_TTL_TABLE;
        tcaVESAlertsTableName = CDAPComponentsConstants.TCA_DEFAULT_VES_ALERTS_NAME_TABLE;
        tcaVESAlertsTableTTLSeconds = AnalyticsConstants.TCA_DEFAULT_VES_ALERTS_TTL_TABLE;
    }

    public String getTcaSubscriberOutputStreamName() {
        return tcaSubscriberOutputStreamName;
    }

    public String getTcaVESMessageStatusTableName() {
        return tcaVESMessageStatusTableName;
    }

    public Integer getTcaVESMessageStatusTableTTLSeconds() {
        return tcaVESMessageStatusTableTTLSeconds;
    }

    public String getTcaVESAlertsTableName() {
        return tcaVESAlertsTableName;
    }

    public Integer getTcaVESAlertsTableTTLSeconds() {
        return tcaVESAlertsTableTTLSeconds;
    }

    public Integer getThresholdCalculatorFlowletInstances() {
        return thresholdCalculatorFlowletInstances;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("appName", appName)
                .add("appDescription", appDescription)
                .add("tcaSubscriberOutputStreamName", tcaSubscriberOutputStreamName)
                .add("thresholdCalculatorFlowletInstances", thresholdCalculatorFlowletInstances)
                .add("tcaVESMessageStatusTableName", tcaVESMessageStatusTableName)
                .add("tcaVESMessageStatusTableTTLSeconds", tcaVESMessageStatusTableTTLSeconds)
                .add("tcaVESAlertsTableName", tcaVESAlertsTableName)
                .add("tcaVESAlertsTableTTLSeconds", tcaVESAlertsTableTTLSeconds)
                .toString();
    }
}
