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

package com.stratio.meta.core.validator.statements;

import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.statements.ExplainPlanStatement;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta.core.validator.BasicValidatorTest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class ExplainPlanStatementTest extends BasicValidatorTest {

  // Test with CORRECT statements
  @Test
  public void validateExplainPlanForSelect() {
    String inputText = "EXPLAIN PLAN FOR SELECT users.name FROM demo.users;";
    validateOk(inputText, "validateExplainPlanForSelect");
  }

  @Test
  public void validateExplainPlanForDropIndex() {
    String methodName = "validateExplainPlanForDropIndex";
    String inputText = "EXPLAIN PLAN FOR DROP INDEX users_gender_idx;";
    MetaStatement stmt = pt.testRegularStatement(inputText, methodName);
    stmt.setSessionCatalog("demo");
    ((ExplainPlanStatement) stmt).getMetaStatement().setSessionCatalog("demo");
    Result result = stmt.validate(metadataManager, null);
    assertNotNull(result, "Sentence validation not supported - " + methodName);
    assertFalse(result.hasError(),
                "Cannot validate sentence - " + methodName + ": " + getErrorMessage(result));
  }

  // Test with WRONG statements
  @Test
  public void validateExplainPlanForWrongSelect() {
    String inputText = "EXPLAIN PLAN FOR SELECT idk.name FROM demo.idk;";
    validateFail(inputText, "validateExplainPlanForWrongSelect");
  }

  @Test
  public void validateExplainPlanForWrongDropIndex() {
    String inputText = "EXPLAIN PLAN FOR DROP INDEX idk;";
    validateFail(inputText, "validateExplainPlanForWrongDropIndex");
  }

}
