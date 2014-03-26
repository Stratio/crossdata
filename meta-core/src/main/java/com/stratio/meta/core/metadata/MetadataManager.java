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

import com.datastax.driver.core.*;


import com.stratio.meta.core.structures.IndexType;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Metadata Manager of the META server that maintains and up-to-date version of
 * the metadata associated with the existing keyspaces and tables.
 */
public class MetadataManager {
		
	/**
	 * Cluster metadata in Cassandra.
	 */
	private Metadata _clusterMetadata = null;

    /**
     * Cassandra session used to query the custom index information.
     */
    private final Session _session;

    /**
     * Lucene index helper used to parse custom index options.
     */
    private final LuceneIndexHelper _luceneHelper;

	/**
	 * Class logger.
	 */
	private static final Logger _logger = Logger.getLogger(MetadataManager.class.getName());
	
	/**
	 * Class constructor.
     * @param cassandraSession The Cassandra session used to retrieve index metadata.
	 */
	public MetadataManager(Session cassandraSession){
        _session = cassandraSession;
        _luceneHelper = new LuceneIndexHelper(_session);
	}

	/**
	 * Load all Metadata from Cassandra.
	 * @return Whether the metadata has been loaded or not.
	 */
	public boolean loadMetadata(){
		_clusterMetadata = _session.getCluster().getMetadata();
		return _clusterMetadata != null;
	}
	
	/**
	 * Get the Metadata associated with a keyspace.
	 * @param keyspace The target keyspace.
	 * @return The KeyspaceMetadata or null if the keyspace is not found,
	 * or the client is not connected to Cassandra.
	 */
	public KeyspaceMetadata getKeyspaceMetadata(String keyspace){
		KeyspaceMetadata result = null;
		if(_clusterMetadata != null){
			result = _clusterMetadata.getKeyspace(keyspace);
			if (_logger.isDebugEnabled()) {
				_logger.debug("Cluster metadata: " + result);
			}
		}
		return result;
	}
	
	/**
	 * Get the Metadata associated with a {@code tablename} of a {@code keyspace}.
	 * @param keyspace The target keyspace.
	 * @param tablename The target table.
	 * @return The TableMetadata or null if the table does not exist in the keyspace,
	 * or the client is not connected to Cassandra.
	 */
	public TableMetadata getTableMetadata(String keyspace, String tablename){
		TableMetadata result = null;
		if(_clusterMetadata != null && _clusterMetadata.getKeyspace(keyspace) != null){
			result = _clusterMetadata.getKeyspace(keyspace).getTable(tablename);
		}
		return result;
	}

    /**
     * Get the comment associated with a {@code tablename} of a {@code keyspace}.
     * @param keyspace The target keyspace.
     * @param tablename The target table.
     * @return The comment or null if the table does not exist in the keyspace,
     * or the client is not connected to Cassandra.
     */
    public String getTableComment(String keyspace, String tablename){
        String result = null;
        if(_clusterMetadata != null && _clusterMetadata.getKeyspace(keyspace) != null){
            result = _clusterMetadata.getKeyspace(keyspace).getTable(tablename).getOptions().getComment();
        }
        return result;
    }

    /**
     * Get the list of keyspaces in Cassandra.
     * @return The list of keyspaces or empty if not connected.
     */
    public List<String> getKeyspacesNames(){
        List<String> result = new ArrayList<>();
        if(_clusterMetadata != null){
            for(KeyspaceMetadata list : _clusterMetadata.getKeyspaces()){
                result.add(list.getName());
            }
        }
        return result;
    }

    /**
     * Get the list of tables in a Cassandra keyspaces.
     * @param keyspace The name of the keyspace
     * @return The list of tables or empty if the keyspace does
     * not exists, or the not connected.
     */
    public List<String> getTablesNames(String keyspace){
        List<String> result = new ArrayList<>();
        if(_clusterMetadata != null && _clusterMetadata.getKeyspace(keyspace) != null){
            KeyspaceMetadata km = _clusterMetadata.getKeyspace(keyspace);
            for(TableMetadata tm : km.getTables()){
                result.add(tm.getName());
            }
        }
        return result;
    }

    /**
     * Return the list of indexes available for each column in a specific table.
     * @param tableMetadata Metadata associated with the target table.
     * @return The list of available indexes.
     */
    public Map<String, List<CustomIndexMetadata>> getColumnIndexes(TableMetadata tableMetadata){
        Map<String, List<CustomIndexMetadata>> result = new HashMap<>();
        for(ColumnMetadata column : tableMetadata.getColumns()){
            if(column.getIndex() != null){
                if(_logger.isTraceEnabled()){
                    _logger.trace("Index found in column " + column.getName());
                }

                CustomIndexMetadata toAdd = null;
                if(!column.getIndex().isCustomIndex()){
                    //A Cassandra index is associated with the column.
                    List<CustomIndexMetadata> indexes = result.get(column.getName());
                    if(indexes == null){
                        indexes = new ArrayList<>();
                        result.put(column.getName(), indexes);
                    }
                    indexes.add(new CustomIndexMetadata(column, IndexType.DEFAULT));

                }else if (column.getIndex().isCustomIndex()
                        && column.getIndex().getIndexClassName().compareTo("org.apache.cassandra.db.index.stratio.RowIndex") == 0){
                    //A Lucene custom index is found that may index several columns.
                    Map<String, List<CustomIndexMetadata>> indexedColumns = _luceneHelper.getIndexedColumns(column);
                    for(String indexedColumn : indexedColumns.keySet()){
                        List<CustomIndexMetadata> existingIndexes = result.get(indexedColumn);
                        if(existingIndexes == null){
                            existingIndexes = new ArrayList<>();
                            result.put(indexedColumn, existingIndexes);
                        }
                        existingIndexes.addAll(indexedColumns.get(indexedColumn));
                    }
                }else{
                    _logger.error("Index " + column.getIndex().getName()
                            + " on " + column.getName()
                            + " with class " + column.getIndex().getIndexClassName() + " not supported.");
                }
            }
        }

        if(_logger.isDebugEnabled()){
            _logger.debug("Table " + tableMetadata.getName() + " has " + result.size() + " indexed columns.");
        }
        return result;
    }

}
