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
import org.openecomp.dcae.apod.analytics.model.domain.cef.Field;
import org.openecomp.dcae.apod.analytics.model.domain.cef.NamedArrayOfFields;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 10/18/2016.
 */
public class EventListenerMixinTest extends BaseAnalyticsModelUnitTest {

    final String eventListenerJsonFileLocation = "data/json/cef/cef_message.json";
    final String cefMessagesJsonFileLocation = "data/json/cef/cef_messages.json";

    @Test
    public void testEventListenerJsonConversions() throws Exception {

        final EventListener eventListener = assertJsonConversions(eventListenerJsonFileLocation, EventListener.class);

        final List<Field> additionalFields =
                eventListener.getEvent().getMeasurementsForVfScalingFields().getAdditionalFields();

        assertThat("Additional Fields size must be 2", additionalFields.size(), is(2));

        final List<NamedArrayOfFields> additionalMeasurements =
                eventListener.getEvent().getMeasurementsForVfScalingFields().getAdditionalMeasurements();

        assertThat("Additional Measurements size must be 1", additionalMeasurements.size(), is(1));

        final List<Field> arrayOfFields = additionalMeasurements.get(0).getArrayOfFields();

        assertThat("Array Of Field size must be 6", arrayOfFields.size(), is(6));
    }

    @Test
    public void testCollectionOfEventListenersJsonConversion() throws Exception {

        final String cefMessageAsString = fromStream(cefMessagesJsonFileLocation);

        final TypeReference<List<EventListener>> eventListenerListTypeReference =
                new TypeReference<List<EventListener>>() {
                };
        List<EventListener> eventListeners = objectMapper.readValue(cefMessageAsString, eventListenerListTypeReference);
        assertThat("Event Listeners size must be 31", eventListeners.size(), is(31));

        // Check serialized json will match deserialized json
        final String eventListenerString = objectMapper.writeValueAsString(eventListeners);
        assertJson(cefMessageAsString, eventListenerString);

        // Checks serialization
        testSerialization(eventListeners, getClass());

    }

}
