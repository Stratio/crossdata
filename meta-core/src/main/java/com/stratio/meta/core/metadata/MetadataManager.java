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

import com.stratio.meta.common.metadata.structures.ColumnType;
import com.stratio.meta.core.structures.IndexType;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Metadata Manager of the META server that maintains and up-to-date version of the metadata
 * associated with the existing catalogs and tables.
 */
public class MetadataManager {

  /**
   * Cluster metadata.
   */
  private ClusterMetadata clusterMetadata = null;

  /**
   * Lucene index helper used to parse custom index options.
   */
  private final LuceneIndexHelper luceneHelper;

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(MetadataManager.class.getName());

  /**
   * Class constructor.
   */
  public MetadataManager() {
    luceneHelper = new LuceneIndexHelper(null);
  }

  /**
   * Load all Metadata from Cassandra.
   * 
   * @return Whether the metadata has been loaded or not.
   */
  public boolean loadMetadata() {
    return clusterMetadata != null;
  }

  /**
   * Get the Metadata associated with a catalog.
   * 
   * @param catalog The target catalog.
   * @return The CatalogMetadata or null if the catalog is not found, or the client is not
   *         connected to Cassandra.
   */
  public CatalogMetadata getCatalogMetadata(String catalog) {
    throw new UnsupportedOperationException();
    /*
    CatalogMetadata result = null;
    if (clusterMetadata != null) {
      result = clusterMetadata.getCatalog(catalog);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Cluster metadata: " + result);
      }
    }
    return result;
    */
  }

  /**
   * Get the Metadata associated with a {@code tablename} of a {@code catalog}.
   * 
   * @param catalog The target catalog.
   * @param tablename The target table.
   * @return The TableMetadata or null if the table does not exist in the catalog, or the client is
   *         not connected to Cassandra.
   */
  public TableMetadata getTableMetadata(String catalog, TableName tablename) {
    throw new UnsupportedOperationException();
    /*
    TableMetadata result = null;
    if (clusterMetadata != null && clusterMetadata.getCatalog(catalog) != null) {
      boolean found = false;
      // Iterate through the tables matching the name. We cannot use the getTable
      // method as it changes the names using the Metadata.handleId
      Iterator<TableMetadata> tables = clusterMetadata.getCatalog(catalog).getTables().iterator();
      TableMetadata tableMetadata = null;
      while (!found && tables.hasNext()) {
        tableMetadata = tables.next();
        if (tableMetadata.getName().equalsIgnoreCase(tablename)) {
          found = true;
        }
      }

      if (found) {
        result = tableMetadata;
      }
    }
    return result;
    */
  }

  /**
   * Get the Metadata associated with a {@code tablename} of a {@code catalog}.
   * 
   * @param catalog The target catalog.
   * @param tablename The target table.
   * @return The TableMetadata or null if the table does not exist in the catalog, or the client is
   *         not connected to Cassandra.
   */
  public com.stratio.meta.common.metadata.structures.TableMetadata getTableGenericMetadata(
      String catalog, TableName tablename) {
    throw new UnsupportedOperationException();
    /*
    com.stratio.meta.common.metadata.structures.TableMetadata result = null;

    if (clusterMetadata != null && clusterMetadata.getCatalog(catalog) != null
        && tablename != null) {
      boolean found = false;

      // TODO Make it generic
      AbstractMetadataHelper helper = new CassandraMetadataHelper();
      CatalogMetadata catalogMetadata = getCatalogMetadata(catalog);

      List<com.stratio.meta.common.metadata.structures.TableMetadata> tableList = new ArrayList<>();
      // Adding db tables
      tableList.addAll(helper.toCatalogMetadata(catalogMetadata).getTables());
      // Adding ephemeral tables
      tableList.addAll(this.getEphemeralTables(catalog));

      Iterator<com.stratio.meta.common.metadata.structures.TableMetadata> tablesIt =
          tableList.iterator();
      while (!found && tablesIt.hasNext()) {
        com.stratio.meta.common.metadata.structures.TableMetadata tableMetadata = tablesIt.next();
        if (tablename.equalsIgnoreCase(tableMetadata.getTableName())) {
          result = tableMetadata;
          found = true;
        }
      }
    }

    return result;
    */
  }

  /**
   * Get the comment associated with a {@code tablename} of a {@code catalog}.
   * 
   * @param catalog The target catalog.
   * @param tablename The target table.
   * @return The comment or null if the table does not exist in the catalog, or the client is not
   *         connected to Cassandra.
   */
  public String getTableComment(String catalog, String tablename) {
    throw new UnsupportedOperationException();
    /*
    String result = null;
    if (clusterMetadata != null && clusterMetadata.getCatalog(catalog) != null) {
      result = clusterMetadata.getCatalog(catalog).getTable(tablename).getOptions().getComment();
    }
    return result;
    */
  }

