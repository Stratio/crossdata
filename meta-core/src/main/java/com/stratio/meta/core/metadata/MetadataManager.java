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

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import com.stratio.meta.common.metadata.structures.TableType;
import com.stratio.meta.core.structures.IndexType;
import com.stratio.meta.streaming.StreamingUtils;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.commons.exceptions.StratioAPIGenericException;
import com.stratio.streaming.commons.exceptions.StratioEngineOperationException;
import com.stratio.streaming.commons.exceptions.StratioEngineStatusException;
import com.stratio.streaming.commons.messages.ColumnNameTypeValue;
import com.stratio.streaming.commons.messages.StreamQuery;
import com.stratio.streaming.commons.streams.StratioStream;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Metadata Manager of the META server that maintains and up-to-date version of the metadata
 * associated with the existing keyspaces and tables.
 */
public class MetadataManager {

  /**
   * Cluster metadata in Cassandra.
   */
  private Metadata clusterMetadata = null;

  /**
   * Cassandra session used to query the custom index information.
   */
  private final Session session;

  /**
   * Lucene index helper used to parse custom index options.
   */
  private final LuceneIndexHelper luceneHelper;

  /**
   * Link with the Stratio streaming API.
   */
  private final IStratioStreamingAPI stratioStreamingAPI;

  /**
   * Class logger.
   */
  private static final Logger logger = Logger.getLogger(MetadataManager.class.getName());

  /**
   * Class constructor.
   * 
   * @param cassandraSession The Cassandra session used to retrieve index metadata.
   */
  public MetadataManager(Session cassandraSession, IStratioStreamingAPI stratioStreamingAPI) {
    session = cassandraSession;
    luceneHelper = new LuceneIndexHelper(session);
    this.stratioStreamingAPI = stratioStreamingAPI;
  }

  /**
   * Load all Metadata from Cassandra.
   * 
   * @return Whether the metadata has been loaded or not.
   */
  public boolean loadMetadata() {
    clusterMetadata = session.getCluster().getMetadata();
    return clusterMetadata != null;
  }

  /**
   * Get the Metadata associated with a keyspace.
   * 
   * @param keyspace The target keyspace.
   * @return The KeyspaceMetadata or null if the keyspace is not found, or the client is not
   *         connected to Cassandra.
   */
  public KeyspaceMetadata getKeyspaceMetadata(String keyspace) {
    KeyspaceMetadata result = null;
    if (clusterMetadata != null && keyspace != null) {
      result = clusterMetadata.getKeyspace(keyspace);
      if (logger.isDebugEnabled()) {
        logger.debug("Cluster metadata: " + result);
      }
    }
    return result;
  }

