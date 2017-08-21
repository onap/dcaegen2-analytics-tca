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

import com.google.common.collect.ImmutableSet;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.openecomp.dcae.apod.analytics.common.service.processor.AbstractMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * A Generic Json Message Filter which filter the json message based on given json Path and list of expected values
 * for that json path. The {@link JsonMessageFilterProcessorContext#isMatched} flag will be changed as per table below:
 * <pre>
 *      Incoming message is blank or invalid Json                               =  null
 *      Incoming message path is matches expected values                        = true
 *      Incoming message does not match expected values or path does not exist  = false
 * </pre>
 * <p>
 * @author Rajiv Singla . Creation Date: 2/10/2017.
 */
public class GenericJsonMessageFilter extends AbstractMessageProcessor<JsonMessageFilterProcessorContext> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericJsonMessageFilter.class);
    private static final long serialVersionUID = 1L;

    private final String filterName;
    private final String jsonPath;
    private final Set<String> expectedValues;

    public GenericJsonMessageFilter(final String filterName, final String jsonPath, final Set<String> expectedValues) {
        this.filterName = filterName;
        this.jsonPath = jsonPath;
        this.expectedValues = expectedValues;
    }

    public GenericJsonMessageFilter(final String filterName, final String jsonPath, final String expectedValue) {
        this(filterName, jsonPath, ImmutableSet.of(expectedValue));
    }

    @Override
    public String getProcessorDescription() {
        return filterName;
    }

    @Override
    public JsonMessageFilterProcessorContext processMessage(final JsonMessageFilterProcessorContext processorContext) {

        final String jsonMessage = processorContext.getMessage().trim();

        if (StringUtils.isNotBlank(jsonMessage) && jsonMessage.startsWith("{") && jsonMessage.endsWith("}")) {

            // locate json path value
            final DocumentContext documentContext = JsonPath.parse(jsonMessage);
            String jsonPathValue = null;
            try {
                jsonPathValue = documentContext.read(jsonPath, String.class);
            } catch (PathNotFoundException ex) {
                LOG.info("Unable to find json Path: {}. Exception: {}, Json Message: {}", jsonPath, ex, jsonMessage);
            }

            LOG.debug("Value for jsonPath: {}, jsonPathValue: {}, expected Values: {}",
                    jsonPath, jsonPathValue, expectedValues);

            // if json path value is null or we json value is not present in expect values then terminate early
            if (jsonPathValue == null || !expectedValues.contains(jsonPathValue)) {
                final String terminatingMessage = String.format("Filter match unsuccessful. " +
                                "JsonPath: %s, Actual JsonPathValue: %s, Excepted Json Path Values: %s",
                        jsonPath, jsonPathValue, expectedValues);
                processorContext.setMatched(false);
                setTerminatingProcessingMessage(terminatingMessage, processorContext);
            } else {
                final String finishProcessingMessage = String.format("Filter match successful. " +
                                "JsonPath: %s, Actual JsonPathValue: %s, Excepted Json Path Values: %s",
                        jsonPath, jsonPathValue, expectedValues);
                processorContext.setMatched(true);
                setFinishedProcessingMessage(finishProcessingMessage, processorContext);
            }
        } else {
            // if incoming message is blank of valid Json then matched flag will be null
            final String terminatingMessage = "Incoming json message is blank or not json. " +
                    "Json filter cannot be applied";
            processorContext.setMatched(null);
            setTerminatingProcessingMessage(terminatingMessage, processorContext);
        }

        return processorContext;
    }
}
