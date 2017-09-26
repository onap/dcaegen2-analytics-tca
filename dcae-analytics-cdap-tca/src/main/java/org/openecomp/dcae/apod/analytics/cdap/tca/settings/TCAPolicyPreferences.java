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

package org.openecomp.dcae.apod.analytics.cdap.tca.settings;

import org.openecomp.dcae.apod.analytics.cdap.common.settings.CDAPAppPreferences;
import org.openecomp.dcae.apod.analytics.model.domain.policy.tca.TCAPolicy;

/**
 * A wrapper over {@link TCAPolicy} to act as app Preferences as TCA Policy is passed
 * by controller as runtime arguments from CDAP app preferences
 * <p>
 * @author Rajiv Singla . Creation Date: 11/29/2016.
 */
public class TCAPolicyPreferences extends TCAPolicy implements CDAPAppPreferences {

    private static final long serialVersionUID = 1L;

}
