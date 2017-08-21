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

package org.openecomp.dcae.apod.analytics.it.util;

/**
 * @author Rajiv Singla . Creation Date: 2/2/2017.
 */
public abstract class StepUtils {


    /**
     * Determines if step should use default publisher topic as configured in environment properties file
     *
     * @param topicName step passed topic name
     * @return true if default publisher topic
     */
    public static boolean isDefaultPublisherTopic(final String topicName) {
        if (topicName.equalsIgnoreCase("default")) {
            return true;
        } else {
            return false;
        }
    }

}
