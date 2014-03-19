package com.stratio.meta.server.metadata;

import org.apache.log4j.Logger;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.driver.MetaDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Metadata Manager of the META server that maintains and up-to-date version of
 * the metadata associated with the existing keyspaces and tables.
 */
public class MetadataManager {
		
	/**
	 * Cluster metadata in Cassandra.
	 */
	private Metadata _clusterMetadata = null;
        private final MetaDriver metaDriver = new MetaDriver();
	
	/**
	 * Class logger.
	 */
	private static final Logger _logger = Logger.getLogger(MetadataManager.class.getName());
	
	/**
	 * Class constructor.
	 */
	public MetadataManager(){
	}
	
	/**
	 * Load all Metadata from Cassandra.
	 * @return Whether the metadata has been loaded or not.
	 */
	public boolean loadMetadata(){
		_clusterMetadata = metaDriver.getClusterMetadata();
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

}
