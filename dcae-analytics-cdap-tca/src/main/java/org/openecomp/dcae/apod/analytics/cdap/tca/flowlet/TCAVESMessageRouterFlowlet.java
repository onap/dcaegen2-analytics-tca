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

package org.openecomp.dcae.apod.analytics.cdap.tca.flowlet;

import co.cask.cdap.api.annotation.Output;
import co.cask.cdap.api.annotation.ProcessInput;
import co.cask.cdap.api.flow.flowlet.AbstractFlowlet;
import co.cask.cdap.api.flow.flowlet.OutputEmitter;
import co.cask.cdap.api.flow.flowlet.StreamEvent;
import com.google.common.base.Charsets;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;
import org.openecomp.dcae.apod.analytics.common.AnalyticsConstants;


/**
 * TCA Message Router Flowlet emits VES Message to {@link TCAVESThresholdViolationCalculatorFlowlet} instances
 *
 * @author Rajiv Singla . Creation Date: 11/14/2016.
 */
public class TCAVESMessageRouterFlowlet extends AbstractFlowlet {

    /**
     * Emits ves message to TCA Calculator Instances
     */
    @Output(CDAPComponentsConstants.TCA_FIXED_VES_MESSAGE_ROUTER_OUTPUT)
    protected OutputEmitter<String> vesMessageEmitter;


    @Override
    public void configure() {
        setName(CDAPComponentsConstants.TCA_FIXED_VES_MESSAGE_ROUTER_NAME_FLOWLET);
        setDescription(CDAPComponentsConstants.TCA_FIXED_VES_MESSAGE_ROUTER_DESCRIPTION_FLOWLET);
    }

    @ProcessInput
    public void routeVESMessage(StreamEvent vesMessageStreamEvent) {
        final String vesMessage = Charsets.UTF_8.decode(vesMessageStreamEvent.getBody()).toString();
        vesMessageEmitter.emit(
                vesMessage, AnalyticsConstants.TCA_VES_MESSAGE_ROUTER_PARTITION_KEY, vesMessage.hashCode());
    }
}
