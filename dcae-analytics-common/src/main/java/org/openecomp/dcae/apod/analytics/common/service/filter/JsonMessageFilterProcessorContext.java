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

package org.openecomp.dcae.apod.analytics.common.service.filter;

import org.openecomp.dcae.apod.analytics.common.service.processor.AbstractProcessorContext;

/**
 * A processor context for Json Message Filter Processor
 * <p>
 * @author Rajiv Singla . Creation Date: 2/10/2017.
 */
public class JsonMessageFilterProcessorContext extends AbstractProcessorContext {

    private static final long serialVersionUID = 1L;

    private Boolean isMatched;

    public JsonMessageFilterProcessorContext(final String jsonMessageString) {
        super(jsonMessageString, true);
    }

    /**
     * Returns true if Json Message Filter match was successful
     *
     * @return true if Json Message Filter match was successful, false if filter was match was unsuccessful
     */
    public Boolean getMatched() {
        return isMatched;
    }

    /**
     * Sets the value for Json Message Filter match
     *
     * @param matched new value for json message filter match
     */
    public void setMatched(final Boolean matched) {
        isMatched = matched;
    }
}
