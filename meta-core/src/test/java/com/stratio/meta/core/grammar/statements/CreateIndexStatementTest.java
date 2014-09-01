/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
    public void createIndexDefaultBasic() {
        String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1, field2);";
        testRegularStatement(inputText, "createIndexDefaultBasic");
    }


    @Test
    public void createIndexDefaultIfNotExist() {
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 ON table1 (field1, field2);";
        testRegularStatement(inputText, "createIndexDefaultIfNotExist");
    }

    @Test
    public void createIndexDefaultUsing() {
        String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1, field2) USING com.company.Index.class;";
        testRegularStatement(inputText, "createIndexDefaultUsing");
    }

    @Test
    public void createIndexLucene() {
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
        String expectedText = inputText.replace("INDEX demo_banks ON", "INDEX stratio_lucene_demo_banks ON");
        testRegularStatement(inputText, expectedText, "createIndexLucene");
    }

    @Test
    public void createIndexDefaultAll() {
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 "
                + "ON table1 (field1, field2) USING com.company.Index.class "
                + "WITH OPTIONS = {'key1': 'val1'};";
        testRegularStatement(inputText, "createIndexDefaultAll");
    }

    @Test
    public void createDefaultIndexWithOptions2() {
                String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 "
                    + "ON table1 (field1, field2) USING com.company.Index.class "
                    + "WITH OPTIONS = {'key1': 'val1', 'key2': 'val2'};";
                testRegularStatement(inputText, "createIndexWithOptions2");
    }

    @Test
    public void createLuceneIndexWithOptions2() {
            String inputText = "CREATE LUCENE INDEX IF NOT EXISTS index1 "
                    + "ON table1 (field1, field2) USING com.company.Index.class "
                    + "WITH OPTIONS = {'key1': 'val1', 'key2': 'val2'};";
            String expectedTest = inputText.replace("index1", "stratio_lucene_index1");
            testRegularStatement(inputText, expectedTest, "createIndexWithOptions2");
    }


    @Test
    public void createIndexWrongOptionAssignment(){
        String inputText = "CREATE LUCENE INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1:val1;";
        testRecoverableError(inputText, "createIndexWrongOptionAssignment");
    }

    @Test
    public void createIndexDefaultBasicWithSpaceBeforeSemicolon() {
        String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1; field2);";
        testRecoverableError(inputText, "createIndexDefaultBasicWithSpaceBeforeSemicolon");
    }

    @Test
    public void createDefaultIndexLowercase() {
        String inputText = "create default index index1 on table1 (field1, field2);";
        testRegularStatement(inputText, "createDefaultIndexLowercase");
    }

    @Test
    public void createLuceneIndexLowercase() {
        String inputText = "create lucene index index1 on table1 (field1, field2);";
        String expectedTest = inputText.replace("index1", "stratio_lucene_index1");
        testRegularStatement(inputText, expectedTest, "createLuceneIndexLowercase");
    }

}
