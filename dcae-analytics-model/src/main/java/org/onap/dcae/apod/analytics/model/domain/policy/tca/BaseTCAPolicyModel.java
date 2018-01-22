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

package org.onap.dcae.apod.analytics.model.domain.policy.tca;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.onap.dcae.apod.analytics.model.BaseDynamicPropertiesProvider;

/**
 * <p>
 *     A Base TCA Policy Model which accumulates all dynamic properties in a dynamicProperties Map
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseTCAPolicyModel extends BaseDynamicPropertiesProvider implements TCAPolicyModel {
}
