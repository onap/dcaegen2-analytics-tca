package org.openecomp.dcae.apod.analytics.model.config.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseTCAHandle extends BaseTCAAppConfigModel {

    private String aafPassword;
    private String aafUserName;
    private DMAAPInfo dmaapInfo;
    private String type;

}
