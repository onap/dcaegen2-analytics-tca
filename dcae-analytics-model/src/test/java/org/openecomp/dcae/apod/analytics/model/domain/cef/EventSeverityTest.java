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

package org.openecomp.dcae.apod.analytics.model.domain.cef;

import static org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity.CRITICAL;
import static org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity.MAJOR;
import static org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity.MINOR;
import static org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity.NORMAL;
import static org.openecomp.dcae.apod.analytics.model.domain.cef.EventSeverity.WARNING;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.test.BaseDCAEAnalyticsUnitTest;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/10/2016.
 */
public class EventSeverityTest extends BaseDCAEAnalyticsUnitTest {

    private static Comparator<EventSeverity> eventSeverityComparator = new Comparator<EventSeverity>() {
        @Override
        public int compare(EventSeverity eventSeverity1, EventSeverity eventSeverity2) {
            return eventSeverity1.compareTo(eventSeverity2);
        }
    };

    @Test
    public void testEventSeverityOrdering() throws Exception {

        List<EventSeverity> eventSeverities = new LinkedList<>();
        Collections.addAll(eventSeverities,
            NORMAL,
            WARNING,
            MINOR,
            MAJOR,
            CRITICAL);

        Collections.sort(eventSeverities);

        List<EventSeverity> expectedEventSeverities = new LinkedList<>();
        Collections.addAll(expectedEventSeverities,
            CRITICAL,
            MAJOR,
            MINOR,
            WARNING,
            NORMAL
        );

        Assert.assertTrue("Severity Order must be CRITICAL, MAJOR, MINOR, WARNING, NORMAL",
            eventSeverities.equals(expectedEventSeverities));
    }
}
