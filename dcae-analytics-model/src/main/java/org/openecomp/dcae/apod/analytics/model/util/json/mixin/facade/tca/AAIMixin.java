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

package org.openecomp.dcae.apod.analytics.model.util.json.mixin.facade.tca;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.openecomp.dcae.apod.analytics.model.util.json.mixin.JsonMixin;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/9/2016.
 */
public abstract class AAIMixin implements JsonMixin {

    private String genericVNFId;
    private String genericServerId;

    @JsonGetter("generic-vnf.vnf-id")
    public String getGenericVNFId() {
        return genericVNFId;
    }

    @JsonSetter("generic-vnf.vnf-id")
    public void setGenericVNFId(String genericVNFId) {
        this.genericVNFId = genericVNFId;
    }

    @JsonGetter("vserver.vserver-name")
    public String getGenericServerId() {
        return genericServerId;
    }

    @JsonSetter("vserver.vserver-name")
    public void setGenericServerId(String genericServerId) {
        this.genericServerId = genericServerId;
    }
}
