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

package org.onap.dcae.apod.analytics.cdap.tca.flowlet;

import co.cask.cdap.api.flow.flowlet.OutputEmitter;
import co.cask.cdap.api.flow.flowlet.StreamEvent;
import com.google.common.base.Charsets;
import org.junit.Test;
import org.mockito.Mockito;
import org.onap.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.onap.dcae.apod.analytics.cdap.tca.BaseAnalyticsCDAPTCAUnitTest;
import org.onap.dcae.apod.analytics.common.AnalyticsConstants;

import java.nio.ByteBuffer;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 12/19/2016.
 */
public class TCAVESMessageRouterFlowletTest extends BaseAnalyticsCDAPTCAUnitTest {

    private static final String TEST_MESSAGE = "test message";
    private final OutputEmitter mockOutputEmitter = Mockito.mock(OutputEmitter.class);

    private class TCATestVESMessageRouterFlowlet extends TCAVESMessageRouterFlowlet {

        @SuppressWarnings("unchecked")
        public TCATestVESMessageRouterFlowlet() {
            this.vesMessageEmitter = mockOutputEmitter;
            doNothing().when(mockOutputEmitter).emit(eq(TEST_MESSAGE),
                    eq(AnalyticsConstants.TCA_VES_MESSAGE_ROUTER_PARTITION_KEY),
                    eq(TEST_MESSAGE.hashCode()));
        }
    }

    @Test
    public void testConfigure() throws Exception {
        final TCAVESMessageRouterFlowlet tcavesMessageRouterFlowlet = new TCAVESMessageRouterFlowlet();
        assertFlowletNameAndDescription(CDAPComponentsConstants.TCA_FIXED_VES_MESSAGE_ROUTER_NAME_FLOWLET,
                CDAPComponentsConstants.TCA_FIXED_VES_MESSAGE_ROUTER_DESCRIPTION_FLOWLET, tcavesMessageRouterFlowlet);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void routeVESMessage() throws Exception {
        final TCATestVESMessageRouterFlowlet tcavesMessageRouterFlowlet = new TCATestVESMessageRouterFlowlet();
        final StreamEvent mockStreamEvent = Mockito.mock(StreamEvent.class);
        final ByteBuffer testMessage = Charsets.UTF_8.encode(TEST_MESSAGE);
        when(mockStreamEvent.getBody()).thenReturn(testMessage);
        tcavesMessageRouterFlowlet.routeVESMessage(mockStreamEvent);
        verify(mockOutputEmitter,
                times(1)).emit(eq(TEST_MESSAGE),
                eq(AnalyticsConstants.TCA_VES_MESSAGE_ROUTER_PARTITION_KEY),
                eq(TEST_MESSAGE.hashCode()));
    }

}
