/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.common.statements.structures.selectors;

/**
 * A floating point selector.
 */
public class FloatingPointSelector extends Selector {

    /**
     * Double precision value.
     */
    private final double value;

    /**
     * Class constructor.
     *
     * @param value A double value.
     */
    public FloatingPointSelector(String value) {
        this.value = Double.valueOf(value);
    }

    public FloatingPointSelector(double value) {
        this.value = value;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.FLOATING_POINT;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FloatingPointSelector that = (FloatingPointSelector) o;

        if (Double.compare(that.value, value) != 0) {
            return false;
        }
        if (!alias.equals(that.alias)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        long temp;
        int result = 1;
        if (alias != null){
            result = alias.hashCode();
        }
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
