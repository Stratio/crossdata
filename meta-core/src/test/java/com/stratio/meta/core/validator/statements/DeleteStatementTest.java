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

public class DeleteStatementTest extends BasicValidatorTest {

    @Test
    public void validateOk(){
        String inputText = "DELETE FROM demo.users WHERE name = 'name_0';";
        validateOk(inputText, "validateOk");
    }

    @Test
    public void validateNotExistsTablename(){
        String inputText = "DELETE FROM unknown_table WHERE name = 'name_0';";
        validateFail(inputText, "validateNotExistsTablename");
    }

    @Test
    public void validateWhere2columnsOk(){
        String inputText = "DELETE FROM demo.users WHERE name = 'name_0' AND age = 15;";
        validateOk(inputText, "validateWhere2columnsOk");
    }

    @Test
    public void validateWhereColumnUnknown(){
        String inputText = "DELETE FROM demo.users WHERE unknown = 'name_0';";
        validateFail(inputText, "validateWhereColumnUnknown");
    }

    @Test
    public void validateWhereIntegerFail(){
        String inputText = "DELETE FROM demo.users WHERE name = 'name_0' AND age = '15';";
        validateFail(inputText, "validateWhereIntegerFail");
    }

    @Test
    public void validateWhereStringFail(){
        String inputText = "DELETE FROM demo.users WHERE name = 15 AND age = 15;";
        validateFail(inputText, "validateWhereStringFail");
    }

}
