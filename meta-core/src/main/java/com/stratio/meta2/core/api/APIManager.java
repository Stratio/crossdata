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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.stratio.meta.common.ask.APICommand;
import com.stratio.meta.common.ask.Command;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.ErrorResult;
import com.stratio.meta.common.result.MetadataResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta2.common.api.Manifest;
import com.stratio.meta2.common.api.generated.PropertiesType;
import com.stratio.meta2.common.api.generated.connector.ConnectorType;
import com.stratio.meta2.common.api.generated.connector.DataStoreRefsType;
import com.stratio.meta2.common.api.generated.connector.SupportedOperationsType;
import com.stratio.meta2.common.api.generated.datastore.DataStoreType;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.metadata.ConnectorMetadata;
import com.stratio.meta2.common.metadata.DataStoreMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
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
        Result result;
        if (APICommand.LIST_CATALOGS().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.LIST_CATALOGS().toString());
            result = MetadataResult.createSuccessMetadataResult();
            //MetadataResult.class.cast(result).setCatalogList(metadata.getCatalogsNames());
        } else if (APICommand.LIST_TABLES().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.LIST_TABLES().toString());
      /*
      CatalogMetadata catalogMetadata = metadata.getCatalogMetadata((String) cmd.params().get(0));
      if (catalogMetadata != null) {
      */
            result = MetadataResult.createSuccessMetadataResult();
            Map<String, TableMetadata> tableList = new HashMap<>();
            //Add db tables.
            //TODO: Review...
            //tableList.putAll(helper.toCatalogMetadata(catalogMetadata).getTables());
            //Add ephemeral tables.
            //tableList.addAll(metadata.getEphemeralTables(cmd.params().get(0)));
            MetadataResult.class.cast(result).setTableList(new ArrayList(tableList.keySet()));
            //} else {
            result =
                    Result.createExecutionErrorResult("CATALOG " + cmd.params().get(0) + " not found");
            //}
        } else if (APICommand.ADD_MANIFEST().equals(cmd.commandType())) {
            LOG.info("Processing " + APICommand.ADD_MANIFEST().toString());
            persistManifest((Manifest) cmd.params().get(0));
            result = CommandResult.createCommandResult("OK");
        } else {
            result =
                    Result.createExecutionErrorResult("Command " + cmd.commandType() + " not supported");
            LOG.error(ErrorResult.class.cast(result).getErrorMessage());
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
        DataStoreName name = dataStoreType.getName();

        // VERSION
        String version = dataStoreType.getVersion();

        // REQUIRED PROPERTIES
        Set<PropertiesType> requiredProperties = dataStoreType.getRequiredProperties();

        // OPTIONAL PROPERTIES
        Set<PropertiesType> optionalProperties = dataStoreType.getOptionalProperties();

        // Create Metadata
        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(name, version, requiredProperties,
                optionalProperties);

        // Persist
        MetadataManager.MANAGER.createDataStore(dataStoreMetadata);
    }

    private void persistConnector(ConnectorType connectorType) {
        // NAME
        ConnectorName name = connectorType.getConnectorName();

        // DATASTORES
        DataStoreRefsType dataStoreRefs = connectorType
                .getDataStores();

        // VERSION
        String version = connectorType.getVersion();

        // REQUIRED PROPERTIES
        Set<PropertiesType> requiredProperties = connectorType.getRequiredProperties();

        // OPTIONAL PROPERTIES
        Set<PropertiesType> optionalProperties = connectorType.getOptionalProperties();

        // SUPPORTED OPERATIONS
        Set<SupportedOperationsType> supportedOperations = connectorType.getSupportedOperations();

        // Create Metadata
        ConnectorMetadata connectorMetadata = new ConnectorMetadata(name, version, dataStoreRefs.getDataStoreName(), requiredProperties,
                optionalProperties, supportedOperations);

        // Persist
        MetadataManager.MANAGER.createConnector(connectorMetadata);
    }

}
