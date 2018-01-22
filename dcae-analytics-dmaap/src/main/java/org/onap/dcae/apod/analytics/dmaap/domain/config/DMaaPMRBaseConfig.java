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

package org.onap.dcae.apod.analytics.dmaap.domain.config;

import com.google.common.base.Objects;
import org.onap.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import static org.onap.dcae.apod.analytics.common.utils.HTTPUtils.JSON_APPLICATION_TYPE;

/**
 * <p>
 *      Contains common parameters for both DMaaP Message Router Publisher and Subscriber Configs
 * <p>
 * @author Rajiv Singla . Creation Date: 10/12/2016.
 */
public abstract class DMaaPMRBaseConfig implements DMaaPMRConfig {

    protected static final Logger LOG = LoggerFactory.getLogger(DMaaPMRBaseConfig.class);

    protected String hostName;
    protected Integer portNumber;
    protected String topicName;
    protected String protocol;
    protected String userName;
    protected String userPassword;
    protected String contentType;

    /**
     * Provides host name e.g. mrlocal-mtnjftle01.homer.com
     *
     * @return host name
     */
    public String getHostName() {
        return hostName;
    }


    /**
     * Provides Port Number of DMaaP MR Topic Host. Defaults to 80
     *
     * @return host port number
     */
    public Integer getPortNumber() {
        return portNumber;
    }

    /**
     * Provides topic name e.g. com.dcae.dmaap.mtnje2.DcaeTestVES
     *
     * @return topic name
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * Provides protocol type e.g. http or https
     *
     * @return protocol type
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Provides content type e.g. application/json
     *
     * @return content type
     */
    public String getContentType() {
        return contentType;
    }


    /**
     * Provides User name for the DMaaP MR Topic authentication
     *
     * @return user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Provides User password for the DMaaP MR Topic authentication
     *
     * @return user Password
     */
    public String getUserPassword() {
        return userPassword;
    }


    /**
     * Trims, adjusts casing and validates user input String for protocol selection
     *
     * @param protocol - User input for protocol String
     * @return - network protocol e.g http or https
     */
    protected static String normalizeValidateProtocol(final String protocol) {
        // validate that only http and https are supported protocols are Supported for DMaaP MR
        String normalizedProtocolString = protocol.trim().toLowerCase(Locale.ENGLISH);
        if (normalizedProtocolString.isEmpty() ||
                !("http".equals(normalizedProtocolString) || "https".equals(normalizedProtocolString))) {

            final String errorMessage =
                    "Unsupported protocol selection. Only HTTPS and HTTPS are currently supported for DMaaP MR";

            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        }
        return normalizedProtocolString;
    }


    /**
     * Trims, adjust casing and validates content type is supported by DMaaP.
     *
     * NOTE: DMaaP currently only support application/json content type
     *
     * @param contentType content type that needs to checked for DMaaP MR support
     * @return true if content type is supported by DMaaP MR
     */
    protected static String normalizeValidateContentType(final String contentType) {
        // Current DMaaP MR is only supporting "application/json" content type
        String normalizedContentType = contentType.trim().toLowerCase(Locale.ENGLISH);
        final boolean isSupported = contentType.equals(JSON_APPLICATION_TYPE);
        if (!isSupported) {
            final String errorMessage =
                    "Unsupported content type selection. Only application/json is currently supported for DMaaP MR";

            throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, new IllegalArgumentException(errorMessage));
        }
        return normalizedContentType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DMaaPMRBaseConfig)) {
            return false;
        }
        DMaaPMRBaseConfig that = (DMaaPMRBaseConfig) o;
        return Objects.equal(hostName, that.hostName) &&
                Objects.equal(portNumber, that.portNumber) &&
                Objects.equal(topicName, that.topicName) &&
                Objects.equal(protocol, that.protocol) &&
                Objects.equal(userName, that.userName) &&
                Objects.equal(userPassword, that.userPassword) &&
                Objects.equal(contentType, that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hostName, portNumber, topicName, protocol, userName, userPassword, contentType);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("hostName", hostName)
                .add("portNumber", portNumber)
                .add("topicName", topicName)
                .add("protocol", protocol)
                .add("userName", userName)
                .add("contentType", contentType)
                .toString();
    }
}
