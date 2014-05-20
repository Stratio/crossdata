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

}