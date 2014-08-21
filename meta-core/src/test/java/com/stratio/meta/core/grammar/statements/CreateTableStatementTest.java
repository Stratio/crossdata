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
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.MetaQuery;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CreateTableStatementTest extends ParsingTest {

  //
  // CREATE TABLE
  //

  @Test
  public void createTableBasic() {
    String inputText = "create table adsa (something text primary key, something2 int, something3 bool);";
    testRegularStatement(inputText, "createTableBasic");
  }

  @Test
  public void createTableBasic2() {
    String inputText = "create table adsa (something text, something2 int primary key, something3 bool);";
    testRegularStatement(inputText, "createTableBasic2");
  }

  @Test
  public void createTableBasic3() {
    String inputText = "create table adsa (something text, something2 int, something3 bool primary key);";
    testRegularStatement(inputText, "createTableBasic3");
  }

  @Test
  public void createTableBasic4() {
    String inputText = "create table adsa (something text, something2 int, something3 bool, primary key (something));";
    testRegularStatement(inputText, "createTableBasic4");
  }

  @Test
  public void createTableBasic5() {
    String inputText = "create table adsa (something text, something2 int, something3 bool, primary key (something, something2));";
    testRegularStatement(inputText, "createTableBasic5");
  }

  @Test
  public void createTableBasic6() {
    String inputText = "create table adsa (something text, something2 int, something3 bool, primary key ((something, something2), something3));";
    testRegularStatement(inputText, "createTableBasic6");
  }

  @Test
  public void createTableBasic7() {
    String inputText = "create table adsa (something text, something2 int, something3 bool, primary key ((something, something2), something3)) "
                       + "with propiedad1=prop1 and propiedad2=2 and propiedad3=3.0;";
    testRegularStatement(inputText, "createTableBasic7");
  }

  @Test
  public void createEphemeralTable() {
    String inputText = "create table temporal (name varchar, age int, rating double, member boolean, primary key (name)) "
                       + "with ephemeral=true;";
    testRegularStatement(inputText, "createEphemeralTable");
  }

  @Test
  public void createTableWithManyProperties() {
    String inputText = "CREATE TABLE key_space1.users (name varchar, password varchar, color varchar, gender varchar,"
                       + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                       + "WITH compression={sstable_compression: DeflateCompressor, chunk_length_kb: 64} AND "
                       + "compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND read_repair_chance=1.0;";
    MetaStatement st = parser.parseStatement(inputText).getStatement();
    assertNotNull(st, "Cannot parse createTableWithManyProperties");

    boolean originalOK = false;
    boolean alternative1 = false;
    boolean alternative2 = false;
    boolean alternative3 = false;

    if(inputText.equalsIgnoreCase(st.toString()+";")){
      originalOK = true;
    }

    String alternative1Str = "CREATE TABLE key_space1.users (name varchar, password varchar, color varchar, gender varchar,"
                             + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                             + "WITH compression={chunk_length_kb: 64, sstable_compression: DeflateCompressor} AND "
                             + "compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND read_repair_chance=1.0;";
    if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
      alternative1 = true;
    }
    String alternative2Str = "CREATE TABLE key_space1.users (name varchar, password varchar, color varchar, gender varchar,"
                             + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                             + "WITH compression={sstable_compression: DeflateCompressor, chunk_length_kb: 64} AND "
                             + "compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND read_repair_chance=1.0;";
    if(alternative2Str.equalsIgnoreCase(st.toString()+";")){
      alternative2 = true;
    }
    String alternative3Str = "CREATE TABLE key_space1.users (name varchar, password varchar, color varchar, gender varchar,"
                             + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                             + "WITH compression={chunk_length_kb: 64, sstable_compression: DeflateCompressor} AND "
                             + "compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND read_repair_chance=1.0;";
    if(alternative3Str.equalsIgnoreCase(st.toString()+";")){
      alternative3 = true;
    }

    assertTrue((originalOK || alternative1 || alternative2 || alternative3),
               "Cannot parse createTableWithManyProperties");
  }

  @Test
  public void createTableCompactStorage() {
    String inputText = "CREATE TABLE key_space1.sblocks (block_id uuid, subblock_id uuid, data blob, PRIMARY KEY "
                       + "(block_id, subblock_id)) WITH COMPACT STORAGE;";
    testRegularStatement(inputText, "createTableCompactStorage");
  }

  @Test
  public void createTableClustering() {
    String inputText = "create table key_space1.timeseries (event_type text, insertion_time timestamp, event blob,"
                       + " PRIMARY KEY (event_type, insertion_time)) WITH CLUSTERING ORDER BY (insertion_time DESC);";
    testRegularStatement(inputText, "createTableClustering");
  }

  @Test
  public void createTableWithProperties() {
    String inputText = "CREATE TABLE key_space1.test (name varchar, color varchar, gender varchar, food varchar, "
                       + "animal varchar, PRIMARY KEY (name)) WITH compression={sstable_compression: DeflateCompressor, "
                       + "chunk_length_kb: 64} AND compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND "
                       + "read_repair_chance=1.0;";
    //MetaStatement st = parser.parseStatement(inputText).getStatement();
    MetaQuery mq = parser.parseStatement(inputText);
    MetaStatement st = mq.getStatement();

    assertNotNull(st, "Statement should not be null createTableWithProperties");

    boolean originalOK = false;
    boolean alternative1 = false;
    boolean alternative2 = false;
    boolean alternative3 = false;

    if(inputText.equalsIgnoreCase(st.toString()+";")){
      originalOK = true;
    }

    String alternative1Str = "CREATE TABLE key_space1.test (name varchar, color varchar, gender varchar, food varchar, "
                             + "animal varchar, PRIMARY KEY (name)) WITH compression={chunk_length_kb: 64, "
                             + "sstable_compression: DeflateCompressor} AND compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND "
                             + "read_repair_chance=1.0;";
    if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
      alternative1 = true;
    }
    String alternative2Str = "CREATE TABLE key_space1.test (name varchar, color varchar, gender varchar, food varchar, "
                             + "animal varchar, PRIMARY KEY (name)) WITH compression={sstable_compression: DeflateCompressor, "
                             + "chunk_length_kb: 64} AND compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND "
                             + "read_repair_chance=1.0;";
    if(alternative2Str.equalsIgnoreCase(st.toString()+";")){
      alternative2 = true;
    }
    String alternative3Str = "CREATE TABLE key_space1.test (name varchar, color varchar, gender varchar, food varchar, "
                             + "animal varchar, PRIMARY KEY (name)) WITH compression={chunk_length_kb: 64, "
                             + "sstable_compression: DeflateCompressor} AND compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND "
                             + "read_repair_chance=1.0;";
    if(alternative3Str.equalsIgnoreCase(st.toString()+";")){
      alternative3 = true;
    }

    assertTrue((originalOK || alternative1 || alternative2 || alternative3),
               "Cannot parse createTableWithProperties");
  }

  @Test
  public void createTableMapColumn() {
    String inputText = "CREATE TABLE demo.banks (day text, key uuid, latitude double, longitude double, name text, "
                       + "address text, tags map<text,boolean>, lucene text, PRIMARY KEY (day, key));";
    testRegularStatement(inputText, "createTableMapColumn");
  }

  @Test
  public void createTableWrongColumnDefinition(){
    String inputText = "CREATE TABLE adsa (something text, primary key ([something, something2],something3));";
    testParseFails(inputText, "createTableWrongColumnDefinition");
  }

  @Test
  public void createTableWithGetMetaProperty() {
    for(String o:new String[]{
        "CLUSTERING ORDER BY (insertion_time DESC)"
        ,"ephemeral=true"
        //,"ephemeral=false" //TODO: ¿tiene sentido?
        ,"CLUSTERING ORDER BY (insertion_time DESC) and ephemeral=true"
    }){
      String inputText = "create table key_space1.timeseries (event_type text, insertion_time timestamp, event blob,"
                         + " PRIMARY KEY (event_type, insertion_time)) WITH "+o+";";
      testRegularStatement(inputText, "createTableWithGetMetaProperty");
    }

    //,"ephemeral=false" //TODO: ¿tiene sentido?
        /*
        String inputText = "create table key_space1.timeseries (event_type text, insertion_time timestamp, event blob,"
                + " PRIMARY KEY (event_type, insertion_time)) WITH ephemeral=false;";
        testRegularStatement(inputText, inputText,"createTableWithGetMetaProperty");
        */
  }

  @Test
  public void createTableWithOptions(){
    String inputText = "CREATE TABLE key_space1.wallet (day text, key uuid, latitude double, longitude double, name text, "
                       + "address text, tags map<text,boolean>, lucene text, PRIMARY KEY (day, key)) WITH COMPACT STORAGE AND " +
                       "read_repair_chance=1.0;";
    testRegularStatement(inputText, "createTableWithOptions");
  }

}
