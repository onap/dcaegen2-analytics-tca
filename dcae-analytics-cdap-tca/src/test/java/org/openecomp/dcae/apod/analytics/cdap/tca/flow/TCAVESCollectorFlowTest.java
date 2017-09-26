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

package org.openecomp.dcae.apod.analytics.cdap.tca.flow;

import co.cask.cdap.AllProgramsApp;
import co.cask.cdap.api.flow.FlowletConnection;
import co.cask.cdap.api.flow.FlowletDefinition;
import co.cask.cdap.internal.app.runtime.flow.DefaultFlowConfigurer;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 1/12/2017.
 */
public class TCAVESCollectorFlowTest extends BaseAnalyticsCDAPTCAUnitTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testConfigure() throws Exception {

        final TCAVESCollectorFlow tcavesCollectorFlow = new TCAVESCollectorFlow(getTCATestAppConfig());
        final DefaultFlowConfigurer configurer = new DefaultFlowConfigurer(new AllProgramsApp.NoOpFlow());
        tcavesCollectorFlow.configure(configurer);
        final String flowName = getPrivateFiledValue(configurer, "name", String.class);
        final String flowDescription = getPrivateFiledValue(configurer, "description", String.class);

        assertThat("TCAVESCollectorFlow Name must match with what is defined in CDAPComponents Constants",
                flowName, is(CDAPComponentsConstants.TCA_FIXED_VES_COLLECTOR_NAME_FLOW));

        assertThat("TCAVESCollectorFlow Description must match with what is defined in CDAPComponents Constants",
                flowDescription, is(CDAPComponentsConstants.TCA_FIXED_VES_COLLECTOR_DESCRIPTION_FLOW));

        final Map<String, FlowletDefinition> flowlets =
                (Map<String, FlowletDefinition>) getPrivateFiledValue(configurer, "flowlets", HashMap.class);

        assertThat("TCAVESCollector must contain all TCA VES flowlets", flowlets.keySet(),
                containsInAnyOrder(CDAPComponentsConstants.TCA_FIXED_VES_MESSAGE_ROUTER_NAME_FLOWLET,
                        CDAPComponentsConstants.TCA_FIXED_VES_THRESHOLD_VIOLATION_CALCULATOR_NAME_FLOWLET,
                        CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_ABATEMENT_NAME_FLOWLET,
                        CDAPComponentsConstants.TCA_FIXED_VES_AAI_ENRICHMENT_NAME_FLOWLET,
                        CDAPComponentsConstants.TCA_FIXED_VES_ALERTS_SINK_NAME_FLOWLET));

        final List<FlowletConnection> connections =
                (List<FlowletConnection>) getPrivateFiledValue(configurer, "connections", ArrayList.class);

        assertThat("There must be four connections in VES Collector Flow", connections.size(), is(5));

    }


}
