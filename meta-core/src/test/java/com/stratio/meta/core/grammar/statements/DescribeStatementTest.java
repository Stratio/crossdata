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

public class DescribeStatementTest extends ParsingTest {

    @Test
    public void describeKeyspaceBasic() {
        String inputText = "DESCRIBE KEYSPACE keyspace1;";
        testRegularStatement(inputText, "describeKeyspaceBasic");
    }

    @Test
    public void describeTableBasic() {
        String inputText = "DESCRIBE TABLE keyspace1.table1;";
        testRegularStatement(inputText, "describeTableBasic");
    }

    @Test
    public void describeTableFail() {
        String inputText = "DESCRIBE UNKNOWN keyspace1.table1;";
        testParseFails(inputText, "describeTableFail");
    }

}