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

package org.openecomp.dcae.apod.analytics.dmaap.service.publisher;

import java.util.List;

/**
 * <p>
 *     DMaaP MR Publisher Queue handles back pressure in case DMaaP MR Publisher topic
 *     is offline for some reason. It does so by having a recovery queue which keeps
 *     messages in order in case there is temporary interruption in DMaaP Publisher
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 11/1/2016.
 */
public interface DMaaPMRPublisherQueue {

    /**
     * <p>
     *     Add batchMessages to Batch Queue
     * </p>
     *
     * @param batchMessages messages that needs to be added to batch queue
     * @return current size of batch queue. Throws {@link IllegalStateException}
     * if batch queue does not have enough space
     */
    int addBatchMessages(List<String> batchMessages);


    /**
     * <p>
     *     Add recoverable messages to Recoverable Queue
     * </p>
     *
     * @param recoverableMessages messages that needs to be added to recoverable queue
     * @return current size of the recoverable queue. Throws {@link IllegalStateException}
     * if recoverable queue does not have enough space
     */
    int addRecoverableMessages(List<String> recoverableMessages);

    /**
     * <p>
     *     Get messages that need to be published to DMaaP topic. Messages in recoverable
     *     queue are appended if present.
     * </p>
     *
     * @return List of messages from both batch and recovery queue
     */
    List<String> getMessageForPublishing();

    /**
     * <p>
     *     Remaining capacity of Batch Queue
     * </p>
     *
     * @return Remaining Batch Queue Size
     */
    int getBatchQueueRemainingSize();

    /**
     * <p>
     *     Remaining capacity of Recovery Queue
     * </p>
     *
     * @return Remaining Recovery Queue Size
     */
    int getRecoveryQueueRemainingSize();

}
