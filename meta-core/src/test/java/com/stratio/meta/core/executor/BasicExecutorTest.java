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

package com.stratio.meta.core.executor;

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta2.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaQuery;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class BasicExecutorTest extends BasicCoreCassandraTest {

  protected static Executor executor = null;

  protected static DeepSparkContext deepContext = null;

  protected static MetadataManager metadataManager = null;

  private final static Logger LOG = Logger.getLogger(BasicExecutorTest.class);

  @BeforeClass
  public static void setUpBeforeClass() {
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    EngineConfig config = initConfig();
    deepContext = new DeepSparkContext(config.getSparkMaster(), config.getJobName());
    executor = new Executor(deepContext, config);
    metadataManager = new MetadataManager();
    metadataManager.loadMetadata();
  }

  @AfterClass
  public static void tearDownAfterClass() {
    deepContext.stop();
  }


  public static EngineConfig initConfig() {
    String[] cassandraHosts = {"127.0.0.1"};
    EngineConfig engineConfig = new EngineConfig();
    engineConfig.setCassandraHosts(cassandraHosts);
    engineConfig.setCassandraPort(9042);
    engineConfig.setSparkMaster("local");
    return engineConfig;
  }


  public Result validateOk(MetaQuery metaQuery, String methodName) {
    MetaQuery result = executor.executeQuery(metaQuery, null);
    assertNotNull(result.getResult(), "Result null - " + methodName);
    assertFalse(result.hasError(),
                metaQuery.getPlan().getNode().getPath() + " execution failed - " + methodName + ": "
                + getErrorMessage(result.getResult()));
    return result.getResult();
  }


  public Result validateRows(MetaQuery metaQuery, String methodName, int expectedNumber) {
    QueryResult result = (QueryResult) validateOk(metaQuery, methodName);
    if (expectedNumber > 0) {
      assertFalse(result.getResultSet().isEmpty(), "Expecting non-empty resultset");
      assertEquals(result.getResultSet().size(), expectedNumber, methodName + ":"
                                                                 + result.getResultSet().size()
                                                                 + " rows found, " + expectedNumber
                                                                 + " rows expected.");
    } else {
      assertTrue(result.getResultSet().isEmpty(), "Expecting empty resultset.");
      assertNull(result.getResultSet(), methodName + ": Result should be null");
    }

    return result;
  }

  public void validateFail(MetaQuery metaQuery, String methodName) {
    try {
      executor.executeQuery(metaQuery, null);
    } catch (Exception ex) {
      LOG.info("Correctly caught exception");
    }
  }

}
