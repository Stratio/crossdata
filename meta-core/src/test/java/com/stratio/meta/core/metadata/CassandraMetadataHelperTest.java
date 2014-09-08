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

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta2.common.metadata.TableMetadata;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CassandraMetadataHelperTest extends BasicCoreCassandraTest {

  private static MetadataManager metadataManager = null;

  public static final CassandraMetadataHelper helper = new CassandraMetadataHelper();

  public void checkTables(TableMetadata tableMetadata,
      com.datastax.driver.core.TableMetadata cassandraTableMetadata) {
    throw new UnsupportedOperationException();
    /*
    assertEquals(tableMetadata.getName(), cassandraTableMetadata.getName(),
        "Invalid table name");
    assertEquals(tableMetadata.getColumns().size(), cassandraTableMetadata.getColumns().size(),
        "List of columns size does not match");
    com.datastax.driver.core.ColumnMetadata cassandraColumnMetadata = null;
    for (ColumnMetadata columnMetadata: tableMetadata.getColumns().values()) {
      cassandraColumnMetadata = cassandraTableMetadata.getColumn(new ColumnName(columnMetadata.getName()));
      assertNotNull(cassandraColumnMetadata,
          "Cannot retrieve column " + columnMetadata.getName());
      assertEquals(columnMetadata.getType().getDbType(), cassandraColumnMetadata.getType()
          .getName().toString().toUpperCase(), "Invalid db type");
      assertEquals(columnMetadata.getType().getDbClass(), cassandraColumnMetadata.getType()
          .asJavaClass(), "Invalid db class");
    }
    */
  }

  @BeforeClass
  public static void setUpBeforeClass() {
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoCatalog.cql");
    metadataManager = new MetadataManager(null);
    metadataManager.loadMetadata();
  }

  @Test
  public void toCatalogMetadata() {
    throw new UnsupportedOperationException();
    /*
    CatalogMetadata catalogMetadata = metadataManager.getCatalogMetadata("demo");
    CatalogMetadata catalogMetadata = helper.toCatalogMetadata(catalogMetadata);
    assertEquals(catalogMetadata.getCatalogName(), catalogMetadata.getName(),
        "Invalid catalog name");
    */
  }

  @Test
  public void toTableMetadata() {
    throw new UnsupportedOperationException();
    /*
    TableMetadata cassandraTableMetadata =
        metadataManager.getTableMetadata("demo", "types");
    TableMetadata tableMetadata = helper.toTableMetadata("demo", cassandraTableMetadata);
    checkTables(tableMetadata, cassandraTableMetadata);
    */
  }

}
