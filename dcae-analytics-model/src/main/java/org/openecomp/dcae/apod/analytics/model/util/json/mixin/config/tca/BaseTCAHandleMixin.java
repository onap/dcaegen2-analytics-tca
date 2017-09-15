package org.openecomp.dcae.apod.analytics.model.util.json.mixin.config.tca;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openecomp.dcae.apod.analytics.model.config.tca.DMAAPInfo;

/**
 * Author: rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
public abstract class BaseTCAHandleMixin extends BaseTCAAppConfigModelMixin {

    @JsonProperty("aaf_password")
    String aafPassword;
    @JsonProperty("aaf_username")
    String aafUserName;
    @JsonProperty("dmaap_info")
    DMAAPInfo dmaapInfo;
    @JsonProperty("type")
    String type;

}
