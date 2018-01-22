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

import co.cask.cdap.api.dataset.DatasetProperties;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.onap.dcae.apod.analytics.cdap.common.BaseAnalyticsCDAPCommonUnitTest;
import org.onap.dcae.apod.analytics.common.utils.PersistenceUtils;
import org.onap.dcae.apod.analytics.model.domain.cef.CommonEventHeader;
import org.onap.dcae.apod.analytics.model.domain.cef.Event;
import org.onap.dcae.apod.analytics.model.domain.cef.EventListener;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.onap.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.onap.dcae.apod.analytics.model.facade.tca.TCAVESResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 9/13/2017.
 */
public class TCAAlertsAbatementPersisterTest extends BaseAnalyticsCDAPCommonUnitTest {

    private static final String EVENT_NAME = "testEventName";
    private static final String SOURCE_NAME = "testSourceName";
    private static final String REPORTING_ENTITY_NAME = "testReportingEntityName";
    private static final String THRESHOLD_CLOSED_LOOP_CONTROL_NAME = "testControlLoopName";
    private static final String THRESHOLD_FIELD_PATH = "testFieldPath";
    private static final String EXPECTED_LOOKUP_KEY = Joiner.on(PersistenceUtils.ROW_KEY_DELIMITER).join(
            ImmutableList.of(EVENT_NAME, SOURCE_NAME, REPORTING_ENTITY_NAME,
                    THRESHOLD_CLOSED_LOOP_CONTROL_NAME, THRESHOLD_FIELD_PATH));

    private ObjectMappedTable<TCAAlertsAbatementEntity> alertsAbatementTable;
    private EventListener eventListener;
    private MetricsPerEventName violatedMetricsPerEventName;
    private TCAVESResponse tcavesResponse;
    private String abatementTS;
    private Event event;
    private CommonEventHeader commonEventHeader;
    private Threshold violatedThreshold;

    @Before
    public void before() throws Exception {
        alertsAbatementTable = mock(ObjectMappedTable.class);
        eventListener = mock(EventListener.class);
        event = mock(Event.class);
        commonEventHeader = mock(CommonEventHeader.class);

        when(eventListener.getEvent()).thenReturn(event);
        when(event.getCommonEventHeader()).thenReturn(commonEventHeader);
        when(commonEventHeader.getEventName()).thenReturn(EVENT_NAME);
        when(commonEventHeader.getSourceName()).thenReturn(SOURCE_NAME);
        when(commonEventHeader.getReportingEntityName()).thenReturn(REPORTING_ENTITY_NAME);

        violatedMetricsPerEventName = mock(MetricsPerEventName.class);
        when(violatedMetricsPerEventName.getEventName()).thenReturn(EVENT_NAME);
        violatedThreshold = mock(Threshold.class);
        when(violatedMetricsPerEventName.getThresholds()).thenReturn(ImmutableList.of(violatedThreshold));
        when(violatedThreshold.getClosedLoopControlName()).thenReturn(THRESHOLD_CLOSED_LOOP_CONTROL_NAME);
        when(violatedThreshold.getFieldPath()).thenReturn(THRESHOLD_FIELD_PATH);
        tcavesResponse = mock(TCAVESResponse.class);
        abatementTS = "1234";
    }

    @Test
    public void testGetDatasetProperties() throws Exception {
        final DatasetProperties datasetProperties = TCAAlertsAbatementPersister.getDatasetProperties(20000);
        assertNotNull(datasetProperties);
    }

    @Test
    public void testPersist() throws Exception {

        TCAAlertsAbatementPersister.persist(eventListener, violatedMetricsPerEventName, tcavesResponse,
                abatementTS, alertsAbatementTable);
        verify(alertsAbatementTable, times(1)).write(anyString(),
                any(TCAAlertsAbatementEntity.class));

    }

    @Test
    public void testLookUpByKey() throws Exception {
        TCAAlertsAbatementPersister.lookUpByKey(eventListener, violatedMetricsPerEventName, alertsAbatementTable);
        verify(alertsAbatementTable, times(1)).read(eq(EXPECTED_LOOKUP_KEY));
    }

    @Test
    public void testCreateKey() throws Exception {
        final String createdKey = TCAAlertsAbatementPersister.createKey(eventListener, violatedMetricsPerEventName);
        assertEquals(createdKey, EXPECTED_LOOKUP_KEY);

    }

}
