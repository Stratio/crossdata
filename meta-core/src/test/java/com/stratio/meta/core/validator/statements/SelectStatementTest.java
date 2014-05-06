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

public class SelectStatementTest extends BasicValidatorTest {

    @Test
    public void validateBasicColumnOk(){
        String inputText = "SELECT name FROM demo.users;";
        validateOk(inputText, "validateBasicColumnOk");
    }

    @Test
    public void validateBasicCountOk(){
        String inputText = "SELECT count(*) FROM demo.users;";
        validateOk(inputText, "validateBasicCountOk");
    }

    @Test
    public void validateBasicSeveralColumnsOk(){
        String inputText = "SELECT name, age FROM demo.users;";
        validateOk(inputText, "validateBasicSeveralColumnsOk");
    }

    @Test
    public void validateColumnUnknown(){
        String inputText = "SELECT name, unknown FROM demo.users;";
        validateFail(inputText, "validateColumnUnknown");
    }

    @Test
    public void validateBasicWhereOk(){
        String inputText = "SELECT name, age FROM demo.users WHERE name = 'name_5';";
        validateOk(inputText, "validateBasicWhereOk");
    }

    @Test
    public void validateWhere2columnsOk(){
        String inputText = "SELECT name, age FROM demo.users WHERE name = 'name_5' AND age = 15;";
        validateOk(inputText, "validateWhere2columnsOk");
    }

    @Test
    public void validateWhereColumnUnknown(){
        String inputText = "SELECT name, age FROM demo.users WHERE unknown = 'name_5' AND age = 15;";
        validateFail(inputText, "validateWhereColumnUnknown");
    }

    @Test
    public void validateWhereIntegerFail(){
        String inputText = "SELECT name, age FROM demo.users WHERE name = 'name_5' AND age = '15';";
        validateFail(inputText, "validateWhereIntegerFail");
    }

    @Test
    public void validateWhereStringFail(){
        String inputText = "SELECT name, age FROM demo.users WHERE name = 15 AND age = 15;";
        validateFail(inputText, "validateWhereStringFail");
    }

    @Test
    public void validateOperatorStringOk(){
        String [] operators = {">", "<", ">=", "<="};
        for(String operator : operators) {
            String inputText = "SELECT name, age FROM demo.users WHERE name "
                    + operator + " 'name_5';";
            validateOk(inputText, "validateOperatorStringOk on column - operator: " + operator);
        }
    }

    @Test
    public void validateOperatorBooleanFail(){
        String [] operators = {">", "<", ">=", "<="};
        for(String operator : operators) {
            String inputText = "SELECT bool FROM demo.users WHERE bool "
                    + operator + " true;";
            validateFail(inputText, "validateOperatorBooleanFail on column - operator: " + operator);
        }
    }

    //
    // Tests with table referred columns.
    //
    @Test
    public void validateReferredOk(){
        String inputText = "SELECT users.name, users.age FROM demo.users WHERE name = 'name_5' AND age = 15;";
        validateOk(inputText, "validateReferredOk");
    }

    @Test
    public void validateReferredFail(){
        String inputText = "SELECT unknown.name, unknown.age FROM demo.users WHERE name = 'name_5' AND age = 15;";
        validateFail(inputText, "validateReferredFail");
    }

    //
    // Tests with inner joins
    //
    @Test
    public void validateInnerJoinBasicOk(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN demo.users ON users.name=users.name;";
        validateOk(inputText, "validateInnerJoinBasicOk");
    }
    
    @Test
    public void validateUnknownKs1Fail(){
        String inputText = "SELECT users.name, users.age, users.email FROM unknown.users "
                + "INNER JOIN demo.users ON users.name=users.name;";
        validateFail(inputText, "validateUnknownKs1Fail");
    }

    @Test
    public void validateUnknownKs2Fail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN unknown.users ON users.name=users.name;";
        validateFail(inputText, "validateUnknownKs2Fail");
    }

    @Test
    public void validateUnknownTable2Fail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN demo.unknown ON users.name=users.name;";
        validateFail(inputText, "validateUnknownTable2Fail");
    }

    @Test
    public void validateUnknownTable2WithoutKsFail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN unknown ON users.name=users.name;";
        validateFail(inputText, "validateUnknownTable2WithoutKsFail");
    }

    @Test
    public void validateOnUnknownKsFail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN demo.users ON unknown.name=users.name;";
        validateFail(inputText, "validateOnUnknownKsFail");
    }

    @Test
    public void validateOnUnknownKs2Fail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN demo.users ON users.name=unknown.name;";
        validateFail(inputText, "validateOnUnknownKs2Fail");
    }

    @Test
    public void validateOnUnknownTableFail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN demo.users ON demo.unknown=users.name;";
        validateFail(inputText, "validateOnUnknownKsFail");
    }

    @Test
    public void validateOnUnknownTable2Fail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN demo.users ON users.name=demo.unknown;";
        validateFail(inputText, "validateOnUnknownKs2Fail");
    }


    @Test
    public void validateInnerJoin2tablesOk(){
        String inputText = "SELECT users.name, users.age, users_info.info FROM demo.users "
                + "INNER JOIN demo.users_info ON users.name=users_info.link_name;";
        validateOk(inputText, "validateInnerJoin2tablesOk");
    }

    @Test
    public void validateInnerJoinWhereOk(){
        String inputText = "SELECT users.name, users.age, users_info.info FROM demo.users "
                + "INNER JOIN demo.users_info ON users.name=users_info.link_name "
                + "WHERE name = 'name_3';";
        validateOk(inputText, "validateInnerJoinWhereOk");
    }

}
