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

import org.junit.Test;
import org.openecomp.dcae.apod.analytics.dmaap.BaseAnalyticsDMaaPUnitTest;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/2/2016.
 */
public class DMaaPMRPublisherQueueImplTest extends BaseAnalyticsDMaaPUnitTest {


    @Test
    public void testAddBatchMessages() throws Exception {
        DMaaPMRPublisherQueue publisherQueue = new DMaaPMRPublisherQueueImpl(10, 20);
        // add two messages to batch queue
        final int batchMessagesSizeAfterFirstInsert = publisherQueue.addBatchMessages(getTwoSampleMessages());
        assertTrue("Batch Message Queue size must be 2", batchMessagesSizeAfterFirstInsert == 2);
        // add two more message to batch queue
        final int batchMessagesSizeAfterSecondInsert = publisherQueue.addBatchMessages(getTwoSampleMessages());
        assertTrue("Batch Message Queue size must be 4", batchMessagesSizeAfterSecondInsert == 4);
        // Now get all messages which must drain out batch queue
        final List<String> messagesToPublish = publisherQueue.getMessageForPublishing();
        assertTrue("There must be 4 messages to publish", messagesToPublish.size() == 4);
        assertTrue("Batch Queue must be empty", publisherQueue.getBatchQueueRemainingSize() == 10);

    }

    @Test(expected = IllegalStateException.class)
    public void testAddBatchMessagesWhenQueueSizeIsFull() throws Exception {
        DMaaPMRPublisherQueue publisherQueue = new DMaaPMRPublisherQueueImpl(2, 20);
        // add two messages to batch queue
        final int batchMessagesSizeAfterFirstInsert = publisherQueue.addBatchMessages(getTwoSampleMessages());
        assertTrue("Batch Message Queue size must be 2", batchMessagesSizeAfterFirstInsert == 2);
        // add 2 more messages should now throw IllegalStateException
        publisherQueue.addBatchMessages(getTwoSampleMessages());
    }

    @Test
    public void testAddRecoverableMessages() throws Exception {
        DMaaPMRPublisherQueue publisherQueue = new DMaaPMRPublisherQueueImpl(10, 20);
        // add two messages to batch queue
        final int batchMessagesSizeAfterFirstInsert = publisherQueue.addBatchMessages(getTwoSampleMessages());
        assertTrue("Batch Message Queue size must be 2", batchMessagesSizeAfterFirstInsert == 2);
        // add two recoverable messages
        final int recoverableMessageSizeAfterFirstInsert =
                publisherQueue.addRecoverableMessages(getTwoSampleMessages());
        assertTrue("Recovery Message Queue size must be 2 after first insert",
                recoverableMessageSizeAfterFirstInsert == 2);
        // add two more recoverable messages
        final int recoverableMessageSizeAfterSecondInsert =
                publisherQueue.addRecoverableMessages(getTwoSampleMessages());
        assertTrue("Recovery Message Queue size must be 4 after second insert",
                recoverableMessageSizeAfterSecondInsert == 4);
        // Now get all messages which must drain out batch queue
        final List<String> messagesToPublish = publisherQueue.getMessageForPublishing();
        assertTrue("There must be 6 messages to publish", messagesToPublish.size() == 6);
        assertTrue("Batch Queue must be empty", publisherQueue.getBatchQueueRemainingSize() == 10);
        assertTrue("Recovery Queue must be empty", publisherQueue.getRecoveryQueueRemainingSize() == 20);
    }


    @Test(expected = IllegalStateException.class)
    public void testAddRecoverableMessagesWhenRecoveryQueueIsFull() throws Exception {
        DMaaPMRPublisherQueue publisherQueue = new DMaaPMRPublisherQueueImpl(10, 2);
        // add two messages to batch queue
        final int batchMessagesSizeAfterFirstInsert = publisherQueue.addBatchMessages(getTwoSampleMessages());
        assertTrue("Batch Message Queue size must be 2", batchMessagesSizeAfterFirstInsert == 2);
        // add two recoverable messages
        final int recoverableMessageSizeAfterFirstInsert =
                publisherQueue.addRecoverableMessages(getTwoSampleMessages());
        assertTrue("Recovery Message Queue size must be 2 after first insert",
                recoverableMessageSizeAfterFirstInsert == 2);
        // add two more recoverable messages which should throw IllegalStateException
        publisherQueue.addRecoverableMessages(getTwoSampleMessages());
    }

