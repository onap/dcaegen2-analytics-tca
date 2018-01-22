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

package org.onap.dcae.apod.analytics.dmaap.service.publisher;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.unmodifiableList;

/**
 * <p>
 *     An implementation of {@link DMaaPMRPublisherQueue} which uses {@link java.util.concurrent.BlockingDeque}
 *     for batch and recovery queues
 * </p>
 *
 *
 * @author Rajiv Singla . Creation Date: 11/1/2016.
 */
public class DMaaPMRPublisherQueueImpl implements DMaaPMRPublisherQueue {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRPublisherQueueImpl.class);

    private final LinkedBlockingDeque<String> batchQueue;
    private final LinkedBlockingDeque<String> recoveryQueue;

    @Inject
    public DMaaPMRPublisherQueueImpl(@Assisted("batchQueueSize") int batchQueueSize,
                                     @Assisted("recoveryQueueSize") int recoveryQueueSize) {
        batchQueue = new LinkedBlockingDeque<>(batchQueueSize);
        recoveryQueue = new LinkedBlockingDeque<>(recoveryQueueSize);
        LOG.debug("Creating Instance of DMaaP Publisher Queue. BatchQueueSize: {}, RecoveryQueueSize: {}",
                batchQueueSize, recoveryQueueSize);
    }

    @Override
    public synchronized int addBatchMessages(List<String> batchMessages) {

        // checks if batchMessages size does not exceed batch queue capacity
        if (batchMessages.size() > batchQueue.remainingCapacity()) {
            throw new IllegalStateException("Not enough capacity to add batchMessages  in batch queue");
        }

        // Add batchMessages to batch queue
        for (String message : batchMessages) {
            batchQueue.add(message);
        }

        // returns current elements size in batch queue
        return batchQueue.size();
    }

    @Override
    public synchronized int addRecoverableMessages(List<String> recoverableMessages) {

        // checks if messages size does not exceed recovery queue size
        if (recoverableMessages.size() > recoveryQueue.remainingCapacity()) {
            throw new IllegalStateException("Not enough capacity to add messages in recovery queue");
        }

        // add messages to recovery queue
        for (String recoverableMessage : recoverableMessages) {
            recoveryQueue.add(recoverableMessage);
        }

        // returns current size of recovery queue
        return recoveryQueue.size();
    }

    @Override
    public synchronized List<String> getMessageForPublishing() {

        final List<String> recoveryMessageList = new LinkedList<>();
        final List<String> batchMessagesList = new LinkedList<>();

        // get messages from recovery queue if present
        if (!recoveryQueue.isEmpty()) {
            final int recoveryQueueSize = recoveryQueue.drainTo(recoveryMessageList);
            LOG.debug("Drained Recovery Queue elements for flushing: {}", recoveryQueueSize);
        }

        // get messages from batch queue if present
        if (!batchQueue.isEmpty()) {
            final int batchQueueSize = batchQueue.drainTo(batchMessagesList);
            LOG.debug("Drained Batch Queue elements for flushing: {}", batchQueueSize);
        }

        // concat recovery and batch queue elements
        return unmodifiableList(newLinkedList(concat(recoveryMessageList, batchMessagesList)));
    }

    @Override
    public synchronized int getBatchQueueRemainingSize() {
        return batchQueue.remainingCapacity();
    }

    @Override
    public synchronized int getRecoveryQueueRemainingSize() {
        return recoveryQueue.remainingCapacity();
    }
}
