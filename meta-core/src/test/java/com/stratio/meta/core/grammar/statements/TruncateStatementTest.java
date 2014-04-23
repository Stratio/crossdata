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

public class TruncateStatementTest extends ParsingTest {

    @Test
    public void truncate_table() {
        String inputText = "TRUNCATE usersTable;";
        testRegularStatement(inputText, "truncate_table");
    }

    @Test
    public void truncate_wrong_identifier(){
        String inputText = "TRUNCATE companyKS..usersTable;";
        testRecoverableError(inputText, "truncate_wrong_identifier");
    }


}