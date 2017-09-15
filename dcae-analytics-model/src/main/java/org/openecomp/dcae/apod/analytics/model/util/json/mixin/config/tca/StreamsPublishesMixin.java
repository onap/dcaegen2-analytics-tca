package org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openecomp.dcae.apod.analytics.model.config.tca.TCAHandleOut;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.policy.tca.BaseTCAPolicyModelMixin;

/**
 * Author: rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
public abstract class StreamsPublishesMixin extends BaseTCAPolicyModelMixin {

    @JsonProperty("tca_handle_out")
    TCAHandleOut tcaHandleOut;
}
