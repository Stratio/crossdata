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

import java.util.Arrays;
import java.util.List;

import com.stratio.crossdata.common.data.TableName;

public class SelectorFunction extends SelectorMeta {

    private TableName name;
    private List<SelectorMeta> params;

    public SelectorFunction(TableName name, List<SelectorMeta> params) {
        this.type = TYPE_FUNCTION;
        this.name = name;
        this.params = params;
    }

    public TableName getName() {
        return name;
    }

    public void setName(TableName name) {
        this.name = name;
    }

    public List<SelectorMeta> getParams() {
        return params;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name.getName());
        sb.append(Arrays.toString(params.toArray()).replace("[", "(").replace("]", ")"));
        return sb.toString();
    }

    @Override
    public void addTablename(TableName tablename) {
        for (SelectorMeta param : params) {
            param.addTablename(tablename);
        }
    }

}
