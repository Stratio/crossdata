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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Class that contains the list of elements requested by the user in a SELECT statement. For
 * example, given the following statement: SELECT a, b as z, includes(c) FROM t this class contains
 * a list with a, b as z, includes(c).
 */
public class SelectExpression implements Serializable {

    /**
     * List of selectors.
     */
    private final List<Selector> selectorList;

    public SelectExpression(List<Selector> selectorList) {
        this.selectorList = selectorList;
    }

    public List<Selector> getSelectorList() {
        return selectorList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SelectExpression that = (SelectExpression) o;

        if (selectorList != null ? !selectorList.equals(that.selectorList) : that.selectorList != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return selectorList != null ? selectorList.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Selector> selectors = selectorList.iterator();
        while (selectors.hasNext()) {
            sb.append(selectors.next().toString());
            if (selectors.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
