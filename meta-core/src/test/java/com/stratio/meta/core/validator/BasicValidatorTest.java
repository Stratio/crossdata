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
import com.stratio.meta2.core.grammar.ParsingTest;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta2.core.statements.MetaStatement;

import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class BasicValidatorTest extends BasicCoreCassandraTest {

  protected static MetadataManager metadataManager = null;

  protected static final ParsingTest pt = new ParsingTest();

  @BeforeClass
  public static void setUpBeforeClass() {
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    metadataManager = new MetadataManager(_session, null);
    metadataManager.loadMetadata();
  }

  public void validateOk(String inputText, String expectedText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, expectedText, methodName);
    Result result = stmt.validate(metadataManager, null);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertFalse(result.hasError(),
                "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateOk(String inputText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, methodName);
    Result result = stmt.validate(metadataManager, null);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertFalse(result.hasError(),
                "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateFail(String inputText, String expectedText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, expectedText, methodName);
    Result result = stmt.validate(metadataManager, null);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertTrue(result.hasError(),
               "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateFail(String inputText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, methodName);
    Result result = stmt.validate(metadataManager, null);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertTrue(result.hasError(),
               "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

}
