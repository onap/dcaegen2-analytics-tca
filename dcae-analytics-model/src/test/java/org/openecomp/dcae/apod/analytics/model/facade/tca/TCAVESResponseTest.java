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

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 12/16/2016.
 */
public class TCAVESResponseTest extends BaseAnalyticsModelUnitTest {

    final String tcaVESCEFResponseJsonFileLocation = "data/json/facade/tca_ves_cef_response.json";


    @Test
    public void testTCAPolicyJsonConversions() throws Exception {

        final TCAVESResponse vesCEFMessageResponse =
                assertJsonConversions(tcaVESCEFResponseJsonFileLocation, TCAVESResponse.class);

        assertThat("VES CEF Message Response AAI generics VNF Id must match",
                vesCEFMessageResponse.getAai().getGenericVNFId(), is("vpp-test(?)"));

        assertThat("VES CEF Message target type must be parsed correctly as VNF",
                vesCEFMessageResponse.getTargetType(), is("VNF"));

        assertThat("VES closed Loop Name must be CL-FRWL-LOW-TRAFFIC-SIG-d925ed73-8231-4d02-9545-db4e101f88f8",
                vesCEFMessageResponse.getClosedLoopControlName(),
                is("CL-FRWL-LOW-TRAFFIC-SIG-d925ed73-8231-4d02-9545-db4e101f88f8"));

        assertThat("version must be 1.0.2", vesCEFMessageResponse.getVersion(), is("1.0.2"));

        assertThat("closedLoopAlarmStart must be 1478189220547",
                vesCEFMessageResponse.getClosedLoopAlarmStart(), is(1478189220547L));
        assertThat("closedLoopEventClient must be tca.instance00001",
                vesCEFMessageResponse.getClosedLoopEventClient(), is("tca.instance00001"));
        assertThat("target_type must be VNF", vesCEFMessageResponse.getTargetType(), is("VNF"));
        assertThat("target must be VNF", vesCEFMessageResponse.getTarget(), is("generic-vnf.vnf-id"));
        assertThat("aai generic vnf id must be vpp-test(?)", vesCEFMessageResponse.getAai().getGenericVNFId(),
                is("vpp-test(?)"));
        assertThat("from must be DCAE", vesCEFMessageResponse.getFrom(), is("DCAE"));
        assertThat("policyScope must be resource=vFirewall;type=configuration",
                vesCEFMessageResponse.getPolicyScope(), is("resource=vFirewall;type=configuration"));

        assertThat("policyName must be configuration.dcae.microservice.tca.xml",
                vesCEFMessageResponse.getPolicyName(), is("configuration.dcae.microservice.tca.xml"));

        assertThat("policyVersion must be v0.0.1",
                vesCEFMessageResponse.getPolicyVersion(), is("v0.0.1"));

        assertThat("closedLoopEventStatus is ONSET",
                vesCEFMessageResponse.getClosedLoopEventStatus(), is("ONSET"));

    }

}
