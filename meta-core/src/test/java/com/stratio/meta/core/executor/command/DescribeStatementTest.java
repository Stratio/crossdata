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

package com.stratio.meta.core.executor.command;

import org.testng.annotations.Test;

import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.core.executor.BasicExecutorTest;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

public class DescribeStatementTest extends BasicExecutorTest {

  @Test
  public void testDescribeDemoKeyspaceOk() {

    MetaQuery metaQuery = new MetaQuery("DESCRIBE KEYSPACE demo;");

    DescribeStatement stmt = new DescribeStatement(DescribeType.KEYSPACE);
    stmt.setKeyspace("demo");

    stmt.validate(metadataManager);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.COMMAND, stmt));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateOk(metaQuery, "testDescribeDemoKeyspaceOk");
  }

  @Test
  public void testDescribeCurrentKeyspaceOk() {

    MetaQuery metaQuery = new MetaQuery("DESCRIBE KEYSPACE;");

    DescribeStatement stmt = new DescribeStatement(DescribeType.KEYSPACE);
    stmt.setSessionKeyspace("demo");

    stmt.validate(metadataManager);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.COMMAND, stmt));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateOk(metaQuery, "testDescribeCurrentKeyspaceOk");
  }

  @Test
  public void testDescribeKeyspacesOk() {

    MetaQuery metaQuery = new MetaQuery("DESCRIBE KEYSPACES;");

    DescribeStatement stmt = new DescribeStatement(DescribeType.KEYSPACES);

    stmt.validate(metadataManager);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.COMMAND, stmt));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateOk(metaQuery, "testDescribeKeyspacesOk");
  }

  @Test
  public void testDescribeTableOk() {

    MetaQuery metaQuery = new MetaQuery("DESCRIBE TABLE users;");

    DescribeStatement stmt = new DescribeStatement(DescribeType.TABLE);
    stmt.setSessionKeyspace("demo");
    stmt.setTableName("users");

    stmt.validate(metadataManager);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.COMMAND, stmt));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateOk(metaQuery, "testDescribeTableOk");
  }

  @Test
  public void testDescribeTablesOk() {

    MetaQuery metaQuery = new MetaQuery("DESCRIBE TABLES;");

    DescribeStatement stmt = new DescribeStatement(DescribeType.TABLES);
    stmt.setSessionKeyspace("demo");

    stmt.validate(metadataManager);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.COMMAND, stmt));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateOk(metaQuery, "testDescribeTablesOk");
  }
}
