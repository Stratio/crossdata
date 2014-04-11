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

package com.stratio.meta.core.grammar;

import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.utils.MetaQuery;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;


/**
 * MetaParser tests that recognize the different options of each Statement.
 */
public class ParsingTest {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(ParsingTest.class);
    protected final Parser parser = new Parser();


    public MetaStatement testRegularStatement(String inputText, String methodName) {
        MetaQuery mq = parser.parseStatement(inputText);
        MetaStatement st = mq.getStatement();
        assertNotNull(st, "Cannot parse "+methodName
                + " parser error: " + mq.hasError()
                + " -> " + mq.getResult().getErrorMessage());
        assertFalse(mq.hasError(), "Parsing expecting '" + inputText
                + "' from '" + st.toString() + "' returned: " + mq.getResult().getErrorMessage());
        
        System.out.println("inputText:"+inputText);
        System.out.println("st.toStrg:"+st.toString()+";");
        
        assertTrue(inputText.equalsIgnoreCase(st.toString()+";"),
                "Cannot parse " + methodName
                        + ": expecting '" + inputText
                        + "' from '" + st.toString()+";'" );
        return st;
    }

    public void testParseFails(String inputText, String methodName){
        MetaQuery mq = parser.parseStatement(inputText);
        assertNotNull(mq, "Parser should return a query");
        assertNull(mq.getStatement(), "Null statement expected. Returned: " + mq.getStatement());
        assertTrue(mq.hasError(), "Parser should return and error for " + methodName);
    }

    public void testRecoverableError(String inputText, String methodName){
        MetaQuery metaQuery = parser.parseStatement(inputText);
        assertTrue(metaQuery.hasError(), "No errors reported in "+methodName);
    }

    // CREATE KEYSPACE
    @Test
    public void createKeyspace_ifNotExists() {
        String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
                + "WITH replication = replicationLevel AND durable_writes = false;";
        testRegularStatement(inputText, "createKeyspace_ifNotExists");
    }

    //TODO: Should we support it in this way?
    @Test
    public void createKeyspace_nestedOptions() {
        String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
                + "WITH replication = {class: NetworkTopologyStrategy, DC1: 1, DC2: 3} "
                +"AND durable_writes = false;";
        Set<String> properties = new HashSet<>();
        properties.add("class: NetworkTopologyStrategy");
        properties.add("DC1: 1");
        properties.add("DC2: 3");
        MetaStatement st = parser.parseStatement(inputText).getStatement();
        String propResultStr = st.toString().substring(st.toString().indexOf("{")+1, st.toString().indexOf("}"));
        String[] str = propResultStr.split(",");
        Set<String> propertiesResult = new HashSet<>();
        for (String str1 : str) {
            propertiesResult.add(str1.trim());
        }
        assertNotNull(st, "Cannot parse create keyspace - nestedOptions");
        assertEquals("CREATE KEYSPACE IF NOT EXISTS key_space1 WITH replication = {",
                st.toString().substring(0, st.toString().indexOf("{")+1),
                "Cannot parse create keyspace - nestedOptions");
        assertEquals("} AND durable_writes = false;",
                st.toString().substring(st.toString().indexOf("}"))+";",
                "Cannot parse create keyspace - nestedOptions");
        assertTrue(propertiesResult.containsAll(properties), "Cannot parse create keyspace - nestedOptions");
        assertTrue(properties.containsAll(propertiesResult), "Cannot parse create keyspace - nestedOptions");
    }

    @Test
    public void createKeyspace_basicOptions() {
        String inputText = "CREATE KEYSPACE key_space1 WITH replication = {class: SimpleStrategy, replication_factor: 1}"
                + " AND durable_writes = false;";
        MetaStatement st = parser.parseStatement(inputText).getStatement();
        assertNotNull(st, "Cannot parse createKeyspace_basicOptions");

        boolean originalOK = false;
        boolean alternative1 = false;

        if(inputText.equalsIgnoreCase(st.toString()+";")){
            originalOK = true;
        }

        String alternative1Str = "CREATE KEYSPACE key_space1 WITH replication = {replication_factor: 1, class: SimpleStrategy}"
                + " AND durable_writes = false;";
        if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
            alternative1 = true;
        }

