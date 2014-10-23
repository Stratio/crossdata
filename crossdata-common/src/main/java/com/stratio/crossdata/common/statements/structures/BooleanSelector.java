/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.statements.structures;

/**
 * Boolean selector.
 */
public class BooleanSelector extends Selector {

    /**
     * Boolean value.
     */
    private final boolean value;

    /**
     * Class constructor.
     *
     * @param value The boolean value.
     */
    public BooleanSelector(boolean value) {
        this.value = value;
    }

    public BooleanSelector(String value) {
        this.value = Boolean.valueOf(value);
    }

    /**
     * Get the value of this selector.
     *
     * @return A boolean value.
     */
    public boolean getValue() {
        return value;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.BOOLEAN;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BooleanSelector that = (BooleanSelector) o;

        if (value != that.value) {
            return false;
        }
        if (!alias.equals(that.alias)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        if (alias != null){
            result = alias.hashCode();
        }
        result = 31 * result + (value ? 1 : 0);
        return result;
    }
}
