package com.stratio.meta2.core.coordinator;

import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.metadata.ClusterAttachedMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.InProgressQuery;
import com.stratio.meta2.core.query.PlannedQuery;
import com.stratio.meta2.core.query.SelectInProgressQuery;
import com.stratio.meta2.core.statements.AttachClusterStatement;

import org.apache.log4j.Logger;

import java.util.Map;

public class Coordinator {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Coordinator.class);
  
  public InProgressQuery coordinate(PlannedQuery plannedQuery) {
    return new SelectInProgressQuery(null);
  }

  private void attachCluster(AttachClusterStatement attachClusterStatement){

    // TODO: Create DataStore

    DataStoreMetadata
        datastoreMetadata =
        MetadataManager.MANAGER
            .getDataStore(new DataStoreName(attachClusterStatement.getDatastoreName()));

    Map<ClusterName, ClusterAttachedMetadata>
        clusterAttachedRefs =
        datastoreMetadata.getClusterAttachedRefs();

    ClusterName key = new ClusterName(attachClusterStatement.getClusterName());
    ClusterName clusterRef = new ClusterName(attachClusterStatement.getClusterName());
    DataStoreName dataStoreRef = new DataStoreName(attachClusterStatement.getDatastoreName());
    Map<String, Object> properties =  attachClusterStatement.getOptions();
    ClusterAttachedMetadata value = new ClusterAttachedMetadata(clusterRef, dataStoreRef, properties);
    clusterAttachedRefs.put(key, value);
  }

}
