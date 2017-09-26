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

package org.openecomp.dcae.apod.analytics.model.util.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.BaseTCAPolicyModel;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ClosedLoopEventStatus;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.ControlLoopSchemaType;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Direction;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.MetricsPerEventName;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.Threshold;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca.BaseTCAPolicyModelMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca.ClosedLoopEventStatusMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca.ControlLoopSchemaTypeMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca.DirectionMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca.MetricsPerEventNameMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca.TCAPolicyMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca.ThresholdMixin;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
public class TCAPolicyModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public TCAPolicyModule() {
        super("Threshold Crossing Alert Policy",
                new Version(1, 0, 0, null, " org.openecomp.dcae.apod.analytics.model", "dcae-analytics-model"));
    }

    @Override
    public void setupModule(final SetupContext setupContext) {
        setupContext.setMixInAnnotations(BaseTCAPolicyModel.class, BaseTCAPolicyModelMixin.class);
        setupContext.setMixInAnnotations(ControlLoopSchemaType.class, ControlLoopSchemaTypeMixin.class);
        setupContext.setMixInAnnotations(Direction.class, DirectionMixin.class);
        setupContext.setMixInAnnotations(ClosedLoopEventStatus.class, ClosedLoopEventStatusMixin.class);
        setupContext.setMixInAnnotations(TCAPolicy.class, TCAPolicyMixin.class);
        setupContext.setMixInAnnotations(MetricsPerEventName.class, MetricsPerEventNameMixin.class);
        setupContext.setMixInAnnotations(Threshold.class, ThresholdMixin.class);
        setupContext.setMixInAnnotations(TCAPolicy.class, TCAPolicyMixin.class);
    }
}
