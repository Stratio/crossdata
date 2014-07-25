/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
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