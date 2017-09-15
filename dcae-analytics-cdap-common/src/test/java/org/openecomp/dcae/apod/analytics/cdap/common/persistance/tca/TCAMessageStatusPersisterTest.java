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

import co.cask.cdap.api.dataset.DatasetProperties;
import co.cask.cdap.api.dataset.lib.ObjectMappedTable;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.common.BaseAnalyticsCDAPCommonUnitTest;
import org.openecomp.dcae.apod.analytics.model.domain.cef.CommonEventHeader;
import org.openecomp.dcae.apod.analytics.model.domain.cef.Domain;
import org.openecomp.dcae.apod.analytics.model.domain.cef.Event;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Direction;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.openecomp.dcae.apod.analytics.tca.processor.TCACEFProcessorContext;
import org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 2/16/2017.
 */
public class TCAMessageStatusPersisterTest extends BaseAnalyticsCDAPCommonUnitTest {

    private static final int TEST_INSTANCE_ID = 0;
    private static final Domain TEST_DOMAIN = Domain.other;
    private static final String TEST_EVENT_NAME = "TEST_EVENT_NAME";

    private ObjectMappedTable<TCAMessageStatusEntity> vesMessageStatusTable;
    private TCACEFProcessorContext processorContext;
    private EventListener eventListener;
    private Event event;
    private CommonEventHeader commonEventHeader;
    private String functionalRole;


    @Before
    public void before() {
        vesMessageStatusTable = mock(ObjectMappedTable.class);
        processorContext = mock(TCACEFProcessorContext.class);
        eventListener = mock(EventListener.class);
        event = mock(Event.class);
        commonEventHeader = mock(CommonEventHeader.class);
        when(processorContext.getMessage()).thenReturn("testMessage");
        when(processorContext.getCEFEventListener()).thenReturn(eventListener);
        when(eventListener.getEvent()).thenReturn(event);
        when(event.getCommonEventHeader()).thenReturn(commonEventHeader);
        when(commonEventHeader.getEventName()).thenReturn(TEST_EVENT_NAME);
        when(commonEventHeader.getDomain()).thenReturn(TEST_DOMAIN);
    }


    @Test
    public void testPersistWithInApplicableMessage() throws Exception {
        TCAMessageStatusPersister.persist
                (processorContext, TEST_INSTANCE_ID, TCACalculatorMessageType.INAPPLICABLE, vesMessageStatusTable);
        verify(vesMessageStatusTable, times(1)).write(anyString(),
                any(TCAMessageStatusEntity.class));
    }

    @Test
    public void testPersistWithNonCompliantMessage() throws Exception {
        final MetricsPerEventName metricsPerEventName = mock(MetricsPerEventName.class);
        final Threshold threshold = mock(Threshold.class);
        when(processorContext.getMetricsPerEventName()).thenReturn(metricsPerEventName);
        when((metricsPerEventName.getThresholds())).thenReturn(ImmutableList.of(threshold));
        when(threshold.getDirection()).thenReturn(Direction.GREATER);
        when(threshold.getSeverity()).thenReturn(EventSeverity.CRITICAL);
        TCAMessageStatusPersister.persist(
                processorContext, TEST_INSTANCE_ID, TCACalculatorMessageType.NON_COMPLIANT,
                vesMessageStatusTable, "testAlert");
        verify(vesMessageStatusTable, times(1)).write(anyString(),
                any(TCAMessageStatusEntity.class));
    }

    @Test
    public void testPersistWithCompliantMessage() throws Exception {
        final String cefMessage = fromStream(CEF_MESSAGE_FILE_LOCATION);
        final String tcaPolicyString = fromStream(TCA_POLICY_FILE_LOCATION);

        final TCAPolicy tcaPolicy = ANALYTICS_MODEL_OBJECT_MAPPER.readValue(tcaPolicyString, TCAPolicy.class);
        final TCACEFProcessorContext tcacefProcessorContext = TCAUtils.filterCEFMessage(cefMessage, tcaPolicy);
        final TCACEFProcessorContext finalProcessorContext =
                TCAUtils.computeThresholdViolations(tcacefProcessorContext);

        TCAMessageStatusPersister.persist(finalProcessorContext, TEST_INSTANCE_ID,
                TCACalculatorMessageType.COMPLIANT, vesMessageStatusTable, "testAlert");
        verify(vesMessageStatusTable, times(1)).write(anyString(),
                any(TCAMessageStatusEntity.class));
    }

    @Test
    public void testGetDatasetProperties() throws Exception {
        final DatasetProperties datasetProperties = TCAMessageStatusPersister.getDatasetProperties(20000);
        Assert.assertNotNull(datasetProperties);

    }

}
