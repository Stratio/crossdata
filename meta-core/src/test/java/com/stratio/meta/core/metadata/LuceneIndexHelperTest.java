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

package com.stratio.meta.core.metadata;

import com.datastax.driver.core.ColumnMetadata;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.structures.IndexType;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class LuceneIndexHelperTest extends BasicCoreCassandraTest {

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    }

    @Test
    public void processLuceneOptions(){
        String options = "{\"schema\":\"{default_analyzer:\\\"org.apache.lucene.analysis.standard.StandardAnalyzer\\\",fields:{name:{type:\\\"string\\\"}, gender:{type:\\\"string\\\"}, email:{type:\\\"string\\\"}, age:{type:\\\"integer\\\"}, bool:{type:\\\"boolean\\\"}, phrase:{type:\\\"text\\\", analyzer:\\\"org.apache.lucene.analysis.en.EnglishAnalyzer\\\"}}}\",\"refresh_seconds\":\"1\",\"class_name\":\"org.apache.cassandra.db.index.stratio.RowIndex\"}";

        int numColumns = 6;
        LuceneIndexHelper lih = new LuceneIndexHelper(_session);
        Map<String, List<CustomIndexMetadata>> indexes = lih.processLuceneOptions(null, "stratio_lucene_index", options);
        assertEquals(numColumns, indexes.size(), "Invalid number of indexes");

        for(Map.Entry<String, List<CustomIndexMetadata>> entry : indexes.entrySet()){
            assertEquals(1, entry.getValue().size(), "Column has several indexes");
            assertEquals(IndexType.LUCENE, entry.getValue().get(0).getIndexType(), "Invalid type of index");
        }
    }

    @Test
    public void processLuceneFields(){

        String options = "{name:{type:\"string\"}, gender:{type:\"string\"}, email:{type:\"string\"}, age:{type:\"integer\"}, bool:{type:\"boolean\"}, phrase:{type:\"text\", analyzer:\"org.apache.lucene.analysis.en.EnglishAnalyzer\"}}";
        int numColumns = 6;

        LuceneIndexHelper lih = new LuceneIndexHelper(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp = null;
        JsonNode root = null;
        try {
            jp = factory.createJsonParser(options);
            root = mapper.readTree(jp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jp != null) {
                try {
                    jp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        assertNotNull(root, "Cannot index options");

        Map<String, List<CustomIndexMetadata>> indexes = lih.processLuceneFields(null, "stratio_lucene_index", root);
        assertNotNull(indexes, "Cannot retrieve mapped columns");
        assertEquals(numColumns, indexes.size(), "Invalid number of indexes");

        for(Map.Entry<String, List<CustomIndexMetadata>> entry : indexes.entrySet()){
            assertEquals(1, entry.getValue().size(), "Column has several indexes");
            assertEquals(IndexType.LUCENE, entry.getValue().get(0).getIndexType(), "Invalid type of index");
        }
    }

    @Test
    public void getIndexedColumns(){
        String keyspace = "demo";
        String table = "users";
        String column = "stratio_lucene_index_1";
        int numIndexedColumns = 6;

        ColumnMetadata cm = _session.getCluster().getMetadata()
                .getKeyspace(keyspace)
                .getTable(table)
                .getColumn(column);
        assertNotNull(cm, "Cannot retrieve test column");
        LuceneIndexHelper lih = new LuceneIndexHelper(_session);
        Map<String, List<CustomIndexMetadata>> indexedColumns = lih.getIndexedColumns(cm, column);
        assertEquals(numIndexedColumns, indexedColumns.size(), "Invalid number of indexes");
        for(Map.Entry<String, List<CustomIndexMetadata>> entry : indexedColumns.entrySet()){
            assertEquals(1, entry.getValue().size(), "Column has several indexes");
            assertEquals(IndexType.LUCENE, entry.getValue().get(0).getIndexType(), "Invalid type of index");
        }

    }

}
