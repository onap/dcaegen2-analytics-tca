package org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openecomp.dcae.apod.analytics.model.config.tca.TCAHandleIn;

/**
 * Author: rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
public abstract class StreamsSubscribesMixin extends BaseTCAAppConfigModelMixin {

    @JsonProperty("tca_handle_in")
    TCAHandleIn tcaHandleIn;

}
