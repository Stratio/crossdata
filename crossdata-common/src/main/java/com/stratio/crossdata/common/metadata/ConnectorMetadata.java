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

package com.stratio.crossdata.common.metadata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.Status;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.manifest.ManifestHelper;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Metadata information associated with a Connector.
 */
public class ConnectorMetadata implements IMetadata {

    /**
     * Connector name.
     */
    private final ConnectorName name;

    /**
     * Connector version.
     */
    private String version;

    /**
     * Set of {@link com.stratio.crossdata.common.data.DataStoreName} that the connector may access.
     */
    private Set<DataStoreName> dataStoreRefs;

    /**
     * Set of {@link com.stratio.crossdata.common.data.ClusterName} the connector has access to.
     */
    //TODO; We can get this info also from clusterProperties.entrySet()
    private Set<ClusterName> clusterRefs = new HashSet<>();

    /**
     * A map of cluster names with their map of properties.
     */
    private Map<ClusterName, Map<Selector, Selector>> clusterProperties = new HashMap<>();

    /**
     * The connector status.
     */
    private Status status;

    /**
     * The actor Akka reference.
     */
    private String actorRef;

    /**
     * The set of required properties.
     */
    private Set<PropertyType> requiredProperties;

    /**
     * The set of optional properties.
     */
    private Set<PropertyType> optionalProperties;

    /**
     * The set of supported operations.
     */
    private Set<Operations> supportedOperations;

    /**
     * Whether the manifest of this connector was already added or not
     */
    private boolean manifestAdded = false;

    /**
     * Class constructor.
     *
     * @param name                The connector name.
     * @param version             The connector version.
     * @param dataStoreRefs       The set of datastores the connector may access.
     * @param clusterProperties   The map of clusters associated with this connector and their associated properties.
     * @param requiredProperties  The set of required properties.
     * @param optionalProperties  The set of optional properties.
     * @param supportedOperations The set of supported operations.
     */
    public ConnectorMetadata(ConnectorName name, String version, Set<DataStoreName> dataStoreRefs,
            Map<ClusterName, Map<Selector, Selector>> clusterProperties,
            Set<PropertyType> requiredProperties, Set<PropertyType> optionalProperties,
            Set<Operations> supportedOperations) throws ManifestException {
        this(name, version, dataStoreRefs, clusterProperties, Status.OFFLINE, null, requiredProperties,
                optionalProperties, supportedOperations);
    }

    /**
     * Class constructor.
     *
     * @param name                The connector name.
     * @param version             The connector version.
     * @param dataStoreRefs       The set of datastores the connector may access.
     * @param clusterProperties   The map of clusters associated with this connector and their associated properties.
     * @param status     The connector status.
     * @param actorRef            The actor Akka reference.
     * @param requiredProperties  The set of required properties.
     * @param optionalProperties  The set of optional properties.
     * @param supportedOperations The set of supported operations.
     */
    public ConnectorMetadata(ConnectorName name, String version,
            Set<DataStoreName> dataStoreRefs,
            Map<ClusterName, Map<Selector, Selector>> clusterProperties, Status status,
            String actorRef,
            Set<PropertyType> requiredProperties,
            Set<PropertyType> optionalProperties,
            Set<Operations> supportedOperations) throws ManifestException {

        if(name.getName().isEmpty()){
            throw new ManifestException(new ExecutionException("Tag name cannot be empty"));
        } else {
            this.name = name;
        }

        this.version = version;

        if(dataStoreRefs == null){
            this.dataStoreRefs = new HashSet<>();
        } else {
            this.dataStoreRefs = dataStoreRefs;
        }

        this.clusterProperties = (clusterProperties!=null)?
                clusterProperties:
                new HashMap<ClusterName, Map<Selector, Selector>>();
        this.requiredProperties = (requiredProperties!=null)?requiredProperties:new HashSet<PropertyType>();
        this.optionalProperties = (optionalProperties!=null)?optionalProperties:new HashSet<PropertyType>();
        this.supportedOperations = (supportedOperations!=null)?supportedOperations:new HashSet<Operations>();
        this.status = status;
        this.actorRef = actorRef;
    }

