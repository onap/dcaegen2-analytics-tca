package org.openecomp.dcae.apod.analytics.model.config.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StreamsPublishes extends BaseTCAAppConfigModel {

    private static final long serialVersionUID = 1L;

    private TCAHandleOut tcaHandleOut;

}
