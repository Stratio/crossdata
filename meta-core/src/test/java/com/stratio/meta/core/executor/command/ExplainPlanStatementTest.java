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

package com.stratio.meta.core.executor.command;

import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.executor.BasicExecutorTest;
import com.stratio.meta.core.statements.DropKeyspaceStatement;
import com.stratio.meta.core.statements.ExplainPlanStatement;
import com.stratio.meta.core.utils.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ExplainPlanStatementTest extends BasicExecutorTest {

    @Test
    public void executionForExplainPlanForDropKeyspace(){

        MetaQuery metaQuery = new MetaQuery("EXPLAIN PLAN FOR DROP KEYSPACE demo;");

        DropKeyspaceStatement dropStmt = new DropKeyspaceStatement("demo", false);

        ExplainPlanStatement stmt = new ExplainPlanStatement(dropStmt);

        stmt.validate(metadataManager);

        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.COMMAND, stmt));
        metaQuery.setPlan(tree);
        metaQuery.setStatus(QueryStatus.PLANNED);

        Result result = validateOk(metaQuery, "executionForExplainPlanForDropKeyspace");

        String expectedPlan = "(CASSANDRA) Drop keyspace demo";

        String resultPlan = ((CommandResult) result).getResult();

        assertEquals(expectedPlan, resultPlan, "  RESULT PLAN: "+resultPlan+System.lineSeparator()+"EXPECTED PLAN: "+expectedPlan);
    }

}
