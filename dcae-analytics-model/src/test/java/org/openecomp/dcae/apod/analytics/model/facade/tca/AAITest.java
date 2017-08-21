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

package org.openecomp.dcae.apod.analytics.model.facade.tca;

import org.junit.Before;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 12/16/2016.
 */
public class AAITest extends BaseAnalyticsModelUnitTest {

    private AAI aai;

    @Before
    public void before() {
        this.aai = new AAI();
    }

    @Test
    public void getGenericVNFId() throws Exception {
        final String genericVNFId = "testVNFID";
        aai.setGenericVNFId(genericVNFId);
        assertThat("VNFID must be same", aai.getGenericVNFId(), is(genericVNFId));
    }

    @Test
    public void setGenericVNFId() throws Exception {
        final String genericVNFId = "testVNFID";
        aai.setGenericVNFId(genericVNFId);
        final String changedGenericVNFId = "changedVNFID";
        aai.setGenericVNFId(changedGenericVNFId);
        assertThat("VNFID must be same as changed VNFID", aai.getGenericVNFId(), is(changedGenericVNFId));

    }

}
