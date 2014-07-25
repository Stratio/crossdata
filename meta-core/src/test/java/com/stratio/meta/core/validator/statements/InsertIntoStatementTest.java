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

public class InsertIntoStatementTest extends BasicValidatorTest {


    @Test
    public void validateBasicOk(){
        String inputText = "INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateOk(inputText, "validateBasicOk");
    }
    
    @Test
    public void validateBasicNoColumns(){
        String inputText = "INSERT INTO demo.users VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateOk(inputText, "validateBasicNoColumns");
    }
    
    @Test
    public void validateBasicNoKs(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validateBasicNoKs");
    }

    @Test
    public void validateUnknownColumn(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validateUnknownColumn");
    }

    @Test
    public void validateBooleanColumnFail(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, 'true', '');";
        validateFail(inputText, "validateBooleanColumnFail");
    }

    @Test
    public void validateIntegerColumnFail(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', '10', true, '');";
        validateFail(inputText, "validateIntegerColumnFail");
    }

    @Test
    public void validateTextColumnFail(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase) VALUES (true, 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validateTextColumnFail");
    }

    @Test
    public void validateStratioColumnFail(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase, stratio_lucene_index_1) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '', 'error');";
        validateFail(inputText, "validateStratioColumnFail");
    }

}
