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

package org.openecomp.dcae.apod.analytics.model.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;

/**
 * @author Rajiv Singla . Creation Date: 12/16/2016.
 */
public class AnalyticsModelJsonUtilsTest extends BaseAnalyticsModelUnitTest {

    private static final TypeReference<List<EventListener>> EVENT_LISTENER_TYPE_REFERENCE =
        new TypeReference<List<EventListener>>() {
        };

    final String cefMessagesJsonFileLocation = "data/json/cef/cef_messages.json";
    final String eventListenerJsonFileLocation = "data/json/cef/event_listener.json";

    @Test
    public void testReadValueWithTypeReference() throws Exception {
        final InputStream resourceAsStream =
            Thread.currentThread().getContextClassLoader().getResourceAsStream(cefMessagesJsonFileLocation);
        List<EventListener> eventListeners = AnalyticsModelJsonUtils.readValue(resourceAsStream,
            EVENT_LISTENER_TYPE_REFERENCE);
        assertThat("Event Listeners size must be 350", eventListeners.size(), is(350));
    }

    @Test
    public void testReadValueWithClassType() throws Exception {
        final InputStream resourceAsStream =
            Thread.currentThread().getContextClassLoader().getResourceAsStream(eventListenerJsonFileLocation);
        final EventListener eventListener = AnalyticsModelJsonUtils.readValue(resourceAsStream, EventListener.class);
        assertNotNull("Event Listener event is not null", eventListener.getEvent());
    }

    @Test
    public void testWriteValueAsString() throws Exception {
        final InputStream resourceAsStream =
            Thread.currentThread().getContextClassLoader().getResourceAsStream(eventListenerJsonFileLocation);
        final EventListener eventListener = AnalyticsModelJsonUtils.readValue(resourceAsStream, EventListener.class);
        final String eventListenerString = AnalyticsModelJsonUtils.writeValueAsString(eventListener);
        assertJson(fromStream(eventListenerJsonFileLocation), eventListenerString);
    }

    @Test
    public void testReadValueWithJsonString() throws Exception {
        final EventListener eventListener =
            AnalyticsModelJsonUtils.readValue(fromStream(eventListenerJsonFileLocation), EventListener.class);
        assertNotNull("Event Listener event is not null", eventListener.getEvent());
    }
}
