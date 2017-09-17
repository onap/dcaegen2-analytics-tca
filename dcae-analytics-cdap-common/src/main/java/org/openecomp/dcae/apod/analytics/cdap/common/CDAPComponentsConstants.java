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

package org.openecomp.dcae.apod.analytics.cdap.common;

/**
 *  Contains static constant variable names and values of all DCAE CDAP Components for
 *  e.g. app names, app descriptions, streams, datasets, flows, flowlets, workers, outputs etc.
 *
 *  <p>
 *      A strict naming convention must be followed for variable names for CDAP Components names for proper
 *      identification of CDAP Component variable purpose and function. A custom configuration settings can be
 *      generated for app
 *      deployment purposes based on variable naming conventions.
 *
 *      The variable names should have 4 parts separated by an underscore:
 *          <ul>
 *              <li>Name of the DCAE sub module (e.g. TCA) to which variable is applicable</li>
 *              <li>Information about variable name:
 *                  <ul>
 *                      <li>FIXED - if variable value is fixed and cannot be changed</li>
 *                      <li>DEFAULT - if variable name is default name and can be changed by cdap settings file
 *                                    when application is created</li>
 *                  </ul>
 *              </li>
 *              <li>Actual Descriptive name about the CDAP component (may contain underscores) </li>
 *              <li>CDAP component type e.g STREAM, DATASET, APP, FLOW, FLOWLET, OUTPUT</li>
 *          </ul>
 *
 *  <p>e.g TCA_DEFAULT_DMAAP_INPUT_STREAM</p>
 *
 *  <p><strong>RegEx Format (DCAE MODULE NAME)_(FIXED|DEFAULT)_(VARIABLE NAME)_(CDAP COMPONENT TYPE)</strong></p>
 *
 * @author Rajiv Singla . Creation Date: 10/24/2016.
 */
public abstract class CDAPComponentsConstants {

    // ===============  Common Module Constants ==================== //

    /**
     * Default DCAE App Name. It should be overridden by sub modules
     */
    public static final String COMMON_DEFAULT_DCAE_CDAP_NAME_APP = "DCAE_ANALYTICS_GENERIC_APP";

    /**
     * Default DCAE App Description. It should be overridden by sub modules
     */
    public static final String COMMON_DEFAULT_DCAE_CDAP_DESCRIPTION_APP = "DCAE ANALYTICS GENERIC APP DESCRIPTION";

    // ===============  TCA Module Constants ==================== //

    /**
     * Default TCA application name if application name is not provided from startup configuration
     */
    public static final String TCA_DEFAULT_NAME_APP = "dcae-tca";

    /**
     * Default TCA application description if not provided from startup configuration
     */
    public static final String TCA_DEFAULT_DESCRIPTION_APP = "DCAE Analytics Threshold Crossing Alert Application";

    /**
     * Default TCA DMaaP Subscriber output stream name if not provided from startup configuration
     */
    public static final String TCA_DEFAULT_SUBSCRIBER_OUTPUT_NAME_STREAM = "TCASubscriberOutputStream";


    /**
     * Fixed TCA DMaaP Subscriber output stream description
     */
    public static final String TCA_FIXED_SUBSCRIBER_OUTPUT_DESCRIPTION_STREAM =
        "Stream which contains all message from VES Collector DMaaP MR topic";


    /**
     * Fixed Name of TCA DMaaP Subscriber Worker - which will be fetching DMaaP Messages posting them to CDAP stream
     */
    public static final String TCA_FIXED_DMAAP_SUBSCRIBER_WORKER = "TCADMaaPMRSubscriberWorker";

    /**
     * Fixed Description of TCA DMaaP Subscriber Worker
     */
    public static final String TCA_FIXED_DMAAP_SUBSCRIBER_DESCRIPTION_WORKER =
        "Fetches messages from DMaaP MR Topic at frequent intervals and writes them to a CDAP stream";

    /**
     * Fixed Name of TCA DMaaP Publisher Worker - which will be publishing messages to DMaaP MR
     */
    public static final String TCA_FIXED_DMAAP_PUBLISHER_WORKER = "TCADMaaPMRPublisherWorker";

