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

package com.stratio.meta2.core.api;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;

import com.stratio.meta.common.ask.APICommand;
import com.stratio.meta.common.ask.Command;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.MetadataResult;
import com.stratio.meta2.common.api.Manifest;
import com.stratio.meta2.common.api.ManifestHelper;
import com.stratio.meta2.common.api.PropertiesType;
import com.stratio.meta2.common.api.connector.ConnectorType;
import com.stratio.meta2.common.api.connector.DataStoreRefsType;
import com.stratio.meta2.common.api.connector.SupportedOperationsType;
import com.stratio.meta2.common.api.datastore.BehaviorsType;
import com.stratio.meta2.common.api.datastore.DataStoreType;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ConnectorMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.result.ErrorResult;
import com.stratio.meta2.common.result.ErrorType;
import com.stratio.meta2.common.result.Result;
import com.stratio.meta2.core.execution.ExecutionManager;
import com.stratio.meta2.core.metadata.MetadataManager;

public class APIManager {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(APIManager.class);

    /**
     * Class constructor.
     */
    public APIManager() {
    }

    /**
     * Process an incoming API request.
     *
     * @param cmd The commnand to be executed.
     * @return A {@link com.stratio.meta.common.result.MetadataResult}.
     */
    public Result processRequest(Command cmd) {
        //TODO: create "LIST CONNECTORS" command
        Result result;
        if (APICommand.LIST_CATALOGS().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.LIST_CATALOGS().toString());
            List<String> catalogs = MetadataManager.MANAGER.getCatalogs();
            result = MetadataResult.createSuccessMetadataResult();
            ((MetadataResult) result).setCatalogList(catalogs);
            //result = MetadataResult.createSuccessMetadataResult();
        } else if (APICommand.LIST_TABLES().equals(cmd.commandType())) {
            List<TableMetadata> tables;
            if (cmd.params() != null && !cmd.params().isEmpty()) {
                String catalog = (String) cmd.params().get(0);
                LOG.info("Processing " + APICommand.LIST_TABLES().toString());
                tables = MetadataManager.MANAGER.getTablesByCatalogName(new CatalogName(catalog));
            } else {
                LOG.info("Processing " + APICommand.LIST_TABLES().toString());
                tables = MetadataManager.MANAGER.getTables();
            }
            result = MetadataResult.createSuccessMetadataResult();
            ((MetadataResult) result).setTableList(tables);
        } else if (APICommand.LIST_COLUMNS().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.LIST_COLUMNS().toString());
            List<ColumnMetadata> columns;
            //TODO Remove this part when migrates to new ColumnMetadata
            List<com.stratio.meta.common.metadata.structures.ColumnMetadata> columnsResult = new ArrayList<>();
            if (cmd.params() != null && !cmd.params().isEmpty()) {
                String catalog = (String) cmd.params().get(0);
                String table = (String) cmd.params().get(1);
                columns = MetadataManager.MANAGER.getColumnByTable(catalog, table);
            } else {
                columns = MetadataManager.MANAGER.getColumns();
            }

            for (ColumnMetadata columnMetadata : columns) {
                columnsResult.add(new com.stratio.meta.common.metadata.structures.ColumnMetadata(columnMetadata
                        .getName().getTableName().getName(), columnMetadata.getName().getName()));
            }
            