  /**
   * Get the list of catalogs in Cassandra.
   * 
   * @return The list of catalogs or empty if not connected.
   */
  public List<String> getCatalogsNames() {
    throw new UnsupportedOperationException();
    /*
    List<String> result = new ArrayList<>();
    if (clusterMetadata != null) {
      for (CatalogMetadata list : clusterMetadata.getCatalogs()) {
        result.add(list.getName());
      }
    }
    return result;
    */
  }

  /**
   * Get the list of tables in a Cassandra catalogs.
   * 
   * @param catalog The name of the catalog
   * @return The list of tables or empty if the catalog does not exist, or the not connected.
   */
  public List<String> getTablesNames(String catalog) {
    throw new UnsupportedOperationException();
    /*
    List<String> result = new ArrayList<>();
    // Retrieve database tables.
    if (clusterMetadata != null && clusterMetadata.getCatalog(catalog) != null) {
      CatalogMetadata km = clusterMetadata.getCatalog(catalog);
      for (TableMetadata tm : km.getTables()) {
        result.add(tm.getName());
      }
    }

    return result;
    */
  }

  /**
   * Get the ephemeral tables in a catalog.
   * 
   * @param catalog The name of the catalog.
   * @return A list of {@link com.stratio.meta.common.metadata.structures.TableMetadata}.
   */
  public List<com.stratio.meta.common.metadata.structures.TableMetadata> getEphemeralTables(
      String catalog) {
    List<com.stratio.meta.common.metadata.structures.TableMetadata> result = new ArrayList<>();
    // Retreive ephemeral tables.
    /*if (stratioStreamingAPI != null) {
      List<StratioStream> ephemeralTables = getEphemeralTables();
      for (StratioStream stream : ephemeralTables) {
        if (stream.getStreamName().startsWith(catalog + "_")) {
          result.add(toTableMetadata(stream));
        }
      }
    }*/
    return result;
  }

  /**
   * Transform a Stratio Streaming {@link com.stratio.streaming.commons.streams.StratioStream} into
   * a META table metadata structure.
   *
   * @return A {@link com.stratio.meta.common.metadata.structures.TableMetadata} or null in case of
   *         error.
   */
  public com.stratio.meta.common.metadata.structures.TableMetadata toTableMetadata() {
    return null;
    /*boolean error = false;

    TableMetadata result = null;
    String[] streamName = stream.getStreamName().split("_");
    String tableName = streamName[1];
    String parentCatalog = streamName[0];
    Set<com.stratio.meta.common.metadata.structures.ColumnMetadata> columns = new HashSet<>();

    try {
      for (ColumnNameTypeValue col: stratioStreamingAPI.columnsFromStream(stream.getStreamName())) {
        ColumnType metaType = convertStreamingToMeta(col.getType());
        com.stratio.meta.common.metadata.structures.ColumnMetadata metaCol =
            new com.stratio.meta.common.metadata.structures.ColumnMetadata(tableName,
                col.getColumn(), metaType);
        columns.add(metaCol);
      }
    } catch (StratioEngineOperationException e) {
      LOG.error("Cannot retrieve streaming columns for " + stream.getStreamName(), e);
      error = true;
    }

    if (!error) {
      result =
          new com.stratio.meta.common.metadata.structures.TableMetadata(tableName, parentCatalog,
              TableType.EPHEMERAL, columns);
    }

    return result;*/
  }

  /**
   * Get the Lucene index associated with a table.
   * 
   * @param tableMetadata The metadata associated with the target table.
   * @return A {@link com.stratio.meta.core.metadata.CustomIndexMetadata} or null if not found.
   */
  public CustomIndexMetadata getLuceneIndex(TableMetadata tableMetadata) {
    List<CustomIndexMetadata> indexes = getTableIndex(tableMetadata);
    CustomIndexMetadata luceneIndex = null;
    for (int index = 0; index < indexes.size() && luceneIndex == null; index++) {
      if (IndexType.LUCENE.equals(indexes.get(index).getIndexType())) {
        luceneIndex = indexes.get(index);
      }
    }
    return luceneIndex;
  }

