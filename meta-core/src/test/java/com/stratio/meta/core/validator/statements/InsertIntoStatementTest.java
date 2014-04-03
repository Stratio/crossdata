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
import static org.testng.Assert.assertTrue;

public class InsertIntoStatementTest extends BasicValidatorTest {


    @Test
    public void validate_basic_ok(){
        String inputText = "INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateOk(inputText, "validate_basic_ok");
    }

    @Test
    public void validate_basic_noKs(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validate_basic_noKs");
    }

    @Test
    public void validate_unknownColumn(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validate_unknownColumn");
    }

    @Test
    public void validate_booleanColumnFail(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, 'true', '');";
        validateFail(inputText, "validate_booleanColumnFail");
    }

    @Test
    public void validate_integerColumnFail(){
        String inputText = "INSERT INTO users (unknown, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', '10', true, '');";
        validateFail(inputText, "validate_integerColumnFail");
    }

    @Test
    public void validate_textColumnFail(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase) VALUES (true, 'male', 'name_0@domain.com', 10, true, '');";
        validateFail(inputText, "validate_textColumnFail");
    }

    @Test
    public void validate_stratioColumnFail(){
        String inputText = "INSERT INTO users (name, gender, email, age, bool, phrase, stratio_lucene_index_1) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '', 'error');";
        validateFail(inputText, "validate_textColumnFail");
    }

}