  /**
   * Get the Metadata associated with a {@code tablename} of a {@code keyspace}.
   * 
   * @param keyspace The target keyspace.
   * @param tablename The target table.
   * @return The TableMetadata or null if the table does not exist in the keyspace, or the client is
   *         not connected to Cassandra.
   */
  public TableMetadata getTableMetadata(String keyspace, String tablename) {
    TableMetadata result = null;
    if (clusterMetadata != null && clusterMetadata.getKeyspace(keyspace) != null) {
      boolean found = false;
      // Iterate through the tables matching the name. We cannot use the getTable
      // method as it changes the names using the Metadata.handleId
      Iterator<TableMetadata> tables = clusterMetadata.getKeyspace(keyspace).getTables().iterator();
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
  }

  /**
   * Get the Metadata associated with a {@code tablename} of a {@code keyspace}.
   * 
   * @param keyspace The target keyspace.
   * @param tablename The target table.
   * @return The TableMetadata or null if the table does not exist in the keyspace, or the client is
   *         not connected to Cassandra.
   */
  public com.stratio.meta.common.metadata.structures.TableMetadata getTableGenericMetadata(
      String keyspace, String tablename) {

    com.stratio.meta.common.metadata.structures.TableMetadata result = null;

    boolean found = false;

    // Looking up the table in the DB
    if (clusterMetadata != null && clusterMetadata.getKeyspace(keyspace) != null
        && tablename != null) {
      // TODO Make it generic
      AbstractMetadataHelper helper = new CassandraMetadataHelper();
      KeyspaceMetadata keyspaceMetadata = getKeyspaceMetadata(keyspace);

      List<com.stratio.meta.common.metadata.structures.TableMetadata> tableList = new ArrayList<>();
      // Adding db tables
      tableList.addAll(helper.toCatalogMetadata(keyspaceMetadata).getTables());
      // Adding ephemeral tables
      tableList.addAll(this.getEphemeralTables(keyspace));

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

    // Looking up the table among the Streams
    if(!found){
      return getStreamingMetadata(keyspace, tablename);
    }

    return result;
  }

  /**
   * Get the comment associated with a {@code tablename} of a {@code keyspace}.
   * 
   * @param keyspace The target keyspace.
   * @param tablename The target table.
   * @return The comment or null if the table does not exist in the keyspace, or the client is not
   *         connected to Cassandra.
   */
  public String getTableComment(String keyspace, String tablename) {
    String result = null;
    if (clusterMetadata != null && clusterMetadata.getKeyspace(keyspace) != null) {
      result = clusterMetadata.getKeyspace(keyspace).getTable(tablename).getOptions().getComment();
    }
    return result;
  }

  /**
   * Get the list of keyspaces in Cassandra.
   * 
   * @return The list of keyspaces or empty if not connected.
   */
  public List<String> getKeyspacesNames() {
    List<String> result = new ArrayList<>();
    if (clusterMetadata != null) {
      for (KeyspaceMetadata list : clusterMetadata.getKeyspaces()) {
        result.add(list.getName());
      }
    }
    return result;
  }

  /**
   * Get the list of tables in a Cassandra keyspaces.
   * 
   * @param catalog The name of the keyspace
   * @return The list of tables or empty if the keyspace does not exist, or the not connected.
   */
  public List<String> getTablesNames(String catalog) {
    List<String> result = new ArrayList<>();
    // Retrieve database tables.
    if (clusterMetadata != null && clusterMetadata.getKeyspace(catalog) != null) {
      KeyspaceMetadata km = clusterMetadata.getKeyspace(catalog);
      for (TableMetadata tm : km.getTables()) {
        result.add(tm.getName());
      }
    }

    return result;
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
    if (stratioStreamingAPI != null) {
      List<StratioStream> ephemeralTables = getEphemeralTables();
      for (StratioStream stream : ephemeralTables) {
        if (stream.getStreamName().startsWith(catalog + "_")) {
          result.add(toTableMetadata(stream));
        }
      }
    }
    return result;
  }

  /**
   * Transform a Stratio Streaming {@link com.stratio.streaming.commons.streams.StratioStream} into
   * a META table metadata structure.
   * 
   * @param stream The stream to be transformed.
   * @return A {@link com.stratio.meta.common.metadata.structures.TableMetadata} or null in case of
   *         error.
   */
  public com.stratio.meta.common.metadata.structures.TableMetadata toTableMetadata(
      StratioStream stream) {
    boolean error = false;

    com.stratio.meta.common.metadata.structures.TableMetadata result = null;
    String[] streamName = stream.getStreamName().split("_");
    String tableName = streamName[1];
    String parentCatalog = streamName[0];
    Set<com.stratio.meta.common.metadata.structures.ColumnMetadata> columns = new LinkedHashSet<>();

    try {
      for (ColumnNameTypeValue col : stratioStreamingAPI.columnsFromStream(stream.getStreamName())) {
        ColumnType metaType = convertStreamingToMeta(col.getType());
        com.stratio.meta.common.metadata.structures.ColumnMetadata metaCol =
            new com.stratio.meta.common.metadata.structures.ColumnMetadata(tableName,
                col.getColumn(), metaType);
        columns.add(metaCol);
      }
    } catch (StratioEngineOperationException e) {
      logger.error("Cannot retrieve streaming columns for " + stream.getStreamName(), e);
      error = true;
    }

    if (!error) {
      result =
          new com.stratio.meta.common.metadata.structures.TableMetadata(tableName, parentCatalog,
              TableType.EPHEMERAL, columns);
    }

    return result;
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
    List<CustomIndexMetadata> result = new ArrayList<>();

    // Iterate through the table columns.
    for (ColumnMetadata column : tableMetadata.getColumns()) {
      if (column.getIndex() != null) {
        if (logger.isTraceEnabled()) {
          logger.trace("Index found in column " + column.getName());
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
          logger.error("Index " + column.getIndex().getName() + " on " + column.getName()
              + " with class " + column.getIndex().getIndexClassName() + " not supported.");
        }
        result.add(toAdd);
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Table " + tableMetadata.getName() + " has " + result.size()
          + " indexed columns.");
    }
    return result;
  }

  public boolean checkStream(String ephemeralTableName) {
    if (stratioStreamingAPI != null) {
      List<StratioStream> streams = getEphemeralTables();
      if (streams != null && streams.size() > 0) {
        for (StratioStream stream : streams) {
          if (stream.getStreamName().equalsIgnoreCase(ephemeralTableName)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Get the list of ephemeral tables using the streaming API.
   * 
   * @return The list of tables or null in case of error.
   */
  public List<StratioStream> getEphemeralTables() {

    List<StratioStream> streamsList = null;
    try {
      streamsList = stratioStreamingAPI.listStreams();
    } catch (Exception e) {
      logger.error("Cannot retrieve stream list", e);
    }

    return streamsList;
  }

  public List<String> getStreamingColumnNames(String ephemeralTableName) {

    logger.debug("Looking up columns from ephemeral table " + ephemeralTableName);
    List<String> colNames = new ArrayList<>();

    List<ColumnNameTypeValue> cols;
    try {
      cols = stratioStreamingAPI.columnsFromStream(ephemeralTableName);
      for (ColumnNameTypeValue ctp : cols) {
        colNames.add(ctp.getColumn().toLowerCase());
      }
    } catch (StratioEngineOperationException e) {
      logger.error("Error retrieving data from stream", e);
    }

    return colNames;
  }

  public StratioStream checkQuery(String s) {

    StratioStream result = null;

    List<StratioStream> streamsList;
    try {
      streamsList = stratioStreamingAPI.listStreams();
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
      logger.error("Error retrieving data from stream", e);
    }

    return result;
  }

  public ColumnNameTypeValue findStreamingColumn(String ephemeralTable, String column) {
    try {
      List<ColumnNameTypeValue> cols = stratioStreamingAPI.columnsFromStream(ephemeralTable);
      for (ColumnNameTypeValue col : cols) {
        if (col.getColumn().equalsIgnoreCase(column)) {
          return col;
        }
      }
    } catch (StratioEngineOperationException e) {
      logger.error("Error retrieving data from stream", e);
    }
    return null;
  }

  public List<ColumnNameTypeValue> getStreamingColumns(String ephemeralTable) {
    try {
      return stratioStreamingAPI.columnsFromStream(ephemeralTable);
    } catch (StratioEngineOperationException e) {
      logger.error("Error retrieving data from stream", e);
    }
    return null;
  }

  public com.stratio.meta.common.metadata.structures.TableMetadata getStreamingMetadata(
      String catalog, String tablename) {
    Set<com.stratio.meta.common.metadata.structures.ColumnMetadata> columns = new LinkedHashSet<>();
    try {
      List<ColumnNameTypeValue>
          colsFromStream =
          stratioStreamingAPI.columnsFromStream(catalog + "_" + tablename);
      if(colsFromStream == null){
        return null;
      }
      for (ColumnNameTypeValue col: colsFromStream) {
        ColumnType metaType = convertStreamingToMeta(col.getType());
        com.stratio.meta.common.metadata.structures.ColumnMetadata metaCol =
            new com.stratio.meta.common.metadata.structures.ColumnMetadata(tablename,
                col.getColumn(), metaType);
        columns.add(metaCol);
      }
    } catch (StratioEngineOperationException e) {
      logger.error("Cannot convert Streaming metadata to meta", e);
      return null;
    }
    com.stratio.meta.common.metadata.structures.TableMetadata tableMetadata =
        new com.stratio.meta.common.metadata.structures.TableMetadata(tablename, catalog,
            TableType.EPHEMERAL, columns);
    return tableMetadata;
  }

  private ColumnType convertStreamingToMeta(com.stratio.streaming.commons.constants.ColumnType type) {
    return StreamingUtils.streamingToMetaType(type.getValue());
  }

}
