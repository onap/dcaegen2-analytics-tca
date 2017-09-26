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
import org.openecomp.dcae.apod.analytics.model.config.tca.BaseTCAAppConfigModel;
import org.openecomp.dcae.apod.analytics.model.config.tca.BaseTCAHandle;
import org.openecomp.dcae.apod.analytics.model.config.tca.DMAAPInfo;
import org.openecomp.dcae.apod.analytics.model.config.tca.StreamsPublishes;
import org.openecomp.dcae.apod.analytics.model.config.tca.StreamsSubscribes;
import org.openecomp.dcae.apod.analytics.model.config.tca.TCAControllerAppConfig;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca.BaseTCAAppConfigModelMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca.BaseTCAHandleMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca.DMAAPInfoMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca.StreamsPublishesMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca.StreamsSubscribesMixin;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca.TCAControllerAppConfigMixin;

/**
 * @author Rajiv Singla . Creation Date: 8/25/2017.
 */
public class TCAControllerConfigModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public TCAControllerConfigModule() {
        super("Threshold Crossing Alert Controller Config",
                new Version(1, 0, 0, null, "org.openecomp.dace.apod.analytics.model", "dcae-analytics-model"));
    }

    @Override
    public void setupModule(final SetupContext setupContext) {
        setupContext.setMixInAnnotations(BaseTCAAppConfigModel.class, BaseTCAAppConfigModelMixin.class);
        setupContext.setMixInAnnotations(BaseTCAHandle.class, BaseTCAHandleMixin.class);
        setupContext.setMixInAnnotations(DMAAPInfo.class, DMAAPInfoMixin.class);
        setupContext.setMixInAnnotations(StreamsPublishes.class, StreamsPublishesMixin.class);
        setupContext.setMixInAnnotations(StreamsSubscribes.class, StreamsSubscribesMixin.class);
        setupContext.setMixInAnnotations(TCAControllerAppConfig.class, TCAControllerAppConfigMixin.class);
    }
}
