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

package org.openecomp.dcae.apod.analytics.cdap.tca.utils;

import co.cask.cdap.api.RuntimeContext;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.openecomp.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertEntity;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAPolicyPreferences;
import org.openecomp.dcae.apod.analytics.cdap.tca.validator.TCAPolicyPreferencesValidator;
import org.openecomp.dcae.apod.analytics.cdap.tca.validator.TCAPreferencesValidator;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.model.config.tca.DMAAPInfo;
import org.openecomp.dcae.apod.analytics.model.config.tca.TCAControllerAppConfig;
import org.openecomp.dcae.apod.analytics.model.config.tca.TCAHandleIn;
import org.openecomp.dcae.apod.analytics.model.config.tca.TCAHandleOut;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.collect.Lists.newArrayList;
import static org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils.validateSettings;
import static org.openecomp.dcae.apod.analytics.common.AnalyticsConstants.TCA_POLICY_METRICS_PER_FUNCTIONAL_ROLE_PATH;

/**
 * Utility Helper methods for CDAP TCA sub module.
 *
 * <p>
 * @author Rajiv Singla . Creation Date: 10/24/2016.
 */
public abstract class CDAPTCAUtils extends TCAUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CDAPTCAUtils.class);

    /**
     * Function that extracts alert message string from {@link TCAVESAlertEntity}
     */
    public static final Function<TCAVESAlertEntity, String> MAP_ALERT_ENTITY_TO_ALERT_STRING_FUNCTION =
            new Function<TCAVESAlertEntity, String>() {
                @Override
                public String apply(TCAVESAlertEntity alertEntity) {
                    return alertEntity == null ? null : alertEntity.getAlertMessage();
                }
            };


    /**
     * Parses and validates Runtime Arguments to {@link TCAAppPreferences} object
     *
     * @param runtimeContext Runtime Context
     *
     * @return validated runtime arguments as {@link TCAAppPreferences} object
     */
    public static TCAAppPreferences getValidatedTCAAppPreferences(final RuntimeContext runtimeContext) {
        // Parse runtime arguments
        final Map<String, String> runtimeArguments = runtimeContext.getRuntimeArguments();
        final TCAAppPreferences tcaAppPreferences =
                ANALYTICS_MODEL_OBJECT_MAPPER.convertValue(runtimeArguments, TCAAppPreferences.class);

        final String appConfigString = runtimeContext.getApplicationSpecification().getConfiguration();

        // populate DMaaP Information from App Config String
        populateDMaaPInfoFromAppConfiguration(appConfigString, tcaAppPreferences);

        // Validate runtime arguments
        validateSettings(tcaAppPreferences, new TCAPreferencesValidator());

        return tcaAppPreferences;
    }

    /**
     * Populated App Preferences DMaaP Information from Application Config String
     *
     * @param appConfigString  CDAP Application config String
     * @param tcaAppPreferences TCA App Preferences
     */
    private static void populateDMaaPInfoFromAppConfiguration(final String appConfigString,
                                                              final TCAAppPreferences tcaAppPreferences) {

        if (null != tcaAppPreferences.getSubscriberHostName() || null != tcaAppPreferences.getPublisherHostName()) {
            LOG.info("DMaaP Information is set from runtime preferences. Skipping getting DMaaP info from App Config");
        }

        LOG.info("Fetching DMaaP information from App Configuration String: {}", appConfigString);

        try {
            final TCAControllerAppConfig tcaControllerAppConfig =
                    readValue(appConfigString, TCAControllerAppConfig.class);

            // Parse Subscriber DMaaP information from App Config String
            if (tcaControllerAppConfig.getStreamsSubscribes() != null &&
                    tcaControllerAppConfig.getStreamsSubscribes().getTcaHandleIn() != null &&
                    tcaControllerAppConfig.getStreamsSubscribes().getTcaHandleIn().getDmaapInfo() != null) {

                final DMAAPInfo subscriberDmaapInfo =
                        tcaControllerAppConfig.getStreamsSubscribes().getTcaHandleIn().getDmaapInfo();
                LOG.debug("App Config Subscriber Host URL: {}", subscriberDmaapInfo.getTopicUrl());
                final URL subscriberUrl = parseURL(subscriberDmaapInfo.getTopicUrl());
                tcaAppPreferences.setSubscriberProtocol(subscriberUrl.getProtocol());
                tcaAppPreferences.setSubscriberHostName(subscriberUrl.getHost());
                final int subscriberUrlPort = subscriberUrl.getPort() != -1 ?
                        subscriberUrl.getPort() : getDefaultDMaaPPort(subscriberUrl.getProtocol());
                tcaAppPreferences.setSubscriberHostPort(subscriberUrlPort);
                tcaAppPreferences.setSubscriberTopicName(subscriberUrl.getPath().substring(8));

                final TCAHandleIn tcaHandleIn = tcaControllerAppConfig.getStreamsSubscribes().getTcaHandleIn();
                tcaAppPreferences.setSubscriberUserName(tcaHandleIn.getAafUserName());
                tcaAppPreferences.setSubscriberUserPassword(tcaHandleIn.getAafPassword());
            } else {
                LOG.warn("Unable to populate Subscriber DMaaP Information from App Config String: {}", appConfigString);
            }


            // Parse Publisher DMaaP information from App Config String
            if (tcaControllerAppConfig.getStreamsPublishes() != null &&
                    tcaControllerAppConfig.getStreamsPublishes().getTcaHandleOut() != null &&
                    tcaControllerAppConfig.getStreamsPublishes().getTcaHandleOut().getDmaapInfo() != null) {

                final DMAAPInfo publisherDmaapInfo =
                        tcaControllerAppConfig.getStreamsPublishes().getTcaHandleOut().getDmaapInfo();
                LOG.debug("App Config Publisher Host URL: {}", publisherDmaapInfo.getTopicUrl());
                final URL publisherUrl = parseURL(publisherDmaapInfo.getTopicUrl());
                tcaAppPreferences.setPublisherProtocol(publisherUrl.getProtocol());
                tcaAppPreferences.setPublisherHostName(publisherUrl.getHost());
                final int publisherUrlPort = publisherUrl.getPort() != -1 ?
                        publisherUrl.getPort() : getDefaultDMaaPPort(publisherUrl.getProtocol());
                tcaAppPreferences.setPublisherHostPort(publisherUrlPort);
                tcaAppPreferences.setPublisherTopicName(publisherUrl.getPath().substring(8));

                final TCAHandleOut tcaHandleOut = tcaControllerAppConfig.getStreamsPublishes().getTcaHandleOut();
                tcaAppPreferences.setPublisherUserName(tcaHandleOut.getAafUserName());
                tcaAppPreferences.setPublisherUserPassword(tcaHandleOut.getAafPassword());
            } else {
                LOG.warn("Unable to populate Publisher DMaaP Information from App Config String: {}", appConfigString);
            }


        } catch (IOException e) {
            throw new CDAPSettingsException(
                    "Unable to parse App Config to Json Object.Invalid App Config String: " + appConfigString, LOG, e);
        }
    }

    /**
     * Parses provided DMaaP MR URL string to {@link URL} object
     *
     * @param urlString url string
     *
     * @return url object
     */
    private static URL parseURL(final String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            final String errorMessage = String.format("Invalid URL format: %s", urlString);
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }
    }

    /**
     * Sets up default DMaaP Port if not provided with DMaaP URL
     *
     * @param protocol protocol e.g. http or https
     *
     * @return default DMaaP MR port number
     */
    private static int getDefaultDMaaPPort(final String protocol) {
        if ("http".equals(protocol)) {
            return 3904;
        } else if ("https".equals(protocol)) {
            return 3905;
        } else {
            return 80;
        }
    }


    /**
     * Extracts alert message strings from {@link TCAVESAlertEntity}
     *
     * @param alertEntities collection of alert entities
     *
     * @return List of alert message strings
     */
    public static List<String> extractAlertFromAlertEntities(final Collection<TCAVESAlertEntity> alertEntities) {
        return Lists.transform(newArrayList(alertEntities), MAP_ALERT_ENTITY_TO_ALERT_STRING_FUNCTION);
    }


    /**
     * Converts Runtime Arguments to {@link TCAPolicyPreferences} object
     *
     * @param runtimeContext CDAP Runtime Arguments
     *
     * @return TCA Policy Preferences
     */
    public static TCAPolicy getValidatedTCAPolicyPreferences(final RuntimeContext runtimeContext) {

        final Map<String, String> runtimeArguments = runtimeContext.getRuntimeArguments();
        final TreeMap<String, String> sortedRuntimeArguments = new TreeMap<>(runtimeArguments);

        LOG.debug("Printing all Received Runtime Arguments:");
        for (Map.Entry<String, String> runtimeArgsEntry : sortedRuntimeArguments.entrySet()) {
            LOG.debug("{}:{}", runtimeArgsEntry.getKey(), runtimeArgsEntry.getValue());
        }

        TCAPolicyPreferences tcaPolicyPreferences = new TCAPolicyPreferences();

        final String tcaPolicyJsonString = sortedRuntimeArguments.get(AnalyticsConstants.TCA_POLICY_JSON_KEY);

        if (StringUtils.isNotBlank(tcaPolicyJsonString)) {

            LOG.info("TcaPolicy will be set from input argument name: {} as JSON String with value: {}",
                    AnalyticsConstants.TCA_POLICY_JSON_KEY, tcaPolicyJsonString);

            // initialize unquotedTCAPolicy
            String unquotedTCAPolicy = tcaPolicyJsonString.trim();

            //remove starting and ending quote from passed tca policy Json string if present
            if (tcaPolicyJsonString.trim().startsWith(AnalyticsConstants.TCA_POLICY_STRING_DELIMITER) &&
                    tcaPolicyJsonString.trim().endsWith(AnalyticsConstants.TCA_POLICY_STRING_DELIMITER)) {
                unquotedTCAPolicy = tcaPolicyJsonString.trim().substring(1, tcaPolicyJsonString.trim().length() - 1);
            }

            try {
                tcaPolicyPreferences = readValue(unquotedTCAPolicy , TCAPolicyPreferences.class);
            } catch (IOException e) {
                throw new CDAPSettingsException(
                        "Input tca_policy string format is not correct. tca_policy: " + tcaPolicyJsonString, LOG, e);
            }

        } else {  // classical controller is being used.  Validate preferences as received from classical controller

            LOG.info("TcaPolicy is being parsed as key value pair from classical controller");

            // extract TCA Policy Domain from Runtime Arguments
            final String policyDomain = sortedRuntimeArguments.get(AnalyticsConstants.TCA_POLICY_DOMAIN_PATH);

            // create new TCA Policy object
            tcaPolicyPreferences.setDomain(policyDomain);

            // filter out other non relevant fields which are not related to tca policy
            final Map<String, String> tcaPolicyMap = filterMapByKeyNamePrefix(sortedRuntimeArguments,
                    TCA_POLICY_METRICS_PER_FUNCTIONAL_ROLE_PATH);

            // determine functional Roles
            final Map<String, Map<String, String>> functionalRolesMap =
                    extractSubTree(tcaPolicyMap, 2, 3, AnalyticsConstants.TCA_POLICY_DELIMITER);

            // create metrics per functional role list
            tcaPolicyPreferences.setMetricsPerEventName(
                    createTCAPolicyMetricsPerEventNameList(functionalRolesMap));

        }

        // validate tca Policy Preferences
        validateSettings(tcaPolicyPreferences, new TCAPolicyPreferencesValidator());

        LOG.info("Printing Effective TCA Policy: {}", tcaPolicyPreferences);

        return tcaPolicyPreferences;
    }
}