            result = MetadataResult.createSuccessMetadataResult();
            ((MetadataResult) result).setColumnList(columnsResult);
        } else if (APICommand.ADD_MANIFEST().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.ADD_MANIFEST().toString());
            persistManifest((Manifest) cmd.params().get(0));
            result = CommandResult.createCommandResult("Manifest added.");
        } else if (APICommand.RESET_METADATA().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.RESET_METADATA().toString());
            result = resetMetadata();
        } else {
            result =
                    Result.createExecutionErrorResult("Command " + cmd.commandType() + " not supported");
            LOG.error(ErrorResult.class.cast(result).getErrorMessage());
        }
        return result;
    }

    private Result resetMetadata() {
        Result result = CommandResult.createCommandResult("Metadata reset.");
        try {
            MetadataManager.MANAGER.clear();
            ExecutionManager.MANAGER.clear();
        } catch (SystemException | NotSupportedException | HeuristicRollbackException | HeuristicMixedException | RollbackException
                e) {
            result = CommandResult.createErrorResult(ErrorType.EXECUTION, e.getMessage());
            LOG.error(e.getMessage());
        }
        return result;
    }

    private void persistManifest(Manifest manifest) {
        if (manifest.getManifestType() == Manifest.TYPE_DATASTORE) {
            persistDataStore((DataStoreType) manifest);
        } else {
            persistConnector((ConnectorType) manifest);
        }
    }

    private void persistDataStore(DataStoreType dataStoreType) {
        // NAME
        DataStoreName name = new DataStoreName(dataStoreType.getName());

        // VERSION
        String version = dataStoreType.getVersion();

        // REQUIRED PROPERTIES
        PropertiesType requiredProperties = dataStoreType.getRequiredProperties();

        // OPTIONAL PROPERTIES
        PropertiesType optionalProperties = dataStoreType.getOptionalProperties();

        // BEHAVIORS
        BehaviorsType behaviorsType = dataStoreType.getBehaviors();

        // Create Metadata
        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(
                name,
                version,
                (requiredProperties == null) ? null : requiredProperties.getProperty(),
                (optionalProperties == null) ? null : optionalProperties.getProperty(),
                (behaviorsType == null) ? null : behaviorsType.getBehavior());

        // Persist
        MetadataManager.MANAGER.createDataStore(dataStoreMetadata, false);

        LOG.debug("DataStore added: " + MetadataManager.MANAGER.getDataStore(name).toString());

    }

    private void persistConnector(ConnectorType connectorType) {
        // NAME
        ConnectorName name = new ConnectorName(connectorType.getConnectorName());

        // DATASTORES
        DataStoreRefsType dataStoreRefs = connectorType
                .getDataStores();

        // VERSION
        String version = connectorType.getVersion();

        // REQUIRED PROPERTIES
        PropertiesType requiredProperties = connectorType.getRequiredProperties();

        // OPTIONAL PROPERTIES
        PropertiesType optionalProperties = connectorType.getOptionalProperties();

        // SUPPORTED OPERATIONS
        SupportedOperationsType supportedOperations = connectorType.getSupportedOperations();

        // Create Metadata
        ConnectorMetadata connectorMetadata;

        if (MetadataManager.MANAGER.exists(name)) {
            connectorMetadata = MetadataManager.MANAGER.getConnector(name);
            connectorMetadata.setVersion(version);
            connectorMetadata.setDataStoreRefs(
                    ManifestHelper.convertManifestDataStoreNamesToMetadataDataStoreNames(dataStoreRefs
                            .getDataStoreName()));
            connectorMetadata.setRequiredProperties((requiredProperties == null) ? null : ManifestHelper
                    .convertManifestPropertiesToMetadataProperties(requiredProperties.getProperty()));
            connectorMetadata.setOptionalProperties((optionalProperties == null) ? null : ManifestHelper
                    .convertManifestPropertiesToMetadataProperties(optionalProperties.getProperty()));
            connectorMetadata.setSupportedOperations(ManifestHelper.convertManifestOperationsToMetadataOperations
                    (supportedOperations.getOperation()));
        } else {
            connectorMetadata = new ConnectorMetadata(
                    name,
                    version,
                    dataStoreRefs.getDataStoreName(),
                    (requiredProperties == null) ? null : requiredProperties.getProperty(),
                    (optionalProperties == null) ? null : optionalProperties.getProperty(),
                    supportedOperations.getOperation());
        }

        // Persist
        MetadataManager.MANAGER.createConnector(connectorMetadata, false);
    }

}
