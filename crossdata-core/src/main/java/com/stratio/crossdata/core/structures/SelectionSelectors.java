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

package com.stratio.crossdata.core.structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stratio.crossdata.common.statements.structures.selectors.SelectorIdentifier;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.common.data.TableName;

public class SelectionSelectors extends Selection {

    private List<SelectionSelector> selectors;

    public SelectionSelectors() {
        this.type = TYPE_SELECTOR;
        selectors = new ArrayList<>();
    }

    public SelectionSelectors(List<SelectionSelector> selectors) {
        this();
        this.selectors = selectors;
    }

    public List<SelectionSelector> getSelectors() {
        return selectors;
    }

    public void addSelectionSelector(SelectionSelector ss) {
        boolean found = false;
        Iterator<SelectionSelector> it = selectors.iterator();
        SelectionSelector s = null;
        while (it.hasNext()) {
            s = it.next();
            if (s.getSelector().toString().equalsIgnoreCase(ss.getSelector().toString())) {
                found = true;
            }
        }
        if (!found) {
            selectors.add(ss);
        }
    }

    @Override
    public String toString() {
        return StringUtils.stringList(selectors, ", ");
    }

    @Override
    public void addTablename(TableName tablename) {
        for (SelectionSelector selectionSelector : selectors) {
            selectionSelector.addTablename(tablename);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.stratio.com.stratio.crossdata.core.structures.Selection#containsFunctions()
     */
    @Override
    public boolean containsFunctions() {

        boolean containsFunction = false;
        Iterator<SelectionSelector> selectorsIt = selectors.iterator();

        while (!containsFunction && selectorsIt.hasNext()) {
            SelectionSelector selector = selectorsIt.next();

            if (!(selector.getSelector() instanceof SelectorIdentifier)) {
                containsFunction = true;
            }
        }

        return containsFunction;
    }
}
