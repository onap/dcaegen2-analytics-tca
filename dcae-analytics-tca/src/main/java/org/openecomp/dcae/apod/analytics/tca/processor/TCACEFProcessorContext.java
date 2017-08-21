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

package org.openecomp.dcae.apod.analytics.tca.processor;

import org.openecomp.dcae.apod.analytics.common.service.processor.AbstractProcessorContext;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerFunctionalRole;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;

/**
 *  TCA CEF Policy Processor Context
 *
 * @author Rajiv Singla . Creation Date: 11/7/2016.
 */
public class TCACEFProcessorContext extends AbstractProcessorContext {

    private static final long serialVersionUID = 1L;

    private final TCAPolicy tcaPolicy;
    private EventListener eventListener;
    private MetricsPerFunctionalRole metricsPerFunctionalRole;

    public TCACEFProcessorContext(final String message, boolean canProcessingContinue, final TCAPolicy tcaPolicy) {
        super(message, canProcessingContinue);
        this.tcaPolicy = tcaPolicy;
        // present only if cef incoming message can be parsed successfully to Event Listener Object
        this.eventListener = null;
        // present only if there are any threshold violations are detected
        this.metricsPerFunctionalRole = null;
    }

    // Auxiliary Constructor which default canProcessingContinue Flag to true
    public TCACEFProcessorContext(final String message, final TCAPolicy tcaPolicy) {
        this(message, true, tcaPolicy);
    }

    /**
     * Returns {@link TCAPolicy} Object
     *
     * @return TCA Policy
     */
    public TCAPolicy getTCAPolicy() {
        return tcaPolicy;
    }

    /**
     * Returns Common Event Format {@link EventListener} if present else null
     *
     * @return CEF Event Listener
     */
    public EventListener getCEFEventListener() {
        return eventListener;
    }


    /**
     * Sets new {@link EventListener}
     *
     * @param eventListener set new value for CEF event listener
     */
    public void setCEFEventListener(final EventListener eventListener) {
        this.eventListener = eventListener;
    }


    /**
     * Returns TCA Policy {@link MetricsPerFunctionalRole} which was has violated Threshold for the CEF Message if
     * present else null
     *
     * @return Violated Threshold
     */
    public MetricsPerFunctionalRole getMetricsPerFunctionalRole() {
        return metricsPerFunctionalRole;
    }

    /**
     * Assign new TCA Policy {@link MetricsPerFunctionalRole} which was has violated Threshold for the CEF Message
     *
     * @param metricsPerFunctionalRole new value for Metrics Per Functional Role with violated threshold
     */
    public void setMetricsPerFunctionalRole(MetricsPerFunctionalRole metricsPerFunctionalRole) {
        this.metricsPerFunctionalRole = metricsPerFunctionalRole;
    }

}
