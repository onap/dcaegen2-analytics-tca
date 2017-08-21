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
import org.openecomp.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertEntity;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAPolicyPreferences;
import org.openecomp.dcae.apod.analytics.cdap.tca.validator.TCAPolicyPreferencesValidator;
import org.openecomp.dcae.apod.analytics.cdap.tca.validator.TCAPreferencesValidator;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

        // Validate runtime arguments
        validateSettings(tcaAppPreferences, new TCAPreferencesValidator());

        return tcaAppPreferences;
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

        final String tcaPolicy = sortedRuntimeArguments.get(AnalyticsConstants.TCA_POLICY_JSON_KEY);

        if (tcaPolicy != null) {

            LOG.debug(" tcaPolicy is being read from JSON String");

            // initialize unquotedTCAPolicy
            String unquotedTCAPolicy = tcaPolicy;

            //remove starting and ending quote from tcaPolicy
            if (tcaPolicy.trim().startsWith(AnalyticsConstants.TCA_POLICY_STRING_DELIMITER) && tcaPolicy.trim().endsWith
                    (AnalyticsConstants.TCA_POLICY_STRING_DELIMITER)) {
                unquotedTCAPolicy = tcaPolicy.trim().substring(1, tcaPolicy.trim().length() - 1);
            }

            try {
                tcaPolicyPreferences = readValue(unquotedTCAPolicy , TCAPolicyPreferences.class);
            } catch (IOException e) {
                throw new CDAPSettingsException("Invalid tca policy format", LOG, e);
            }

        } else {  // old controller is being used.  Validate preferences as received from old controller

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
            tcaPolicyPreferences.setMetricsPerFunctionalRole(
                    createTCAPolicyMetricsPerFunctionalRoleList(functionalRolesMap));

        }

        // validate tca Policy Preferences
        validateSettings(tcaPolicyPreferences, new TCAPolicyPreferencesValidator());

        LOG.info("Printing Effective TCA Policy: {}", tcaPolicyPreferences);

        return tcaPolicyPreferences;
    }
}
