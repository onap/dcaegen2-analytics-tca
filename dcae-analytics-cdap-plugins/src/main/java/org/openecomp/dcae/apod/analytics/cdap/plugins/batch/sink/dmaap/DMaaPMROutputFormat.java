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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.openecomp.dcae.apod.analytics.cdap.plugins.utils.DMaaPSinkConfigMapper;
import org.openecomp.dcae.apod.analytics.dmaap.DMaaPMRFactory;
import org.openecomp.dcae.apod.analytics.dmaap.domain.config.DMaaPMRPublisherConfig;
import org.openecomp.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;

import java.io.IOException;

/**
 * DMaaP MR Output format used by DMaaP MR Sink Plugin to create a MR Publisher and pass to custom {@link
 * DMaaPMRRecordWriter}
 * <p>
 * @author Rajiv Singla . Creation Date: 1/27/2017.
 */
public class DMaaPMROutputFormat extends OutputFormat<String, NullWritable> {

    @Override
    public RecordWriter<String, NullWritable> getRecordWriter(TaskAttemptContext context) throws IOException,
            InterruptedException {
        final Configuration configuration = context.getConfiguration();
        final DMaaPMRPublisherConfig publisherConfig = DMaaPSinkConfigMapper.map(configuration);
        final DMaaPMRPublisher publisher = DMaaPMRFactory.create().createPublisher(publisherConfig);
        return new DMaaPMRRecordWriter(publisher);
    }

    @Override
    public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {
        // do nothing
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
        return new NoOpOutputCommitter();
    }

    /**
     * A dummy implementation for {@link OutputCommitter} that does nothing.
     */
    protected static class NoOpOutputCommitter extends OutputCommitter {

        @Override
        public void setupJob(JobContext jobContext) throws IOException {
            // no op
        }

        @Override
        public void setupTask(TaskAttemptContext taskContext) throws IOException {
            // no op
        }

        @Override
        public boolean needsTaskCommit(TaskAttemptContext taskContext) throws IOException {
            return false;
        }

        @Override
        public void commitTask(TaskAttemptContext taskContext) throws IOException {
            // no op
        }

        @Override
        public void abortTask(TaskAttemptContext taskContext) throws IOException {
            // no op
        }
    }
}