    @Test
    public void testGetMessageForPublishing() throws Exception {
        DMaaPMRPublisherQueue publisherQueue = new DMaaPMRPublisherQueueImpl(10, 20);
        // add two messages to batch queue
        final int batchMessagesSizeAfterFirstInsert = publisherQueue.addBatchMessages(getTwoSampleMessages());
        assertTrue("Batch Message Queue size must be 2", batchMessagesSizeAfterFirstInsert == 2);
        // add two recoverable messages
        final int recoverableMessageSizeAfterFirstInsert =
                publisherQueue.addRecoverableMessages(getTwoSampleMessages());
        assertTrue("Recovery Message Queue size must be 2 after first insert",
                recoverableMessageSizeAfterFirstInsert == 2);
        // add two more recoverable messages
        final int recoverableMessageSizeAfterSecondInsert =
                publisherQueue.addRecoverableMessages(getTwoSampleMessages());
        assertTrue("Recovery Message Queue size must be 4 after second insert",
                recoverableMessageSizeAfterSecondInsert == 4);
        // Now get all messages which must drain out batch queue
        final List<String> messagesToPublish = publisherQueue.getMessageForPublishing();
        assertTrue("There must be 6 messages to publish", messagesToPublish.size() == 6);
        // add two more batch and recovery messages
        final int batchQueueSize = publisherQueue.addBatchMessages(getTwoSampleMessages());
        final int recoveryQueueSize = publisherQueue.addRecoverableMessages(getTwoSampleMessages());
        final int messagePublishCount = publisherQueue.getMessageForPublishing().size();
        assertTrue("Batch Queue + Recovery Queue message total must batch publish message count",
                messagePublishCount == (batchQueueSize + recoveryQueueSize));
        assertTrue("Batch Queue must be empty", publisherQueue.getBatchQueueRemainingSize() == 10);
        assertTrue("Recovery Queue must be empty", publisherQueue.getRecoveryQueueRemainingSize() == 20);

    }

    @Test
    public void testGetBatchQueueRemainingSize() throws Exception {

        DMaaPMRPublisherQueue publisherQueue = new DMaaPMRPublisherQueueImpl(10, 20);
        // add two messages to batch queue
        final int batchMessagesSizeAfterFirstInsert = publisherQueue.addBatchMessages(getTwoSampleMessages());
        assertTrue("Batch Message Queue size must be 2", batchMessagesSizeAfterFirstInsert == 2);
        assertTrue("Batch remaining capacity should be reduced by 2",
                publisherQueue.getBatchQueueRemainingSize() == 8);

        // add two recoverable messages
        final int recoverableMessageSizeAfterFirstInsert =
                publisherQueue.addRecoverableMessages(getTwoSampleMessages());
        assertTrue("Recovery Message Queue size must be 2 after first insert",
                recoverableMessageSizeAfterFirstInsert == 2);

        // recoverable message should not change batch queue capacity
        assertTrue("Adding recoverable Message must not have any impact on batch queue remaining capacity ",
                publisherQueue.getBatchQueueRemainingSize() == 8);
        // Now get all messages which must drain out batch queue
        final List<String> messagesToPublish = publisherQueue.getMessageForPublishing();
        assertTrue("There must be exactly 4 messages to publish", messagesToPublish.size() == 4);

        // Batch queue remaining capacity should now match original batch size
        assertTrue("Batch Queue remaining capacity must match original batch queue size", publisherQueue
                .getBatchQueueRemainingSize() == 10);
    }

    @Test
    public void testGetRecoveryQueueRemainingSize() throws Exception {
        DMaaPMRPublisherQueue publisherQueue = new DMaaPMRPublisherQueueImpl(10, 20);

        // add two recoverable messages
        final int recoverableMessageSizeAfterFirstInsert =
                publisherQueue.addRecoverableMessages(getTwoSampleMessages());
        assertTrue("Recovery Message Queue size must be 2 after first insert",
                recoverableMessageSizeAfterFirstInsert == 2);
        assertTrue("Recovery Queue remaining capacity should be reduced by 2",
                publisherQueue.getRecoveryQueueRemainingSize() == 18);

        // add two messages to batch queue
        final int batchMessagesSizeAfterFirstInsert = publisherQueue.addBatchMessages(getTwoSampleMessages());
        assertTrue("Batch Message Queue size must be 2", batchMessagesSizeAfterFirstInsert == 2);

        // batch message should not change recoverable queue capacity
        assertTrue("Adding batch queue Message must not have any impact on recovery queue remaining capacity ",
                publisherQueue.getRecoveryQueueRemainingSize() == 18);

        // Now get all messages which must drain out recovery queue
        final List<String> messagesToPublish = publisherQueue.getMessageForPublishing();
        assertTrue("There must be exactly 4 messages to publish", messagesToPublish.size() == 4);

        // Recoverable queue remaining capacity should now match original recovery queue size
        assertTrue("Recoverable Queue remaining capacity must match original batch queue size", publisherQueue
                .getRecoveryQueueRemainingSize() == 20);
    }

}
