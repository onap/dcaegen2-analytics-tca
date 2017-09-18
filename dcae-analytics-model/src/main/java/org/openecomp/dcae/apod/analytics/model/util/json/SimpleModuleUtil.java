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

package org.openecomp.dcae.apod.analytics.model.util.json;

import com.fasterxml.jackson.core.Version;

/**
 * Utility class for simple modules.
 */
public final class SimpleModuleUtil {

    private SimpleModuleUtil() {
    }

    /**
     * Returns version for dcae analytics model
     * @return version
     */
    public static Version getVersion() {
        return new Version( //version
            25, //major version
            0, //minor version
            0, //patch level
            null, //snapshot info
            " org.openecomp.dcae.apod.analytics.model", //group id
            "dcae-analytics-model" //artifact id
        );
    }
}
