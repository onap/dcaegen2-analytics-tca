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

package org.openecomp.dcae.apod.analytics.it.plugins;

import static com.google.common.collect.ImmutableList.of;

import co.cask.cdap.etl.mock.test.HydratorTestBase;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all the Hydrator test base , where the utility and common code for integration testing is written
 * <p/>
 * @author Manjesh Gowda. Creation Date: 2/3/2017.
 */

public abstract class BaseAnalyticsPluginsIT extends HydratorTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(BaseAnalyticsPluginsIT.class);

    /**
     * Provides two simple messages for testing of Plugin.
     * <p/>
     *
     * @return two simple messages for testing of Plugin
     */
    protected static List<String> getTwoSampleMessage() {
        String message1 = "{ \"message\" : \"Test Message 1 from DMaaP source Plugin\"}";
        String message2 = "{ \"message\" : \"Test Message 2 from DMaaP source Plugin\"}";
        return of(message1, message2);
    }
}
