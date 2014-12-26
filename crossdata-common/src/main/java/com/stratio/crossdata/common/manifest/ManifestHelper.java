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

package com.stratio.crossdata.common.manifest;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.stratio.crossdata.common.data.DataStoreName;

/**
 * Helper class that facilitates the processing of manifest files.
 */
public final class ManifestHelper implements Serializable {

    /**
     * Serial version UID in order to be Serializable.
     */
    private static final long serialVersionUID = -6979108221035957858L;

    /**
     * Private constructor as all methods are static.
     */
    private ManifestHelper() {
    }

    /**
     * Convert a manifest file into an String.
     *
     * @param manifest The {@link com.stratio.crossdata.common.manifest.CrossdataManifest}.
     * @return A String representation.
     */
    public static String manifestToString(CrossdataManifest manifest) {
        String result = null;
        if (manifest instanceof DataStoreType) {
            result = dataStoreManifestToString((DataStoreType) manifest);
        } else if (manifest instanceof ConnectorType) {
            result = connectorManifestToString((ConnectorType) manifest);
        }
        return result;
    }

    /**
     * Transform a DataStoreType into an String.
     *
     * @param dataStoreType A {@link com.stratio.crossdata.common.manifest.DataStoreType}.
     * @return A String representation.
     */
    private static String dataStoreManifestToString(DataStoreType dataStoreType) {
        StringBuilder sb = new StringBuilder("DATASTORE");
        sb.append(System.lineSeparator());

        // NAME
        sb.append("Name: ").append(dataStoreType.getName()).append(System.lineSeparator());

        // VERSION
        sb.append("Version: ").append(dataStoreType.getVersion()).append(System.lineSeparator());

        // REQUIRED PROPERTIES
        if (dataStoreType.getRequiredProperties() != null) {
            sb.append("Required properties: ").append(System.lineSeparator());
            for (PropertyType propertyType : dataStoreType.getRequiredProperties().getProperty()) {
                sb.append("\t").append("Property: ").append(System.lineSeparator());
                sb.append("\t").append("\t").append("PropertyName: ").append(propertyType.getPropertyName())
                        .append(System.lineSeparator());
                sb.append("\t").append("\t").append("Description: ").append(propertyType.getDescription()).append(System
                        .lineSeparator());
            }
        }

        // OPTIONAL PROPERTIES
        if (dataStoreType.getOptionalProperties() != null) {
            sb.append("Optional properties: ").append(System.lineSeparator());
            for (PropertyType propertyType : dataStoreType.getOptionalProperties().getProperty()) {
                sb.append("\t").append("Property: ").append(System.lineSeparator());
                sb.append("\t").append("\t").append("PropertyName: ").append(propertyType.getPropertyName())
                        .append(System.lineSeparator());
                sb.append("\t").append("\t").append("Description: ").append(propertyType.getDescription()).append(System
                        .lineSeparator());
            }
        }

        // BEHAVIORS
        if (dataStoreType.getBehaviors() != null) {
            sb.append("Behaviors: ").append(System.lineSeparator());
            for (String behavior : dataStoreType.getBehaviors().getBehavior()) {
                sb.append("\t").append("Behavior: ").append(behavior).append(System.lineSeparator());
            }
        }

        // RESULT
        return sb.toString();
    }

