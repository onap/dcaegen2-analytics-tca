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

package org.openecomp.dcae.apod.analytics.common.utils;

import java.util.Date;

/**
 * Contains Utility methods for creating persistence row keys etc.
 *
 * @author Rajiv Singla . Creation Date: 11/16/2016.
 */
public abstract class PersistenceUtils {


    /**
     * Name of the column which will contain Table Key
     */
    public static final String TABLE_ROW_KEY_COLUMN_NAME = "key";

    /**
     * Delimited to be used when creating a row key with multiple fields
     */
    public static final String ROW_KEY_DELIMITER = "-";


    private PersistenceUtils() {

    }

    /**
     * Creates a decreasing number using current timestamp. Handy when you want to keep records most recent records
     * close to the top of column table like HBase
     *
     * @return decreasing number
     */
    public static String getCurrentTimeReverseSubKey() {
        final long timeReverseLong = Long.MAX_VALUE - new Date().getTime();
        return String.format("%025d", timeReverseLong);
    }

}