        assertTrue((originalOK || alternative1), "Cannot parse createKeyspace_basicOptions");
    }

    @Test
    public void createKeyspace_durable_writes() {
        String inputText = "CREATE KEYSPACE demo WITH replication = {class: SimpleStrategy, replication_factor: 1} "
                + "AND durable_writes = false;";
        MetaStatement st = parser.parseStatement(inputText).getStatement();
        assertNotNull(st, "Cannot parse createKeyspace_durable_writes");

        boolean originalOK = false;
        boolean alternative1 = false;

        if(inputText.equalsIgnoreCase(st.toString()+";")){
            originalOK = true;
        }

        String alternative1Str = "CREATE KEYSPACE demo WITH replication = {replication_factor: 1, class: SimpleStrategy} "
                + "AND durable_writes = false;";
        if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
            alternative1 = true;
        }

        assertTrue((originalOK || alternative1), "Cannot parse createKeyspace_durable_writes");
    }

    @Test
    public void createKeyspace_map_column() {
        String inputText = "CREATE TABLE demo.banks (day text, key uuid, latitude double, longitude double, name text, "
                + "address text, tags map<text,boolean>, lucene text, PRIMARY KEY (day, key));";
        testRegularStatement(inputText, "createKeyspace_map_column");
    }

    @Test
    public void update_tablename() {
        String inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
                + " WHERE ident3 IN (term3, term4) IF field1 = 25;";
        testRegularStatement(inputText, "update_tablename");
    }

    @Test
    public void insert_into() {
        String inputText = "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES (term1, term2) "
                + "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
        testRegularStatement(inputText, "insert_into");
    }

    @Test
    public void insert_into_2() {
        String inputText = "INSERT INTO mykeyspace.tablename (column1, column2) VALUES (value1, value2)"
                + " IF NOT EXISTS USING TTL = 10;";
        testRegularStatement(inputText, "insert_into_2");
    }

    @Test
    public void insert_into_allValueTypes() {
        String inputText = "INSERT INTO mykeyspace.tablename (c1, c2, c3, c4, c5) VALUES (text, 'quoted_text', 123, 1.23, true);";
        testRegularStatement(inputText, "insert_into_allValueTypes");
    }

    @Test
    public void truncate_table() {
        String inputText = "TRUNCATE usersTable;";
        testRegularStatement(inputText, "truncate_table");
    }

    //
    //CREATE INDEX

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
        testRegularStatement(inputText, "createIndex_default_all");
    }

    //DROP INDEX
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
    public void select_statement() {
        String inputText = "SELECT ident1 AS name1, myfunction(innerIdent, anotherIdent) AS functionName "
                + "FROM newks.newtb WITH WINDOW 5 ROWS INNER JOIN tablename ON field1=field2 WHERE ident1 LIKE whatever"
                + " ORDER BY id1 ASC GROUP BY col1 LIMIT 50 DISABLE ANALYTICS;";
        testRegularStatement(inputText, "select_statement");
    }

    @Test
    public void select_statement_2() {
        String inputText = "SELECT lucene FROM newks.newtb;";
        testRegularStatement(inputText, "select_statement_2");
    }

    @Test
    public void select_withTimeWindow() {
        String inputText = "SELECT column1 FROM table1 WITH WINDOW 5 SECONDS WHERE column2 = 3;";
        testRegularStatement(inputText, "select_withTimeWindow");
    }

    //ADD
    @Test
    public void add_basic() {
        String inputText = "ADD \"jar_name-v1.0.jar\";";
        testRegularStatement(inputText, "add_basic");
    }

    @Test
    public void add_relative() {
        String inputText = "ADD \"dir/jar_name-v1.0.jar\";";
        testRegularStatement(inputText, "add_relative");
    }

    @Test
    public void add_absolute() {
        String inputText = "ADD \"/dir/jar_name-v1.0.jar\";";
        testRegularStatement(inputText, "add_absolute");
    }

    //LIST

    @Test
    public void list_process() {
        String inputText = "LIST PROCESS;";
        testRegularStatement(inputText, "list_process");
    }

    @Test
    public void list_process_lowercase() {
        String inputText = "LIST process;";
        testRegularStatement(inputText, "list_process_lowercase");
    }

    @Test
    public void list_udf() {
        String inputText = "LIST UDF;";
        testRegularStatement(inputText, "list_udf");
    }

    @Test
    public void list_trigger() {
        String inputText = "LIST TRIGGER;";
        testRegularStatement(inputText, "list_trigger");
    }

    //REMOVE UDF
    @Test
    public void removeUDF() {
        String inputText = "REMOVE UDF \"jar.name\";";
        testRegularStatement(inputText, "removeUDF");
    }

    //DELETE ( <selection> ( ',' <selection> )* )?
    //FROM <tablename>
    //WHERE <where-clause>
    @Test
    public void delete_where() {
        String inputText = "DELETE FROM table1 WHERE field1 = value1;";
        testRegularStatement(inputText, "delete_where");
    }

    @Test
    public void delete_selection() {
        String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1;";
        testRegularStatement(inputText, "delete_selection");
    }

    @Test
    public void delete_full() {
        String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
        testRegularStatement(inputText, "delete_full");
    }

    @Test
    public void set_basic() {
        String inputText = "SET OPTIONS ANALYTICS=true AND CONSISTENCY=LOCAL_ONE;";
        testRegularStatement(inputText, "set_basic");
    }

    //UPDATE

    @Test
    public void update_basic() {
        String inputText = "UPDATE table1 SET field1 = value1 WHERE field3 = value3;";
        testRegularStatement(inputText, "update_basic");
    }

    @Test
    public void explain_plan() {
        String inputText = "EXPLAIN PLAN FOR DROP INDEX indexName;";
        testRegularStatement(inputText, "explain_plan");
    }

    @Test
    public void drop_table() {
        String inputText = "DROP TABLE IF EXISTS lastTable;";
        testRegularStatement(inputText, "drop_table");
    }

    @Test
    public void update_where() {
        String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
                + " field2 = value2 WHERE field3 = value3 AND field4 = value4;";
        testRegularStatement(inputText, "update_where");
    }

    @Test
    public void update_full() {
        String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
                + " field2 = value2 WHERE field3 = value3 AND field4 = value4"
                + " IF field5 = transaction_value5;";
        testRegularStatement(inputText, "update_full");
    }

    @Test
    public void createTable_basic() {
        String inputText = "create table adsa (algo text primary key, algo2 int, algo3 bool);";
        testRegularStatement(inputText, "createTable_basic");
    }

    @Test
    public void createTable_basic_2() {
        String inputText = "create table adsa (algo text, algo2 int primary key, algo3 bool);";
        testRegularStatement(inputText, "createTable_basic_2");
    }

    @Test
    public void createTable_basic_3() {
        String inputText = "create table adsa (algo text, algo2 int, algo3 bool primary key);";
        testRegularStatement(inputText, "createTable_basic_3");
    }

    @Test
    public void createTable_basic_4() {
        String inputText = "create table adsa (algo text, algo2 int, algo3 bool, primary key (algo));";
        testRegularStatement(inputText, "createTable_basic_4");
    }

    @Test
    public void createTable_basic_5() {
        String inputText = "create table adsa (algo text, algo2 int, algo3 bool, primary key (algo, algo2));";
        testRegularStatement(inputText, "createTable_basic_5");
    }

    @Test
    public void createTable_basic_6() {
        String inputText = "create table adsa (algo text, algo2 int, algo3 bool, primary key ((algo, algo2), algo3));";
        testRegularStatement(inputText, "createTable_basic_6");
    }

    @Test
    public void createTable_basic_7() {
        String inputText = "create table adsa (algo text, algo2 int, algo3 bool, primary key ((algo, algo2), algo3)) "
                + "with propiedad1=prop1 and propiedad2=2 and propiedad3=3.0;";
        testRegularStatement(inputText, "createTable_basic_7");
    }

    @Test
    public void createTable_with_many_properties() {
        String inputText = "CREATE TABLE key_space1.users (name varchar, password varchar, color varchar, gender varchar,"
                + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                + "WITH compression={sstable_compression: DeflateCompressor, chunk_length_kb: 64} AND "
                + "compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND read_repair_chance=1.0;";
        MetaStatement st = parser.parseStatement(inputText).getStatement();
        assertNotNull(st, "Cannot parse createTable_with_many_properties");

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
                "Cannot parse createTable_with_many_properties");
    }

    @Test
    public void createTable_compact_storage() {
        String inputText = "CREATE TABLE key_space1.sblocks (block_id uuid, subblock_id uuid, data blob, PRIMARY KEY "
                + "(block_id, subblock_id)) WITH COMPACT STORAGE;";
        testRegularStatement(inputText, "createTable_compact_storage");
    }

    @Test
    public void createTable_clustering() {
        String inputText = "create table key_space1.timeseries (event_type text, insertion_time timestamp, event blob,"
                + " PRIMARY KEY (event_type, insertion_time)) WITH CLUSTERING ORDER BY (insertion_time DESC);";
        testRegularStatement(inputText, "createTable_clustering");
    }

    @Test
    public void createTable_with_properties() {
        String inputText = "CREATE TABLE key_space1.test (name varchar, color varchar, gender varchar, food varchar, "
                + "animal varchar, PRIMARY KEY (name)) WITH compression={sstable_compression: DeflateCompressor, "
                + "chunk_length_kb: 64} AND compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND "
                + "read_repair_chance=1.0;";
        //MetaStatement st = parser.parseStatement(inputText).getStatement();
        MetaQuery mq = parser.parseStatement(inputText);
        MetaStatement st = mq.getStatement();

        assertNotNull(st, "Statement should not be null createTable_with_properties");

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
                "Cannot parse createTable_with_properties");
    }

    @Test
    public void alterKeyspace() {
        String inputText = "ALTER KEYSPACE mykeyspace WITH ident1 = value1 AND ident2 = 54;";
        testRegularStatement(inputText, "alterKeyspace");
    }

    @Test
    public void dropKeyspace() {
        String inputText = "drop keyspace IF EXISTS mykeyspace;";
        testRegularStatement(inputText, "dropKeyspace");
    }

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
    public void stopProcess() {
        String inputText = "STOP process process1;";
        testRegularStatement(inputText, "stopProcess");
    }

    @Test
    public void createTrigger() {
        String inputText = "create trigger trigger1 on table_name USING triggerClassName;";
        testRegularStatement(inputText, "createTrigger");
    }

    @Test
    public void dropTrigger() {
        String inputText = "drop trigger trigger1 on table_name;";
        testRegularStatement(inputText, "dropTrigger");
    }

    // TEST META EXCEPTIONS

    @Test
    public void update_for_invalid_assignment(){
        String inputText = "UPDATE table1 SET field1 = value1 WHERE field3: value3;";
        testRecoverableError(inputText, "update_for_invalid_assignment");
    }

    @Test
    public void update_wrong_spelling(){
        String inputText = "UPDDATE table1 SET field1 = value1 WHERE field3: value3;";
        testParseFails(inputText, "update_wrong_spelling");
    }

    @Test
    public void unknown_first_word_of_statement(){
        String inputText = "WINDOWS GO HOME;";
        testParseFails(inputText, "unknown_first_word_of_statement");
    }

    @Test
    public void wrong_plan_token(){
        String inputText = "EXPLAIN PLAANS FOR DROP INDEX indexName;";
        testParseFails(inputText, "wrong_plan_token");
    }

    @Test
    public void wrong_into_token(){
        String inputText = "INSERT INTI mykeyspace.tablename (ident1, ident2) VALUES(term1, term2)"
                + " IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
        testRecoverableError(inputText, "wrong_into_token");
    }

    @Test
    public void create_table_wrong_column_definition(){
        String inputText = "CREATE TABLE adsa (algo text, primary key ([algo, algo2],algo3));";
        testParseFails(inputText, "create_table_wrong_column_definition");
    }

    @Test
    public void insert_into_wrong_values_token(){
        String inputText = "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUED (term1, term2)"
                + " IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
        testParseFails(inputText, "insert_into_wrong_values_token");
    }

    @Test
    public void create_keyspace_wrong_identifier(){
        String inputText = "CREATE KEYSPACE name.key_space1 WITH replication = replicationLevel;";
        testParseFails(inputText, "create_keyspace_wrong_identifier");
    }

    @Test
    public void truncate_wrong_identifier(){
        String inputText = "TRUNCATE companyKS..usersTable;";
        testRecoverableError(inputText, "truncate_wrong_identifier");
    }

    @Test
    public void alter_wrong_keyspace_token(){
        String inputText = "ALTER KEYSPACES mykeyspace WITH ident1 = value1;";
        testParseFails(inputText, "alter_wrong_keyspace_token");
    }

    @Test
    public void drop_wrong_place_for_if_exists(){
        String inputText = "DROP KEYSPACE mykeyspace IF EXISTS;";
        testRecoverableError(inputText, "drop_wrong_place_for_if_exists");
    }

    @Test
    public void set_wrong_boolean(){
        String inputText = "SET OPTIONS ANALYTICS=5;";
        testParseFails(inputText, "set_wrong_boolean");
    }

    @Test
    public void alter_wrong_property_identifier(){
        String inputText = "ALTER TABLE tabla1 with 2property1=value1;";
        testParseFails(inputText, "set_wrong_boolean");
    }

    @Test
    public void drop_not_missing(){
        String inputText = "DROP TABLE IF EXISTS _lastTable;";
        testRecoverableError(inputText, "drop_not_missing");
    }

    @Test
    public void create_index_wrong_option_assignment(){
        String inputText = "CREATE LUCENE INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1:val1;";
        testRecoverableError(inputText, "create_index_wrong_option_assignment");
    }

    @Test
    public void drop_index_wrong_not_word(){
        String inputText = "DROP INDEX IF NOT EXISTS index_name;";
        testRecoverableError(inputText, "drop_index_wrong_not_word");
    }

    @Test
    public void delete_wrong_property_assignment(){
        String inputText = "DELETE (col1 AND col2) FROM table1 WHERE field1: value1;";
        testRecoverableError(inputText, "delete_wrong_property_assignment");
    }

    @Test
    public void select_with_match(){
        String inputText = "SELECT * FROM demo.emp WHERE first_name MATCH s2o;";
        testRegularStatement(inputText, "select_with_match");
    }
    
    @Test
    public void select_wrong_like_word(){
        String inputText = "SELECT ident1, myfunction(innerIdent, anotherIdent) LIKE ident1 FROM newks.newtb;";
        testParseFails(inputText, "select_wrong_like_word");
    }

    @Test
    public void add_ending_quote_missing(){
        String inputText = "ADD \"/dir/jar_name-v1.0.jar;";
        testRecoverableError(inputText, "add_ending_quote_missing");
    }

    @Test
    public void list_reserved_word_use(){
        String inputText = "LIST PROCESS LAST;";
        testParseFails(inputText, "list_reserved_word_use");
    }

    @Test
    public void remove_udf_not_expected_word() {
        String inputText = "REMOVE UDF \"jar.name\" NOW;";
        testParseFails(inputText, "remove_udf_not_expected_word");
    }

    @Test
    public void stop_wrong_process_identifier(){
        String inputText = "STOP process kstest.process1;";
        testParseFails(inputText, "stop_wrong_process_identifier");
    }

    @Test
    public void create_trigger_wrong_as_word_use(){
        String inputText = "create trigger trigger1 on table_name USING triggerClassName AS ident1;";
        testParseFails(inputText, "create_trigger_wrong_as_word_use");
    }

    @Test
    public void drop_trigger_wrong_assignment(){
        String inputText = "drop trigger trigger1 on table_name = 20;";
        testParseFails(inputText, "drop_trigger_wrong_assignment");
    }

}
