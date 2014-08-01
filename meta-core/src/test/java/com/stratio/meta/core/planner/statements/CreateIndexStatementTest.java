/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.planner.statements;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.CreateIndexStatement;
import com.stratio.meta.core.structures.IndexType;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.Tree;

public class CreateIndexStatementTest extends BasicPlannerTest {

  @Test
  public void testPlanDefaultIndex() {

    // CREATE DEFAULT INDEX ON demo.users (email);
    stmt = new CreateIndexStatement();
    ((CreateIndexStatement) stmt).setType(IndexType.DEFAULT);
    ((CreateIndexStatement) stmt).setTableName("demo.users");
    ((CreateIndexStatement) stmt).addColumn("email");
    ((CreateIndexStatement) stmt).setCreateIndex(true);
    validateCassandraPath("testPlanDefaultIndex");
  }

  @Test
  public void testPlanLuceneIndex() {

    // CREATE LUCENE INDEX new_index ON demo.types (varchar_column, boolean_column);
    stmt = new CreateIndexStatement();
    ((CreateIndexStatement) stmt).setType(IndexType.LUCENE);
    ((CreateIndexStatement) stmt).setName("new_index");
    ((CreateIndexStatement) stmt).setTableName("demo.types");
    ((CreateIndexStatement) stmt).addColumn("varchar_column");
    ((CreateIndexStatement) stmt).addColumn("boolean_column");
    ((CreateIndexStatement) stmt).setCreateIndex(true);
    Tree tree = stmt.getPlan(_metadataManager, "demo");
    assertTrue(tree.getNode().getPath().equals(MetaPath.CASSANDRA));
    List<Tree> children = tree.getChildren();
    for (Tree t : children) {
      assertTrue(t.getNode().getPath().equals(MetaPath.CASSANDRA));
    }
  }
}
