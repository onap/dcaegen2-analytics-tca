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

package org.openecomp.dcae.apod.analytics.cdap.plugins.streaming.dmaap;

import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.metrics.Metrics;
import com.google.common.base.Optional;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import org.openecomp.dcae.apod.analytics.cdap.common.utils.DMaaPMRUtils;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.CDAPPluginUtils;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.DMaaPSourceConfigMapper;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DMaaP MR Receiver which calls DMaaP MR Topic and stores structured records
 * <p>
 * @author Rajiv Singla . Creation Date: 1/19/2017.
 */
public class DMaaPMRReceiver extends Receiver<StructuredRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRReceiver.class);
    private static final long serialVersionUID = 1L;

    private final DMaaPMRSourcePluginConfig pluginConfig;
    private final Metrics metrics;

    public DMaaPMRReceiver(final StorageLevel storageLevel, final DMaaPMRSourcePluginConfig pluginConfig,
                           final Metrics metrics) {
        super(storageLevel);
        this.pluginConfig = pluginConfig;
        this.metrics = metrics;
        LOG.debug("Created DMaaP MR Receiver instance with plugin Config: {}", pluginConfig);
    }

    @Override
    public void onStart() {

        // create DMaaP MR Subscriber
        final DMaaPMRSubscriber subscriber =
                DMaaPMRFactory.create().createSubscriber(DMaaPSourceConfigMapper.map(pluginConfig));

        // Start a new thread with indefinite loop until receiver is stopped
        new Thread() {
            @Override
            public void run() {
                while (!isStopped()) {
                    storeStructuredRecords(subscriber);
                    try {
                        final Integer pollingInterval = pluginConfig.getPollingInterval();
                        LOG.debug("DMaaP MR Receiver sleeping for polling interval: {}", pollingInterval);
                        TimeUnit.MILLISECONDS.sleep(pollingInterval);
                    } catch (InterruptedException e) {
                        final String errorMessage = String.format(
                                "Interrupted Exception while DMaaP MR Receiver sleeping polling interval: %s", e);
                        throw new DCAEAnalyticsRuntimeException(errorMessage, LOG, e);
                    }
                }
            }
        }.start();

    }

    @Override
    public void onStop() {
        LOG.debug("Stopping DMaaP MR Receiver with plugin config: {}", pluginConfig);
    }

    /**
     * Fetches records from DMaaP MR Subscriber and store them as structured records
     *
     * @param subscriber DMaaP MR Subscriber Instance
     */
    public void storeStructuredRecords(final DMaaPMRSubscriber subscriber) {

        LOG.debug("DMaaP MR Receiver start fetching messages from DMaaP MR Topic");

        // Fetch messages from DMaaP MR Topic
        final Optional<List<String>> subscriberMessagesOptional =
                DMaaPMRUtils.getSubscriberMessages(subscriber, metrics);

        // store records
        if (subscriberMessagesOptional.isPresent()) {
            final List<String> messages = subscriberMessagesOptional.get();
            for (final String message : messages) {
                store(CDAPPluginUtils.createDMaaPMRResponseStructuredRecord(message));
            }
            LOG.debug("Stored DMaaP Subscriber messages as Structured Records. Message count {}", messages.size());
        }
    }

}
