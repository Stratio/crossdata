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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class LuceneIndexHelperTest extends BasicCoreCassandraTest {

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    }

    @Test
    public void processLuceneMapping(){
        String options = "{\"schema\":\"{default_analyzer:\\\"org.apache.lucene.analysis.standard.StandardAnalyzer\\\",fields:{name:{type:\\\"string\\\"}, gender:{type:\\\"string\\\"}, email:{type:\\\"string\\\"}, age:{type:\\\"integer\\\"}, bool:{type:\\\"boolean\\\"}, phrase:{type:\\\"text\\\", analyzer:\\\"org.apache.lucene.analysis.en.EnglishAnalyzer\\\"}}}\",\"refresh_seconds\":\"1\",\"class_name\":\"org.apache.cassandra.db.index.stratio.RowIndex\"}";

        int numColumns = 6;
        LuceneIndexHelper lih = new LuceneIndexHelper(_session);
        CustomIndexMetadata cim = lih.processLuceneMapping(null, "stratio_lucene_index", options);

        assertEquals(numColumns, cim.getIndexedColumns().size(), "Invalid number of indexes");

    }

    @Test
    public void getLuceneIndex(){
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
        CustomIndexMetadata index = lih.getLuceneIndex(cm, column);

        assertEquals(numIndexedColumns, index.getIndexedColumns().size(), "Invalid number of indexes");

    }
}