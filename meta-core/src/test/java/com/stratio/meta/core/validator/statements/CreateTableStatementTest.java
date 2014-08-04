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

public class CreateTableStatementTest extends BasicValidatorTest {

  @Test
  public void validateBasicOk(){
    String inputText = "CREATE TABLE demo.new_table (id INT, name VARCHAR, check BOOLEAN, PRIMARY KEY (id, name));";
    validateOk(inputText, "validateBasicOk");
  }

  @Test
  public void validateAllSupportedOk(){
    String inputText = "CREATE TABLE demo.new_table (id INT, name VARCHAR, check BOOLEAN, PRIMARY KEY (id, name));";
    validateOk(inputText, "validateAllSupportedOk");
  }

  @Test
  public void validateIfNotExitsOk(){
    String inputText = "CREATE TABLE IF NOT EXISTS demo.users (name VARCHAR, gender VARCHAR, email VARCHAR, age INT, bool BOOLEAN, phrase VARCHAR, PRIMARY KEY ((name, gender), email, age));";
    validateOk(inputText, "validateIfNotExitsOk");
  }

  @Test
  public void validateEphemeralOk(){
    String inputText = "CREATE TABLE demo.temporal (name VARCHAR, gender VARCHAR, email VARCHAR, age INT, bool BOOLEAN, phrase VARCHAR, PRIMARY KEY (name))"
                       + " WITH ephemeral=true;";
    validateOk(inputText, "validateEphemeralOk");
  }

  @Test
  public void validatePkNotDeclared(){
    String inputText = "CREATE TABLE IF NOT EXISTS demo.users (name VARCHAR, gender VARCHAR, email VARCHAR, age INT, bool BOOLEAN, phrase VARCHAR, PRIMARY KEY ((unknown, gender), email, age));";
    validateFail(inputText, "validatePkNotDeclared");
  }

  @Test
  public void validateCkNotDeclared(){
    String inputText = "CREATE TABLE IF NOT EXISTS demo.users (name VARCHAR, gender VARCHAR, email VARCHAR, age INT, bool BOOLEAN, phrase VARCHAR, PRIMARY KEY ((name, gender), unknown, age));";
    validateFail(inputText, "validateCkNotDeclared");
  }

  @Test
  public void validateUnsupportedType(){
    String [] unsupported = {
        "ASCII",  "BLOB",   "DECIMAL",
        "INET",   "TEXT",   "TIMESTAMP",
        "UUID",   "VARINT", "TIMEUUID",
        "UNKNOWN"};
    for(String u : unsupported) {
      String inputText = "CREATE TABLE demo.table_fail (id " + u +", PRIMARY KEY (id));";
      validateFail(inputText, "validate_unsupportedType: " + u);
    }
  }

  @Test
  public void validateStratioColumnFail(){
    String inputText = "CREATE TABLE demo.table_fail (name VARCHAR, stratio_column VARCHAR, PRIMARY KEY (name));";
    validateFail(inputText, "validate_stratioColumnFail");
  }
}
