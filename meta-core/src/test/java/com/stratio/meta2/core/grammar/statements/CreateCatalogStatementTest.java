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

package com.stratio.meta2.core.grammar.statements;

import com.stratio.meta.core.grammar.ParsingTest;

import org.testng.annotations.Test;

public class CreateCatalogStatementTest extends ParsingTest {

  @Test
  public void createCatalogIfNotExists() {
    String inputText = "CREATE CATALOG IF NOT EXISTS key_space1;";
    testRegularStatement(inputText, "createCatalogIfNotExists");
  }

  @Test
  public void createCatalogIfNotExistsWithEmptyOptions() {
    String inputText = "CREATE CATALOG IF NOT EXISTS key_space1 WITH {};";
    testRegularStatement(inputText, "createCatalogIfNotExistsWithEmptyOptions");
  }

  @Test
  public void createCatalogWithOptions() {
    String inputText = "CREATE CATALOG key_space1 WITH {\"comment\":\"This is a comment\"};";
    testRegularStatement(inputText, "createCatalogWithOptions");
  }

  @Test
  public void createCatalogWrongName() {
    String inputText = "CREATE CATALOG remove.name;";
    testParseFails(inputText, "createCatalogWrongName");
  }


}
