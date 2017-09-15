package org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */

public abstract class DMAAPInfoMixin extends BaseTCAAppConfigModelMixin {

    @JsonProperty("topic_url")
    String topicUrl;
}
