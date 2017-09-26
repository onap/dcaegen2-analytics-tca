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

package org.openecomp.dcae.apod.analytics.common.exception;

import org.slf4j.Logger;

/**
 * Runtime Exception for DCAE Analytics Exceptions. All DCAE Analytics
 * runtime Exceptions must be wrapped inside this exception
 * <p>
 * @author Rajiv Singla . Creation Date: 10/5/2016.
 */
public class DCAEAnalyticsRuntimeException extends RuntimeException {


    /**
     * @param message - Error Message for Exception
     * @param cause   - Actual Exception which caused {@link DCAEAnalyticsRuntimeException}
     */
    public DCAEAnalyticsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates and logs the DCAE Runtime Exception to given logger
     *
     * @param message - Error Message for Exception and logging
     * @param logger  - Logger used for logging exception
     * @param cause   - Actual exception which caused {@link DCAEAnalyticsRuntimeException}
     */
    public DCAEAnalyticsRuntimeException(String message, Logger logger, Throwable cause) {
        super(message, cause);
        logger.error(message);
    }

}
