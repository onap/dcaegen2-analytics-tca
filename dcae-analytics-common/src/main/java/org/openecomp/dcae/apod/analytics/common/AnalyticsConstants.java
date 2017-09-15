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

package org.openecomp.dcae.apod.analytics.common;

/**
 * Contains static variable for all DCAE Components.
 *
 * @author Rajiv Singla . Creation Date: 10/21/2016.
 */
public abstract class AnalyticsConstants {

    // ===============  Common Constants for all DCAE Analytics Modules ==================== //


    // ===============  DMaaP Constants for all DCAE Analytics Modules ==================== //

    // DMaaP Config Constants
    public static final Integer DEFAULT_PORT_NUMBER = 80; // default port number
    public static final String DEFAULT_USER_NAME = null; // default to no username
    public static final String DEFAULT_USER_PASSWORD = null; // defaults to no userPassword
    public static final String DEFAULT_PROTOCOL = "https"; // defaults to using https protocol
    public static final String DEFAULT_CONTENT_TYPE = "application/json";  // defaults to json content type

    public static final String DMAAP_URI_PATH_PREFIX = "/events/";
    public static final String DMAAP_GROUP_PREFIX = "OpenDCAE-";

    // ================== DMaaP MR Constants ============================== //
    // Publisher Constants
    public static final int DEFAULT_PUBLISHER_MAX_BATCH_SIZE = 1; // disable batching by default
    public static final int DEFAULT_PUBLISHER_MAX_RECOVERY_QUEUE_SIZE = 100000; // default recovery messages size
    public static final int PUBLISHER_MAX_FLUSH_RETRIES_ON_CLOSE = 5; // number of retries when flushing messages
    public static final int PUBLISHER_DELAY_MS_ON_RETRIES_ON_CLOSE = 5000; // delay in retrying for flushing messages
    // Subscriber Constants
    public static final int DEFAULT_SUBSCRIBER_TIMEOUT_MS = -1;
    public static final int DEFAULT_SUBSCRIBER_MESSAGE_LIMIT = -1;
    public static final String DEFAULT_SUBSCRIBER_GROUP_PREFIX = DMAAP_GROUP_PREFIX + "DMaaPSub-";
    public static final String SUBSCRIBER_TIMEOUT_QUERY_PARAM_NAME = "timeout";
    public static final String SUBSCRIBER_MSG_LIMIT_QUERY_PARAM_NAME = "limit";


    // ================== TCA Constants ============================== //

    // Default subscriber polling interval
    public static final Integer TCA_DEFAULT_SUBSCRIBER_POLLING_INTERVAL_MS = 30000;

    // Default publisher polling interval
    public static final Integer TCA_DEFAULT_PUBLISHER_POLLING_INTERVAL_MS = 30000;

    // Default publisher max batch queue size determines the minimum number of messages that need to be published in
    // batch mode
    public static final Integer TCA_DEFAULT_PUBLISHER_MAX_BATCH_QUEUE_SIZE = 10;

    // Default publisher max recovery queue size determines max number of messages can be cached in memory
    // in case publisher is not responding
    public static final Integer TCA_DEFAULT_PUBLISHER_MAX_RECOVERY_QUEUE_SIZE = 100000;

    // Default interval during which TCA DMaaP Worker checks if scheduler is shut down
    public static final Integer TCA_DEFAULT_WORKER_SHUTDOWN_CHECK_INTERVAL_MS = 5000;

    // ***** TCA Quartz Scheduler Settings ******//

    public static final String TCA_QUARTZ_SUBSCRIBER_PROPERTIES_FILE_NAME = "quartz-subscriber.properties";

    public static final String TCA_QUARTZ_PUBLISHER_PROPERTIES_FILE_NAME = "quartz-publisher.properties";

