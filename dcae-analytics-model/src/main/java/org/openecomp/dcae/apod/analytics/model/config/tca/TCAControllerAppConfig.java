package org.openecomp.dcae.apod.analytics.model.config.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Model Object Representing the App Config passed in by the controller
 *
 * Author: rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TCAControllerAppConfig extends BaseTCAAppConfigModel {

    private static final long serialVersionUID = 1L;

    private String appName;
    private String appDescription;
    private StreamsPublishes streamsPublishes;
    private StreamsSubscribes streamsSubscribes;

}
