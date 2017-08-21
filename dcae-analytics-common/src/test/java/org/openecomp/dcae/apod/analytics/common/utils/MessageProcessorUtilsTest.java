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

package org.openecomp.dcae.apod.analytics.common.utils;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.common.BaseAnalyticsCommonUnitTest;
import org.openecomp.dcae.apod.analytics.common.service.filter.JsonMessageFilterProcessorContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Rajiv Singla . Creation Date: 3/3/2017.
 */
public class MessageProcessorUtilsTest extends BaseAnalyticsCommonUnitTest {


    @Test
    public void testProcessJsonFilterMappings() throws Exception {
        final String jsonMessage = fromStream(CEF_MESSAGE_FILE_PATH);

        final Map<String, Set<String>> jsonFilterMappings = new HashMap<>();
        jsonFilterMappings.put("$.event.commonEventHeader.domain", ImmutableSet.of("measurementsForVfScaling"));
        jsonFilterMappings.put(
                "$.event.commonEventHeader.functionalRole", ImmutableSet.of("vFirewall", "vLoadBalancer"));

        final JsonMessageFilterProcessorContext jsonMessageFilterProcessorContext =
                MessageProcessorUtils.processJsonFilterMappings(jsonMessage, jsonFilterMappings);
        final Boolean matched = jsonMessageFilterProcessorContext.getMatched();
        Assert.assertNotNull(matched);
        Assert.assertTrue(matched);
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessJsonFilterMappingsWhenMappingsAreEmpty() throws Exception {
        final String jsonMessage = fromStream(CEF_MESSAGE_FILE_PATH);
        final Map<String, Set<String>> jsonFilterMappings = new HashMap<>();
        MessageProcessorUtils.processJsonFilterMappings(jsonMessage, jsonFilterMappings);
    }

}