    // TCA Quartz Group Settings
    public static final String TCA_QUARTZ_GROUP_NAME = "TCAQuartzGroup";
    // TCA Quartz Trigger Settings
    public static final String TCA_DMAAP_SUBSCRIBER_QUARTZ_TRIGGER_NAME = "TCADMaaPSubscriberTrigger";
    public static final String TCA_DMAAP_PUBLISHER_QUARTZ_TRIGGER_NAME = "TCADMaaPPublisherTrigger";

    // TCA Quartz DMaaP Subscriber Job Settings
    public static final String TCA_DMAAP_SUBSCRIBER_QUARTZ_JOB_NAME = "TCADMaaPSubscriberJob";
    public static final String TCA_DMAAP_PUBLISHER_QUARTZ_JOB_NAME = "TCADMaaPPublisherJob";

    // TCA Quartz Publisher and Subscriber Job Parameters
    // Common Job parameters for both Publisher and Subscriber
    public static final String WORKER_CONTEXT_VARIABLE_NAME = "WORKER_CONTEXT";
    public static final String DMAAP_METRICS_VARIABLE_NAME = "DMAAP_METRICS";
    // TCA Quartz DMaaP Subscriber Job Parameter Settings
    public static final String CDAP_STREAM_VARIABLE_NAME = "CDAP_STREAM_NAME";
    public static final String DMAAP_SUBSCRIBER_VARIABLE_NAME = "DMAAP_SUBSCRIBER";
    // TCA Quartz DMaaP Publisher Job Parameter Settings
    public static final String CDAP_ALERTS_TABLE_VARIABLE_NAME = "CDAP_TCA_ALERTS_TABLE_NAME";
    public static final String DMAAP_PUBLISHER_VARIABLE_NAME = "DMAAP_PUBLISHER";

    // TCA VES Response Constants
    // VNF Constants
    public static final String TCA_VES_RESPONSE_VNF_TARGET_TYPE = "VNF";
    public static final String TCA_VES_RESPONSE_VNF_TARGET = "generic-vnf.vnf-id";
    // VM Constants
    public static final String TCA_VES_RESPONSE_VM_TARGET_TYPE = "VM";
    public static final String TCA_VES_RESPONSE_VM_TARGET = "vserver.vserver-name";
    // VNF & VM - Common Constants
    public static final String TCA_VES_RESPONSE_FROM = "DCAE";

    // TCA VES Message Router Partition Key
    public static final String TCA_VES_MESSAGE_ROUTER_PARTITION_KEY = "VESMessageHash";

    /**
     * Default Number of instances for Threshold violation calculator flowlet
     */
    public static final Integer TCA_DEFAULT_THRESHOLD_CALCULATOR_FLOWLET_INSTANCES = 2;

    /**
     * Default TTL for TCA VES Message status table which contain status of all messages processed by TCA
     */
    public static final Integer TCA_DEFAULT_VES_MESSAGE_STATUS_TTL_TABLE = 60 * 60 * 24 * 10; // 10 Days

    /**
     * Default TTL for TCA VES Alerts table which contains alerts that can be send to downstream systems
     */
    public static final Integer TCA_DEFAULT_VES_ALERTS_TTL_TABLE = 60 * 60 * 24 * 30; // 30 Days


    /**
     * Default TTL for TCA Alerts abatement table which contains information to send out abated alerts
     */
    public static final Integer TCA_DEFAULT_ALERTS_ABATEMENT_TTL_TABLE = 60 * 60 * 24 * 30; // 30 Days


    // TCA Policy Runtime Argument Paths
    public static final String TCA_POLICY_DELIMITER = ".";
    public static final String TCA_POLICY_DOMAIN_PATH = "domain";
    public static final String TCA_POLICY_METRICS_PER_FUNCTIONAL_ROLE_PATH = "configuration.metricsPerEventName";
    public static final String TCA_POLICY_THRESHOLDS_PATH_POSTFIX = "thresholds";

    public static final String TCA_POLICY_JSON_KEY = "tca_policy";
    public static final String TCA_POLICY_STRING_DELIMITER = "\"";

    private AnalyticsConstants() {

    }

}
