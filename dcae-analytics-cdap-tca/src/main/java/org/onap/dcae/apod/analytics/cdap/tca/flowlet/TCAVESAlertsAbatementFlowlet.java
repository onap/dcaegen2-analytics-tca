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

package org.onap.dcae.apod.analytics.cdap.tca.flowlet;

import co.cask.cdap.api.annotation.Output;
import co.cask.cdap.api.annotation.ProcessInput;
import co.cask.cdap.api.annotation.Property;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.flow.flowlet.AbstractFlowlet;
import co.cask.cdap.api.flow.flowlet.FlowletContext;
import co.cask.cdap.api.flow.flowlet.OutputEmitter;
import org.apache.commons.lang3.StringUtils;
import org.onap.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.onap.dcae.apod.analytics.cdap.common.domain.tca.ThresholdCalculatorOutput;
import org.onap.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCAAlertsAbatementEntity;
import org.onap.dcae.apod.analytics.cdap.common.persistance.tca.TCAAlertsAbatementPersister;
import org.onap.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.onap.dcae.apod.analytics.cdap.tca.utils.CDAPTCAUtils;
import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.onap.dcae.apod.analytics.model.domain.cef.EventListener;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.ClosedLoopEventStatus;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.onap.dcae.apod.analytics.model.facade.tca.TCAVESResponse;
import org.onap.dcae.apod.analytics.tca.utils.TCAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Flowlet responsible to sending out abatement alerts
 *
 * @author Rajiv Singla . Creation Date: 9/11/2017.
 */
public class TCAVESAlertsAbatementFlowlet extends AbstractFlowlet {

    private static final Logger LOG = LoggerFactory.getLogger(TCAVESAlertsAbatementFlowlet.class);

    @Property
    private final String tcaAlertsAbatementTableName;

    private Set<HostAndPort> redisHostAndPorts = null;

    @Output(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_ABATEMENT_NAME_OUTPUT)
    protected OutputEmitter<String> alertsAbatementOutputEmitter;

    private ObjectMappedTable<TCAAlertsAbatementEntity> tcaAlertsAbatementTable;

    public TCAVESAlertsAbatementFlowlet(final String tcaAlertsAbatementTableName) {
        this.tcaAlertsAbatementTableName = tcaAlertsAbatementTableName;
    }

    @Override
    public void configure() {
        setName(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_ABATEMENT_NAME_FLOWLET);
        setDescription(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_ABATEMENT_DESCRIPTION_FLOWLET);
    }

    @Override
    public void initialize(FlowletContext flowletContext) throws Exception {
        super.initialize(flowletContext);
        tcaAlertsAbatementTable = getContext().getDataset(tcaAlertsAbatementTableName);
        // Parse runtime arguments
        final TCAAppPreferences tcaAppPreferences = CDAPTCAUtils.getValidatedTCAAppPreferences(flowletContext);
        if(tcaAppPreferences.getEnableRedisCaching()){
            final String redisHosts = tcaAppPreferences.getRedisHosts();
            LOG.info("Redis Distributed Caching is enabled for abated alerts with Redis Hosts: {}", redisHosts);
            redisHostAndPorts = getRedisHostsAndPorts(redisHosts);
            checkRedisConnection(redisHostAndPorts);
        } else {
            LOG.info("Redis Distributed caching is disabled for abated alerts");
        }
    }

