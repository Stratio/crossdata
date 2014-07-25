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

package com.stratio.meta2.core.validator.statements;

import com.stratio.meta.core.validator.BasicValidatorTest;

import org.testng.annotations.Test;

public class AlterCatalogStatementTest extends BasicValidatorTest {

  @Test
  public void alterCatalogInvalidOptions() {
    String inputText = "ALTER CATALOG demo WITH {};";
    validateFail(inputText, "alterCatalogInvalidOptions");
  }

  @Test
  public void alterCatalogNotFound() {
    String inputText = "ALTER CATALOG unknown WITH {};";
    validateFail(inputText, "alterCatalogNotFound");
  }

  @Test
  public void alterCatalogValid() {
    String inputText = "ALTER CATALOG key_space1 WITH {\"comment\":\"This is a comment\"};";
    validateFail(inputText, "alterCatalogInvalidOptions");
  }

}