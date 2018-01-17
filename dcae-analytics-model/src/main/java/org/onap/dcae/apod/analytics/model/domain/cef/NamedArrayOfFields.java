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

package org.onap.dcae.apod.analytics.model.domain.cef;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * An array of name value pairs along with a name for the array
 * <p>
 * @author Rajiv Singla. Creation Date: 08/15/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NamedArrayOfFields extends BaseCEFModel {

    private static final long serialVersionUID = 1L;

    /**
     * Name of the NamedArrayOfFields
     *
     * @param name New name for the Field
     * @return Name of the Field
     */
    private String name;

    /**
     * Array of name value pairs
     *
     * @param arrayOfFields New value for array of name value pairs
     * @return Array of name value pairs
     */
    private List<Field> arrayOfFields;

}
