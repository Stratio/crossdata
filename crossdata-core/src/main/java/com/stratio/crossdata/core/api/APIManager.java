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

package com.stratio.crossdata.core.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.ask.APICommand;
import com.stratio.crossdata.common.ask.Command;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.manifest.BehaviorsType;
import com.stratio.crossdata.common.manifest.ConnectorType;
import com.stratio.crossdata.common.manifest.CrossdataManifest;
import com.stratio.crossdata.common.manifest.DataStoreRefsType;
import com.stratio.crossdata.common.manifest.DataStoreType;
import com.stratio.crossdata.common.manifest.ManifestHelper;
import com.stratio.crossdata.common.manifest.PropertiesType;
import com.stratio.crossdata.common.manifest.SupportedOperationsType;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.ErrorType;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.core.execution.ExecutionManager;
import com.stratio.crossdata.core.metadata.MetadataManager;

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
     * @return A {@link com.stratio.crossdata.common.result.MetadataResult}.
     */
    public Result processRequest(Command cmd) {
        Result result;
        if (APICommand.LIST_CATALOGS().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.LIST_CATALOGS().toString());
            List<String> catalogs = MetadataManager.MANAGER.getCatalogs();
            result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_LIST_CATALOGS);
            ((MetadataResult) result).setCatalogList(catalogs);
        } else if (APICommand.LIST_TABLES().equals(cmd.commandType())) {
            List<TableMetadata> tables;

            if (cmd.params() != null && !cmd.params().isEmpty()) {
                String catalog = (String) cmd.params().get(0);
                LOG.info("Processing " + APICommand.LIST_TABLES().toString());
                tables = MetadataManager.MANAGER.getTablesByCatalogName(catalog);
            } else {
                LOG.info("Processing " + APICommand.LIST_TABLES().toString());
                tables = MetadataManager.MANAGER.getTables();
            }
            result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_LIST_TABLES);
            ((MetadataResult) result).setTableList(tables);
        } else if (APICommand.LIST_COLUMNS().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.LIST_COLUMNS().toString());
            List<ColumnMetadata> columns;
            //TODO Remove this part when migrates to new ColumnMetadata
            List<com.stratio.crossdata.common.metadata.structures.ColumnMetadata> columnsResult = new ArrayList<>();
            if (cmd.params() != null && !cmd.params().isEmpty()) {
                String catalog = (String) cmd.params().get(0);
                String table = (String) cmd.params().get(1);
                columns = MetadataManager.MANAGER.getColumnByTable(catalog, table);
            } else {
                columns = MetadataManager.MANAGER.getColumns();
            }

            for (ColumnMetadata columnMetadata : columns) {
                columnsResult.add(new com.stratio.crossdata.common.metadata.structures.ColumnMetadata(columnMetadata
                        .getName().getTableName().getName(), columnMetadata.getName().getName()));
            }
            result = MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_LIST_COLUMNS);
            ((MetadataResult) result).setColumnList(columnsResult);
        } else if (APICommand.ADD_MANIFEST().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.ADD_MANIFEST().toString());
            persistManifest((CrossdataManifest) cmd.params().get(0));
            result = CommandResult.createCommandResult("CrossdataManifest added "
                    + System.lineSeparator()
                    + cmd.params().get(0).toString());
        } else if (APICommand.RESET_METADATA().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.RESET_METADATA().toString());
            result = resetMetadata();
        } else if (APICommand.LIST_CONNECTORS().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.LIST_CONNECTORS().toString());
            result = listConnectors();
        } else {
            result =
                    Result.createUnsupportedOperationErrorResult("Command " + cmd.commandType() + " not supported");
            LOG.error(ErrorResult.class.cast(result).getErrorMessage());
        }
        return result;
    }

    private Result listConnectors() {
        Result result;
        List<ConnectorMetadata> connectors = MetadataManager.MANAGER.getConnectors();
        StringBuilder stringBuilder = new StringBuilder().append(System.getProperty("line.separator"));

        for (ConnectorMetadata connector : connectors) {
            stringBuilder = stringBuilder.append("Connector: ").append(connector.getName())
                    .append("\t").append(connector.getConnectorStatus());
            // ClusterRefs
            if (connector.getClusterRefs() == null) {
                stringBuilder = stringBuilder.append("\t")
                        .append("UNKNOWN");
            } else {
                stringBuilder = stringBuilder.append("\t")
                        .append(Arrays.toString(connector.getClusterRefs().toArray()));
            }
            // DatastoreRefs
            if (connector.getDataStoreRefs() == null) {
                stringBuilder = stringBuilder.append("\t")
                        .append("UNKNOWN");
            } else {
                stringBuilder = stringBuilder.append("\t")
                        .append(Arrays.toString(connector.getDataStoreRefs().toArray()));
            }
            // ActorRef
            if (connector.getActorRef() == null) {
                stringBuilder = stringBuilder.append("\t")
                        .append("UNKNOWN");
            } else {
                stringBuilder = stringBuilder.append("\t")
                        .append(connector.getActorRef());
            }

            stringBuilder = stringBuilder.append(System.getProperty("line.separator"));
        }
        result = CommandResult.createCommandResult(stringBuilder.toString());
        return result;
    }

    private Result resetMetadata() {
        Result result = CommandResult.createCommandResult("Metadata reset.");
        try {
            MetadataManager.MANAGER.clear();
            ExecutionManager.MANAGER.clear();
        } catch (SystemException | NotSupportedException | HeuristicRollbackException | HeuristicMixedException | RollbackException
                e) {
            result = CommandResult.createErrorResult(e);
            LOG.error(e.getMessage());
        }
        return result;
    }

    private void persistManifest(CrossdataManifest manifest) {
        if (manifest.getManifestType() == CrossdataManifest.TYPE_DATASTORE) {
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
            connectorMetadata.setSupportedOperations(supportedOperations.getOperation());
        } else {
            connectorMetadata = new ConnectorMetadata(
                    name,
                    version,
                    (dataStoreRefs == null) ? null : dataStoreRefs.getDataStoreName(),
                    (requiredProperties == null) ? null : requiredProperties.getProperty(),
                    (optionalProperties == null) ? null : optionalProperties.getProperty(),
                    (supportedOperations == null) ? null : supportedOperations.getOperation());
        }

        // Persist
        MetadataManager.MANAGER.createConnector(connectorMetadata, false);
    }

}
