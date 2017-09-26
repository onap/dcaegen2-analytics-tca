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

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import com.google.common.base.Objects;
import org.openecomp.dcae.apod.analytics.cdap.common.settings.CDAPBasePluginConfig;

import javax.annotation.Nullable;

/**
 * Simple TCA Plugin Configuration
 * <p>
 * @author Rajiv Singla . Creation Date: 2/13/2017.
 */
public class SimpleTCAPluginConfig extends CDAPBasePluginConfig {

    private static final long serialVersionUID = 1L;

    @Description("Field name containing VES Message")
    @Macro
    protected String vesMessageFieldName;

    @Description("Policy JSON that need to be applied to VES Message")
    @Macro
    protected String policyJson;

    @Description("Name of the output field that will contain the alert")
    @Macro
    protected String alertFieldName;

    @Description("Name of the output field that will contain message type: INAPPLICABLE, COMPLIANT, NON_COMPLIANT")
    @Macro
    protected String messageTypeFieldName;

    @Description("Specifies the output schema")
    protected String schema;

    @Description("Enables")
    @Nullable
    @Macro
    protected Boolean enableAlertCEFFormat;


    /**
     * Creates an instance of TCA Plugin Configs
     *
     * @param vesMessageFieldName Ves message field name from incoming plugin schema
     * @param policyJson TCA Policy Json String
     * @param alertFieldName Alert field name that will be added in TCA plugin output schema
     * @param messageTypeFieldName Message type field name that will be added in TCA plugin output schema
     * @param schema TCA Plugin output schema
     * @param enableAlertCEFFormat enables alert message to be formatted in VES format
     */
    public SimpleTCAPluginConfig(final String vesMessageFieldName, final String policyJson,
                                 final String alertFieldName, final String messageTypeFieldName,
                                 final String schema, final Boolean enableAlertCEFFormat) {
        this.vesMessageFieldName = vesMessageFieldName;
        this.policyJson = policyJson;
        this.alertFieldName = alertFieldName;
        this.messageTypeFieldName = messageTypeFieldName;
        this.schema = schema;
        this.enableAlertCEFFormat = enableAlertCEFFormat;
    }

    /**
     * Name of the field containing VES Message
     *
     * @return VES Message field name
     */
    public String getVesMessageFieldName() {
        return vesMessageFieldName;
    }

    /**
     * Policy Json String
     *
     * @return Policy Json String
     */
    public String getPolicyJson() {
        return policyJson;
    }


    /**
     * Alert Field name in outgoing schema
     *
     * @return alert field name in outgoing schema
     */
    public String getAlertFieldName() {
        return alertFieldName;
    }

    /**
     * Returns output schema string
     *
     * @return output schema string
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Return TCA message type - INAPPLICABLE, COMPLIANT, NON_COMPLIANT
     *
     * @return tca message type
     */
    public String getMessageTypeFieldName() {
        return messageTypeFieldName;
    }


    /**
     * Returns if Alert output in Common Event format
     *
     * @return true if alert output is in common event format
     */
    @Nullable
    public Boolean getEnableAlertCEFFormat() {
        return enableAlertCEFFormat;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("referenceName", referenceName)
                .add("vesMessageFieldName", vesMessageFieldName)
                .add("policyJson", policyJson)
                .add("alertFieldName", alertFieldName)
                .add("messageTypeFieldName", messageTypeFieldName)
                .add("schema", schema)
                .add("enableAlertCEFFormat", true)
                .toString();
    }
}
