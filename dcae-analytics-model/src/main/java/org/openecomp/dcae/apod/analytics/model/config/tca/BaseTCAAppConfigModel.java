package org.openecomp.dcae.apod.analytics.model.config.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openecomp.dcae.apod.analytics.model.domain.BaseDynamicPropertiesProvider;

/**
 * <p>
 *     Base TCA App Config model class
 * </p>
 *
 * @author  rs153v (Rajiv Singla) . Creation Date: 8/25/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseTCAAppConfigModel extends BaseDynamicPropertiesProvider implements TCAAppConfigModel {
}
