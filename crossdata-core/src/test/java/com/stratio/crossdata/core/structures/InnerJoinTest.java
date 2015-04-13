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

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.JoinType;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.AbstractRelation;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.Selector;

public class InnerJoinTest {

    @Test
    public void testGetOrderedRelations() throws Exception {
        // Create tables
        List<TableName> tableNames = new ArrayList<>();
        tableNames.add(new TableName("demo", "t1"));
        tableNames.add(new TableName("demo", "t2"));
        tableNames.add(new TableName("demo", "t3"));

        // Create relations
        List<AbstractRelation> joinRelations = new ArrayList<>();
        Selector leftTerm = new ColumnSelector(new ColumnName("demo", "t3", "id"));
        Operator operator = Operator.EQ;
        Selector rightTerm = new ColumnSelector(new ColumnName("demo", "t1", "id"));
        joinRelations.add(new Relation(leftTerm, operator, rightTerm));
        leftTerm = new ColumnSelector(new ColumnName("demo", "t2", "code"));
        rightTerm = new ColumnSelector(new ColumnName("demo", "t1", "code"));
        joinRelations.add(new Relation(leftTerm, operator, rightTerm));

        // Create join type
        JoinType type = JoinType.INNER;

        // Create join
        InnerJoin join = new InnerJoin(tableNames, joinRelations, type);

        List<Relation> orderedRelations = join.getOrderedRelations();

        assertEquals(
                orderedRelations.size(),
                2,
                "Ordered relations should have 2 relations");
        assertEquals(
                orderedRelations.get(0).getLeftTerm().getColumnName(),
                new ColumnName("demo", "t1", "code"),
                "demo.t1.code was expected");
        assertEquals(
                orderedRelations.get(0).getRightTerm().getColumnName(),
                new ColumnName("demo", "t2", "code"),
                "demo.t2.code was expected");
        assertEquals(
                orderedRelations.get(1).getLeftTerm().getColumnName(),
                new ColumnName("demo", "t1", "id"),
                "demo.t1.code was expected");
        assertEquals(
                orderedRelations.get(1).getRightTerm().getColumnName(),
                new ColumnName("demo", "t3", "id"),
                "demo.t2.code was expected");
    }
}
