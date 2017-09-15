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

package org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca;

import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.api.data.schema.UnsupportedTypeException;
import co.cask.cdap.api.dataset.DatasetProperties;
import co.cask.cdap.api.dataset.lib.IndexedTable;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.dataset.lib.ObjectMappedTableProperties;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.common.utils.PersistenceUtils;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.openecomp.dcae.apod.analytics.model.facade.tca.TCAVESResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

import static org.openecomp.dcae.apod.analytics.common.utils.PersistenceUtils.TABLE_ROW_KEY_COLUMN_NAME;

/**
 * Utility methods to persist TCA Alerts Abatement information
 *
 * @author rs153v (Rajiv Singla) . Creation Date: 9/11/2017.
 */
public abstract class TCAAlertsAbatementPersister {

    private static final Logger LOG = LoggerFactory.getLogger(TCAAlertsAbatementPersister.class);

    private static final Joiner KEY_JOINER = Joiner.on(PersistenceUtils.ROW_KEY_DELIMITER);

    private TCAAlertsAbatementPersister() {
        // private constructor
    }

    /**
     * Creates {@link DatasetProperties} for Alerts Table
     *
     * @param timeToLiveSeconds alerts table Time to Live
     *
     * @return Alerts Abatement table properties
     */
    public static DatasetProperties getDatasetProperties(final int timeToLiveSeconds) {
        try {
            return ObjectMappedTableProperties.builder()
                    .setType(TCAAlertsAbatementEntity.class)
                    .setRowKeyExploreName(TABLE_ROW_KEY_COLUMN_NAME)
                    .setRowKeyExploreType(Schema.Type.STRING)
                    .add(IndexedTable.PROPERTY_TTL, timeToLiveSeconds)
                    .setDescription(CDAPComponentsConstants.TCA_FIXED_ALERTS_ABATEMENT_DESCRIPTION_TABLE)
                    .build();
        } catch (UnsupportedTypeException e) {
            final String errorMessage = "Unable to convert TCAAlertsAbatementEntity class to Schema";
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }
    }


    public static void persist(final EventListener eventListener,
                               final MetricsPerEventName violatedMetricsPerEventName,
                               final TCAVESResponse tcavesResponse,
                               final String abatementTS,
                               final ObjectMappedTable<TCAAlertsAbatementEntity> tcaAlertsAbatementTable) {
        final String abatementTableKey = createKey(eventListener, violatedMetricsPerEventName);

        final long currentTimestamp = new Date().getTime();
        final String requestID = tcavesResponse.getRequestID();
        final TCAAlertsAbatementEntity tcaAlertsAbatementEntity = new TCAAlertsAbatementEntity(currentTimestamp,
                requestID, abatementTS);
        tcaAlertsAbatementTable.write(abatementTableKey, tcaAlertsAbatementEntity);

        LOG.debug("Persisted AlertsAbatementEntity: {} with Key: {}", tcaAlertsAbatementEntity, abatementTableKey);

    }

    public static TCAAlertsAbatementEntity lookUpByKey(final EventListener eventListener,
                                                       final MetricsPerEventName violatedMetricsPerEventName,
                                                       final ObjectMappedTable<TCAAlertsAbatementEntity>
                                                               tcaAlertsAbatementTable) {
        final String abatementTableKey = createKey(eventListener, violatedMetricsPerEventName);
        return tcaAlertsAbatementTable.read(abatementTableKey);
    }


    public static String createKey(final EventListener eventListener,
                                   final MetricsPerEventName violatedMetricsPerEventName) {
        // no null check required as all are required fields
        final String eventName = violatedMetricsPerEventName.getEventName();
        final String sourceName = eventListener.getEvent().getCommonEventHeader().getSourceName();
        final String reportingEntityName = eventListener.getEvent().getCommonEventHeader().getReportingEntityName();
        // violated threshold will always be present
        final Threshold violatedThreshold = violatedMetricsPerEventName.getThresholds().get(0);
        final String closedLoopControlName = violatedThreshold.getClosedLoopControlName();
        final String fieldPath = violatedThreshold.getFieldPath();

        final List<String> abatementKeyList =
                ImmutableList.of(eventName, sourceName, reportingEntityName, closedLoopControlName, fieldPath);

        return KEY_JOINER.join(abatementKeyList);
    }

}
