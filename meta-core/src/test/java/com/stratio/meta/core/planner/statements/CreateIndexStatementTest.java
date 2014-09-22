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

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.Tree;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.IndexType;
import com.stratio.meta2.core.statements.CreateIndexStatement;

public class CreateIndexStatementTest extends BasicPlannerTest {

  @Test
  public void testPlanDefaultIndex(){
    String inputText = "CREATE DEFAULT INDEX ON demo.users (email);";
    stmt = new CreateIndexStatement();
    ((CreateIndexStatement)stmt).setType(IndexType.DEFAULT);
    TableName tablename = new TableName("demo", "users");
    ((CreateIndexStatement)stmt).setTableName(tablename);
    ((CreateIndexStatement)stmt).addColumn(new ColumnName(tablename, "email"));
    ((CreateIndexStatement)stmt).setCreateIndex(true);
    validateCassandraPath("testPlanDefaultIndex");
  }

  @Test
  public void testPlanLuceneIndex(){
    String inputText = "CREATE LUCENE INDEX new_index ON demo.types (varchar_column, boolean_column);";
    stmt = new CreateIndexStatement();
    ((CreateIndexStatement)stmt).setType(IndexType.FULL_TEXT);
    ((CreateIndexStatement)stmt).setName("new_index");
    TableName tablename = new TableName("demo", "types");
    ((CreateIndexStatement)stmt).setTableName(tablename);
    ((CreateIndexStatement)stmt).addColumn(new ColumnName(tablename, "varchar_column"));
    ((CreateIndexStatement)stmt).addColumn(new ColumnName(tablename, "boolean_column"));
    ((CreateIndexStatement)stmt).setCreateIndex(true);
    //Tree tree = stmt.getPlan(_metadataManager,"demo");
    Tree tree = null;
    assertTrue(tree.getNode().getPath().equals(MetaPath.CASSANDRA));
    List<Tree> children = tree.getChildren();
    for(Tree t : children){
      assertTrue(tree.getNode().getPath().equals(MetaPath.CASSANDRA));
    }
  }
}
