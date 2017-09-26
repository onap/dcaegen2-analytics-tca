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
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.common.BaseAnalyticsCDAPCommonUnitTest;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Rajiv Singla . Creation Date: 2/16/2017.
 */
public class TCAVESAlertsPersisterTest extends BaseAnalyticsCDAPCommonUnitTest {

    @Test
    public void testPersist() throws Exception {
        final ObjectMappedTable<TCAVESAlertEntity> tcaVESAlertTable = Mockito.mock(ObjectMappedTable.class);
        TCAVESAlertsPersister.persist("test alert message", tcaVESAlertTable);
        verify(tcaVESAlertTable, times(1)).write(anyString(),
                any(TCAVESAlertEntity.class));
    }

    @Test
    public void testGetDatasetProperties() throws Exception {
        final DatasetProperties datasetProperties = TCAVESAlertsPersister.getDatasetProperties(20000);
        Assert.assertNotNull(datasetProperties);
    }

    @Test
    public void testCreateRowKey() throws Exception {
        final String rowKey = TCAVESAlertsPersister.createRowKey(new Date());
        Assert.assertThat(rowKey.toCharArray().length, is(25));
    }

}
