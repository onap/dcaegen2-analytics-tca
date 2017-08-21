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

import co.cask.cdap.api.Config;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;

/**
 * Base class for all DACE Analytics Application Configurations
 *
 * @author Rajiv Singla . Creation Date: 10/4/2016.
 */
public abstract class CDAPBaseAppConfig extends Config implements CDAPAppConfig {


    /**
     * DCAE Analytics App Name
     */
    protected String appName = CDAPComponentsConstants.COMMON_DEFAULT_DCAE_CDAP_NAME_APP;

    /**
     * DCAE Analytics App Description
     */
    protected String appDescription = CDAPComponentsConstants.COMMON_DEFAULT_DCAE_CDAP_DESCRIPTION_APP;


    /**
     * Returns DCAE Analytics CDAP Application name
     *
     * @return CDAP application name
     */
    @Override
    public String getAppName() {
        return appName;
    }

    /**
     * Returns DCAE Analytics CDAP Application descrption
     *
     * @return CDAP application description
     */
    @Override
    public String getAppDescription() {
        return appDescription;
    }
}
