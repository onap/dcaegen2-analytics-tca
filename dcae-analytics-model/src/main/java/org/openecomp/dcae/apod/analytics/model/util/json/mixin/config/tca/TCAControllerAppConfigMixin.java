package org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openecomp.dcae.apod.analytics.model.config.tca.StreamsPublishes;
import org.openecomp.dcae.apod.analytics.model.config.tca.StreamsSubscribes;

/**
 * Author: rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
public abstract class TCAControllerAppConfigMixin extends BaseTCAAppConfigModelMixin {

    @JsonProperty("streams_publishes")
    StreamsPublishes streamsPublishes;
    @JsonProperty("streams_subscribes")
    StreamsSubscribes streamsSubscribes;

}
