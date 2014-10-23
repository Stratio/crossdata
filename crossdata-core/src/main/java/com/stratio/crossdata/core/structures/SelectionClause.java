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
import java.util.List;

import com.stratio.crossdata.common.statements.structures.SelectorGroupBy;
import com.stratio.crossdata.common.statements.structures.SelectorIdentifier;
import com.stratio.crossdata.common.statements.structures.SelectorMeta;
import com.stratio.crossdata.common.data.TableName;

public abstract class SelectionClause {

    public static final int TYPE_SELECTION = 1;
    public static final int TYPE_COUNT = 2;

    protected int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public abstract List<SelectorGroupBy> getSelectorsGroupBy();

    public abstract void addTablename(TableName tablename);

    public abstract List<String> getIds();

    @Override
    public abstract String toString();

    public List<String> getFields() {
        List<String> ids = new ArrayList<>();
        if (type == TYPE_COUNT) {
            return ids;
        }
        SelectionList sList = (SelectionList) this;
        Selection selection = sList.getSelection();
        if (selection.getType() == Selection.TYPE_ASTERISK) {
            ids.add("*");
            return ids;
        }
        SelectionSelectors sSelectors = (SelectionSelectors) selection;
        for (SelectionSelector sSelector : sSelectors.getSelectors()) {
            SelectorMeta selector = sSelector.getSelector();
            if (selector.getType() == SelectorMeta.TYPE_IDENT) {
                SelectorIdentifier selectorId = (SelectorIdentifier) selector;
                ids.add(selectorId.getField());
            }
        }
        return ids;
    }

    /**
     * Checks whether the selection clause contains some function or not.
     *
     * @return true, if functions are used; false, otherwise.
     */
    public abstract boolean containsFunctions();
}
