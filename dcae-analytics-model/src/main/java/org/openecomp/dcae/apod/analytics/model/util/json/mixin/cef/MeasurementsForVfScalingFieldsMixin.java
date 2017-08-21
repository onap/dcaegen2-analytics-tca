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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.openecomp.dcae.apod.analytics.model.domain.cef.VNicUsageArray;

import java.util.List;

/**
 * @author Rajiv Singla . Creation Date: 10/18/2016.
 */
public abstract class MeasurementsForVfScalingFieldsMixin extends BaseCEFModelMixin {

    @JsonSetter("vNicUsageArray")
    public abstract void setVNicUsageArray(List<VNicUsageArray> vNicUsageArrays);

    @JsonGetter("vNicUsageArray")
    public abstract List<VNicUsageArray> getVNicUsageArray();
}
