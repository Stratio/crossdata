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
import com.stratio.meta2.core.statements.CreateCatalogStatement;
import com.stratio.meta2.core.statements.CreateIndexStatement;
import com.stratio.meta2.core.statements.CreateTableStatement;
import com.stratio.meta2.core.statements.DropCatalogStatement;
import com.stratio.meta2.core.statements.DropIndexStatement;
import com.stratio.meta2.core.statements.DropTableStatement;
import com.stratio.meta2.core.statements.MetaStatement;

import org.apache.log4j.Logger;

import java.util.Map;

public class Coordinator {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Coordinator.class);
  
  private StatementEnum queryStatement = null;

  enum StatementEnum {
    ATTACH_CLUSTER,
    ATTACH_CONNECTOR,
    CREATE_CATALOG,
    CREATE_INDEX,
    CREATE_TABLE,
    DESCRIBE,
    DETACH_CLUSTER,
    DETACH_CONNECTOR,
    DROP_CATALOG,
    DROP_INDEX,
    DROP_TABLE
  }


  public InProgressQuery coordinate(PlannedQuery plannedQuery) {
    switch (getStatement(plannedQuery)) {
      //METADATA
      case ATTACH_CLUSTER:
        attachCluster((AttachClusterStatement) plannedQuery.getStatement());
        break;
      case ATTACH_CONNECTOR:
        attachConnector((AttachConnectorStatement) plannedQuery.getStatement());
        break;
      case CREATE_CATALOG:
        break;
      case CREATE_INDEX:
        break;
      case CREATE_TABLE:
        break;
      case DESCRIBE:
        break;
      case DETACH_CLUSTER:
        break;
      case DETACH_CONNECTOR:
        break;
      case DROP_CATALOG:
        break;
      case DROP_INDEX:
        break;
      case DROP_TABLE:
        break;
      //OTHERS
      default:
        break;
    }
    
    return new MetadataInProgressQuery(plannedQuery);

  }

  private StatementEnum getStatement(PlannedQuery plannedQuery) {
    //METADATA
    if (plannedQuery instanceof MetadataPlannedQuery) {
      MetaStatement statement = ((MetadataPlannedQuery) plannedQuery).getStatement();

      if (statement instanceof AttachClusterStatement) {
        queryStatement = StatementEnum.ATTACH_CLUSTER; 
      }
      if (statement instanceof AttachConnectorStatement) {
        queryStatement = StatementEnum.ATTACH_CONNECTOR; 
      }
      if (statement instanceof CreateCatalogStatement) {
        queryStatement = StatementEnum.CREATE_CATALOG; 
      }
      if (statement instanceof CreateIndexStatement) {
        queryStatement = StatementEnum.CREATE_INDEX; 
      }
      if (statement instanceof CreateTableStatement) {
        queryStatement = StatementEnum.CREATE_TABLE; 
      }
      if (statement instanceof DropCatalogStatement) {
        queryStatement = StatementEnum.DROP_CATALOG; 
      }
      if (statement instanceof DropIndexStatement) {
        queryStatement = StatementEnum.DROP_INDEX; 
      }
      if (statement instanceof DropTableStatement) {
        queryStatement = StatementEnum.DROP_TABLE; 
      }

    }
    return queryStatement;
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

    Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs =
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
