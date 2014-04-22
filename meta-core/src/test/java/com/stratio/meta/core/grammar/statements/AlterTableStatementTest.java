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

package com.stratio.meta.core.grammar.statements;

import com.stratio.meta.core.grammar.ParsingTest;
import org.testng.annotations.Test;

public class AlterTableStatementTest extends ParsingTest{

    @Test
    public void alterTable_basic() {
        String inputText = "alter table tabla1 alter column1 type int;";
        testRegularStatement(inputText, "alterTable_basic");
    }

    @Test
    public void alterTable_basic_1() {
        String inputText = "alter table tabla1 add column1 int;";
        testRegularStatement(inputText, "alterTable_basic_1");
    }

    @Test
    public void alterTable_basic_2() {
        String inputText = "alter table tabla1 drop column1;";
        testRegularStatement(inputText, "alterTable_basic_2");
    }

    @Test
    public void alterTable_basic_3() {
        String inputText = "Alter table tabla1 with property1=value1 and property2=2 and property3=3.0;";
        testRegularStatement(inputText, "alterTable_basic_3");
    }

    @Test
    public void alter_wrong_property_identifier(){
        String inputText = "ALTER TABLE tabla1 with 2property1=value1;";
        testParseFails(inputText, "set_wrong_boolean");
    }


}