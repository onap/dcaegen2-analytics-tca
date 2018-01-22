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

package org.onap.dcae.apod.analytics.common.utils;

/**
 * Contains common utils to check HTTP Related Utils
 *
 * @author Rajiv Singla . Creation Date: 11/2/2016.
 */
public abstract class HTTPUtils {

    /**
     * HTTP Status code for successful HTTP call
     */
    public static final Integer HTTP_SUCCESS_STATUS_CODE = 200;

    /**
     * HTTP Response code when request has been accepted for processing, but the processing has not been completed
     */
    public static final Integer HTTP_ACCEPTED_RESPONSE_CODE = 202;

    /**
     * HTTP Response code when there is no content
     */
    public static final Integer HTTP_NO_CONTENT_RESPONSE_CODE = 204;


    public static final String JSON_APPLICATION_TYPE = "application/json";


    private HTTPUtils() {

    }

    /**
     *  Checks if HTTP Status code is less than or equal to 200 but less then 300
     *
     * @param statusCode http status code
     * @return true if response code between 200 and 300
     */
    public static boolean isSuccessfulResponseCode(Integer statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
}
