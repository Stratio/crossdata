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

import com.stratio.crossdata.common.statements.structures.SelectorMeta;
import com.stratio.crossdata.common.data.TableName;

public class SelectionSelector {

    private SelectorMeta selector;
    private boolean aliasInc;
    private String alias;

//TODO:javadoc
    public SelectionSelector(SelectorMeta selector, boolean aliasInc, String alias) {
        this.selector = selector;
        this.aliasInc = aliasInc;
        this.alias = alias;
    }

//TODO:javadoc
    public SelectionSelector(SelectorMeta selector) {
        this(selector, false, null);
    }

//TODO:javadoc
    public SelectorMeta getSelector() {
        return selector;
    }

//TODO:javadoc
    public void setSelector(SelectorMeta selector) {
        this.selector = selector;
    }

//TODO:javadoc
    public boolean isAliasInc() {
        return aliasInc;
    }

//TODO:javadoc
    public void setAliasInc(boolean aliasInc) {
        this.aliasInc = aliasInc;
    }

//TODO:javadoc
    public String getAlias() {
        return alias;
    }

//TODO:javadoc
    public void setAlias(String alias) {
        this.aliasInc = true;
        this.alias = alias;
    }

    @Override
//TODO:javadoc
    public String toString() {
        StringBuilder sb = new StringBuilder(selector.toString());
        if (aliasInc) {
            sb.append(" AS ").append(alias);
        }
        return sb.toString();
    }

//TODO:javadoc
    public void addTablename(TableName tablename) {
        selector.addTablename(tablename);
    }
}
