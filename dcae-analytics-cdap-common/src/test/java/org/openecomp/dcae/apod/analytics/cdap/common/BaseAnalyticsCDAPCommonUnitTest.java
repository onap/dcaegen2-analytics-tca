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

package org.openecomp.dcae.apod.analytics.cdap.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Suppliers;
import org.openecomp.dcae.apod.analytics.cdap.common.settings.CDAPAppSettings;
import org.openecomp.dcae.apod.analytics.cdap.common.settings.CDAPBaseAppConfig;
import org.openecomp.dcae.apod.analytics.cdap.common.utils.ValidationUtils;
import org.openecomp.dcae.apod.analytics.cdap.common.validation.CDAPAppSettingsValidator;
import org.openecomp.dcae.apod.analytics.common.validation.GenericValidationResponse;
import org.openecomp.dcae.apod.analytics.model.util.json.AnalyticsModelObjectMapperSupplier;
import org.openecomp.dcae.apod.analytics.test.BaseDCAEAnalyticsUnitTest;

/**
 * @author Rajiv Singla . Creation Date: 1/12/2017.
 */
public abstract class BaseAnalyticsCDAPCommonUnitTest extends BaseDCAEAnalyticsUnitTest {

    protected static final String CEF_MESSAGE_FILE_LOCATION = "data/json/cef/cef_message.json";
    protected static final String TCA_POLICY_FILE_LOCATION = "data/json/policy/tca_policy.json";
    protected static final ObjectMapper ANALYTICS_MODEL_OBJECT_MAPPER =
            Suppliers.memoize(new AnalyticsModelObjectMapperSupplier()).get();

    /**
     * Test Implementation for {@link CDAPBaseAppConfig}
     */
    public class CDAPBaseAppConfigImp extends CDAPBaseAppConfig {
    }


    /**
     * Test implementation for {@link CDAPAppSettings}
     */
    public class CDAPTestAppSettings implements CDAPAppSettings {

        private String settingsField;

        public String getSettingsField() {
            return settingsField;
        }

        public void setSettingsField(String settingsField) {
            this.settingsField = settingsField;
        }
    }


    /**
     * Test implementation for {@link CDAPAppSettingsValidator}
     */
    public class CDAPTestAppSettingsValidator implements CDAPAppSettingsValidator<CDAPTestAppSettings,
            GenericValidationResponse<CDAPTestAppSettings>> {

        private static final long serialVersionUID = 1L;

        @Override
        public GenericValidationResponse<CDAPTestAppSettings>
        validateAppSettings(CDAPTestAppSettings cdapTestAppSettings) {
            GenericValidationResponse<CDAPTestAppSettings> validationResponse = new
                    GenericValidationResponse<>();
            if (ValidationUtils.isEmpty(cdapTestAppSettings.getSettingsField())) {
                validationResponse
                        .addErrorMessage("settingsField", "Settings Field must not be empty");
            }
            return validationResponse;
        }
    }


}
