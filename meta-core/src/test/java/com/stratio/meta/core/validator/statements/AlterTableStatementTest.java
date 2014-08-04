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

import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class AlterTableStatementTest extends BasicValidatorTest {

    @Test
    public void testValidateAddColumn(){
        String inputText = "ALTER TABLE demo.users ADD column1 INT;";
        validateOk(inputText, "testValidateAddColumn");
    }

    @Test
    public void testValidateAlterColumn(){
        String inputText = "ALTER TABLE demo.users ALTER age TYPE FLOAT;";
        validateOk(inputText, "testValidateAlterColumn");
    }

    @Test
    public void testValidateDropColumn(){
        String inputText = "ALTER TABLE demo.users DROP age;";
        validateOk(inputText, "testValidateDropColumn");
    }

    @Test
    public void testValidateWithOptions(){
        String inputText = "ALTER TABLE demo.users WITH comment='Table for testing purposes';";
        validateOk(inputText, "testValidateWithOptions");
    }

    @Test
    public void testValidateUnknownKS(){
        String inputText = "ALTER TABLE unknownks.table1 ADD column1 INT;";
        validateFail(inputText, "testValidateUnknownKS");
    }

    @Test
    public void testValidateUnknownTable(){
        String inputText = "ALTER TABLE demo.table1 ADD column1 INT;";
        validateFail(inputText, "testValidateUnknownTable");
    }

    @Test
    public void testValidateUnknownType(){
        String inputText = "ALTER TABLE demo.users ADD column1 NONTYPE;";
        validateFail(inputText, "testValidateUnknownType");
    }

    @Test
    public void testValidateUnknownCol(){
        String inputText = "ALTER TABLE demo.users ALTER unknowncolumn TYPE INT;";
        validateFail(inputText, "testValidateUnknownType");
    }

}
