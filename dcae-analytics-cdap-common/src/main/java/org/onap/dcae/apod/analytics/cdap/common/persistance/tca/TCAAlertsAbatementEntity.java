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

package org.onap.dcae.apod.analytics.cdap.common.persistance.tca;

import com.google.common.base.Objects;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * TCA Alerts Abatement Entity is used to persist information to determine if abatement event need to sent to downstream
 * systems
 *
 *  @author Rajiv Singla . Creation Date: 9/11/2017.
 */
public class TCAAlertsAbatementEntity implements Writable, Serializable {

    private static final long serialVersionUID = 1L;

    private long creationTS;
    private String requestId;
    // Kept as string to avoid null checks
    private String abatementSentTS;

    /**
     * No Arg constructor required for Jackson Json Serialization / Deserialization
     */
    public TCAAlertsAbatementEntity() {
        // required no arg constructor
    }

    /**
     * Creates TCA Alerts Abatement Entity to persist information to determine if abatement alerts need to be posted
     *
     * @param creationTS record creation time
     * @param requestId request ID of generated alert
     * @param abatementSentTS time when abatement was sent out for that alert if any
     */
    public TCAAlertsAbatementEntity(long creationTS, String requestId, String abatementSentTS) {
        this.creationTS = creationTS;
        this.requestId = requestId;
        this.abatementSentTS = abatementSentTS;
    }

    /**
     * Timestamp when record was created
     *
     * @return timestamp when record was created
     */
    public long getCreationTS() {
        return creationTS;
    }

    /**
     * Set value for timestamp when record was created
     *
     * @param creationTS new value for timestamp when record was created
     */
    public void setCreationTS(long creationTS) {
        this.creationTS = creationTS;
    }

    /**
     * Request Id of ONSET alert which was sent
     *
     * @return request Id of ONSET alert which was sent
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Set Request Id of ONSET alert
     *
     * @param requestId set new value for ONSET alert request id
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    /**
     * Get abatement Sent Timestamp
     *
     * @return get abatement alert sent timestamp
     */
    public String getAbatementSentTS() {
        return abatementSentTS;
    }

    /**
     * Set timestamp when abatement alert is sent
     *
     * @param abatementSentTS sent new value for timestamp when abatement alert is sent
     */
    public void setAbatementSentTS(String abatementSentTS) {
        this.abatementSentTS = abatementSentTS;
    }

    /**
     * Write entity to Table
     *
     * @param dataOutput data output
     * @throws IOException io exception
     */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        WritableUtils.writeVLong(dataOutput, creationTS);
        WritableUtils.writeString(dataOutput, requestId);
        WritableUtils.writeString(dataOutput, abatementSentTS);
    }

    /**
     * Read entity from table
     *
     * @param dataInput data input
     * @throws IOException io exception
     */
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        creationTS = WritableUtils.readVLong(dataInput);
        requestId = WritableUtils.readString(dataInput);
        abatementSentTS = WritableUtils.readString(dataInput);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("creationTS", creationTS)
                .add("requestId", requestId)
                .add("abatementSentTS", abatementSentTS)
                .toString();
    }
}
