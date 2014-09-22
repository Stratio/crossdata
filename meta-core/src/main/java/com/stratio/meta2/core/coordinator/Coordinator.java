package com.stratio.meta2.core.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterAttachedMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ConnectorAttachedMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.IndexMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.InProgressQuery;
import com.stratio.meta2.core.query.MetadataInProgressQuery;
import com.stratio.meta2.core.query.MetadataPlannedQuery;
import com.stratio.meta2.core.query.PlannedQuery;
import com.stratio.meta2.core.query.SelectInProgressQuery;
import com.stratio.meta2.core.query.SelectPlannedQuery;
import com.stratio.meta2.core.query.StoragePlannedQuery;
import com.stratio.meta2.core.statements.AttachClusterStatement;
import com.stratio.meta2.core.statements.AttachConnectorStatement;
import com.stratio.meta2.core.statements.CreateCatalogStatement;
import com.stratio.meta2.core.statements.CreateIndexStatement;
import com.stratio.meta2.core.statements.CreateTableStatement;
import com.stratio.meta2.core.statements.DeleteStatement;
import com.stratio.meta2.core.statements.DropCatalogStatement;
import com.stratio.meta2.core.statements.DropIndexStatement;
import com.stratio.meta2.core.statements.DropTableStatement;
import com.stratio.meta2.core.statements.InsertIntoStatement;
import com.stratio.meta2.core.statements.MetaStatement;

