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

package org.onap.dcae.apod.analytics.model.config.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DMaaP Information nested inside DMaaP Controller config
 *
 * @author Rajiv Singla . Creation Date: 8/25/2017.
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
