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
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import org.openecomp.dcae.apod.analytics.cdap.plugins.domain.config.dmaap.DMaaPMRSourcePluginConfig;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.CDAPPluginUtils;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.DMaaPSourceConfigMapper;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.openecomp.dcae.apod.analytics.dmaap.service.subscriber.DMaaPMRSubscriber;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openecomp.dcae.apod.analytics.model.util.AnalyticsModelJsonUtils.readValue;
import static org.openecomp.dcae.apod.analytics.model.util.AnalyticsModelJsonUtils.writeValueAsString;

/**
 * DMaaP MR Receiver which calls DMaaP MR Topic and stores structured records
 * <p>
 * @author Rajiv Singla . Creation Date: 1/19/2017.
 */
public class MockDMaaPMRReceiver extends Receiver<StructuredRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(MockDMaaPMRReceiver.class);
    private static final long serialVersionUID = 1L;

    private static final String MOCK_MESSAGE_FILE_LOCATION = "ves_mock_messages.json";
    private static final TypeReference<List<EventListener>> EVENT_LISTENER_TYPE_REFERENCE =
            new TypeReference<List<EventListener>>() {
            };

    private final DMaaPMRSourcePluginConfig pluginConfig;

    public MockDMaaPMRReceiver(final StorageLevel storageLevel, final DMaaPMRSourcePluginConfig pluginConfig) {
        super(storageLevel);
        this.pluginConfig = pluginConfig;
        LOG.debug("Created DMaaP MR Receiver instance with plugin Config: {}", pluginConfig);
    }

    @Override
    public void onStart() {

        // create DMaaP MR Subscriber
        final DMaaPMRSubscriber subscriber =
                DMaaPMRFactory.create().createSubscriber(DMaaPSourceConfigMapper.map(pluginConfig));
        storeStructuredRecords(subscriber);

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
        
        try (InputStream resourceAsStream =
                     Thread.currentThread().getContextClassLoader().getResourceAsStream(MOCK_MESSAGE_FILE_LOCATION)) {

            if (resourceAsStream == null) {
                LOG.error("Unable to find file at location: {}", MOCK_MESSAGE_FILE_LOCATION);
                throw new DCAEAnalyticsRuntimeException("Unable to find file", LOG, new FileNotFoundException());
            }

            List<EventListener> eventListeners = readValue(resourceAsStream, EVENT_LISTENER_TYPE_REFERENCE);

            final int totalMessageCount = eventListeners.size();
            LOG.debug("Mock message count to be written to cdap stream: ()", totalMessageCount);

            int i = 1;
            for (EventListener eventListener : eventListeners) {
                if (isStopped()) {
                    return;
                }
                final String eventListenerString = writeValueAsString(eventListener);
                LOG.debug("=======>> Writing message to cdap stream no: {} of {}", i, totalMessageCount);
                store(CDAPPluginUtils.createDMaaPMRResponseStructuredRecord(eventListenerString));
                i++;
                try {
                    TimeUnit.MILLISECONDS.sleep(pluginConfig.getPollingInterval());
                } catch (InterruptedException e) {
                    LOG.error("Error while sleeping");
                    throw new DCAEAnalyticsRuntimeException("Error while sleeping", LOG, e);
                }

            }

            LOG.debug("Finished writing mock messages to CDAP Stream");

        } catch (IOException e) {
            LOG.error("Error while parsing json file");
            throw new DCAEAnalyticsRuntimeException("Error while parsing mock json file", LOG, e);
        }
    }

}
