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
import java.util.Arrays;
import java.util.List;

import com.stratio.crossdata.common.statements.structures.selectors.GroupByFunction;
import com.stratio.crossdata.common.statements.structures.selectors.SelectorFunction;
import com.stratio.crossdata.common.statements.structures.selectors.SelectorGroupBy;
import com.stratio.crossdata.common.statements.structures.selectors.SelectorIdentifier;
import com.stratio.crossdata.common.statements.structures.selectors.SelectorMeta;
import com.stratio.crossdata.common.data.TableName;

public class SelectionList extends SelectionClause {

    private boolean distinct;
    private Selection selection;

    public SelectionList(boolean distinct, Selection selection) {
        this.type = TYPE_SELECTION;
        this.distinct = distinct;
        this.selection = selection;
    }

    public SelectionList(Selection selection) {
        this(false, selection);
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public int getTypeSelection() {
        return selection.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (distinct) {
            sb.append("DISTINCT ");
        }
        sb.append(selection.toString());
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.stratio.com.stratio.crossdata.core.structures.SelectionClause#getIds()
     */
    @Override
    public List<String> getIds() {

        List<String> ids = new ArrayList<>();

        Selection selection = this.getSelection();
        if (selection.getType() == Selection.TYPE_SELECTOR) {
            SelectionSelectors sSelectors = (SelectionSelectors) selection;
            for (SelectionSelector sSelector : sSelectors.getSelectors()) {
                SelectorMeta selector = sSelector.getSelector();
                if (selector.getType() == SelectorMeta.TYPE_IDENT) {
                    SelectorIdentifier selectorId = (SelectorIdentifier) selector;
                    ids.add(selectorId.toString());
                } else {
                    ids.addAll(retrieveIdsFromFunctionSelector(selector));
                }
            }
        }

        return ids;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.stratio.com.stratio.crossdata.core.structures.SelectionClause#getSelectorsGroupBy()
     */
    @Override
    public List<SelectorGroupBy> getSelectorsGroupBy() {

        List<SelectorGroupBy> selectorsList = new ArrayList<>();

        Selection selection = this.getSelection();
        if (selection.getType() == Selection.TYPE_SELECTOR) {

            SelectionSelectors selectors = (SelectionSelectors) selection;
            for (SelectionSelector selectionSelector : selectors.getSelectors()) {

                SelectorMeta selector = selectionSelector.getSelector();
                if (selector.getType() == SelectorMeta.TYPE_GROUPBY) {
                    SelectorGroupBy selectorGroupBy = (SelectorGroupBy) selector;
                    selectorsList.add(selectorGroupBy);
                }
            }
        }

        return selectorsList;
    }

    private List<String> retrieveIdsFromFunctionSelector(SelectorMeta selector) {

        List<String> ids = new ArrayList<>();
        if (selector instanceof SelectorGroupBy) {
            SelectorGroupBy selectorGroupBy = (SelectorGroupBy) selector;
            if (!selectorGroupBy.getGbFunction().equals(GroupByFunction.COUNT)) {
                ids.addAll(retrieveIdsFromFunctionSelector(selectorGroupBy.getParam()));
            }
        } else if (selector instanceof SelectorFunction) {
            SelectorFunction selectorFunction = (SelectorFunction) selector;
            List<SelectorMeta> params = selectorFunction.getParams();
            for (SelectorMeta subselector : params) {
                ids.addAll(retrieveIdsFromFunctionSelector(subselector));
            }
        } else {
            return Arrays.asList(((SelectorIdentifier) selector).getField());
        }

        return ids;
    }

    @Override
    public void addTablename(TableName tablename) {
        selection.addTablename(tablename);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.stratio.com.stratio.crossdata.core.structures.SelectionClause#containsFunctions()
     */
    @Override
    public boolean containsFunctions() {

        return this.selection.containsFunctions();
    }

}
