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

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.meta.common.metadata.structures.CatalogMetadata;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.TableMetadata;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CassandraMetadataHelperTest extends BasicCoreCassandraTest {

  private static MetadataManager metadataManager = null;

  public static final CassandraMetadataHelper helper = new CassandraMetadataHelper();

  public void checkTables(TableMetadata tableMetadata,
      com.datastax.driver.core.TableMetadata cassandraTableMetadata) {
    assertEquals(tableMetadata.getTableName(), cassandraTableMetadata.getName(),
        "Invalid table name");
    assertEquals(tableMetadata.getColumns().size(), cassandraTableMetadata.getColumns().size(),
        "List of columns size does not match");
    com.datastax.driver.core.ColumnMetadata cassandraColumnMetadata = null;
    for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
      cassandraColumnMetadata = cassandraTableMetadata.getColumn(columnMetadata.getColumnName());
      assertNotNull(cassandraColumnMetadata,
          "Cannot retrieve column " + columnMetadata.getColumnName());
      assertEquals(columnMetadata.getType().getDbType(), cassandraColumnMetadata.getType()
          .getName().toString().toUpperCase(), "Invalid db type");
      assertEquals(columnMetadata.getType().getDbClass(), cassandraColumnMetadata.getType()
          .asJavaClass(), "Invalid db class");
    }
  }

  @BeforeClass
  public static void setUpBeforeClass() {
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    metadataManager = new MetadataManager(_session, stratioStreamingAPI);
    metadataManager.loadMetadata();
  }

  @Test
  public void toCatalogMetadata() {
    KeyspaceMetadata keyspaceMetadata = metadataManager.getKeyspaceMetadata("demo");
    CatalogMetadata catalogMetadata = helper.toCatalogMetadata(keyspaceMetadata);
    assertEquals(catalogMetadata.getCatalogName(), keyspaceMetadata.getName(),
        "Invalid catalog name");
  }

  @Test
  public void toTableMetadata() {
    com.datastax.driver.core.TableMetadata cassandraTableMetadata =
        metadataManager.getTableMetadata("demo", "types");
    TableMetadata tableMetadata = helper.toTableMetadata("demo", cassandraTableMetadata);
    checkTables(tableMetadata, cassandraTableMetadata);
  }

}
