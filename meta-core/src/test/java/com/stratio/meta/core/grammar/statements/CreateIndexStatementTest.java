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

public class CreateIndexStatementTest extends ParsingTest{

    // CREATE <type_index>? INDEX (IF NOT EXISTS)? <identifier>? ON <tablename> '(' <identifier> (',' <identifier>)* ')'
    // ( USING <string> )? WITH OPTIONS? (<maps> AND <maps>...) ';'
    //DEFAULT → Usual inverted index, Hash index. (By default).
    //LUCENE → Full text index.
    //CUSTOM → custom index. (new feature for release 2)

    @Test
    public void createIndex_default_basic() {
        String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1, field2);";
        testRegularStatement(inputText, "createIndex_default_basic");
    }


    @Test
    public void createIndex_default_ifNotExist() {
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 ON table1 (field1, field2);";
        testRegularStatement(inputText, "createIndex_default_ifNotExist");
    }

    @Test
    public void createIndex_default_using() {
        String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1, field2) USING com.company.Index.class;";
        testRegularStatement(inputText, "createIndex_default_using");
    }

    @Test
    public void createIndex_lucene() {
        String inputText = "CREATE LUCENE INDEX demo_banks ON banks"
                + " (day, entry_id, latitude, longitude, name, address, tags)"
                + " USING \'org.apache.cassandra.db.index.stratio.RowIndex\'"
                + " WITH OPTIONS = {\'schema\': "
                + "\'{default_analyzer:\"org.apache.lucene.analysis.standard.StandardAnalyzer\","
                + "fields:"
                + "{day:{type:\"date\", pattern:\"yyyy-MM-dd\"},"
                + " entry_id:{type:\"uuid\"}, latitude:{type:\"double\"},"
                + " longitude:{type:\"double\"}, name:{type:\"text\"},"
                + " address:{type:\"string\"}, tags:{type:\"boolean\"}}}\'};";
        testRegularStatement(inputText, "createIndex_lucene");
    }

    @Test
    public void createIndex_default_all() {
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 "
                + "ON table1 (field1, field2) USING com.company.Index.class "
                + "WITH OPTIONS = {'key1': 'val1'};";
        System.out.println(inputText+" works");
        testRegularStatement(inputText, "createIndex_default_all");
    }

    @Test
    public void create_index_wrong_option_assignment(){
        String inputText = "CREATE LUCENE INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1:val1;";
        testRecoverableError(inputText, "create_index_wrong_option_assignment");
    }

    //CREATE HASH INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1=val1 AND opt2=val2;
    @Test
    public void createIndex_default_all_and() {
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 "
                + "ON table1 (field1, field2) USING com.company.Index.class "
                + "WITH OPTIONS = {'key1': 'val1' AND 'key2': 'val2'};";
        testRegularStatement(inputText, "createIndex_default_all_and");
    }

    @Test
    public void createIndex_default_basic_with_space_before_semicolon() {
        String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1, field2) ;";
        testRegularStatement(inputText, "createIndex_default_basic_with_space_before_semicolon");
    }



}