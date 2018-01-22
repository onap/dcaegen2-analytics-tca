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

package org.onap.dcae.apod.analytics.aai.service;

import java.util.Map;

/**
 * <p>
 * A client used to get enrichment details from A&AI
 * </p>
 *
 * @author Rajiv Singla . Creation Date: 9/15/2017.
 */
public interface AAIEnrichmentClient {

    /**
     * Provides enrichment details from A&AI API and returns them as string. If no enrichment lookup fails returns null
     *
     * @param aaiAPIPath A&AI API Path
     * @param queryParams A&AI Query Params map
     * @param headers A&AI HTTP Headers
     *
     * @return Enrichment details from A&AI API and returns them as string. If enrichment lookup fails returns null
     */
    String getEnrichmentDetails(String aaiAPIPath, Map<String, String> queryParams, Map<String, String> headers);

}
