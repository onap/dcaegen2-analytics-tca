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

package org.openecomp.dcae.apod.analytics.common.service.processor;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.common.BaseAnalyticsCommonUnitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 12/12/2016.
 */
public class GenericProcessorInfoTest extends BaseAnalyticsCommonUnitTest {


    @Test
    public void getProcessorName() throws Exception {
        final String processorName = "testProcessorName";
        final String processorDescription = "testProcessorDescription";
        GenericProcessorInfo genericProcessorInfo = new GenericProcessorInfo(processorName, processorDescription);
        assertThat("Processor Name must match", genericProcessorInfo.getProcessorName(), is(processorName));

    }

    @Test
    public void getProcessorDescription() throws Exception {
        final String processorName = "testProcessorName";
        final String processorDescription = "testProcessorDescription";
        GenericProcessorInfo genericProcessorInfo = new GenericProcessorInfo(processorName, processorDescription);
        assertThat("Processor Description must match", genericProcessorInfo.getProcessorDescription(),
                is(processorDescription));
    }

}
