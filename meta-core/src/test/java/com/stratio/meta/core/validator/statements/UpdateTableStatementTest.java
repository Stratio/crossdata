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

public class UpdateTableStatementTest extends BasicValidatorTest {

    // TEST FOR CORRECT QUERIES

    @Test
    public void validateUpdateUsingTtl(){
        String inputText = "UPDATE demo.users USING TTL = 54321 SET age = 50 WHERE name = name_5 AND gender = male;";
        validateOk(inputText, "validateUsingTtlAndTimestamp");
    }

    @Test
    public void validateUpdateUsingTtlAndTimestamp(){
        String inputText = "UPDATE demo.users USING TTL = 54321 AND TIMESTAMP = 98760 SET age = 50 WHERE name = name_5 AND gender = male;";
        validateOk(inputText, "validateUsingTtlAndTimestamp");
    }

    @Test
    public void validateUpdateSetIdentifierAndTerm(){
        String inputText = "UPDATE demo.users SET age = 50 WHERE name = name_5 AND gender = male;";
        validateOk(inputText, "validateSetIdentifierAndTerm");
    }

    @Test
    public void validateUpdateSetPlus(){
        String inputText = "UPDATE demo.users SET age = age + 1 WHERE name = name_5 AND gender = male;";
        validateOk(inputText, "validateSetPlus");
    }

    @Test
    public void validateUpdateSetPlusAndCondition(){
        String inputText = "UPDATE demo.users SET age = age + 1 WHERE name = name_5 AND gender = male IF email = whatever@bestcompany.com;";
        validateOk(inputText, "validateUpdateSetPlusAndCondition");
    }

    // TEST FOR WRONG QUERIES

    @Test
    public void validateUpdateWrongKeyspace(){
        String inputText = "UPDATE idk.users SET age = 50 WHERE name = name_5 AND gender = male;";
        validateFail(inputText, "validateUpdateWrongKeyspace");
    }

    @Test
    public void validateUpdateWrongTablename(){
        String inputText = "UPDATE demo.idk SET age = 50 WHERE name = name_5 AND gender = male;";
        validateFail(inputText, "validateUpdateWrongTablename");
    }

    @Test
    public void validateUpdateWrongTypeAssignment(){
        String inputText = "UPDATE demo.users SET age = fifty WHERE name = name_5 AND gender = male;";
        validateFail(inputText, "validateUpdateWrongType");
    }

    @Test
    public void validateUpdateWrongWhere(){
        String inputText = "UPDATE demo.users SET age = 50 WHERE surname = name_5;";
        validateFail(inputText, "validateUpdateWrongWhere");
    }

    @Test
    public void validateUpdateWrongTypeWhere(){
        String inputText = "UPDATE demo.users SET age = 50 WHERE name = 25;";
        validateFail(inputText, "validateUpdateWrongWhere");
    }

    @Test
    public void validateUpdateUsingWrongOption1(){
        String inputText = "UPDATE demo.users USING ESTIMATED = 12345 SET age = 50 WHERE name = name_5 AND gender = male;";
        validateFail(inputText, "validateUsingWrongOption1");
    }

    @Test
    public void validateUpdateUsingWrongOption2(){
        String inputText = "UPDATE demo.users USING TTL = 67890 AND ESTIMATED = 12345 SET age = 50 WHERE name = name_5 AND gender = male;";
        validateFail(inputText, "validateUsingWrongOption2");
    }

    @Test
    public void validateUpdateSetLiteral(){
        String inputText = "UPDATE demo.users SET email = email + { whatever@bestcompany.com } WHERE name = name_5 AND gender = male;";
        validateFail(inputText, "validateUpdateSetLiteral");
    }

    @Test
    public void validateUpdateMapLiteral(){
        String inputText = "UPDATE demo.users SET email[1] = whatever@bestcompany.com WHERE name = name_5 AND gender = male;";
        validateFail(inputText, "validateUpdateMapLiteral");
    }

    @Test
    public void validateUpdateListLiteral(){
        String inputText = "UPDATE demo.users SET email = email - [ whatever@bestcompany.com ] WHERE name = name_5 AND gender = male;";
        validateFail(inputText, "validateUpdateListLiteral");
    }

    @Test
    public void validateUpdateSetPlusAndWrongCondition(){
        String inputText = "UPDATE demo.users SET age = age + 1 WHERE name = name_5 AND gender = male IF address = whatever@bestcompany.com;";
        validateFail(inputText, "validateUpdateSetPlusAndWrongCondition");
    }

    @Test
    public void validateUpdateSetPlusAndTypeWrongCondition(){
        String inputText = "UPDATE demo.users SET age = age + 1 WHERE name = name_5 AND gender = male IF age = whatever@bestcompany.com;";
        validateFail(inputText, "validateUpdateSetPlusAndTypeWrongCondition");
    }

}
