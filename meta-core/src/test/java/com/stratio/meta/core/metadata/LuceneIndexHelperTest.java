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