    /**
     * Fixed Description of TCA DMaaP Publisher Worker
     */
    public static final String TCA_FIXED_DMAAP_PUBLISHER_DESCRIPTION_WORKER =
        "Polls TCA Alerts Table at frequent intervals for new alerts and publishes them to DMaaP MR Topic";

    /**
     * Fixed name for TCA VES Collector Messages Processing Flow
     */
    public static final String TCA_FIXED_VES_COLLECTOR_NAME_FLOW = "TCAVESCollectorFlow";


    /**
     * Fixed description for TCA VES Collector Messages Processing Flow
     */
    public static final String TCA_FIXED_VES_COLLECTOR_DESCRIPTION_FLOW = "Flow performs TCA on VES Collector Messages";


    /**
     * Fixed Name for TCA VES Message Router Flowlet
     */
    public static final String TCA_FIXED_VES_MESSAGE_ROUTER_NAME_FLOWLET = "TCAVESMessageRouterFlowlet";

    /**
     * Fixed Description for TCA VES Message Router Flowlet
     */
    public static final String TCA_FIXED_VES_MESSAGE_ROUTER_DESCRIPTION_FLOWLET =
        "Routes message received from TCA VES Collector to TCA Threshold Calculator Flowlet instances";

    /**
     * Fixed TCA VES Message Router Flowlet Output
     */
    public static final String TCA_FIXED_VES_MESSAGE_ROUTER_OUTPUT = "TCAVESMessageRouterFlowlet";

    /**
     * Fixed Name for TCA VES Message Policy Violated Threshold Calculator Flowlet
     */
    public static final String TCA_FIXED_VES_THRESHOLD_VIOLATION_CALCULATOR_NAME_FLOWLET =
        "TCAVESThresholdViolationCalculatorFlowlet";

    /**
     * Fixed Description for TCA VES Message Policy Violated Threshold Calculator Flowlet
     */
    public static final String TCA_FIXED_VES_THRESHOLD_VIOLATION_CALCULATOR_DESCRIPTION_FLOWLET =
        "Applies TCA Policy Thresholds to VES Message and determined if any message violated TCA Policy thresholds";

    /**
     * Fixed Name for TCA VES Message Policy Violated Threshold Calculator Output
     */
    public static final String TCA_FIXED_VES_TCA_CALCULATOR_NAME_OUTPUT = "TCAThresholdViolationCalculatorOutput";


    /**
     * Fixed Name for TCA VES Alerts Sink Flowlet
     */
    public static final String TCA_FIXED_VES_ALERTS_SINK_NAME_FLOWLET = "TCAVESAlertsSinkFlowlet";

    /**
     * Fixed Description for TCA VES Alerts Sink Flowlet
     */
    public static final String TCA_FIXED_VES_ALERTS_SINK_DESCRIPTION_FLOWLET =
        "Saves messages which violated TCA Policy in a data set";


    /**
     * Default Name for TCA VES Message status table which contain status of all messages processed by TCA
     */
    public static final String TCA_DEFAULT_VES_MESSAGE_STATUS_NAME_TABLE = "TCAVESMessageStatusTable";


    /**
     * Fixed Description for TCA VES Message status table which contain status of all messages processed by TCA
     */
    public static final String TCA_FIXED_VES_MESSAGE_STATUS_DESCRIPTION_TABLE =
        "Store processing information about all incoming TCA VES Messages";

    /**
     * Default Name for TCA VES Alerts table which contains alerts that can be send to downstream systems
     */
    public static final String TCA_DEFAULT_VES_ALERTS_NAME_TABLE = "TCAVESAlertsTable";

    /**
     * Fixed Description for TCA VES Alerts table which contains alerts that can be send to downstream systems
     */
    public static final String TCA_DEFAULT_VES_ALERTS_DESCRIPTION_TABLE =
        "Stores alert messages that need to be DMaaP";


    private CDAPComponentsConstants() {
    }
}
