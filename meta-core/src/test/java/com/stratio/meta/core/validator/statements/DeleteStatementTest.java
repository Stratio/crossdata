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
