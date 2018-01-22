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

package org.onap.dcae.apod.analytics.cdap.common.persistance.tca;

import co.cask.cdap.api.data.schema.Schema;
import co.cask.cdap.api.data.schema.UnsupportedTypeException;
import co.cask.cdap.api.dataset.DatasetProperties;
import co.cask.cdap.api.dataset.lib.IndexedTable;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import co.cask.cdap.api.dataset.lib.ObjectMappedTableProperties;
import org.apache.commons.lang3.StringEscapeUtils;
import org.onap.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.onap.dcae.apod.analytics.common.utils.PersistenceUtils.TABLE_ROW_KEY_COLUMN_NAME;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/16/2016.
 */
public abstract class TCAVESAlertsPersister {

    private static final Logger LOG = LoggerFactory.getLogger(TCAVESAlertsPersister.class);

    private TCAVESAlertsPersister() {

    }

    /**
     * Persists Alert Message to Alerts Table
     *
     * @param alertMessage alert Message
     * @param tcaVESAlertTable alert Table Name
     */
    public static void persist(final String alertMessage, final ObjectMappedTable<TCAVESAlertEntity> tcaVESAlertTable) {
        final Date currentDate = new Date();
        final TCAVESAlertEntity alertEntity = new TCAVESAlertEntity(currentDate.getTime(),
                StringEscapeUtils.unescapeJson(alertMessage));
        // row key is same as current timestamp
        final String rowKey = createRowKey(currentDate);
        tcaVESAlertTable.write(rowKey, alertEntity);

        LOG.debug("Finished persisting VES Alert message ID: {} in VES Alerts table.", rowKey);
    }


    /**
     * Creates {@link DatasetProperties} for Alerts Table
     *
     * @param timeToLiveSeconds alerts table Time to Live
     * @return Alerts table properties
     */
    public static DatasetProperties getDatasetProperties(final int timeToLiveSeconds) {
        try {
            return ObjectMappedTableProperties.builder()
                    .setType(TCAVESAlertEntity.class)
                    .setRowKeyExploreName(TABLE_ROW_KEY_COLUMN_NAME)
                    .setRowKeyExploreType(Schema.Type.STRING)
                    .add(IndexedTable.PROPERTY_TTL, timeToLiveSeconds)
                    .setDescription(CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_DESCRIPTION_TABLE)
                    .build();
        } catch (UnsupportedTypeException e) {
            final String errorMessage = "Unable to convert TCAVESAlertEntity class to Schema";
            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
        }

    }

    /**
     * Creates Row Key for Alerts Table
     *
     * @param date current Date
     *
     * @return row key
     */
    public static String createRowKey(final Date date) {
       return String.format("%025d", date.getTime());
    }

}
