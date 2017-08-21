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

/**
 * <p>
 *      Contains CDAP Plugin Constants
 * </p>
 * @author Rajiv Singla . Creation Date: 1/17/2017.
 */
public abstract class CDAPPluginConstants {

    /**
     * Common Reference Name property name and description used to create an external Dataset for metadata, lineage
     * purposes
     */
    public static class Reference {

        public static final String REFERENCE_NAME = "referenceName";
        public static final String REFERENCE_NAME_DESCRIPTION =
                "This will be used to uniquely identify this source/sink for lineage, annotating metadata, etc.";

        private Reference() {
            // private constructor
        }

    }

    /**
     * Contains fields for DMaaP MR Sink Map Reduce Hadoop Configuration
     */
    public static class DMaaPMRSinkHadoopConfigFields {

        public static final String HOST_NAME = "dmaap.mr.sink.hostName";
        public static final String PORT_NUMBER = "dmaap.mr.sink.portNumber";
        public static final String TOPIC_NAME = "dmaap.mr.sink.topicName";
        public static final String PROTOCOL = "dmaap.mr.sink.protocol";
        public static final String USER_NAME = "dmaap.mr.sink.userName";
        public static final String USER_PASS = "dmaap.mr.sink.userPassword";
        public static final String CONTENT_TYPE = "dmaap.mr.sink.contentType";
        public static final String MAX_BATCH_SIZE = "dmaap.mr.sink.maxBatchSize";
        public static final String MAX_RECOVER_QUEUE_SIZE = "dmaap.mr.sink.maxRecoveryQueueSize";

        private DMaaPMRSinkHadoopConfigFields() {
            // private constructor
        }
    }


    private CDAPPluginConstants() {
        // private constructor
    }
}
