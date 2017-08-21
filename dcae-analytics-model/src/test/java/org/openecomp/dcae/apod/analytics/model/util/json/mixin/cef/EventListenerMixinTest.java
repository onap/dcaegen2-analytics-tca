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

package org.openecomp.dcae.apod.analytics.model.util.json.mixin.cef;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.model.BaseAnalyticsModelUnitTest;
import org.openecomp.dcae.apod.analytics.model.domain.cef.EventListener;
import org.openecomp.dcae.apod.analytics.model.domain.cef.MeasurementsForVfScalingFields;
import org.openecomp.dcae.apod.analytics.model.domain.cef.VNicUsageArray;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 10/18/2016.
 */
public class EventListenerMixinTest extends BaseAnalyticsModelUnitTest {

    final String eventListenerJsonFileLocation = "data/json/cef/event_listener.json";
    final String cefMessagesJsonFileLocation = "data/json/cef/cef_messages.json";

    @Test
    public void testEventListenerJsonConversions() throws Exception {

        final EventListener eventListener = assertJsonConversions(eventListenerJsonFileLocation, EventListener.class);

        Map<String, Object> dynamicProperties = eventListener.getDynamicProperties();

        assertThat("Dynamic Properties size must be 1", dynamicProperties.size(), is(1));


    }

    @Test
    public void testCollectionOfEventListenersJsonConversion() throws Exception {

        final String cefMessageAsString = fromStream(cefMessagesJsonFileLocation);

        final TypeReference<List<EventListener>> eventListenerListTypeReference =
                new TypeReference<List<EventListener>>() {
                };
        List<EventListener> eventListeners = objectMapper.readValue(cefMessageAsString, eventListenerListTypeReference);
        assertThat("Event Listeners size must be 350", eventListeners.size(), is(350));

        final MeasurementsForVfScalingFields measurementsForVfScalingFields = eventListeners.get(0).getEvent()
                .getMeasurementsForVfScalingFields();

        // Note: vNicUsageArray - due to odd naming convention have to be explicitly resolved with Mixin annotations
        assertThat("vNicUsageArray is present on the first measurementForVfScaling",
                measurementsForVfScalingFields.getVNicUsageArray().size(), is(1));
        final VNicUsageArray vNicUsageArray = measurementsForVfScalingFields.getVNicUsageArray().get(0);
        assertThat("ByesIn is present on vNicUsageArray", vNicUsageArray.getBytesIn(), is(6086L));

        // Note: vNicIdentifier - due to odd naming convention have to be explicity resolved with Mixin annotations
        assertThat("vNicIdentifier is present on vNicUsageArray", vNicUsageArray.getVNicIdentifier(), is("eth0"));

        // Check serialized json will match deserialized json
        final String eventListenerString = objectMapper.writeValueAsString(eventListeners);
        assertJson(cefMessageAsString, eventListenerString);

        // Checks serialization
        testSerialization(eventListeners, getClass());

    }

}
