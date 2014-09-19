package com.stratio.meta2.core.coordinator;

import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.metadata.ClusterAttachedMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.ConnectorAttachedMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.InProgressQuery;
import com.stratio.meta2.core.query.MetadataInProgressQuery;
import com.stratio.meta2.core.query.MetadataPlannedQuery;
import com.stratio.meta2.core.query.PlannedQuery;
import com.stratio.meta2.core.query.SelectPlannedQuery;
import com.stratio.meta2.core.query.StoragePlannedQuery;
import com.stratio.meta2.core.statements.AttachClusterStatement;
import com.stratio.meta2.core.statements.AttachConnectorStatement;

import org.apache.log4j.Logger;

import java.util.Map;

public class Coordinator {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Coordinator.class);
  
  public InProgressQuery coordinate(PlannedQuery plannedQuery) {
    InProgressQuery inProgressQuery = null;
    if(plannedQuery instanceof MetadataPlannedQuery){
      MetadataPlannedQuery metadataPlannedQuery = (MetadataPlannedQuery) plannedQuery;
      inProgressQuery = coordinateMetadata(metadataPlannedQuery);
    } else if(plannedQuery instanceof StoragePlannedQuery){
      StoragePlannedQuery storagePlannedQuery = (StoragePlannedQuery) plannedQuery;
      inProgressQuery = coordinateStorage(storagePlannedQuery);
    } else {
      SelectPlannedQuery selectPlannedQuery = (SelectPlannedQuery) plannedQuery;
      inProgressQuery = coordinateSelect(selectPlannedQuery);
    }
    return inProgressQuery;
  }

  public InProgressQuery coordinateMetadata(MetadataPlannedQuery metadataPlannedQuery){
    InProgressQuery inProgressQuery = null;
    if(metadataPlannedQuery.getStatement() instanceof AttachClusterStatement){
      AttachClusterStatement attachClusterStatement =
          (AttachClusterStatement) metadataPlannedQuery.getStatement();
      attachCluster(attachClusterStatement);
      inProgressQuery = new MetadataInProgressQuery(metadataPlannedQuery);
    } else if (metadataPlannedQuery.getStatement() instanceof AttachConnectorStatement){
      AttachConnectorStatement attachConnectorStatement =
          (AttachConnectorStatement) metadataPlannedQuery.getStatement();
      attachConnector(attachConnectorStatement);
      inProgressQuery = new MetadataInProgressQuery(metadataPlannedQuery);
    }
    return inProgressQuery;
  }

  private InProgressQuery coordinateStorage(StoragePlannedQuery storagePlannedQuery) {
    InProgressQuery inProgressQuery = null;
    return inProgressQuery;
  }

  private InProgressQuery coordinateSelect(SelectPlannedQuery selectPlannedQuery) {
    InProgressQuery inProgressQuery = null;
    return inProgressQuery;
  }

  private void attachCluster(AttachClusterStatement attachClusterStatement){
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
    Map<Selector, Selector> properties =  attachClusterStatement.getOptions();
    ClusterAttachedMetadata value = new ClusterAttachedMetadata(clusterRef, dataStoreRef, properties);
    clusterAttachedRefs.put(key, value);
    datastoreMetadata.setClusterAttachedRefs(clusterAttachedRefs);

    MetadataManager.MANAGER.createDataStore(datastoreMetadata, false);
  }

  private void attachConnector(AttachConnectorStatement attachConnectorStatement) {
    ClusterMetadata
        clusterMetadata =
        MetadataManager.MANAGER
            .getCluster(new ClusterName(attachConnectorStatement.getClusterName()));

    Map<ConnectorName, ConnectorAttachedMetadata>
        connectorAttachedRefs =
        clusterMetadata.getConnectorAttachedRefs();

    ConnectorName key = new ConnectorName(attachConnectorStatement.getConnectorName());
    ConnectorName connectorRef = new ConnectorName(attachConnectorStatement.getConnectorName());
    ClusterName clusterRef = new ClusterName(attachConnectorStatement.getClusterName());
    Map<Selector, Selector> properties =  attachConnectorStatement.getOptions();
    ConnectorAttachedMetadata value = new ConnectorAttachedMetadata(connectorRef, clusterRef, properties);
    connectorAttachedRefs.put(key, value);
    clusterMetadata.setConnectorAttachedRefs(connectorAttachedRefs);

    MetadataManager.MANAGER.createCluster(clusterMetadata, false);
  }

}