    /**
     * Transform a ConnectorType into an String.
     *
     * @param connectorType A {@link com.stratio.crossdata.common.manifest.ConnectorType}.
     * @return An String representation.
     */
    private static String connectorManifestToString(ConnectorType connectorType) {
        StringBuilder sb = new StringBuilder("CONNECTOR");
        sb.append(System.lineSeparator());

        // CONNECTOR NAMES
        sb.append("ConnectorName: ").append(connectorType.getConnectorName()).append(System.lineSeparator());

        // DATA STORES NAME
        sb.append("DataStores: ").append(System.lineSeparator());
        for (String dataStoreName : connectorType.getDataStores().getDataStoreName()) {
            sb.append("\t").append("DataStoreName: ").append(dataStoreName).append(System.lineSeparator());
        }

        // VERSION
        sb.append("Version: ").append(connectorType.getVersion()).append(System.lineSeparator());

        // REQUIRED PROPERTIES
        if (connectorType.getRequiredProperties() != null) {
            sb.append("Required properties: ").append(System.lineSeparator());
            for (PropertyType propertyType : connectorType.getRequiredProperties().getProperty()) {
                sb.append("\t").append("Property: ").append(System.lineSeparator());
                sb.append("\t").append("\t").append("PropertyName: ").append(propertyType.getPropertyName())
                        .append(System.lineSeparator());
                sb.append("\t").append("\t").append("Description: ").append(propertyType.getDescription()).append(System
                        .lineSeparator());
            }
        }

        // OPTIONAL PROPERTIES
        if (connectorType.getOptionalProperties() != null) {
            sb.append("Optional properties: ").append(System.lineSeparator());
            for (PropertyType propertyType : connectorType.getOptionalProperties().getProperty()) {
                sb.append("\t").append("Property: ").append(System.lineSeparator());
                sb.append("\t").append("\t").append("PropertyName: ").append(propertyType.getPropertyName())
                        .append(System.lineSeparator());
                sb.append("\t").append("\t").append("Description: ").append(propertyType.getDescription()).append(System
                        .lineSeparator());
            }
        }

        // SUPPORTED OPERATIONS
        sb.append("Supported operations: ").append(System.lineSeparator());
        for (String operation : connectorType.getSupportedOperations().getOperation()) {
            sb.append("\t").append("Operation: ").append(operation).append(System.lineSeparator());
        }

        // RESULT
        return sb.toString();
    }

    /**
     * Conversion method (Manifest Properties to Metadata Properties).
     *
     * @param requiredProperties The list of required properties.
     * @return Set<PropertyType> A set of {@link com.stratio.crossdata.common.manifest.PropertyType}.
     */
    public static Set<PropertyType> convertManifestPropertiesToMetadataProperties(
            List<PropertyType> requiredProperties) {
        Set<PropertyType> metadataProperties = new HashSet<>();
        for (PropertyType propertyType : requiredProperties) {
            metadataProperties.add(propertyType);
        }
        return metadataProperties;
    }

    /**
     * Conversion method (Manifest DataStore Names to Metadata Store Names).
     *
     * @param dataStoreRefs The list of data store names.
     * @return Set<DataStoreName> A set of {@link com.stratio.crossdata.common.data.DataStoreName}.
     */
    public static Set<DataStoreName> convertManifestDataStoreNamesToMetadataDataStoreNames(List<String> dataStoreRefs) {
        Set<DataStoreName> dataStoreNames = new HashSet<>();
        for (String name : dataStoreRefs) {
            dataStoreNames.add(new DataStoreName(name));
        }
        return dataStoreNames;
    }

    /**
     * Conversion method (Manifest Behaviors to Metadata Behaviors).
     *
     * @param behaviors The list of behaviors.
     * @return A set of behaviors.
     */
    public static Set<String> convertManifestBehaviorsToMetadataBehaviors(List<String> behaviors) {
        Set<String> metadataBehaviors = new HashSet<>();
        for (String behavior : behaviors) {
            metadataBehaviors.add(behavior);
        }
        return metadataBehaviors;
    }

    /**
     * Conversion method (Manifest Functions to Metadata Functions).
     *
     * @param connectorFunctions The list of connector functions.
     * @return Set<PropertyType> A set of {@link com.stratio.crossdata.common.manifest.FunctionType}.
     */
    public static Set<FunctionType> convertManifestFunctionsToMetadataFunctions(
            List<FunctionType> connectorFunctions) {
        Set<FunctionType> metadataFunctions = new HashSet<>();
        for (FunctionType functionType : connectorFunctions) {
            metadataFunctions.add(functionType);
        }
        return metadataFunctions;
    }

    public static Set<String> convertManifestExcludedFunctionsToMetadataExcludedFunctions(
            List<ExcludeType> excludedFunctions) {
        Set<String> metadataExcludedFunctions = new HashSet<>();
        for (ExcludeType excludeFunction: excludedFunctions) {
            metadataExcludedFunctions.add(excludeFunction.getFunctionName());
        }
        return metadataExcludedFunctions;
    }
}
