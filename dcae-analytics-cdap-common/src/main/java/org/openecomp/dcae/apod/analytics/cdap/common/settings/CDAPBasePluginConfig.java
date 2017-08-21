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

package org.openecomp.dcae.apod.analytics.cdap.common.settings;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.plugin.PluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPPluginConstants;

/**
 * <p>
 *      Base class for all DCAE Analytics CDAP Plugin config
 * </p>
 * <p>
 * @author Rajiv Singla . Creation Date: 1/17/2017.
 */
public abstract class CDAPBasePluginConfig extends PluginConfig implements CDAPPluginSettings {

    @Name(CDAPPluginConstants.Reference.REFERENCE_NAME)
    @Description(CDAPPluginConstants.Reference.REFERENCE_NAME_DESCRIPTION)
    @Macro
    protected String referenceName;

    /**
     * Provides Reference Name that can be used to trace lineage or meta data information inside CDAP container
     *
     * @return reference name
     */
    public String getReferenceName() {
        return referenceName;
    }


}