  /**
   * Get the list of indexes associated with a table.
   * 
   * @param tableMetadata The metadata associated with the target table.
   * @return A list of {@link com.stratio.meta.core.metadata.CustomIndexMetadata} with the indexes.
   */
  public List<CustomIndexMetadata> getTableIndex(TableMetadata tableMetadata) {
    throw new UnsupportedOperationException();
    /*
    List<CustomIndexMetadata> result = new ArrayList<>();

    // Iterate through the table columns.
    for (ColumnMetadata column : tableMetadata.getColumns()) {
      if (column.getIndex() != null) {
        if (LOG.isTraceEnabled()) {
          LOG.trace("Index found in column " + column.getName());
        }
        CustomIndexMetadata toAdd = null;
        if (!column.getIndex().isCustomIndex()) {
          // A Cassandra index is associated with the column.
          toAdd =
              new CustomIndexMetadata(column, column.getIndex().getName(), IndexType.DEFAULT,
                  column.getName());
        } else if (column.getIndex().getIndexClassName()
            .compareTo("org.apache.cassandra.db.index.stratio.RowIndex") == 0) {
          // A Lucene custom index is found that may index several columns.
          toAdd = luceneHelper.getLuceneIndex(column, column.getIndex().getName());
        } else {
          LOG.error("Index " + column.getIndex().getName() + " on " + column.getName()
              + " with class " + column.getIndex().getIndexClassName() + " not supported.");
        }
        result.add(toAdd);
      }
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Table " + tableMetadata.getName() + " has " + result.size() + " indexed columns.");
    }
    return result;*/
  }

  public boolean checkStream(String ephemeralTableName) {
    /*if(stratioStreamingAPI != null) {
      List<StratioStream> streams = getEphemeralTables();
      if (streams != null && streams.size() > 0) {
        for (StratioStream stream : streams) {
      if (stream.getStreamName().equalsIgnoreCase(ephemeralTableName)) {
        return true;
      }
    }
      }
    }*/
    return false;
  }

  /**
   * Get the list of ephemeral tables using the streaming API.
   * 
   * @return The list of tables or null in case of error.
   */
  /*public List<StratioStream> getEphemeralTables() {
    List<StratioStream> streamsList = null;
    try {
      streamsList = stratioStreamingAPI.listStreams();
    } catch (Exception e) {
      LOG.error("Cannot retrieve stream list", e);
    }
    return streamsList;
  }*/

  public List<String> getStreamingColumnNames(String ephemeralTableName) {
    LOG.debug("Looking up columns from ephemeral table " + ephemeralTableName);
    List<String> colNames = new ArrayList<>();
    /*try {
      List<ColumnNameTypeValue> cols = stratioStreamingAPI.columnsFromStream(ephemeralTableName);
      for (ColumnNameTypeValue ctp : cols) {
        colNames.add(ctp.getColumn().toLowerCase());
      }
    } catch (Exception e) {
      LOG.error(e);
    }*/
    return colNames;
  }

  /*public StratioStream checkQuery(String s) {
    StratioStream result = null;
    try {
      List<StratioStream> streamsList = stratioStreamingAPI.listStreams();
      for (StratioStream stream : streamsList) {
        if (stream.getQueries().size() > 0) {
          for (StreamQuery query : stream.getQueries()) {
            if (s.contentEquals(query.getQueryId())) {
              result = stream;
            }
          }
        }
      }
    } catch (Exception e) {
      LOG.error(e);
    }
    return result;
  }*/

  /*public ColumnNameTypeValue findStreamingColumn(String ephemeralTable, String column) {
    try {
      List<ColumnNameTypeValue> cols = stratioStreamingAPI.columnsFromStream(ephemeralTable);
      for (ColumnNameTypeValue col : cols) {
        if (col.getColumn().equalsIgnoreCase(column)) {
          return col;
        }
      }
    } catch (StratioEngineOperationException e) {
      LOG.error(e);
    }
    return null;
  }*/

  /*public List<ColumnNameTypeValue> getStreamingColumns(String ephemeralTable) {
    try {
      return stratioStreamingAPI.columnsFromStream(ephemeralTable);
    } catch (StratioEngineOperationException e) {
      LOG.error(e);
    }
    return null;
  }*/

  public TableMetadata convertStreamingToMeta(
      String catalog, TableName tablename) {
    throw new UnsupportedOperationException();
    /*
    Set<com.stratio.meta.common.metadata.structures.ColumnMetadata> columns = new HashSet<>();
    try {
      for (ColumnNameTypeValue col : stratioStreamingAPI.columnsFromStream(catalog + "_"
          + tablename)) {
        ColumnType metaType = convertStreamingToMeta(col.getType());
        com.stratio.meta.common.metadata.structures.ColumnMetadata metaCol =
            new com.stratio.meta.common.metadata.structures.ColumnMetadata(tablename,
                col.getColumn(), metaType);
        columns.add(metaCol);
      }
    } catch (StratioEngineOperationException e) {
      LOG.error("Cannot convert Streaming metadata to meta", e);
      return null;
    }
    com.stratio.meta.common.metadata.structures.TableMetadata tableMetadata =
        new com.stratio.meta.common.metadata.structures.TableMetadata(tablename, catalog,
            TableType.EPHEMERAL, columns);
    return tableMetadata;
    */
  }

  private ColumnType convertStreamingToMeta(ColumnType type) {
    //return StreamingUtils.streamingToMetaType(type.getValue());
    return null;
  }

}
