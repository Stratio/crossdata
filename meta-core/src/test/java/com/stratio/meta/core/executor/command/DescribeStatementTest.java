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

package com.stratio.meta.core.executor.command;

import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.core.executor.BasicExecutorTest;
import com.stratio.meta.core.statements.DescribeStatement;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import org.testng.annotations.Test;

public class DescribeStatementTest extends BasicExecutorTest {

  @Test
  public void testDescribeDemoKeyspaceOk() {

    MetaQuery metaQuery = new MetaQuery("DESCRIBE KEYSPACE demo;");

    DescribeStatement stmt = new DescribeStatement(DescribeType.KEYSPACE);
    stmt.setKeyspace("demo");

    stmt.validate(metadataManager, null);

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

    stmt.validate(metadataManager, null);

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

    stmt.validate(metadataManager, null);

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

    stmt.validate(metadataManager, null);

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

    stmt.validate(metadataManager, null);

    Tree tree = new Tree();
    tree.setNode(new MetaStep(MetaPath.COMMAND, stmt));
    metaQuery.setPlan(tree);
    metaQuery.setStatus(QueryStatus.PLANNED);

    validateOk(metaQuery, "testDescribeTablesOk");
  }
}
