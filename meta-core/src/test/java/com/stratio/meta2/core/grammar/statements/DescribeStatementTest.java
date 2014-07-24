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

package com.stratio.meta2.core.grammar.statements;

import org.testng.annotations.Test;

import com.stratio.meta.core.grammar.ParsingTest;

public class DescribeStatementTest extends ParsingTest {

  @Test
  public void describeCatalogBasic() {
    String inputText = "DESCRIBE CATALOG catalog1;";
    testRegularStatement(inputText, "describeCatalogBasic");
  }

  @Test
  public void describeCurrentCatalogBasic() {
    String inputText = "DESCRIBE CATALOG;";
    testRegularStatement(inputText, "describeCurrentCatalogBasic");
  }

  @Test
  public void describeTableBasic() {
    String inputText = "DESCRIBE TABLE catalog1.table1;";
    testRegularStatement(inputText, "describeTableBasic");
  }

  @Test
  public void describeTableFail() {
    String inputText = "DESCRIBE UNKNOWN catalog1.table1;";
    testParseFails(inputText, "describeTableFail");
  }

  @Test
  public void describeCatalogsBasic() {
    String inputText = "DESCRIBE CATALOGS;";
    testRegularStatement(inputText, "describeCatalogsBasic");
  }

  @Test
  public void describeTablesBasic() {
    String inputText = "DESCRIBE TABLES;";
    testRegularStatement(inputText, "describeTablesBasic");
  }
}