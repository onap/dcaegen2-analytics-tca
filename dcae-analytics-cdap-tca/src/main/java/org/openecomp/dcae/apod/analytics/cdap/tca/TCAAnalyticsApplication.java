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

package org.openecomp.dcae.apod.analytics.cdap.tca;

import co.cask.cdap.api.app.AbstractApplication;
import co.cask.cdap.api.data.stream.Stream;
import co.cask.cdap.api.dataset.DatasetProperties;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca.TCAAlertsAbatementPersister;
import org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca.TCAMessageStatusPersister;
import org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca.TCAVESAlertsPersister;
import org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils;
import org.openecomp.dcae.apod.analytics.cdap.tca.flow.TCAVESCollectorFlow;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppConfig;
import org.openecomp.dcae.apod.analytics.cdap.tca.validator.TCAAppConfigValidator;
import org.openecomp.dcae.apod.analytics.cdap.tca.worker.TCADMaaPMockSubscriberWorker;
import org.openecomp.dcae.apod.analytics.cdap.tca.worker.TCADMaaPPublisherWorker;
import org.openecomp.dcae.apod.analytics.cdap.tca.worker.TCADMaaPSubscriberWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rajiv Singla . Creation Date: 10/21/2016.
 */
public class TCAAnalyticsApplication extends AbstractApplication<TCAAppConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(TCAAnalyticsApplication.class);

    @Override
    public void configure() {


        // ========= Application configuration Setup ============== //
        final TCAAppConfig tcaAppConfig = getConfig();

        LOG.info("Configuring TCA Application with startup application configuration: {}", tcaAppConfig);

        // Validate application configuration
        ValidationUtils.validateSettings(tcaAppConfig, new TCAAppConfigValidator());

        // App Setup
        setName(tcaAppConfig.getAppName());
        setDescription(tcaAppConfig.getAppDescription());

        // ========== Streams Setup ============== //
        // Create DMaaP MR Subscriber CDAP output stream
        final String tcaSubscriberOutputStreamName = tcaAppConfig.getTcaSubscriberOutputStreamName();
        LOG.info("Creating TCA VES Output Stream: {}", tcaSubscriberOutputStreamName);
        final Stream subscriberOutputStream = new Stream(tcaSubscriberOutputStreamName,
                CDAPComponentsConstants.TCA_FIXED_SUBSCRIBER_OUTPUT_DESCRIPTION_STREAM);
        addStream(subscriberOutputStream);


        // ============ Datasets Setup ======== //
        // Create TCA Message Status Table
        final String tcaVESMessageStatusTableName = tcaAppConfig.getTcaVESMessageStatusTableName();
        final Integer messageStatusTableTTLSeconds = tcaAppConfig.getTcaVESMessageStatusTableTTLSeconds();
        LOG.info("Creating TCA Message Status Table: {} with TTL: {}",
                tcaVESMessageStatusTableName, messageStatusTableTTLSeconds);
        final DatasetProperties messageStatusTableProperties =
                TCAMessageStatusPersister.getDatasetProperties(messageStatusTableTTLSeconds);
        createDataset(tcaVESMessageStatusTableName, ObjectMappedTable.class, messageStatusTableProperties);


        // Create TCA Alerts Abatement Table
        final String tcaAlertsAbatementTableName = tcaAppConfig.getTcaAlertsAbatementTableName();
        final Integer tcaAlertsAbatementTableTTLSeconds = tcaAppConfig.getTcaAlertsAbatementTableTTLSeconds();
        LOG.info("Creating Alerts Abatement Table: {} with TTL: {}",
                tcaAlertsAbatementTableName, tcaAlertsAbatementTableTTLSeconds);
        final DatasetProperties alertsAbatementTableProperties =
                TCAAlertsAbatementPersister.getDatasetProperties(tcaAlertsAbatementTableTTLSeconds);
        createDataset(tcaAlertsAbatementTableName, ObjectMappedTable.class, alertsAbatementTableProperties);

        // Create TCA VES Alerts Table
        final String tcaVESAlertsTableName = tcaAppConfig.getTcaVESAlertsTableName();
        final Integer alertsTableTTLSeconds = tcaAppConfig.getTcaVESAlertsTableTTLSeconds();
        LOG.info("Creating TCA Alerts Table: {} with TTL: {}",
                tcaVESAlertsTableName, alertsTableTTLSeconds);
        final DatasetProperties alertTableProperties =
                TCAVESAlertsPersister.getDatasetProperties(alertsTableTTLSeconds);
        createDataset(tcaVESAlertsTableName, ObjectMappedTable.class, alertTableProperties);

        // =========== Flow Setup ============= //
        addFlow(new TCAVESCollectorFlow(tcaAppConfig));

        // ========== Workers Setup =========== //
        LOG.info("Creating TCA DMaaP Subscriber Worker");
        addWorker(new TCADMaaPSubscriberWorker(tcaAppConfig.getTcaSubscriberOutputStreamName()));
        LOG.info("Creating TCA DMaaP Publisher Worker");
        addWorker(new TCADMaaPPublisherWorker(tcaAppConfig.getTcaVESAlertsTableName()));
        // TODO: Remove this before going to production
        addWorker(new TCADMaaPMockSubscriberWorker(tcaAppConfig.getTcaSubscriberOutputStreamName()));
    }


}
