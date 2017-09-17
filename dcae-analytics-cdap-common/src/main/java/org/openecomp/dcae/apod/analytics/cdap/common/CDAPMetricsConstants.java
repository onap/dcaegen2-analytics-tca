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
 * Contains all metrics names used for DCAE CDAP modules
 *
 * <p>
 *     Format should be (ModuleName)_(Description of metrics)_METRIC e.g. TCA_WORKER_FAILED_ATTEMPTS_METRIC
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 10/25/2016.
 */
public abstract class CDAPMetricsConstants {

    /**
     * Metric captures count of all responses received from DMaaP MR Subscriber Topic
     */
    public static final String DMAAP_MR_SUBSCRIBER_ALL_RESPONSES_COUNT_METRIC =
        "dmaap.subscriber.fetch.all_responses";

    /**
     * Metric captures count of responses from DMaaP MR Subscriber Topic which does not have 200 HTTP Response code.
     * This can be due to DMaaP topic being down or any internal server errors etc.
     */
    public static final String DMAAP_MR_SUBSCRIBER_UNSUCCESSFUL_RESPONSES_METRIC =
        "dmaap.subscriber.fetch.unsuccessful";

    /**
     * Metric that counts the number of successful (200 HTTP Response Code) calls to DMaaP which did not had empty
     * messages
     */
    public static final String DMAAP_MR_SUBSCRIBER_RESPONSES_WITH_NO_MESSAGES_METRIC =
        "dmaap.subscriber.fetch.no_message";

    /**
     * Metric to count total number of message processed by DMaaP MR subscriber
     */
    public static final String DMAAP_MR_SUBSCRIBER_TOTAL_MESSAGES_PROCESSED_METRIC = "dmaap.subscriber.message.count";


    /**
     * Metric to record time taken in ms by subscriber in its most recent call to fetch DMaaP MR messages
     */
    public static final String DMAAP_MR_SUBSCRIBER_RESPONSE_TIME_MS_METRIC = "dmaap.subscriber.fetch.response_time";

    /**
     * Metric captures the count of number of times DMaaP MR Subscriber was unable to write to DMaaP Stream due
     * some CDAP error while writing to stream. This should ideally never happen assuming we have enough space
     * on CDAP machine and CDAP process is functioning normally
     */
    public static final String TCA_SUBSCRIBER_FAILURE_TO_WRITE_TO_STREAM_METRIC = "tca.subscriber.stream.writing.error";


    /**
     * Metric captures number of VES messages that are not applicable as per TCA Policy
     */
    public static final String TCA_VES_INAPPLICABLE_MESSAGES_METRIC = "tca.ves.calculator.inapplicable";

    /**
     * Metric captures number of VES messages that are applicable as per TCA Policy but don't violate any thresholds
     */
    public static final String TCA_VES_COMPLIANT_MESSAGES_METRIC = "tca.ves.calculator.compliant";

    /**
     * Metrics captures number of VES messages that are applicable as per TCA Policy and does violate thresholds and
     * will likely cause an alert
     */
    public static final String TCA_VES_NON_COMPLIANT_MESSAGES_METRIC = "tca.ves.calculator.non_compliant";

    /**
     * Metric that counts the number of publisher look ups in alerts table which resulted in 0 new alerts
     */
    public static final String TCA_PUBLISHER_NO_NEW_ALERTS_LOOKUP_METRIC = "tca.publisher.lookup.no_message";

    /**
     * Metric that counts the number of new alerts found by the publisher in alerts table
     */
    public static final String TCA_PUBLISHER_NEW_ALERTS_METRIC = "tca.publisher.lookup.new_messages";

    /**
     * Metric that counts the number of alerts deleted by publisher in alerts table
     */
    public static final String TCA_PUBLISHER_DELETED_ALERTS_METRIC = "tca.publisher.deleted.alerts";

    /**
     * Metric that counts the number of publisher calls to DMaaP which resulted in successful response code
     */
    public static final String TCA_PUBLISHER_SUCCESSFUL_DMAAP_RESPONSE_METRIC = "tca.publisher.publish.successful";

    /**
     * Metric that counts the number of publisher calls to DMaaP which resulted in unsuccessful response code
     */
    public static final String TCA_PUBLISHER_UNSUCCESSFUL_DMAAP_RESPONSE_METRIC = "tca.publisher.publish.unsuccessful";

    private CDAPMetricsConstants() {

    }
}
