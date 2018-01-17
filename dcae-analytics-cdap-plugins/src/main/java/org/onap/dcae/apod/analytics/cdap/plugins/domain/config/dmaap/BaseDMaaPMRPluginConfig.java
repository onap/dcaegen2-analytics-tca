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

package org.onap.dcae.apod.analytics.cdap.plugins.domain.config.dmaap;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import com.google.common.base.Objects;
import org.onap.dcae.apod.analytics.cdap.common.settings.CDAPBasePluginConfig;

import javax.annotation.Nullable;

/**
 * Base class for all DMaaP MR Configs
 * <p>
 * @author Rajiv Singla . Creation Date: 1/17/2017.
 */
public abstract class BaseDMaaPMRPluginConfig extends CDAPBasePluginConfig {

    @Description("DMaaP Message Router HostName")
    @Macro
    protected String hostName;

    @Description("DMaaP Message Router Host Port number. Defaults to Port 80")
    @Nullable
    @Macro
    protected Integer portNumber;

    @Description("DMaaP Message Router Topic Name")
    @Macro
    protected String topicName;

    @Description("DMaaP Message Router HTTP Protocol e.g. HTTP or HTTPS. Defaults to HTTPS")
    @Nullable
    @Macro
    protected String protocol;

    @Description("DMaaP Message Router User Name used for AAF Authentication. Defaults to no authentication")
    @Nullable
    @Macro
    protected String userName;

    @Description("DMaaP Message Router User Password used for AAF Authentication. Defaults to no authentication")
    @Nullable
    @Macro
    protected String userPassword;

    @Description("DMaaP Message Router Content Type. Defaults to 'application/json'")
    @Nullable
    @Macro
    protected String contentType;


    public BaseDMaaPMRPluginConfig(final String referenceName, final String hostName, final String topicName) {
        this.referenceName = referenceName;
        this.hostName = hostName;
        this.topicName = topicName;
    }

    /**
     * Host Name for DMaaP MR Publisher or Subscriber
     *
     * @return host name
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Port Number for DMaaP MR Publisher or Subscriber
     *
     * @return port number
     */
    @Nullable
    public Integer getPortNumber() {
        return portNumber;
    }

    /**
     * DMaaP MR Topic Name for Subscriber or Publisher
     *
     * @return topic name
     */
    public String getTopicName() {
        return topicName;
    }


    /**
     * DMaaP MR HTTP or HTTPS protocol
     *
     * @return http or https protocol
     */
    @Nullable
    public String getProtocol() {
        return protocol;
    }

    /**
     * User name used for DMaaP MR AAF Authentication
     *
     * @return User name for DMaaP MR AAF Authentication
     */
    @Nullable
    public String getUserName() {
        return userName;
    }

    /**
     * User password used for DMaaP MR AAF Authentication
     *
     * @return User password used for DMaaP MR AAF Authentication
     */
    @Nullable
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Content type used for DMaaP MR Topic e.g. 'application/json'
     *
     * @return content type for DMaaP MR Topic
     */
    @Nullable
    public String getContentType() {
        return contentType;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("referenceName", referenceName)
                .add("hostName", hostName)
                .add("portNumber", portNumber)
                .add("topicName", topicName)
                .add("protocol", protocol)
                .add("userName", userName)
                .add("userPassword", "xxxx")
                .add("contentType", contentType)
                .toString();
    }
}
