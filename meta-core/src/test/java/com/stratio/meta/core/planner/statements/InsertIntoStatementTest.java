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

package com.stratio.meta.core.planner.statements;

import com.stratio.meta.core.planner.BasicPlannerTest;
import com.stratio.meta.core.statements.InsertIntoStatement;
import com.stratio.meta.core.structures.BooleanTerm;
import com.stratio.meta.core.structures.IntegerTerm;
import com.stratio.meta.core.structures.StringTerm;
import com.stratio.meta.core.structures.ValueCell;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertIntoStatementTest  extends BasicPlannerTest {

    @Test
    public void testPlanForInsert(){
        String inputText = "INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');";
        List<String> ids = Arrays.asList("name", "gender", "email", "age", "bool", "phrase");
        List<ValueCell> list = new ArrayList();
        list.add(new StringTerm("name_0"));
        list.add(new StringTerm("male"));
        list.add(new IntegerTerm("10"));
        list.add(new BooleanTerm("false"));
        list.add(new StringTerm(""));
        stmt = new InsertIntoStatement("demo.users", ids, list, false, new ArrayList());
        validateCassandraPath("testPlanForInsert");
    }
}
