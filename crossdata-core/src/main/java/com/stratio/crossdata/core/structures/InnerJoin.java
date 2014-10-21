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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.stratio.crossdata.common.statements.structures.relationships.Relation;
import com.stratio.crossdata.common.data.TableName;

public class InnerJoin implements Serializable {

    private TableName tableName;

    private List<Relation> relations;

    private InnerJoin(TableName tableName) {
        this.tableName = tableName;
    }

    public InnerJoin(TableName tableName, List<Relation> joinRelations) {
        this(tableName);
        this.relations = joinRelations;
    }

    public TableName getTablename() {
        return tableName;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tableName);
        sb.append(" ON ");
        Iterator<Relation> it = relations.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }

}
