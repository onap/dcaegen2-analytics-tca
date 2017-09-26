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

package org.openecomp.dcae.apod.analytics.cdap.tca.worker;

import co.cask.cdap.api.annotation.Property;
import co.cask.cdap.api.worker.AbstractWorker;
import co.cask.cdap.api.worker.WorkerContext;
import com.fasterxml.jackson.core.type.TypeReference;
import org.openecomp.dcae.apod.analytics.cdap.tca.settings.TCAAppPreferences;
import org.openecomp.dcae.apod.analytics.cdap.tca.utils.CDAPTCAUtils;
import org.openecomp.dcae.apod.analytics.common.exception.DCAEAnalyticsRuntimeException;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils.readValue;
import static org.openecomp.dcae.apod.analytics.tca.utils.TCAUtils.writeValueAsString;

/**
 * CDAP Worker which mocks fetching VES Messages from DMaaP MR topic.
 * The mock instead of making DMaaP MR calls will actually take messages
 * from file and send them to stream at subscriber polling interval
 *
 * TODO: To be removed before going to production - only for testing purposes
 *
 * @author Rajiv Singla . Creation Date: 11/4/2016.
 */
public class TCADMaaPMockSubscriberWorker extends AbstractWorker {

    private static final Logger LOG = LoggerFactory.getLogger(TCADMaaPMockSubscriberWorker.class);

    // TODO: Remove this file before going to production - only for mocking purposes
    private static final String MOCK_MESSAGE_FILE_LOCATION = "ves_mock_messages.json";
    private static final TypeReference<List<EventListener>> EVENT_LISTENER_TYPE_REFERENCE =
            new TypeReference<List<EventListener>>() {
            };

    private TCAAppPreferences tcaAppPreferences;
    private boolean stopSendingMessages;
    @Property
    private final String tcaSubscriberOutputStreamName;

    public TCADMaaPMockSubscriberWorker(final String tcaSubscriberOutputStreamName) {
        this.tcaSubscriberOutputStreamName = tcaSubscriberOutputStreamName;
    }

    @Override
    public void configure() {
        setName("MockTCASubscriberWorker");
        setDescription("Writes Mocked VES messages to CDAP Stream");
        LOG.info("Configuring Mock TCA MR DMaaP Subscriber worker with name: {}", "MockTCASubscriberWorker");
    }

    @Override
    public void initialize(WorkerContext context) throws Exception {
        super.initialize(context);

        final TCAAppPreferences appPreferences = CDAPTCAUtils.getValidatedTCAAppPreferences(context);
        LOG.info("Initializing Mock TCA MR DMaaP Subscriber worker with preferences: {}", appPreferences);
        this.tcaAppPreferences = appPreferences;
        this.stopSendingMessages = false;
    }


    @Override
    public void run() {
        final Integer subscriberPollingInterval = tcaAppPreferences.getSubscriberPollingInterval();
        LOG.debug("Mock TCA Subscriber Polling interval: {}", subscriberPollingInterval);

        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream
                (MOCK_MESSAGE_FILE_LOCATION);

        if (resourceAsStream == null) {
            LOG.error("Unable to find file at location: {}", MOCK_MESSAGE_FILE_LOCATION);
            throw new DCAEAnalyticsRuntimeException("Unable to find file", LOG, new FileNotFoundException());
        }


        try {
            List<EventListener> eventListeners = readValue(resourceAsStream, EVENT_LISTENER_TYPE_REFERENCE);

            final int totalMessageCount = eventListeners.size();
            LOG.debug("Mock message count to be written to cdap stream: ()", totalMessageCount);

            int i = 1;
            for (EventListener eventListener : eventListeners) {
                if (stopSendingMessages) {
                    LOG.debug("Stop sending messages......");
                    break;
                }
                final String eventListenerString = writeValueAsString(eventListener);
                LOG.debug("=======>> Writing message to cdap stream no: {} of {}", i, totalMessageCount);
                getContext().write(tcaSubscriberOutputStreamName, eventListenerString);
                i++;

                try {
                    Thread.sleep(subscriberPollingInterval);
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

    @Override
    public void stop() {
        stopSendingMessages = true;
    }
}