public class Coordinator {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Coordinator.class);


  enum StatementEnum {
    ATTACH_CLUSTER, ATTACH_CONNECTOR, CREATE_CATALOG, CREATE_INDEX, CREATE_TABLE, DESCRIBE, DETACH_CLUSTER, DETACH_CONNECTOR, DROP_CATALOG, DROP_INDEX, DROP_TABLE, SELECT, INSERT_INTO, DELETE
  }


  public InProgressQuery coordinate(PlannedQuery plannedQuery) {
    switch (getStatement(plannedQuery)) {
    // METADATA
      case ATTACH_CLUSTER:
        persist(plannedQuery);
        break;
      case ATTACH_CONNECTOR:
        persist(plannedQuery);
        break;
      case CREATE_CATALOG:
        return new MetadataInProgressQuery(plannedQuery);
      case CREATE_INDEX:
        return new MetadataInProgressQuery(plannedQuery);
      case CREATE_TABLE:
        return new MetadataInProgressQuery(plannedQuery);
      case DESCRIBE:
        break;
      case DETACH_CLUSTER:
        persist(plannedQuery);
        break;
      case DETACH_CONNECTOR:
        persist(plannedQuery);
        break;
      case DROP_CATALOG:
        return new MetadataInProgressQuery(plannedQuery);
      case DROP_INDEX:
        return new MetadataInProgressQuery(plannedQuery);
      case DROP_TABLE:
        return new MetadataInProgressQuery(plannedQuery);
        // SELECT
      case SELECT:
        return coordinateSelect((SelectPlannedQuery) plannedQuery);
        // STORAGE
      case INSERT_INTO:
        break;
      case DELETE:
        break;


      default:
        break;
    }

    return null;

  }

  public void persist(PlannedQuery plannedQuery) {

    switch (getStatement(plannedQuery)) {
    // METADATA
      case ATTACH_CLUSTER:
        persistAttachCluster((AttachClusterStatement) plannedQuery.getStatement());
        break;
      case ATTACH_CONNECTOR:
        persistAttachConnector((AttachConnectorStatement) plannedQuery.getStatement());
        break;
      case CREATE_CATALOG:
        persistCreateCatalog((CreateCatalogStatement) plannedQuery.getStatement());
        break;
      case CREATE_INDEX:
        persistCreateIndex((CreateIndexStatement) plannedQuery.getStatement());
        break;
      case CREATE_TABLE:
        persistCreateTable((CreateTableStatement) plannedQuery.getStatement());
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
      // STORAGE
      case INSERT_INTO:
        break;
      case DELETE:
        break;

      default:
        break;
    }

  }


  private StatementEnum getStatement(PlannedQuery plannedQuery) {

    // METADATA
    if (plannedQuery instanceof MetadataPlannedQuery) {
      MetaStatement statement = ((MetadataPlannedQuery) plannedQuery).getStatement();

      if (statement instanceof AttachClusterStatement) {
        return StatementEnum.ATTACH_CLUSTER;
      }
      if (statement instanceof AttachConnectorStatement) {
        return StatementEnum.ATTACH_CONNECTOR;
      }
      if (statement instanceof CreateCatalogStatement) {
        return StatementEnum.CREATE_CATALOG;
      }
      if (statement instanceof CreateIndexStatement) {
        return StatementEnum.CREATE_INDEX;
      }
      if (statement instanceof CreateTableStatement) {
        return StatementEnum.CREATE_TABLE;
      }
      if (statement instanceof DropCatalogStatement) {
        return StatementEnum.DROP_CATALOG;
      }
      if (statement instanceof DropIndexStatement) {
        return StatementEnum.DROP_INDEX;
      }
      if (statement instanceof DropTableStatement) {
        return StatementEnum.DROP_TABLE;
      }

    }

    // SELECT
    if (plannedQuery instanceof SelectPlannedQuery) {
      return StatementEnum.SELECT;
    }

    // STORAGE
    if (plannedQuery instanceof StoragePlannedQuery) {
      MetaStatement statement = ((StoragePlannedQuery) plannedQuery).getStatement();
      if (statement instanceof InsertIntoStatement) {
        return StatementEnum.INSERT_INTO;
      }
      if (statement instanceof DeleteStatement) {
        return StatementEnum.DELETE;
      }
    }
    return null;
  }

  private SelectInProgressQuery coordinateSelect(SelectPlannedQuery selectPlannedQuery) {
    SelectInProgressQuery inProgressQuery = new SelectInProgressQuery(selectPlannedQuery);
    return inProgressQuery;
  }

  private InProgressQuery coordinateStorage(StoragePlannedQuery storagePlannedQuery) {
    InProgressQuery inProgressQuery = null;
    return inProgressQuery;
  }

  private void persistAttachCluster(AttachClusterStatement attachClusterStatement) {
    DataStoreMetadata datastoreMetadata =
        MetadataManager.MANAGER.getDataStore(new DataStoreName(attachClusterStatement
            .getDatastoreName()));

    Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs =
        datastoreMetadata.getClusterAttachedRefs();

    ClusterName key = new ClusterName(attachClusterStatement.getClusterName());
    ClusterName clusterRef = new ClusterName(attachClusterStatement.getClusterName());
    DataStoreName dataStoreRef = new DataStoreName(attachClusterStatement.getDatastoreName());
    Map<Selector, Selector> properties = attachClusterStatement.getOptions();

    ClusterAttachedMetadata value =
        new ClusterAttachedMetadata(clusterRef, dataStoreRef, properties);

    clusterAttachedRefs.put(key, value);
    datastoreMetadata.setClusterAttachedRefs(clusterAttachedRefs);

    MetadataManager.MANAGER.createDataStore(datastoreMetadata, false);
  }

  private void persistCreateCatalog(CreateCatalogStatement createCatalogStatement) {
    MetadataManager.MANAGER.createCatalog(new CatalogMetadata(createCatalogStatement
        .getCatalogName(), createCatalogStatement.getOptions(), null));
  }

  private void persistCreateTable(CreateTableStatement createTableStatement) {
    MetadataManager.MANAGER.createTable(createTableStatement.getTableMetadata());
  }

  private void persistCreateIndex(CreateIndexStatement createIndexStatement) {
    TableMetadata table = MetadataManager.MANAGER.getTable(createIndexStatement.getTableName());

    List<ColumnName> targetColumnsNames = createIndexStatement.getTargetColumns();
    List<ColumnMetadata> targetColumnsMetadata = new ArrayList<>();
    for (ColumnName c : targetColumnsNames) {
      targetColumnsMetadata.add(table.getColumns().get(c));
    }

    IndexMetadata index =
        new IndexMetadata(createIndexStatement.getName(), targetColumnsMetadata,
            createIndexStatement.getType(), createIndexStatement.getOptions());
    table.addIndex(createIndexStatement.getName(), index);
    
    MetadataManager.MANAGER.createTable(table, false);

  }

  private void persistAttachConnector(AttachConnectorStatement attachConnectorStatement) {
    ClusterMetadata clusterMetadata =
        MetadataManager.MANAGER.getCluster(new ClusterName(attachConnectorStatement
            .getClusterName()));

    Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs =
        clusterMetadata.getConnectorAttachedRefs();

    ConnectorName key = new ConnectorName(attachConnectorStatement.getConnectorName());
    ConnectorName connectorRef = new ConnectorName(attachConnectorStatement.getConnectorName());
    ClusterName clusterRef = new ClusterName(attachConnectorStatement.getClusterName());
    Map<Selector, Selector> properties = attachConnectorStatement.getOptions();
    ConnectorAttachedMetadata value =
        new ConnectorAttachedMetadata(connectorRef, clusterRef, properties);
    connectorAttachedRefs.put(key, value);
    clusterMetadata.setConnectorAttachedRefs(connectorAttachedRefs);

    MetadataManager.MANAGER.createCluster(clusterMetadata, false);
  }
}
