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

package org.openecomp.dcae.apod.analytics.model.util.json.mixin.facade.tca;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;
import org.openecomp.dcae.apod.analytics.model.facade.tca.TCAVESResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public class TCAVESResponseMixinTest extends BaseAnalyticsModelUnitTest {

    final String tcaVESCEFResponseJsonFileLocation = "data/json/facade/tca_ves_cef_response.json";

    @Test
    public void testTCAPolicyJsonConversions() throws Exception {

        final TCAVESResponse vesCEFMessageResponse =
                assertJsonConversions(tcaVESCEFResponseJsonFileLocation, TCAVESResponse.class);

        assertThat("VES CEF Message Response AAI generics VNF Id must match",
                vesCEFMessageResponse.getAai().getGenericVNFId(), is("vpp-test(?)"));

        assertThat("VES CEF Message Response AAI generic Server Id must match",
                vesCEFMessageResponse.getAai().getGenericServerId(), is("dfw1lb01lb01"));

        assertThat("VES CEF Message target type must be parsed correctly as VNF",
                vesCEFMessageResponse.getTargetType(), is("VNF"));

        testSerialization(vesCEFMessageResponse, getClass());

    }

}
