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

package com.stratio.meta.core.validator.statements;

import com.stratio.meta.core.validator.BasicValidatorTest;
import org.testng.annotations.Test;

public class CreateIndexStatementTest extends BasicValidatorTest {

    @Test
    public void validateDefaultNoNameOk(){
        String inputText = "CREATE DEFAULT INDEX ON demo.users (email);";
        validateOk(inputText, "validateDefaultNoNameOk");
    }

    @Test
    public void validateDefaultNamedOk(){
        String inputText = "CREATE DEFAULT INDEX new_index ON demo.users (email);";
        validateOk(inputText, "validateDefaultNamedOk");
    }

    @Test
    public void validateDefaultIfNotExistsOk(){
        String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS users_gender_idx ON demo.users (gender);";
        validateOk(inputText, "validateDefaultIfNotExistsOk");
    }

    @Test
    public void validateDefaultExistsFail(){
        String inputText = "CREATE DEFAULT INDEX users_gender_idx ON demo.users (gender);";
        validateFail(inputText, "validateDefaultExistsFail");
    }

    @Test
    public void validateNotExistsTablename(){
        String inputText = "CREATE DEFAULT INDEX users_gender_idx ON demo.unknown (gender);";
        validateFail(inputText, "validateNotExistsTablename");
    }

    @Test
    public void validateNotExistsKeyspace(){
        String inputText = "CREATE DEFAULT INDEX users_gender_idx ON unknown.users (gender);";
        validateFail(inputText, "validateNotExistsKeyspace");
    }

    //
    // --- Lucene ---
    //

    @Test
    public void validateLuceneNoNameOk(){
        String inputText = "CREATE LUCENE INDEX ON demo.types (varchar_column);";
        validateOk(inputText, "validateLuceneNoNameOk");
    }

    @Test
    public void validateLuceneNamedOk() {
        String inputText = "CREATE LUCENE INDEX new_index ON demo.types (varchar_column);";
        String expectedText = inputText.replace("INDEX new_index ON", "INDEX stratio_lucene_new_index ON");
        validateOk(inputText, expectedText, "validateLuceneNamedOk");
    }

    @Test
    public void validateLucene2columnsOk() {
        String inputText = "CREATE LUCENE INDEX new_index ON demo.types (varchar_column, boolean_column);";
        String expectedText = inputText.replace("INDEX new_index ON", "INDEX stratio_lucene_new_index ON");
        validateOk(inputText, expectedText, "validateLucene2columnsOk");
    }

    @Test
    public void validateLuceneStratioNameFail() {
        String inputText = "CREATE LUCENE INDEX stratio_new_index ON demo.types (varchar_column, boolean_column);";
        String expectedText = inputText.replace("INDEX stratio_new_index ON", "INDEX stratio_lucene_stratio_new_index ON");
        validateFail(inputText, expectedText, "validateLuceneStratioNameFail");
    }

    @Test
    public void validateLucene2indexesFail(){
        String inputText = "CREATE LUCENE INDEX ON demo.users (gender);";
        validateFail(inputText, "validateLucene2indexesFail");
    }

    @Test
    public void validateLuceneStratioColumnFail() {
        String inputText = "CREATE LUCENE INDEX new_index ON demo.users (email, name, stratio_lucene_index_1);";
        String expectedText = inputText.replace("INDEX new_index ON", "INDEX stratio_lucene_new_index ON");
        validateFail(inputText, expectedText, "validateLuceneStratioColumnFail");
    }

    @Test
    public void validateLuceneWithOptionsFail() {
        String inputText = "CREATE LUCENE INDEX new_index ON demo.users (email, bool, age)"
                + " WITH OPTIONS = {sdfsfsf};";
        String expectedText = inputText.replace("INDEX new_index ON", "INDEX stratio_lucene_new_index ON");
        validateFail(inputText, expectedText, "validateLuceneWithOptionsFail");
    }

    @Test
    public void validateLuceneWithOptionsFullFail() {
        String inputText = "CREATE LUCENE INDEX demo_banks ON demo.banks (lucene) "
                + "USING org.apache.cassandra.db.index.stratio.RowIndex WITH OPTIONS = "
                + "{schema: '{default_analyzer:\"org.apache.lucene.analysis.standard.StandardAnalyzer\", fields: {day: {type: \"date\", pattern: \"yyyy-MM-dd\"}, key: {type:\"uuid\"}}}'};";
        String expectedText = inputText.replace("INDEX demo_banks ON", "INDEX stratio_lucene_demo_banks ON");
        validateFail(inputText, expectedText, "validateLuceneWithOptionsFullFail");
    }
    
}
