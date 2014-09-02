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

package com.stratio.meta2.core.grammar.statements;

import com.stratio.meta.common.exceptions.ParsingException;
import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta2.core.statements.MetaStatement;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CreateTableStatementTest extends ParsingTest {

  //
  // CREATE TABLE
  //

  @Test
  public void createTableBasic() {
    String inputText = "CREATE TABLE myTable ON CLUSTER siliconValley (something text PRIMARY KEY, something2 int, something3 bool);";
    String expectedText = "CREATE TABLE <unknown_name>.myTable ON CLUSTER cluster.siliconvalley (<unknown_name>.myTable.something text PRIMARY KEY, <unknown_name>.myTable.something2 int, <unknown_name>.myTable.something3 bool);";
    testRegularStatement(inputText, expectedText, "createTableBasic");
  }

  @Test
  public void createTableBasic2() {
    String inputText = "CREATE TABLE myTable ON CLUSTER siliconValley (something text, something2 int PRIMARY KEY, something3 bool);";
    String expectedText = "CREATE TABLE demo.myTable ON CLUSTER cluster.siliconValley (demo.myTable.something text, demo.myTable.something2 int PRIMARY KEY, demo.myTable.something3 bool);";
    testRegularStatementSession("demo", inputText, expectedText, "createTableBasic2");
  }

  @Test
  public void createTableBasic3() {
    String inputText = "CREATE TABLE business.myTable ON CLUSTER siliconValley (something text, something2 int, something3 bool PRIMARY KEY);";
    String expectedText = "CREATE TABLE business.myTable ON CLUSTER cluster.siliconValley (business.myTable.something text, business.myTable.something2 int, business.myTable.something3 bool PRIMARY KEY);";
    testRegularStatementSession("demo", inputText, expectedText, "createTableBasic3");
  }

  @Test
  public void createTableBasic4() {
    String inputText = "CREATE TABLE myTable ON CLUSTER siliconValley (something text, something2 int, something3 bool, PRIMARY KEY (something));";
    String expectedText = "CREATE TABLE demo.myTable ON CLUSTER cluster.siliconValley (demo.myTable.something text, demo.myTable.something2 int, demo.myTable.something3 bool, PRIMARY KEY (demo.myTable.something));";
    testRegularStatementSession("demo", inputText, expectedText, "createTableBasic4");
  }

  @Test
  public void createTableBasic5() {
    String inputText = "CREATE TABLE myTable ON CLUSTER siliconValley (something text, something2 int, something3 bool, PRIMARY KEY (something, something2));";
    String expectedText = "CREATE TABLE demo.myTable ON CLUSTER cluster.siliconValley (demo.myTable.something text, demo.myTable.something2 int, demo.myTable.something3 bool, PRIMARY KEY (demo.myTable.something, demo.myTable.something2));";
    testRegularStatementSession("demo", inputText, expectedText, "createTableBasic5");
  }

  @Test
  public void createTableBasic6() {
    String inputText = "CREATE TABLE myTable ON CLUSTER siliconValley (something text, something2 int, something3 bool, PRIMARY KEY ((something, something2), something3));";
    String expectedText = "CREATE TABLE demo.myTable ON CLUSTER cluster.siliconValley (demo.myTable.something text, demo.myTable.something2 int, demo.myTable.something3 bool, PRIMARY KEY ((demo.myTable.something, demo.myTable.something2), demo.myTable.something3));";
    testRegularStatementSession("demo", inputText, expectedText, "createTableBasic6");
  }

  @Test
  public void createTableBasic7() {
    String inputText = "CREATE TABLE myTable ON CLUSTER siliconValley (something text, something2 int, something3 bool, PRIMARY KEY ((something, something2), something3)) "
                       + "WITH 'propiedad1'='prop1' AND 'propiedad2'=2 and 'propiedad3'=3.0;";
    String expectedText = "CREATE TABLE demo.myTable ON CLUSTER cluster.siliconValley (demo.myTable.something text, demo.myTable.something2 int, demo.myTable.something3 bool, PRIMARY KEY ((demo.myTable.something, demo.myTable.something2), demo.myTable.something3)) "
                          + "WITH 'propiedad1'='prop1' and 'propiedad2'=2 AND 'propiedad3'=3.0;";
    testRegularStatementSession("demo", inputText, expectedText, "createTableBasic7");
  }

  @Test
  public void createEphemeralTable() {
    String inputText = "CREATE TABLE streaming.temporal ON CLUSTER siliconValley (name varchar, age int, rating double, member boolean, PRIMARY KEY (name)) "
                       + "WITH ephemeral=true;";
    String expectedText = "CREATE TABLE streaming.temporal ON CLUSTER cluster.siliconValley (streaming.temporal.name varchar, streaming.temporal.age int, streaming.temporal.rating double, streaming.temporal.member boolean, PRIMARY KEY (streaming.temporal.name)) "
                          + "WITH ephemeral=true;";
    testRegularStatementSession("demo", inputText, expectedText, "createEphemeralTable");
  }

  @Test
  public void createTableWithManyProperties() {
    String inputText = "CREATE TABLE key_space1.users ON CLUSTER siliconValley (name varchar, password varchar, color varchar, gender varchar,"
                       + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                       + "WITH compression={sstable_compression: DeflateCompressor, chunk_length_kb: 64} AND "
                       + "compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND read_repair_chance=1.0;";
    MetaStatement st = null;
    try {
      st = parser.parseStatement("key_space1", inputText);
    } catch (ParsingException e) {
      e.printStackTrace();
    }
    assertNotNull(st, "Cannot parse createTableWithManyProperties");

    boolean originalOK = false;
    boolean alternative1 = false;
    boolean alternative2 = false;
    boolean alternative3 = false;

    if(inputText.equalsIgnoreCase(st.toString()+";")){
      originalOK = true;
    }

    String alternative1Str = "CREATE TABLE key_space1.users ON CLUSTER cluster.siliconValley (key_space1.users.name varchar, key_space1.users.password varchar, key_space1.users.color varchar, key_space1.users.gender varchar,"
                             + " key_space1.users.food varchar, key_space1.users.animal varchar, key_space1.users.age int, key_space1.users.code int, PRIMARY KEY ((key_space1.users.name, key_space1.users.gender), key_space1.users.color, key_space1.users.animal)) "
                             + "WITH compression={chunk_length_kb: 64, sstable_compression: DeflateCompressor} AND "
                             + "compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND read_repair_chance=1.0;";
    if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
      alternative1 = true;
    }
    String alternative2Str = "CREATE TABLE key_space1.users ON CLUSTER cluster.siliconValley (key_space1.users.name varchar, key_space1.users.password varchar, key_space1.users.color varchar, key_space1.users.gender varchar,"
                             + " key_space1.users.food varchar, key_space1.users.animal varchar, key_space1.users.age int, key_space1.users.code int, PRIMARY KEY ((key_space1.users.name, key_space1.users.gender), key_space1.users.color, key_space1.users.animal)) "
                             + "WITH compression={sstable_compression: DeflateCompressor, chunk_length_kb: 64} AND "
                             + "compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND read_repair_chance=1.0;";
    if(alternative2Str.equalsIgnoreCase(st.toString()+";")){
      alternative2 = true;
    }
    String alternative3Str = "CREATE TABLE key_space1.users ON CLUSTER cluster.siliconValley (key_space1.users.name varchar, key_space1.users.password varchar, key_space1.users.color varchar, key_space1.users.gender varchar,"
                             + " key_space1.users.food varchar, key_space1.users.animal varchar, key_space1.users.age int, key_space1.users.code int, PRIMARY KEY ((key_space1.users.name, key_space1.users.gender), key_space1.users.color, key_space1.users.animal)) "
                             + "WITH compression={chunk_length_kb: 64, sstable_compression: DeflateCompressor} AND "
                             + "compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND read_repair_chance=1.0;";
    if(alternative3Str.equalsIgnoreCase(st.toString()+";")){
      alternative3 = true;
    }

    assertTrue((originalOK || alternative1 || alternative2 || alternative3), "Cannot parse createTableWithManyProperties");
  }

  @Test
  public void createTableCompactStorage() {
    String inputText = "CREATE TABLE key_space1.sblocks ON CLUSTER siliconValley (block_id uuid, subblock_id uuid, data blob, PRIMARY KEY "
                       + "(block_id, subblock_id)) WITH COMPACT STORAGE;";
    String expectedText = "CREATE TABLE key_space1.sblocks ON CLUSTER cluster.siliconValley (key_space1.sblocks.block_id uuid, key_space1.sblocks.subblock_id uuid, key_space1.sblocks.data blob, PRIMARY KEY "
                          + "(key_space1.sblocks.block_id, key_space1.sblocks.subblock_id)) WITH COMPACT STORAGE;";
    testRegularStatementSession("demo", inputText, expectedText, "createTableCompactStorage");
  }

  @Test
  public void createTableClustering() {
    String inputText = "CREATE TABLE key_space1.timeseries ON CLUSTER siliconValley (event_type text, insertion_time timestamp, event blob,"
                       + " PRIMARY KEY (event_type, insertion_time)) WITH CLUSTERING ORDER BY (insertion_time DESC);";
    String expectedText = "CREATE TABLE key_space1.timeseries ON CLUSTER cluster.siliconValley (key_space1.timeseries.event_type text, key_space1.timeseries.insertion_time timestamp, key_space1.timeseries.event blob,"
                          + " PRIMARY KEY (key_space1.timeseries.event_type, key_space1.timeseries.insertion_time)) WITH CLUSTERING ORDER BY (key_space1.timeseries.insertion_time DESC);";
    testRegularStatementSession("demo", inputText, expectedText, "createTableClustering");
  }

  @Test
  public void createTableWithProperties() {
    String inputText = "CREATE TABLE key_space1.test ON CLUSTER siliconValley (name varchar, color varchar, gender varchar, food varchar, "
                       + "animal varchar, PRIMARY KEY (name)) WITH 'compression'='{sstable_compression: DeflateCompressor, "
                       + "chunk_length_kb: 64}' AND 'compaction'='{class: SizeTieredCompactionStrategy, min_threshold: 6}' AND "
                       + "'read_repair_chance'=1.0;";
    //MetaStatement st = parser.parseStatement(inputText).getStatement();
    MetaStatement st = null;
    try {
      st = parser.parseStatement("key_space1", inputText);
    } catch (ParsingException e) {
      e.printStackTrace();
    }

    assertNotNull(st, "Statement should not be null createTableWithProperties");

    boolean originalOK = false;
    boolean alternative1 = false;
    boolean alternative2 = false;
    boolean alternative3 = false;

    if(inputText.equalsIgnoreCase(st.toString()+";")){
      originalOK = true;
    }

    String alternative1Str = "CREATE TABLE key_space1.test ON CLUSTER cluster.siliconValley (key_space1.test.name varchar, key_space1.test.color varchar, key_space1.test.gender varchar, key_space1.test.food varchar, "
                             + "key_space1.test.animal varchar, PRIMARY KEY (key_space1.test.name)) WITH compression={chunk_length_kb: 64, "
                             + "sstable_compression: DeflateCompressor} AND compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND "
                             + "read_repair_chance=1.0;";
    if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
      alternative1 = true;
    }
    String alternative2Str = "CREATE TABLE key_space1.test ON CLUSTER cluster.siliconValley (key_space1.test.name varchar, key_space1.test.color varchar, key_space1.test.gender varchar, key_space1.test.food varchar, "
                             + "key_space1.test.animal varchar, PRIMARY KEY (key_space1.test.name)) WITH compression={sstable_compression: DeflateCompressor, "
                             + "chunk_length_kb: 64} AND compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND "
                             + "read_repair_chance=1.0;";
    if(alternative2Str.equalsIgnoreCase(st.toString()+";")){
      alternative2 = true;
    }
    String alternative3Str = "CREATE TABLE key_space1.test ON CLUSTER cluster.siliconValley (key_space1.test.name varchar, key_space1.test.color varchar, key_space1.test.gender varchar, key_space1.test.food varchar, "
                             + "key_space1.test.animal varchar, PRIMARY KEY (key_space1.test.name)) WITH compression={chunk_length_kb: 64, "
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
    String inputText = "CREATE TABLE demo.banks ON CLUSTER siliconValley (day text, key uuid, latitude double, longitude double, name text, "
                       + "address text, tags map<text,boolean>, lucene text, PRIMARY KEY (day, key));";
    String expectedText = "CREATE TABLE demo.banks ON CLUSTER cluster.siliconValley (demo.banks.day text, demo.banks.key uuid, demo.banks.latitude double, demo.banks.longitude double, demo.banks.name text, "
                          + "demo.banks.address text, demo.banks.tags map<text,boolean>, demo.banks.lucene text, PRIMARY KEY (demo.banks.day, demo.banks.key));";
    testRegularStatementSession("demo", inputText, expectedText, "createTableMapColumn");
  }

  @Test
  public void createTableWrongColumnDefinition(){
    String inputText = "CREATE TABLE myTable ON CLUSTER siliconValley (something text, PRIMARY KEY ([something, something2],something3));";
    testParserFails(inputText, "createTableWrongColumnDefinition");
  }

  @Test
  public void createTableWithGetMetaProperty() {
    String inputText = "CREATE TABLE key_space1.timeseries ON CLUSTER siliconValley (event_type text, insertion_time timestamp, event blob,"
                       + " PRIMARY KEY (event_type, insertion_time)) WITH CLUSTERING ORDER BY (insertion_time DESC) AND ephemeral=true;";
    String expectedText = "CREATE TABLE key_space1.timeseries ON CLUSTER cluster.siliconValley (key_space1.timeseries.event_type text, key_space1.timeseries.insertion_time timestamp, key_space1.timeseries.event blob,"
                          + " PRIMARY KEY (key_space1.timeseries.event_type, key_space1.timeseries.insertion_time)) WITH CLUSTERING ORDER BY (key_space1.timeseries.insertion_time DESC) AND ephemeral=true;";
    testRegularStatementSession("demo", inputText, expectedText, "createTableWithGetMetaProperty");
  }

  @Test
  public void createTableWithOptions(){
    String inputText = "CREATE TABLE key_space1.wallet ON CLUSTER siliconValley (day text, key uuid, latitude double, longitude double, name text, "
                       + "address text, tags map<text,boolean>, lucene text, PRIMARY KEY (day, key)) WITH COMPACT STORAGE AND " +
                       "'read_repair_chance'=1.0;";
    String expectedText = "CREATE TABLE key_space1.wallet ON CLUSTER cluster.siliconValley (key_space1.wallet.day text, key_space1.wallet.key uuid, key_space1.wallet.latitude double, key_space1.wallet.longitude double, key_space1.wallet.name text, "
                          + "key_space1.wallet.address text, key_space1.wallet.tags map<text,boolean>, key_space1.wallet.lucene text, PRIMARY KEY (key_space1.wallet.day, key_space1.wallet.key)) WITH COMPACT STORAGE AND " +
                          "'read_repair_chance'=1.0;";
    testRegularStatementSession("demo", inputText, expectedText, "createTableWithOptions");
  }

}