    /**
     * Class constructor.
     *
     * @param name                The connector name.
     * @param version             The connector version.
     * @param dataStoreRefs       The set of datastores the connector may access.
     * @param requiredProperties  The set of required properties.
     * @param optionalProperties  The set of optional properties.
     * @param supportedOperations The set of supported operations.
     */
    public ConnectorMetadata(ConnectorName name, String version, List<String> dataStoreRefs,
            List<PropertyType> requiredProperties, List<PropertyType> optionalProperties,
            List<String> supportedOperations) throws ManifestException {

        if(name.getName().isEmpty()){
            throw new ManifestException(new ExecutionException("Tag name cannot be empty"));
        } else {
            this.name = name;
        }

        if(version.isEmpty()){
            throw new ManifestException(new ExecutionException("Tag version cannot be empty"));
        } else {
            this.version = version;
        }

        this.dataStoreRefs = ManifestHelper.convertManifestDataStoreNamesToMetadataDataStoreNames(dataStoreRefs);
        if(this.dataStoreRefs == null){
            this.dataStoreRefs = new HashSet<>();
        }

        if (requiredProperties != null) {
            this.requiredProperties = ManifestHelper.convertManifestPropertiesToMetadataProperties(requiredProperties);
        } else {
            this.requiredProperties = new HashSet<>();
        }

        if (optionalProperties != null) {
            this.optionalProperties = ManifestHelper.convertManifestPropertiesToMetadataProperties(optionalProperties);
        } else {
            this.optionalProperties = new HashSet<>();
        }

        if(supportedOperations != null){
            this.supportedOperations = convertManifestOperationsToMetadataOperations(supportedOperations);
        } else {
            this.supportedOperations = new HashSet<>();
        }

        this.status = Status.OFFLINE;
    }

    /**
     * Get the connector name.
     *
     * @return A {@link com.stratio.crossdata.common.data.ConnectorName}.
     */
    public ConnectorName getName() {
        return name;
    }

    /**
     * Get the connector version.
     *
     * @return A String with the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get the set of datastores the connector may access.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.DataStoreName}.
     */
    public Set<DataStoreName> getDataStoreRefs() {
        return dataStoreRefs;
    }

    /**
     * Get the set of required properties.
     *
     * @return A set of {@link com.stratio.crossdata.common.manifest.PropertyType}.
     */
    public Set<PropertyType> getRequiredProperties() {
        return requiredProperties;
    }

    /**
     * Get the set of optional properties.
     *
     * @return A set of {@link com.stratio.crossdata.common.manifest.PropertyType}.
     */
    public Set<PropertyType> getOptionalProperties() {
        return optionalProperties;
    }

    /**
     * Get the set of supported operations.
     *
     * @return A set of {@link com.stratio.crossdata.common.metadata.Operations}.
     */
    public Set<Operations> getSupportedOperations() {
        return supportedOperations;
    }

    /**
     * Get the set of clusters the connector has access to.
     *
     * @return A set of {@link com.stratio.crossdata.common.data.ClusterName}.
     */
    public Set<ClusterName> getClusterRefs() {
        return clusterRefs;
    }

    /**
     * Set the clusters the connector has access to.
     *
     * @param clusterRefs A set of {@link com.stratio.crossdata.common.data.ClusterName}.
     */
    public void setClusterRefs(Set<ClusterName> clusterRefs) {
        this.clusterRefs = clusterRefs;
    }

    /**
     * Get the map of clusters associated with this connector and their associated properties.
     *
     * @return A map of {@link com.stratio.crossdata.common.data.ClusterName} associated with a map
     * of {@link com.stratio.crossdata.common.statements.structures.Selector} tuples.
     */
    public Map<ClusterName, Map<Selector, Selector>> getClusterProperties() {
        return clusterProperties;
    }

