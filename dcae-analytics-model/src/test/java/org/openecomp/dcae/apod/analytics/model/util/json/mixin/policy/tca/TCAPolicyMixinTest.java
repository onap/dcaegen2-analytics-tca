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

package org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
public class TCAPolicyMixinTest extends BaseAnalyticsModelUnitTest {

    final String tcaPolicyJsonFileLocation = "data/json/policy/tca_policy.json";

    @Test
    public void testTCAPolicyJsonConversions() throws Exception {

        final TCAPolicy tcaPolicy = assertJsonConversions(tcaPolicyJsonFileLocation, TCAPolicy.class);

        assertThat("TCA Policy Metrics Per Event Name must be 2",
                tcaPolicy.getMetricsPerEventName().size(), is(2));

        assertThat("TCA Policy Thresholds for first event name must be 3",
                tcaPolicy.getMetricsPerEventName().get(0).getThresholds().size(), is(3));

        testSerialization(tcaPolicy, getClass());

    }




}
