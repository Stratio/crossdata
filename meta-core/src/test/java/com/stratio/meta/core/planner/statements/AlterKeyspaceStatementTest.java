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
import com.stratio.meta.core.statements.AlterKeyspaceStatement;
import com.stratio.meta.core.utils.Tree;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.testng.Assert.assertTrue;

public class AlterKeyspaceStatementTest extends BasicPlannerTest {
    @Test
    public void planificationNotSupported(){
        String inputText = "ALTER KEYSPACE demo WITH ident1 = value1 AND ident2 = 54;";
        stmt = new AlterKeyspaceStatement("demo", new HashMap());
        Tree tree = stmt.getPlan(_metadataManager,"demo");
        assertTrue(tree.isEmpty(), "Sentence planification not supported - planificationNotSupported");
    }
}
