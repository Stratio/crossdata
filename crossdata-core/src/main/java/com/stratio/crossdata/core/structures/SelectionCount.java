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

import com.stratio.crossdata.common.statements.structures.selectors.SelectorGroupBy;
import com.stratio.crossdata.common.data.TableName;

public class SelectionCount extends SelectionClause {

    /**
     * The selection symbol.
     */
    private char symbol;
    private boolean identInc;
    private String identifier;

    /**
     * Class constructor.
     */
    public SelectionCount() {
        this.type = TYPE_COUNT;
    }

    public SelectionCount(char symbol, boolean identInc, String identifier) {
        this.type = TYPE_COUNT;
        this.symbol = symbol;
        this.identInc = identInc;
        this.identifier = identifier;
    }

    public SelectionCount(char symbol) {
        this(symbol, false, null);
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isIdentInc() {
        return identInc;
    }

    public void setIdentInc(boolean identInc) {
        this.identInc = identInc;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("COUNT(");
        sb.append(symbol).append(")");
        if (identInc) {
            sb.append(" AS ").append(identifier);
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.stratio.com.stratio.crossdata.core.structures.SelectionClause#getIds()
     */
    @Override
    public List<String> getIds() {

        return new ArrayList<>();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.stratio.com.stratio.crossdata.core.structures.SelectionClause#getSelectorsGroupBy()
     */
    @Override
    public List<SelectorGroupBy> getSelectorsGroupBy() {

        return new ArrayList<>();
    }

    @Override
    public void addTablename(TableName tablename) {

    }

    /*
     * (non-Javadoc)
     *
     * @see com.stratio.com.stratio.crossdata.core.structures.SelectionClause#containsFunctions()
     */
    @Override
    public boolean containsFunctions() {

        return false;
    }
}
