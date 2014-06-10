/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.meta.common.metadata.structures.CatalogMetadata;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.TableMetadata;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;

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
    metadataManager = new MetadataManager(_session);
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
