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

package com.stratio.meta.core.validator;

import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioEngineConnectionException;

import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class BasicValidatorTest extends BasicCoreCassandraTest {

  protected static MetadataManager metadataManager = null;

  protected static IStratioStreamingAPI stratioStreamingAPI = null;

  protected static EngineConfig engine = null;

  protected static final ParsingTest pt = new ParsingTest();

  @BeforeClass
  public static void setUpBeforeClass() {
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");

    try {
      stratioStreamingAPI = StratioStreamingAPIFactory.create().initializeWithServerConfig("127.0.0.1", 9092, "127.0.0.1", 2181);
    } catch (StratioEngineConnectionException e) {
      e.printStackTrace();
    }
    metadataManager = new MetadataManager(_session, stratioStreamingAPI);
    metadataManager.loadMetadata();
  }

  public void validateOk(String inputText, String expectedText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, expectedText, methodName);
    Result result = stmt.validate(metadataManager, engine);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertFalse(result.hasError(),
                "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateOk(String inputText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, methodName);
    Result result = stmt.validate(metadataManager, engine);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertFalse(result.hasError(),
                "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateFail(String inputText, String expectedText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, expectedText, methodName);
    Result result = stmt.validate(metadataManager, engine);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertTrue(result.hasError(),
               "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateFail(String inputText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, methodName);
    Result result = stmt.validate(metadataManager, engine);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertTrue(result.hasError(),
               "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

}
