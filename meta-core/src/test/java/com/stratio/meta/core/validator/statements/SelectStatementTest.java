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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

public class SelectStatementTest extends BasicValidatorTest {

    @Test
    public void validate_basicColumn_ok(){
        String inputText = "SELECT name FROM demo.users;";
        validateOk(inputText, "validate_basicColumn_ok");
    }

    @Test
    public void validate_basicCount_ok(){
        String inputText = "SELECT count(*) FROM demo.users;";
        validateOk(inputText, "validate_basicCount_ok");
    }

    @Test
    public void validate_basicSeveralColumns_ok(){
        String inputText = "SELECT name, age FROM demo.users;";
        validateOk(inputText, "validate_basicSeveralColumns_ok");
    }

    @Test
    public void validate_columnUnknown(){
        String inputText = "SELECT name, unknown FROM demo.users;";
        validateFail(inputText, "validate_columnUnknown");
    }

    @Test
    public void validate_basicWhere_ok(){
        String inputText = "SELECT name, age FROM demo.users WHERE name = 'name_5';";
        validateOk(inputText, "validate_basicWhere_ok");
    }

    @Test
    public void validate_where_2columns_ok(){
        String inputText = "SELECT name, age FROM demo.users WHERE name = 'name_5' AND age = 15;";
        validateOk(inputText, "validate_where_2columns_ok");
    }

    @Test
    public void validate_where_columnUnknown(){
        String inputText = "SELECT name, age FROM demo.users WHERE unknown = 'name_5' AND age = 15;";
        validateFail(inputText, "validate_where_columnUnknown");
    }

    @Test
    public void validate_where_integerFail(){
        String inputText = "SELECT name, age FROM demo.users WHERE name = 'name_5' AND age = '15';";
        validateFail(inputText, "validate_where_integerFail");
    }

    @Test
    public void validate_where_stringFail(){
        String inputText = "SELECT name, age FROM demo.users WHERE name = 15 AND age = 15;";
        validateFail(inputText, "validate_where_integerFail");
    }

    @Test
    public void validate_operatorString_ok(){
        String [] operators = {">", "<", ">=", "<="};
        for(String operator : operators) {
            String inputText = "SELECT name, age FROM demo.users WHERE name "
                    + operator + " 'name_5';";
            validateOk(inputText, "validate_operatorString_ok on column - operator: " + operator);
        }
    }

    @Test
    public void validate_operatorBoolean_fail(){
        String [] operators = {">", "<", ">=", "<="};
        for(String operator : operators) {
            String inputText = "SELECT bool FROM demo.users WHERE bool "
                    + operator + " true;";
            validateFail(inputText, "validate_operatorBoolean_fail on column - operator: " + operator);
        }
    }

    //
    // Tests with table referred columns.
    //
    @Test
    public void validate_referred_ok(){
        String inputText = "SELECT users.name, users.age FROM demo.users WHERE name = 'name_5' AND age = 15;";
        validateOk(inputText, "validate_referred_ok");
    }

    @Test
    public void validate_referred_fail(){
        String inputText = "SELECT unknown.name, unknown.age FROM demo.users WHERE name = 'name_5' AND age = 15;";
        validateFail(inputText, "validate_referred_fail");
    }

    //
    // Tests with inner joins
    //
    @Test
    public void validate_innerJoin_basic_ok(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN demo.users ON users.name=users.name;";
        validateOk(inputText, "validate_innerJoin_basic_ok");
    }

    @Test
    public void validate_unknownKs1_fail(){
        String inputText = "SELECT users.name, users.age, users.email FROM unknown.users "
                + "INNER JOIN demo.users ON users.name=users.name;";
        validateFail(inputText, "validate_unknownKs1_fail");
    }

    @Test
    public void validate_unknownKs2_fail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN unknown.users ON users.name=users.name;";
        validateFail(inputText, "validate_unknownKs2_fail");
    }

    @Test
    public void validate_unknownTable2_fail(){
        String inputText = "SELECT users.name, users.age, users.email FROM demo.users "
                + "INNER JOIN demo.unknown ON users.name=users.name;";
        validateFail(inputText, "validate_unknownTable2_fail");
    }

    @Test
    public void validate_innerJoin_2tables_ok(){
        String inputText = "SELECT users.name, users.age, users_info.info FROM demo.users "
                + "INNER JOIN demo.users_info ON users.name=users_info.link_name;";
        validateOk(inputText, "validate_innerJoin_basic_ok");
    }

}
