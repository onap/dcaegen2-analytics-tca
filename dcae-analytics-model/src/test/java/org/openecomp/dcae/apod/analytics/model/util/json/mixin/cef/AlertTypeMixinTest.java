/*
 * ===============================LICENSE_START======================================
 *  dcae-analytics
 * ================================================================================
 *    Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  ============================LICENSE_END===========================================
 */

package org.openecomp.dcae.apod.analytics.model.util.json.mixin.cef;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.openecomp.dcae.apod.analytics.model.domain.cef.AlertType.CARD_ANOMALY;

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;
import org.openecomp.dcae.apod.analytics.model.domain.cef.AlertType;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/3/2016.
 */
public class AlertTypeMixinTest extends BaseAnalyticsModelUnitTest {

    // NOTE: Alert type enum has some special customizations in AlertTypeMixin class
    // as Java enum names does not allow for "-" so actual values are coded as enum names
    @Test
    public void testAlertTypeJsonConversions() throws Exception {

        final String alertTypeJson = serializeModelToJson(CARD_ANOMALY);
        assertThat("Alert Type Json for CARD ANOMALY must have hyphen in it", alertTypeJson,
            is("\"CARD-ANOMALY\""));
        // convert parsed alert type back to enum
        final AlertType alertType = objectMapper.readValue(alertTypeJson, AlertType.class);
        LOG.debug(alertType.toString());
        assertThat("Json String for CARD ANOMALY with hyphen can be converted back to Alert Type", alertType,
            is(CARD_ANOMALY));
    }
}
