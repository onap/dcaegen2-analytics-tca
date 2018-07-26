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

package org.onap.dcae.apod.analytics.cdap.common.validation;

import org.onap.dcae.apod.analytics.cdap.common.settings.CDAPAppSettings;
import org.onap.dcae.apod.analytics.common.validation.DCAEValidator;
import org.onap.dcae.apod.analytics.common.validation.ValidationResponse;

/**
 * <p>
 *     Validates CDAP Application Settings (AppConfig, Preferences etc)
 * <p>
 *
 * @param <T> {@link CDAPAppSettings} DCAE Analytics App Settings (e.g. AppConfig, Preferences)
 * @param <R> {@link ValidationResponse} Validator response implementations
 *
 * @author Rajiv Singla . Creation Date: 11/2/2016.
 */
public interface CDAPAppSettingsValidator<T extends CDAPAppSettings, R extends ValidationResponse>
        extends DCAEValidator {

    /**
     * Validates DCAE Analytics App Settings and return Validation response which can be
     * checked for any app setting issues
     *
     * @param appSettings DCAE CDAP Application Settings (e.g. AppConfig, Preferences etc.)
     * @return validation response
     */
    R validateAppSettings(T appSettings);


}


