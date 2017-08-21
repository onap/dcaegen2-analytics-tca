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

package org.openecomp.dcae.apod.analytics.cdap.common.settings;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.openecomp.dcae.apod.analytics.cdap.common.BaseAnalyticsCDAPCommonUnitTest;
import org.openecomp.dcae.apod.analytics.cdap.common.CDAPComponentsConstants;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Rajiv Singla . Creation Date: 12/12/2016.
 */
public class CDAPBaseAppConfigTest extends BaseAnalyticsCDAPCommonUnitTest {

    private CDAPBaseAppConfigImp cdapBaseAppConfigImp = null;

    @Before
    public void before() {
        cdapBaseAppConfigImp = new CDAPBaseAppConfigImp();
    }

    @Test
    public void testGetAppName() throws Exception {
        assertThat("Common Default Name must match",
                cdapBaseAppConfigImp.getAppName(),
                CoreMatchers.is(CDAPComponentsConstants.COMMON_DEFAULT_DCAE_CDAP_NAME_APP));
    }

    @Test
    public void testGetAppDescription() throws Exception {
        assertThat("Default App Description must match",
                cdapBaseAppConfigImp.getAppDescription(),
                is(CDAPComponentsConstants.COMMON_DEFAULT_DCAE_CDAP_DESCRIPTION_APP));
    }

}