    @ProcessInput(CDAPComponentsConstants.TCA_FIXED_VES_TCA_CALCULATOR_NAME_OUTPUT)
    public void determineAbatementAlerts(final ThresholdCalculatorOutput thresholdCalculatorOutput) throws IOException {

        final String cefMessage = thresholdCalculatorOutput.getCefMessage();
        final String alertMessageString = thresholdCalculatorOutput.getAlertMessage();
        final String violatedMetricsPerEventNameString = thresholdCalculatorOutput.getViolatedMetricsPerEventName();

        // alerts must have violated metrics per event name present
        if (StringUtils.isBlank(violatedMetricsPerEventNameString)) {
            final String errorMessage = String.format(
                    "No violated metricsPerEventName found for VES Message: %s." +
                            "Ignored alert message: %s", cefMessage, alertMessageString);
            throw new CDAPSettingsException(errorMessage, LOG, new IllegalStateException(errorMessage));
        }

        final MetricsPerEventName violatedMetricsPerEventName =
                TCAUtils.readValue(violatedMetricsPerEventNameString, MetricsPerEventName.class);
        final EventListener eventListener = TCAUtils.readValue(cefMessage, EventListener.class);
        final TCAVESResponse tcavesResponse = TCAUtils.readValue(alertMessageString, TCAVESResponse.class);
        final Threshold violatedThreshold = violatedMetricsPerEventName.getThresholds().get(0);
        final ClosedLoopEventStatus closedLoopEventStatus = violatedThreshold.getClosedLoopEventStatus();

        switch (closedLoopEventStatus) {

            case ONSET:

                LOG.debug("Saving information for ONSET event for cefMessage: {}", cefMessage);
                TCAAlertsAbatementPersister.persist(eventListener, violatedMetricsPerEventName, tcavesResponse,
                        null, tcaAlertsAbatementTable, redisHostAndPorts);
                LOG.debug("Emitting ONSET alert: {}", alertMessageString);
                alertsAbatementOutputEmitter.emit(alertMessageString);
                break;

            case ABATED:

                LOG.debug("Looking up previous sent alert for abated threshold: {}", violatedThreshold);
                final TCAAlertsAbatementEntity previousAlertsAbatementEntry =
                        TCAAlertsAbatementPersister.lookUpByKey(eventListener, violatedMetricsPerEventName,
                                tcaAlertsAbatementTable, redisHostAndPorts);

                if (previousAlertsAbatementEntry != null) {

                    LOG.debug("Found previous AlertsAbatementEntity: {}", previousAlertsAbatementEntry);

                    final String abatementSentTS = previousAlertsAbatementEntry.getAbatementSentTS();
                    if (abatementSentTS != null) {
                        LOG.debug("Abatement alert was already sent at timestamp: {}. " +
                                "Skip resending this abatement alert again", abatementSentTS);
                    } else {

                        final long newAbatementSentTS = new Date().getTime();
                        LOG.debug(
                                "No abatement alert was sent before." +
                                        "Sending abatement alert:{} for the first time at:{}",
                                alertMessageString, newAbatementSentTS);

                        // save new Abatement alert sent timestamp in table
                        TCAAlertsAbatementPersister.persist(eventListener, violatedMetricsPerEventName, tcavesResponse,
                                Long.toString(newAbatementSentTS), tcaAlertsAbatementTable, redisHostAndPorts);

                        // Set request id to be same as previous ONSET event request ID
                        tcavesResponse.setRequestID(previousAlertsAbatementEntry.getRequestId());
                        final String abatedAlertString = TCAUtils.writeValueAsString(tcavesResponse);

                        LOG.info("Emitting ABATED alert: {}", abatedAlertString);
                        alertsAbatementOutputEmitter.emit(abatedAlertString);

                    }

                } else {
                    LOG.info("No previous ONSET alert was found for this ABATED alert: {}.Skip sending abated alert.",
                            alertMessageString);
                }

                break;

            default:

                final String errorMessage = String.format(
                        "Unexpected ClosedLoopEventStatus: %s. Only ONSET and ABATED are supported." +
                                "Ignoring alert: %s", closedLoopEventStatus, alertMessageString);
                throw new CDAPSettingsException(errorMessage, LOG, new IllegalStateException(errorMessage));

        }


    }

    private static Set<HostAndPort> getRedisHostsAndPorts(final String redisHosts) {
        final LinkedHashSet<HostAndPort> hostAndPorts = new LinkedHashSet<>();
        final String[] redisHostsString = redisHosts.split(",");
        for (String redisHostString : redisHostsString) {
            hostAndPorts.add(HostAndPort.parseString(redisHostString.trim()));
        }
        return hostAndPorts;
    }

    private static void checkRedisConnection(final Set<HostAndPort> redisHostAndPorts) {
        LOG.info("Checking Redis Connection for Redis Hosts: {}", redisHostAndPorts);
        try (final JedisCluster jedisCluster = new JedisCluster(redisHostAndPorts)) {
            final Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
            jedisCluster.get("testKey");
            LOG.info("Confirmed redis cluster Nodes: {}", clusterNodes.keySet());
        } catch (JedisConnectionException | IOException e) {
            LOG.error("Unable to make Redis connection for given redisHosts: {}", redisHostAndPorts);
            throw new DCAEAnalyticsRuntimeException("No Redis Connection", LOG, e);
        }
    }

}
