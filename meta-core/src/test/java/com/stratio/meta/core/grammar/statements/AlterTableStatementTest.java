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

package com.stratio.meta.core.grammar.statements;

import com.stratio.meta.core.grammar.ParsingTest;
import org.testng.annotations.Test;

public class AlterTableStatementTest extends ParsingTest{

    @Test
    public void alterTableBasic() {
        String inputText = "alter table table1 alter column1 type int;";
        testRegularStatement(inputText, "alterTableBasic");
    }

  @Test
  public void alterTableCatalog() {
    String inputText = "alter table catalog.table1 alter column1 type int;";
    testRegularStatement(inputText, "alterTableCatalog");
  }

    @Test
    public void alterTableBasic1() {
        String inputText = "alter table table1 add column1 int;";
        testRegularStatement(inputText, "alterTableBasic1");
    }

    @Test
    public void alterTableBasic2() {
        String inputText = "alter table table1 drop column1;";
        testRegularStatement(inputText, "alterTableBasic2");
    }

    @Test
    public void alterTableBasic3() {
        String inputText = "Alter table table1 with property1=value1 and property2=2 and property3=3.0;";
        testRegularStatement(inputText, "alterTableBasic3");
    }

    @Test
    public void alterWrongPropertyIdentifier(){
        String inputText = "ALTER TABLE table1 with 2property1=value1;";
        testRecoverableError(inputText, "alterWrongPropertyIdentifier");
    }


}
