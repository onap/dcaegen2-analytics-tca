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
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.onap.dcae.apod.analytics.dmaap.service.publisher.DMaaPMRPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * A simple implementation of {@link RecordWriter} which writes messages to DMaaP MR topic
 * <p>
 * @author Rajiv Singla . Creation Date: 1/27/2017.
 */
public class DMaaPMRRecordWriter extends RecordWriter<String, NullWritable> {

    private static final Logger LOG = LoggerFactory.getLogger(DMaaPMRRecordWriter.class);

    private final DMaaPMRPublisher dMaaPMRPublisher;

    public DMaaPMRRecordWriter(DMaaPMRPublisher dMaaPMRPublisher) {
        this.dMaaPMRPublisher = dMaaPMRPublisher;
    }

    @Override
    public void write(String message, NullWritable value) throws IOException, InterruptedException {
        LOG.debug("Writing message to DMaaP MR Topic: {}", message);
        dMaaPMRPublisher.publish(Arrays.asList(message));
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        dMaaPMRPublisher.flush();
    }
}
