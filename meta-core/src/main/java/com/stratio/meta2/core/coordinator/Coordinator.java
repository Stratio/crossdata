package com.stratio.meta2.core.coordinator;

import java.util.Map;

import org.apache.log4j.Logger;

import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.IndexName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterAttachedMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
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
import com.stratio.meta2.core.query.StorageInProgressQuery;
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
            return new MetadataInProgressQuery((MetadataPlannedQuery) plannedQuery);
        case CREATE_INDEX:
            return new MetadataInProgressQuery((MetadataPlannedQuery) plannedQuery);
        case CREATE_TABLE:
            return new MetadataInProgressQuery((MetadataPlannedQuery) plannedQuery);
        case DESCRIBE:
            break;
        case DETACH_CLUSTER:
            persist(plannedQuery);
            break;
        case DETACH_CONNECTOR:
            persist(plannedQuery);
            break;
        case DROP_CATALOG:
            return new MetadataInProgressQuery((MetadataPlannedQuery) plannedQuery);
        case DROP_INDEX:
            return new MetadataInProgressQuery((MetadataPlannedQuery) plannedQuery);
        case DROP_TABLE:
            return new MetadataInProgressQuery((MetadataPlannedQuery) plannedQuery);
            // SELECT
        case SELECT:
//            return coordinateSelect((SelectPlannedQuery) plannedQuery);
            // STORAGE
        case INSERT_INTO:
            return new StorageInProgressQuery((StoragePlannedQuery) plannedQuery);
        case DELETE:
            return new StorageInProgressQuery((StoragePlannedQuery) plannedQuery);

        default:
            LOG.info("not known statement detected");
        }
        return null;
    }

    public void persist(PlannedQuery plannedQuery) {

        switch (getStatement(plannedQuery)) {
        // METADATA
        case ATTACH_CLUSTER:
            // persistAttachCluster((AttachClusterStatement) plannedQuery.getStatement());
            break;
        case ATTACH_CONNECTOR:
            // persistAttachConnector((AttachConnectorStatement) plannedQuery.getStatement());
            break;
        case CREATE_CATALOG:
            // persistCreateCatalog((CreateCatalogStatement) plannedQuery.getStatement());
            break;
        case CREATE_INDEX:
            // persistCreateIndex((CreateIndexStatement) plannedQuery.getStatement());
            break;
        case CREATE_TABLE:
            // persistCreateTable((CreateTableStatement) plannedQuery.getStatement());
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
        // SELECT
        case SELECT:
            LOG.info("select statement");
            break;
        // STORAGE
        case INSERT_INTO:
            LOG.info("insert into statement");
            break;
        case DELETE:
            LOG.info("delete statement");
            break;
        default:
            LOG.info("not known statement detected");
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

    public void persistAttachCluster(ClusterName clusterName, DataStoreName datastoreName,
            Map<Selector, Selector> options) {
        DataStoreMetadata datastoreMetadata =
                MetadataManager.MANAGER.getDataStore(datastoreName);

        Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs =
                datastoreMetadata.getClusterAttachedRefs();

        ClusterAttachedMetadata value =
                new ClusterAttachedMetadata(clusterName, datastoreName, options);

        clusterAttachedRefs.put(clusterName, value);
        datastoreMetadata.setClusterAttachedRefs(clusterAttachedRefs);

        MetadataManager.MANAGER.createDataStore(datastoreMetadata, false);
    }

    public void persistCreateCatalog(CatalogMetadata catalog) {
        MetadataManager.MANAGER.createCatalog(catalog);
    }

    public void persistCreateTable(TableMetadata table) {
        MetadataManager.MANAGER.createTable(table);
    }

    public void persistCreateIndex(IndexMetadata index) {
        // TODO move to MetadataManager
        TableMetadata table = MetadataManager.MANAGER.getTable(index.getName().getTableName());
        table.addIndex(index.getName(), index);
        MetadataManager.MANAGER.createTable(table, false);
    }

    public void persistDropCatalog(CatalogName catalog) {
        MetadataManager.MANAGER.deleteCatalog(catalog);
    }

    public void persistDropTable(TableName table) {
        MetadataManager.MANAGER.deleteTable(table);
    }

    public void persistDropIndex(IndexName index) {
        // TODO move to MetadataManager
        TableMetadata table = MetadataManager.MANAGER.getTable(index.getTableName());
        table.deleteIndex(index);
        MetadataManager.MANAGER.createTable(table, false);
    }

    public void persistAttachConnector(ClusterName clusterName, ConnectorName connectorName,
            Map<Selector, Selector> options) {
        ClusterMetadata clusterMetadata =
                MetadataManager.MANAGER.getCluster(clusterName);

        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs =
                clusterMetadata.getConnectorAttachedRefs();

        ConnectorAttachedMetadata value =
                new ConnectorAttachedMetadata(connectorName, clusterName, options);
        connectorAttachedRefs.put(connectorName, value);
        clusterMetadata.setConnectorAttachedRefs(connectorAttachedRefs);

        MetadataManager.MANAGER.createCluster(clusterMetadata, false);
    }

    enum StatementEnum {
        ATTACH_CLUSTER, ATTACH_CONNECTOR, CREATE_CATALOG, CREATE_INDEX, CREATE_TABLE, DESCRIBE, DETACH_CLUSTER, DETACH_CONNECTOR, DROP_CATALOG, DROP_INDEX, DROP_TABLE, SELECT, INSERT_INTO, DELETE
    }
}
