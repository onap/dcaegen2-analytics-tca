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

package org.openecomp.dcae.apod.analytics.aai.it;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.aai.AAIClientFactory;
import org.openecomp.dcae.apod.analytics.aai.service.AAIEnrichmentClient;

import java.util.Collections;
import java.util.Map;

/**
 * @author Rajiv Singla . Creation Date: 9/19/2017.
 */
@Ignore
public class AAIClientFactoryIT extends BaseAnalyticsAAIIT {

    private AAIEnrichmentClient enrichmentClient;

    @Before
    public void before() {
        enrichmentClient = AAIClientFactory.create().getEnrichmentClient(getAAIHttpClientConfig());
    }

    @Test
    public void testGetEnrichmentClientForVNF() throws Exception {
        final String vnfName = "vCPEInfraVNF13";
        Map<String, String> queryParams = ImmutableMap.of("vnf-name", vnfName);
        final String vCPEInfraVNF13 =
                enrichmentClient.getEnrichmentDetails(AAI_VNF_ENRICHMENT_PATH, queryParams, AAI_HEADERS);
        LOG.info("Fetched VNF A&AI Enrichment Response: \n{}", vCPEInfraVNF13);

    }

    @Test
    public void testGetVServerObjectResourceLink() throws Exception {
        final String vServerName = "example-vserver-name-val-2";
        Map<String, String> queryParams = ImmutableMap.of("search-node-type", "vserver", "filter",
                "vserver-name:EQUALS:" + vServerName);
        final String serverResourceLink =
                enrichmentClient.getEnrichmentDetails(AAI_VSERVER_NODE_QUERY_PATH, queryParams, AAI_HEADERS);

        LOG.info("Fetched Vserver Object Resource Link A&AI Enrichment Response: \n{}", serverResourceLink);

    }

    @Test
    public void testGetVServerEnrichmentDetails() throws Exception {
        final String serverResourceLink = fromStream(AAI_VSERVER_QUERY_RESPONSE_LOCATION);
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode jsonNode = objectMapper.readTree(serverResourceLink);
        final String resourceLink = jsonNode.findPath("resource-link").asText();
        final String vServerEnrichmentDetails =
                enrichmentClient.getEnrichmentDetails(resourceLink, Collections.<String, String>emptyMap(),
                        AAI_HEADERS);
        LOG.info("Fetched Vserver enrichment details: \n{}", vServerEnrichmentDetails);
    }
}
