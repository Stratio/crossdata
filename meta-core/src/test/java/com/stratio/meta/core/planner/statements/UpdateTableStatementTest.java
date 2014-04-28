/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.planner.statements;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.UpdateTableStatement;
import com.stratio.meta.core.structures.*;
import com.stratio.meta.core.utils.Tree;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class UpdateTableStatementTest  extends BasicPlannerTest {

    @Test
    public void planificationNotSupported(){
        String inputText = "UPDATE table1 SET field1 = value1 WHERE field3 = value3;";
        IdentifierAssignment idAsig = new IdentifierAssignment("field1",new StringTerm("value1"),1);
        ValueAssignment vaAsig = new ValueAssignment(new StringTerm("value1"));
        Relation relation = new RelationCompare("field3", "=", new StringTerm("value3"));
        List<Assignment> listAsig = Arrays.asList(new Assignment(idAsig, vaAsig));
        List<Relation> whereClauses = Arrays.asList(relation);
        stmt = new UpdateTableStatement("table1", listAsig, whereClauses);
        Tree tree = stmt.getPlan(_metadataManager, "demo");
        assertTrue(tree.isEmpty(), "Sentence planification not supported - planificationNotSupported");
    }
}
