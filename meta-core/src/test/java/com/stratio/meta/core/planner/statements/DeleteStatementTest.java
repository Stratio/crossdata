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
import com.stratio.meta.core.statements.DeleteStatement;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.statements.structures.relationships.RelationCompare;
import com.stratio.meta.core.structures.StringTerm;
import org.testng.annotations.Test;

public class DeleteStatementTest  extends BasicPlannerTest {

    @Test
    public void testPlanForDelete(){
        String input = "DELETE FROM demo.users WHERE name = 'name_0';";
        stmt = new DeleteStatement();
        ((DeleteStatement)stmt).setTableName("demo.users");
        Relation relation = new RelationCompare("name", "=", new StringTerm("name_0"));
        ((DeleteStatement)stmt).addRelation(relation);
        validateCassandraPath("testPlanForDelete");
    }
}
