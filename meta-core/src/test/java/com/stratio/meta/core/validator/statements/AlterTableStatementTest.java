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