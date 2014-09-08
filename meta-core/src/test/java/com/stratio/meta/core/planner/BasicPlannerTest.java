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

package com.stratio.meta.core.planner;

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.Tree;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertTrue;

public class BasicPlannerTest extends BasicCoreCassandraTest {

  protected static MetadataManager _metadataManager = null;

  protected MetaStatement stmt;

  @BeforeClass
  public static void setUpBeforeClass(){
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    _metadataManager = new MetadataManager();
    _metadataManager.loadMetadata();
  }

  public void validateCassandraPath(String method){
    //Tree tree = stmt.getPlan(_metadataManager, "demo");
    Tree tree = null;
    assertTrue(tree.getNode().getPath().equals(MetaPath.CASSANDRA), method+": Plan path should be CASSANDRA");
  }

  public void validateDeepPath(String method){
    //Tree tree = stmt.getPlan(_metadataManager, "demo");
    Tree tree = null;
    assertTrue(tree.getNode().getPath().equals(MetaPath.DEEP), method+": Plan path should be DEEP");
  }

  public void validateCommandPath(String method){
    //Tree tree = stmt.getPlan(_metadataManager, "demo");
    Tree tree = null;
    assertTrue(tree.getNode().getPath().equals(MetaPath.COMMAND), method+": Plan path should be COMMAND");
  }

  public void validateStreamingPath(String method){
    //Tree tree = stmt.getPlan(_metadataManager, "demo");
    Tree tree = null;
    assertTrue(tree.getNode().getPath().equals(MetaPath.STREAMING), method+": Plan path should be STREAMING");
  }

  public void validateNotSupported(){
    //Tree tree = stmt.getPlan(_metadataManager,"demo");
    Tree tree = null;
    assertTrue(tree.isEmpty(), "Sentence planification not supported - planificationNotSupported");
  }
}
