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

package org.openecomp.dcae.apod.analytics.cdap.tca.worker;

import co.cask.cdap.api.app.ApplicationSpecification;
import co.cask.cdap.api.worker.WorkerConfigurer;
import co.cask.cdap.api.worker.WorkerContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.common.exception.CDAPSettingsException;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 12/20/2016.
 */
public class TCADMaaPPublisherWorkerTest extends BaseAnalyticsCDAPTCAUnitTest {

    private static final String VES_ALERTS_TABLE_NAME = "vesAlertsTable";

    private WorkerConfigurer workerConfigurer;
    private WorkerContext workerContext;
    private TCADMaaPPublisherWorker publisherWorker;
    private ApplicationSpecification mockApplicationSpecification;

    @Before
    public void before() throws Exception {
        workerConfigurer = mock(WorkerConfigurer.class);
        workerContext = mock(WorkerContext.class);
        doNothing().when(workerConfigurer).setName(anyString());
        doNothing().when(workerConfigurer).setDescription(anyString());
        mockApplicationSpecification = Mockito.mock(ApplicationSpecification.class);
        when(workerContext.getApplicationSpecification()).thenReturn(mockApplicationSpecification);
        publisherWorker = new TCADMaaPPublisherWorker(VES_ALERTS_TABLE_NAME);

    }

    @Test
    public void testConfigure() throws Exception {
        publisherWorker.configure(workerConfigurer);
        verify(workerConfigurer, times(1))
                .setName(eq(CDAPComponentsConstants.TCA_FIXED_DMAAP_PUBLISHER_WORKER));
        verify(workerConfigurer, times(1))
                .setDescription(eq(CDAPComponentsConstants.TCA_FIXED_DMAAP_PUBLISHER_DESCRIPTION_WORKER));
    }

    @Test(expected = CDAPSettingsException.class)
    public void testInitializeWhenSettingsHaveErrors() throws Exception {
        when(mockApplicationSpecification.getConfiguration()).thenReturn("{}");
        publisherWorker.initialize(workerContext);
    }

    @Test
    public void testInitializeWhenSettingsAreValid() throws Exception {
        when(workerContext.getRuntimeArguments()).thenReturn(getPreferenceMap());
        when(mockApplicationSpecification.getConfiguration()).thenReturn(fromStream(TCA_APP_CONFIG_FILE_LOCATION));
        publisherWorker.initialize(workerContext);
    }

}
