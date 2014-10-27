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

import com.stratio.crossdata.common.data.TableName;

public abstract class SelectorMeta {

    public static final int TYPE_IDENT = 1;
    public static final int TYPE_FUNCTION = 2;
    public static final int TYPE_GROUPBY = 3;

    protected int type;

//TODO:javadoc
    public int getType() {
        return type;
    }

//TODO:javadoc
    public void setType(int type) {
        this.type = type;
    }

    @Override
//TODO:javadoc
    public abstract String toString();

//TODO:javadoc
    public abstract void addTablename(TableName tablename);
}
