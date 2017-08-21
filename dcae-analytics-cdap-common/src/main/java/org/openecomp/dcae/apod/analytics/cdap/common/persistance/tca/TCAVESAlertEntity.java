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

package org.openecomp.dcae.apod.analytics.cdap.common.persistance.tca;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Rajiv Singla . Creation Date: 11/16/2016.
 */
public class TCAVESAlertEntity implements Writable, Serializable {

    private static final long serialVersionUID = 1L;

    private long creationTS;
    private String alertMessage;

    public TCAVESAlertEntity() {
        // no argument constructor required for json serialization / deserialization
    }

    public TCAVESAlertEntity(long creationTS, String alertMessage) {
        this.creationTS = creationTS;
        this.alertMessage = alertMessage;
    }

    public long getCreationTS() {
        return creationTS;
    }

    public void setCreationTS(long creationTS) {
        this.creationTS = creationTS;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        WritableUtils.writeVLong(dataOutput, creationTS);
        WritableUtils.writeString(dataOutput, alertMessage);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        creationTS = WritableUtils.readVLong(dataInput);
        alertMessage = WritableUtils.readString(dataInput);
    }
}
