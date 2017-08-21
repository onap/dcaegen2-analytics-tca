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

package org.openecomp.dcae.apod.analytics.cdap.plugins.batch.sink.dmaap;

import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openecomp.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;

import java.util.Arrays;

import static org.mockito.Mockito.times;

/**
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class DMaaPMRRecordWriterTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private DMaaPMRPublisher publisher;
    private DMaaPMRRecordWriter dMaaPMRRecordWriter;

    @Before
    public void before() {
        publisher = Mockito.mock(DMaaPMRPublisher.class);
        dMaaPMRRecordWriter = new DMaaPMRRecordWriter(publisher);
    }

    @Test
    public void testWrite() throws Exception {
        final String testMessage = "test Message";
        dMaaPMRRecordWriter.write(testMessage, null);
        Mockito.verify(publisher, times(1)).publish(Arrays.asList(testMessage));
    }

    @Test
    public void testClose() throws Exception {
        final TaskAttemptContext taskAttemptContext = Mockito.mock(TaskAttemptContext.class);
        dMaaPMRRecordWriter.close(taskAttemptContext);
        Mockito.verify(publisher, times(1)).flush();
    }

}
