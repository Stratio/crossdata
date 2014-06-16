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

package com.stratio.meta.core.validator;

import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.MetaStatement;

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
    Result result = stmt.validate(metadataManager);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertFalse(result.hasError(),
                "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateOk(String inputText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, methodName);
    Result result = stmt.validate(metadataManager);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertFalse(result.hasError(),
                "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateFail(String inputText, String expectedText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, expectedText, methodName);
    Result result = stmt.validate(metadataManager);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertTrue(result.hasError(),
               "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  public void validateFail(String inputText, String methodName) {
    MetaStatement stmt = pt.testRegularStatement(inputText, methodName);
    Result result = stmt.validate(metadataManager);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertTrue(result.hasError(),
               "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

}
