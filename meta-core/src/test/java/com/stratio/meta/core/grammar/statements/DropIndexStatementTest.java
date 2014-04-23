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

public class DropIndexStatementTest extends ParsingTest {

    @Test
    public void dropIndex_basic() {
        String inputText = "DROP INDEX demo.index_name;";
        testRegularStatement(inputText, "dropIndex_basic");
    }

    @Test
    public void dropIndex_noKs_ok() {
        String inputText = "DROP INDEX index_name;";
        testRegularStatement(inputText, "dropIndex_noKs_ok");
    }

    @Test
    public void dropIndex_ifExists() {
        String inputText = "DROP INDEX IF EXISTS demo.index_name;";
        testRegularStatement(inputText, "dropIndex_ifExists");
    }

    @Test
    public void drop_index_wrong_not_word(){
        String inputText = "DROP INDEX IF NOT EXISTS index_name;";
        testRecoverableError(inputText, "drop_index_wrong_not_word");
    }


}