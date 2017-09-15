package org.openecomp.dcae.apod.analytics.model.config.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DMaaP Information nested inside DMaaP Controller config
 *
 * @author rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DMAAPInfo extends BaseTCAAppConfigModel {

    private static final long serialVersionUID = 1L;

    /**
     * DMaaP Topic URL
     *
     * @param topicUrl new value for DMaaP topic URL
     * @return DMaaP Topic URL
     */
    private String topicUrl;

}
