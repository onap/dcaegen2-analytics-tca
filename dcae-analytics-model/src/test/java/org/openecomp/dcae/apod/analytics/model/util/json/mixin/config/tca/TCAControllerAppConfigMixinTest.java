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

package org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;
import org.openecomp.dcae.apod.analytics.model.config.tca.DMAAPInfo;
import org.openecomp.dcae.apod.analytics.model.config.tca.TCAControllerAppConfig;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 8/25/2017.
 */
public class TCAControllerAppConfigMixinTest extends BaseAnalyticsModelUnitTest {

    final String tcaControllerAppConfigJsonFileLocation = "data/json/config/controller_app_config.json";

    @Test
    public void testTCAControllerConfigJsonConversions() throws Exception {

        final TCAControllerAppConfig tcaControllerAppConfig =
                assertJsonConversions(tcaControllerAppConfigJsonFileLocation, TCAControllerAppConfig.class);

        assertThat("TCA Stream Publishes must not be null",
                tcaControllerAppConfig.getStreamsPublishes(), is(notNullValue()));

        assertThat("TCA Stream Subscribes must not be null",
                tcaControllerAppConfig.getStreamsSubscribes(), is(notNullValue()));

        final DMAAPInfo publisherDmaaPInfo = tcaControllerAppConfig.getStreamsPublishes().getTcaHandleOut()
                .getDmaapInfo();
        assertThat("TCA publisher URL Info must not be null",
                publisherDmaaPInfo.getTopicUrl(), is(notNullValue()));

        final DMAAPInfo subscriberDmaaPInfo = tcaControllerAppConfig.getStreamsSubscribes().getTcaHandleIn()
                .getDmaapInfo();
        assertThat("TCA subscriber URL Info must not be null",
                subscriberDmaaPInfo.getTopicUrl(), is(notNullValue()));

    }

}
