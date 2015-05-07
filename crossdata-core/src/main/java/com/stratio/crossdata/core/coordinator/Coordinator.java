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

package com.stratio.crossdata.core.coordinator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stratio.crossdata.communication.*;
import org.apache.log4j.Logger;

import com.stratio.crossdata.common.data.AlterOptions;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.executionplan.MetadataWorkflow;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterAttachedMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.core.metadata.MetadataManager;

/**
 * Coordinator class in charge of managing the operations required to execute a query. In particular, this class
 * defines a set of methods to persists data after the successful execution of a query.
 */
public class Coordinator implements Serializable {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Coordinator.class);

    /**
     * Persists workflow in Metadata Manager.
     *
     * @param metadataWorkflow The metadata workflow.
     * @param result The object to persist.
     */
    public void persist(MetadataWorkflow metadataWorkflow, MetadataResult result) {
        switch (metadataWorkflow.getExecutionType()) {
        case CREATE_CATALOG:
            persistCreateCatalog(metadataWorkflow.getCatalogMetadata(), metadataWorkflow.isIfNotExists());
            break;
        case CREATE_INDEX:
            persistCreateIndex(metadataWorkflow.getIndexMetadata());
            break;
        case CREATE_TABLE_AND_CATALOG:
        case REGISTER_TABLE_CREATE_CATALOG:
        case CREATE_TABLE_REGISTER_CATALOG:
            persistCreateCatalogInCluster(metadataWorkflow.getCatalogName(), metadataWorkflow.getClusterName());
            persistCreateTable(metadataWorkflow.getTableMetadata());
            break;
        case CREATE_TABLE:
            persistCreateTable(metadataWorkflow.getTableMetadata());
            break;
        case ALTER_CATALOG:
            persistAlterCatalog(metadataWorkflow.getCatalogMetadata());
            break;
        case ALTER_TABLE:
            persistAlterTable(metadataWorkflow.getTableName(), metadataWorkflow.getAlterOptions());
            break;
        case DROP_CATALOG:
            persistDropCatalog(metadataWorkflow.getCatalogName(), true);
            break;
        case DROP_INDEX:
            persistDropIndex(metadataWorkflow.getIndexMetadata().getName());
            break;
        case DROP_TABLE:
            persistDropTable(metadataWorkflow.getTableName());
            break;
        case IMPORT_CATALOGS:
            persistImportCatalogs(result.getCatalogMetadataList());
            persistCreateCatalogsInCluster(result.getCatalogMetadataList(), metadataWorkflow.getClusterName());
            break;
        case IMPORT_CATALOG:
            persistImportCatalogs(result.getCatalogMetadataList());
            persistCreateCatalogsInCluster(result.getCatalogMetadataList(), metadataWorkflow.getClusterName());
            break;
        case IMPORT_TABLE:
            persistTables(result.getTableList(), metadataWorkflow.getClusterName());
            break;
        default:
            LOG.info("unknown statement detected");
            break;
        }
    }

    /**
     * Executes operations that do not send anything to the underlying connectors.
     *
     * @param workflow The management workflow.
     * @return A {@link com.stratio.crossdata.common.result.Result}.
     */
    public Result executeManagementOperation(ManagementOperation workflow) throws ManifestException {
        Result result = Result.createExecutionErrorResult("Unrecognized management operation.");

        if (AttachCluster.class.isInstance(workflow)) {
            AttachCluster ac = AttachCluster.class.cast(workflow);
            result = persistAttachCluster(ac.targetCluster(), ac.datastoreName(), ac.options());
        } else if (DetachCluster.class.isInstance(workflow)) {
            DetachCluster dc = DetachCluster.class.cast(workflow);
            result = persistDetachCluster(dc.targetCluster());
        } else if (AttachConnector.class.isInstance(workflow)) {
            AttachConnector ac = AttachConnector.class.cast(workflow);
            result = persistAttachConnector(ac.targetCluster(), ac.connectorName(), ac.options(), ac.priority(), ac.pageSize());
        } else if (ForceDetachConnector.class.isInstance(workflow)) {
            result = CommandResult.createCommandResult("CONNECTOR detached successfully");
        } else if (DetachConnector.class.isInstance(workflow)) {
            DetachConnector dc = DetachConnector.class.cast(workflow);
            result = persistDetachConnector(dc.targetCluster(), dc.connectorName());
        } else if (AlterCluster.class.isInstance(workflow)) {
            AlterCluster ac = AlterCluster.class.cast(workflow);
            result = persistAlterCluster(ac.targetCluster(), ac.options());
        }

        result.setQueryId(workflow.queryId());

        return result;
    }

    /**
     * Persists cluster's characteristics in Metadata Manager.
     *
     * @param clusterName   The cluster name.
     * @param datastoreName The datastore name.
     * @param options       A set of cluster options.
     * @return A {@link com.stratio.crossdata.common.result.Result}.
     */
    public Result persistAttachCluster(ClusterName clusterName, DataStoreName datastoreName,
            Map<Selector, Selector> options) throws ManifestException {

        // Create and persist Cluster metadata
        ClusterMetadata clusterMetadata = new ClusterMetadata(clusterName, datastoreName, options,
                new HashMap<ConnectorName, ConnectorAttachedMetadata>());
        MetadataManager.MANAGER.createCluster(clusterMetadata, false);

        // Add new attachment to DataStore
        DataStoreMetadata datastoreMetadata =
                MetadataManager.MANAGER.getDataStore(datastoreName);

        Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs =
                datastoreMetadata.getClusterAttachedRefs();

        ClusterAttachedMetadata value =
                new ClusterAttachedMetadata(clusterName, datastoreName, options);

        clusterAttachedRefs.put(clusterName, value);
        datastoreMetadata.setClusterAttachedRefs(clusterAttachedRefs);

        MetadataManager.MANAGER.createDataStore(datastoreMetadata, false);
        return CommandResult.createCommandResult("Cluster attached successfully");
    }

    /**
     * Persists new options' cluster in Metadata manager.
     *
     * @param clusterName The cluster name.
     * @param options     A set of cluster options.
     * @return A {@link com.stratio.crossdata.common.result.Result}.
     */
    public Result persistAlterCluster(ClusterName clusterName,
            Map<Selector, Selector> options) {

        // Create and persist Cluster metadata
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);
        clusterMetadata.setOptions(options);
        MetadataManager.MANAGER.createCluster(clusterMetadata, false);

        return CommandResult.createCommandResult("New cluster's options modified successfully");
    }

    /**
     * Detaches cluster from Metadata Manager.
     *
     * @param clusterName The cluster name.
     * @return A {@link com.stratio.crossdata.common.result.Result}.
     */
    public Result persistDetachCluster(ClusterName clusterName) {
        //TODO: Move this type of operations to MetadataManager in order to use a single lock
        //find the datastore first, to which the cluster is connected
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);
        DataStoreName datastoreName = clusterMetadata.getDataStoreRef();
        DataStoreMetadata datastoreMetadata = MetadataManager.MANAGER.getDataStore(datastoreName);

        Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs = datastoreMetadata.getClusterAttachedRefs();

        clusterAttachedRefs.remove(clusterName);
        datastoreMetadata.setClusterAttachedRefs(clusterAttachedRefs);

        MetadataManager.MANAGER.createDataStore(datastoreMetadata, false);

        MetadataManager.MANAGER.deleteCluster(clusterName, false);

        return CommandResult.createCommandResult("CLUSTER detached successfully");
    }

    /**
     * Persists catalog data in Metadata Manager.
     *
     * @param catalog     The catalog metadata to be stored.
     * @param ifNotExists If Catalog doesn't exist.
     */
    public void persistCreateCatalog(CatalogMetadata catalog, boolean ifNotExists) {
        boolean persistOperation = true;
        if (ifNotExists && (MetadataManager.MANAGER.exists(catalog.getName()))) {
            persistOperation = false;
        }
        if (persistOperation) {
            MetadataManager.MANAGER.createCatalog(catalog);
        }
    }

    /**
     * Persist the Alter Catalog Statement into Metadata Manager.
     * @param catalog The catalog to persist.
     */
    public void persistAlterCatalog(CatalogMetadata catalog) {
        MetadataManager.MANAGER.createCatalog(catalog, false);
    }

    /**
     * Persist the Alter Table Statement into Metadata Manager.
     * @param tableName The table name to persist.
     * @param alterOptions The options of the alter table statement.
     */
    private void persistAlterTable(TableName tableName, AlterOptions alterOptions) {
        TableMetadata storedTable = MetadataManager.MANAGER.getTable(tableName);
        switch(alterOptions.getOption()){
        case ALTER_COLUMN:
            ColumnName columnName = alterOptions.getColumnMetadata().getName();
            ColumnMetadata column = storedTable.getColumns().get(columnName);
            column.setColumnType(alterOptions.getColumnMetadata().getColumnType());
            storedTable.getColumns().put(columnName, column);
            break;
        case ADD_COLUMN:
            columnName = alterOptions.getColumnMetadata().getName();
            storedTable.getColumns().put(columnName, alterOptions.getColumnMetadata());
            break;
        case DROP_COLUMN:
            columnName = alterOptions.getColumnMetadata().getName();
            storedTable.getColumns().remove(columnName);
            break;
        case ALTER_OPTIONS:
            storedTable.setOptions(alterOptions.getProperties());
            break;
        default:
            LOG.info("unknown statement detected");
            break;
        }
        MetadataManager.MANAGER.createTable(storedTable, false);
    }

    /**
     * Persist the Catalog into Metadata Manager into its cluster.
     * @param catalog The catalog to persist.
     * @param clusterName The catalog cluster to persist.
     */
    public void persistCreateCatalogInCluster(CatalogName catalog, ClusterName clusterName) {
        MetadataManager.MANAGER.addCatalogToCluster(catalog, clusterName);
    }

    /**
     * Persist the Catalog into Metadata Manager into its cluster.
     * @param catalogMetadataList The catalogs to persist.
     * @param clusterName The catalog cluster to persist.
     */
    public void persistCreateCatalogsInCluster(List<CatalogMetadata> catalogMetadataList, ClusterName clusterName) {
        for (CatalogMetadata catalog:catalogMetadataList) {
            MetadataManager.MANAGER.addCatalogToCluster(catalog.getName(), clusterName);
        }
    }

    /**
     * Persists table Metadata in Metadata Manager.
     *
     * @param table The table metadata to be stored.
     */
    public void persistCreateTable(TableMetadata table) {
        MetadataManager.MANAGER.createTable(table);
    }

    /**
     * Persists index Metadata in Metadata Manager.
     *
     * @param index The index metadata to be stored.
     */
    public void persistCreateIndex(IndexMetadata index) {
        // TODO move to MetadataManager
        TableMetadata table = MetadataManager.MANAGER.getTable(index.getName().getTableName());
        table.addIndex(index.getName(), index);
        MetadataManager.MANAGER.createTable(table, false);
    }

    /**
     * Deletes catalog from Metadata Manager.
     *
     * @param catalog The catalog name.
     * @param ifExist The variable that indicates if it is can be stored if exists previously.
     */
    public void persistDropCatalog(CatalogName catalog, boolean ifExist) {
        MetadataManager.MANAGER.deleteCatalog(catalog, ifExist);
        MetadataManager.MANAGER.removeCatalogFromClusters(catalog);
    }

    /**
     * Deletes table data from Metadata Manager.
     *
     * @param table The table name.
     */
    public void persistDropTable(TableName table) {
        MetadataManager.MANAGER.deleteTable(table);
    }

    /**
     * Deletes index from Metadata Manager.
     *
     * @param index The index name.
     */
    public void persistDropIndex(IndexName index) {
        // TODO move to MetadataManager
        TableMetadata table = MetadataManager.MANAGER.getTable(index.getTableName());
        table.deleteIndex(index);
        MetadataManager.MANAGER.createTable(table, false);
    }

    /**
     * Persists attached connector's metadata in Metadata Manager.
     *
     * @param clusterName   The cluster name.
     * @param connectorName The connector name.
     * @param options       The map of connector options.
     * @param priority      The priority of the connector for the associated cluster.
     * @param pageSize      The pagination size.
     * @return A {@link com.stratio.crossdata.common.result.Result}.
     */
    public Result persistAttachConnector(ClusterName clusterName, ConnectorName connectorName,
            Map<Selector, Selector> options, int priority, int pageSize) {

        // Update information in Cluster
        ClusterMetadata clusterMetadata =
                MetadataManager.MANAGER.getCluster(clusterName);
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs =
                clusterMetadata.getConnectorAttachedRefs();
        ConnectorAttachedMetadata value =
                new ConnectorAttachedMetadata(connectorName, clusterName, options, priority);
        connectorAttachedRefs.put(connectorName, value);
        clusterMetadata.setConnectorAttachedRefs(connectorAttachedRefs);
        MetadataManager.MANAGER.createCluster(clusterMetadata, false);

        // Update information in Connector
        ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
        connectorMetadata.addClusterProperties(clusterName, options);
        connectorMetadata.setPageSize(pageSize);
        connectorMetadata.addClusterPriority(clusterName, priority);
        MetadataManager.MANAGER.createConnector(connectorMetadata, false);

        return CommandResult.createCommandResult("Connector started its session successfully");
    }

    /**
     * Deletes persisted data from an attached connector in Metadata Manager.
     *
     * @param clusterName   The cluster name.
     * @param connectorName The connector name.
     * @return A {@link com.stratio.crossdata.common.result.Result}.
     */
    public Result persistDetachConnector(ClusterName clusterName, ConnectorName connectorName) {
        ClusterMetadata clusterMetadata =
                MetadataManager.MANAGER.getCluster(clusterName);

        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs =
                clusterMetadata.getConnectorAttachedRefs();

        connectorAttachedRefs.remove(connectorName);
        clusterMetadata.setConnectorAttachedRefs(connectorAttachedRefs);

        MetadataManager.MANAGER.createCluster(clusterMetadata, false);

        ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
        connectorMetadata.getClusterRefs().remove(clusterName);
        connectorMetadata.getClusterProperties().remove(clusterName);
        connectorMetadata.getClusterPriorities().remove(clusterName);

        MetadataManager.MANAGER.createConnector(connectorMetadata, false);

        return CommandResult.createCommandResult("CONNECTOR detached successfully");
    }

    /**
     * Create into metadata manager the previous catalogs that exists in the datastore.
     * @param catalogMetadataList The catalogs.
     */
    private void persistImportCatalogs(List<CatalogMetadata> catalogMetadataList) {
        for(CatalogMetadata catalog: catalogMetadataList){
            MetadataManager.MANAGER.createCatalog(catalog);
        }
    }

    /**
     * Store into metadata manager the tables in its cluster.
     * @param tableMetadataList The list of tables.
     * @param clusterName The cluster where the tables are created.
     */
    private void persistTables(List<TableMetadata> tableMetadataList, ClusterName clusterName) {
        for(TableMetadata table: tableMetadataList){
            CatalogName catalogName = table.getName().getCatalogName();
            if(!MetadataManager.MANAGER.exists(catalogName)){
                CatalogMetadata catalogMetadata = new CatalogMetadata(
                        catalogName,
                        new HashMap<Selector, Selector>(),
                        new HashMap<TableName, TableMetadata>());
                MetadataManager.MANAGER.createCatalog(catalogMetadata);
            }
            table.setClusterRef(clusterName);
            MetadataManager.MANAGER.createTable(table);
        }
    }

}
