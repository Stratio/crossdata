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

package com.stratio.meta.core.planner.statements;

import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.statements.structures.relationships.RelationCompare;
import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.UpdateTableStatement;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta2.common.statements.structures.terms.StringTerm;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class UpdateTableStatementTest  extends BasicPlannerTest {

    @Test
    public void planUpdateTableStatement(){
        String inputText = "UPDATE table1 SET field1 = value1 WHERE field3 = value3;";
        IdentifierAssignment idAsig = new IdentifierAssignment("field1",new StringTerm("value1"),1);
        ValueAssignment vaAsig = new ValueAssignment(new StringTerm("value1"));
        Relation relation = new RelationCompare("field3", "=", new StringTerm("value3"));
        List<Assignment> listAsig = Arrays.asList(new Assignment(idAsig, vaAsig));
        List<Relation> whereClauses = Arrays.asList(relation);
        stmt = new UpdateTableStatement("table1", listAsig, whereClauses);
        Tree tree = stmt.getPlan(_metadataManager, "demo");
        validateCassandraPath("planUpdateTableStatement");
    }
}
