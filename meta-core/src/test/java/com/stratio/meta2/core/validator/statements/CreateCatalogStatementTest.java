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

package com.stratio.meta2.core.validator.statements;

import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class CreateCatalogStatementTest extends BasicValidatorTest {

  @Test
  public void createCatalogIfNotExists() {
    String inputText = "CREATE CATALOG IF NOT EXISTS new_catalog;";
    validateOk(inputText, "createCatalogIfNotExists");
  }

  @Test
  public void createCatalogIfNotExistsWithExistingCatalog() {
    String inputText = "CREATE CATALOG IF NOT EXISTS demo;";
    validateOk(inputText, "createCatalogIfNotExistsWithExistingCatalog");
  }

  @Test
  public void createCatalogWithExistingCatalog() {
    String inputText = "CREATE CATALOG demo;";
    validateFail(inputText, "createCatalogWithExistingCatalog");
  }

  @Test
  public void createCatalogWithOptions() {
    String inputText = "CREATE CATALOG new_catalog WITH {\"comment\":\"This is a comment\"};";
    validateOk(inputText, "createCatalogWithOptions");
  }

}
