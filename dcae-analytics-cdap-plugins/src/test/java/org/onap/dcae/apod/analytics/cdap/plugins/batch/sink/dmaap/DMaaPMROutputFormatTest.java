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

package org.onap.dcae.apod.analytics.cdap.plugins.batch.sink.dmaap;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.onap.dcae.apod.analytics.cdap.plugins.BaseAnalyticsCDAPPluginsUnitTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv Singla . Creation Date: 1/30/2017.
 */
public class DMaaPMROutputFormatTest extends BaseAnalyticsCDAPPluginsUnitTest {

    private DMaaPMROutputFormat dMaaPMROutputFormat;

    @Before
    public void before() {
        dMaaPMROutputFormat = new DMaaPMROutputFormat();
    }

    @Test
    public void testGetRecordWriter() throws Exception {
        final TaskAttemptContext taskAttemptContext = Mockito.mock(TaskAttemptContext.class);
        when(taskAttemptContext.getConfiguration()).thenReturn(getTestConfiguration());
        final RecordWriter<String, NullWritable> recordWriter = dMaaPMROutputFormat.getRecordWriter(taskAttemptContext);
        assertNotNull(recordWriter);
        final JobContext jobContext = Mockito.mock(JobContext.class);
        dMaaPMROutputFormat.checkOutputSpecs(jobContext);
    }

    @Test
    public void testGetOutputCommitter() throws Exception {
        final TaskAttemptContext taskAttemptContext = Mockito.mock(TaskAttemptContext.class);
        final OutputCommitter outputCommitter = dMaaPMROutputFormat.getOutputCommitter(taskAttemptContext);
        assertTrue(outputCommitter.getClass().equals(DMaaPMROutputFormat.NoOpOutputCommitter.class));
        final JobContext jobContext = Mockito.mock(JobContext.class);
        outputCommitter.setupJob(jobContext);
        outputCommitter.setupTask(taskAttemptContext);
        assertFalse(outputCommitter.needsTaskCommit(taskAttemptContext));
        outputCommitter.commitJob(jobContext);
        outputCommitter.commitTask(taskAttemptContext);
        outputCommitter.abortTask(taskAttemptContext);

    }

}