    /**
     * Set the map of clusters and their associated properties.
     *
     * @param clusterProperties A map of {@link com.stratio.crossdata.common.data.ClusterName} associated with a map
     *                          of {@link com.stratio.crossdata.common.statements.structures.Selector} tuples.
     */
    public void setClusterProperties(Map<ClusterName, Map<Selector, Selector>> clusterProperties) {
        this.clusterProperties = clusterProperties;
    }

    /**
     * Get the connector status.
     *
     * @return A {@link com.stratio.crossdata.common.data.Status}.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set the connector status.
     *
     * @param status A {@link com.stratio.crossdata.common.data.Status}.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Get the actor Akka reference.
     *
     * @return A String Akka reference.
     */
    public String getActorRef() {
        return actorRef;
    }

    /**
     * Sets the actor Akka reference.
     *
     * @param actorRef String of the actor reference path.
     */
    public void setActorRef(String actorRef) {
        this.status = Status.ONLINE;
        this.actorRef = actorRef;
    }

    /**
     * Determine if the connector supports a specific operation.
     *
     * @param operation The required operation.
     * @return Whether it is supported.
     */
    public boolean supports(Operations operation) {
        return supportedOperations.contains(operation);
    }

    /**
     * Adds a map of properties to a cluster.
     *
     * @param clusterName The cluster name.
     * @param options     A map of {@link com.stratio.crossdata.common.statements.structures.Selector} tuples.
     */
    public void addClusterProperties(ClusterName clusterName, Map<Selector, Selector> options) {
        if (clusterProperties == null) {
            this.clusterProperties = new HashMap<>();
        }
        clusterProperties.put(clusterName, options);
    }

    /**
     * Sets the connector version.
     *
     * @param version The connector version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Set the datastores the connector may access.
     *
     * @param dataStoreRefs A set of {@link com.stratio.crossdata.common.data.DataStoreName}.
     */
    public void setDataStoreRefs(Set<DataStoreName> dataStoreRefs) {
        this.dataStoreRefs = dataStoreRefs;
    }

    /**
     * Set the required properties.
     *
     * @param requiredProperties A set of {@link com.stratio.crossdata.common.manifest.PropertyType}.
     */
    public void setRequiredProperties(Set<PropertyType> requiredProperties) {
        this.requiredProperties = requiredProperties;
    }

    /**
     * Set the optional properties.
     *
     * @param optionalProperties A set of {@link com.stratio.crossdata.common.manifest.PropertyType}.
     */
    public void setOptionalProperties(Set<PropertyType> optionalProperties) {
        this.optionalProperties = optionalProperties;
    }

    /**
     * Set the supported operations.
     *
     * @param supportedOperations A set of {@link com.stratio.crossdata.common.metadata.Operations}.
     */
    public void setSupportedOperations(Set<Operations> supportedOperations) {
        this.supportedOperations = supportedOperations;
    }

    /**
     * Set the supported operations.
     *
     * @param supportedOperations A list of supported operations.
     */
    public void setSupportedOperations(List<String> supportedOperations) throws ManifestException {
        this.supportedOperations = convertManifestOperationsToMetadataOperations(supportedOperations);
    }

    public boolean isManifestAdded() {
        return manifestAdded;
    }

    public void setManifestAdded(boolean manifestAdded) {
        this.manifestAdded = manifestAdded;
    }

    /**
     * Convert a list of supported operations into a set of {@link com.stratio.crossdata.common.metadata.Operations}.
     *
     * @param supportedOperations The list of supported operations.
     * @return A set of {@link com.stratio.crossdata.common.metadata.Operations}.
     */
    private Set<Operations> convertManifestOperationsToMetadataOperations(
            List<String> supportedOperations) throws ManifestException {
        Set<Operations> operations = new HashSet<>();
        try {
            for (String supportedOperation: supportedOperations) {
                operations.add(Operations.valueOf(supportedOperation.toUpperCase()));
            }
        } catch (IllegalArgumentException ex) {
            throw new ManifestException(ex);
        }

        return operations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Connector: ");
        sb.append(name).append(" status: ").append(status).append(" actorRef: ").append(actorRef);
        return sb.toString();
    }
}
