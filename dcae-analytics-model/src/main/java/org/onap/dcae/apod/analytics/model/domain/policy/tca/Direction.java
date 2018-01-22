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

package org.onap.dcae.apod.analytics.model.domain.policy.tca;

import java.math.BigDecimal;

import javax.annotation.Nonnull;

/**
 * <p>
 *     Enum for Threshold Direction
 * </p>
 * @author Rajiv Singla . Creation Date: 11/5/2016.
 */
public enum Direction implements TCAPolicyModel {

    EQUAL {
        @Override
        public Boolean operate(@Nonnull BigDecimal value1, @Nonnull BigDecimal value2) {
            return value1.compareTo(value2) == 0;
        }
    },
    LESS {
        @Override
        public Boolean operate(@Nonnull BigDecimal value1, @Nonnull BigDecimal value2) {
            return value1.compareTo(value2) < 0;
        }
    },
    LESS_OR_EQUAL {
        @Override
        public Boolean operate(@Nonnull BigDecimal value1, @Nonnull BigDecimal value2) {
            return value1.compareTo(value2) <= 0;
        }
    },
    GREATER {
        @Override
        public Boolean operate(@Nonnull BigDecimal value1, @Nonnull BigDecimal value2) {
            return value1.compareTo(value2) > 0;
        }
    },
    GREATER_OR_EQUAL {
        @Override
        public Boolean operate(@Nonnull BigDecimal value1, @Nonnull BigDecimal value2) {
            return value1.compareTo(value2) >= 0;
        }
    };

    /**
     * Configure logic for a particular Direction
     *
     * @param value1 left operand for Direction operation
     * @param value2 right operand for Direction operation
     *
     * @return result of operation for the direction logic
     */
    public abstract Boolean operate(@Nonnull BigDecimal value1, @Nonnull BigDecimal value2);

}
