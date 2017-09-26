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

package org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.tca;

/**
 * @author Rajiv Singla . Creation Date: 2/17/2017.
 */
public class TestSimpleTCAPluginConfig extends SimpleTCAPluginConfig {

    public TestSimpleTCAPluginConfig(String vesMessageFieldName, String policyJson, String alertFieldName,
                                     String messageTypeFieldName, String schema, Boolean enableAlertCEFFormat) {
        super(vesMessageFieldName, policyJson, alertFieldName, messageTypeFieldName, schema, enableAlertCEFFormat);
    }

    public void setVesMessageFieldName(String vesMessageFieldName) {
        this.vesMessageFieldName = vesMessageFieldName;
    }

    public void setPolicyJson(String policyJson) {
        this.policyJson = policyJson;
    }

    public void setAlertFieldName(String alertFieldName) {
        this.alertFieldName = alertFieldName;
    }

    public void setMessageTypeFieldName(String messageTypeFieldName) {
        this.messageTypeFieldName = messageTypeFieldName;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setEnableAlertCEFFormat(Boolean enableAlertCEFFormat) {
        this.enableAlertCEFFormat = enableAlertCEFFormat;
    }
}
