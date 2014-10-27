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

import com.stratio.crossdata.common.data.ConnectorStatus;
import com.stratio.crossdata.common.manifest.ManifestHelper;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.statements.structures.Selector;

public class ConnectorMetadata implements IMetadata {

    private final ConnectorName name;
    private String version;
    private Set<DataStoreName> dataStoreRefs;

    //TODO; We can get this info also from clusterProperties.entrySet()
    private Set<ClusterName> clusterRefs = new HashSet<>();

    private Map<ClusterName, Map<Selector, Selector>> clusterProperties = new HashMap<>();

    private ConnectorStatus connectorStatus;
    private String actorRef;

    private Set<PropertyType> requiredProperties;
    private Set<PropertyType> optionalProperties;
    private Set<Operations> supportedOperations;

    /**
     * Constructor
     *
     * @param name
     * @param version
     * @param dataStoreRefs
     * @param clusterProperties
     * @param requiredProperties
     * @param optionalProperties
     * @param supportedOperations
     */
    public ConnectorMetadata(ConnectorName name, String version, Set<DataStoreName> dataStoreRefs,
            Map<ClusterName, Map<Selector, Selector>> clusterProperties,
            Set<PropertyType> requiredProperties, Set<PropertyType> optionalProperties,
            Set<Operations> supportedOperations) {
        this(name,version,dataStoreRefs,clusterProperties, ConnectorStatus.OFFLINE,null,requiredProperties,optionalProperties,
                supportedOperations);
    }

    /**
     * Constructor
     *
     * @param name
     * @param version
     * @param dataStoreRefs
     * @param clusterProperties
     * @param connectorStatus
     * @param actorRef
     * @param requiredProperties
     * @param optionalProperties
     * @param supportedOperations
     */
    public ConnectorMetadata(ConnectorName name, String version,
            Set<DataStoreName> dataStoreRefs,
            Map<ClusterName, Map<Selector, Selector>> clusterProperties, ConnectorStatus connectorStatus, String actorRef,
            Set<PropertyType> requiredProperties,
            Set<PropertyType> optionalProperties,
            Set<Operations> supportedOperations) {

        this.name = name;
        this.version = version;
        this.dataStoreRefs = dataStoreRefs;
        this.clusterProperties = clusterProperties;
        this.requiredProperties = requiredProperties;
        this.optionalProperties = optionalProperties;
        this.supportedOperations = supportedOperations;
        this.connectorStatus = connectorStatus;
        this.actorRef = actorRef;
    }

    /**
     * Constructor
     *
     * @param name
     * @param version
     * @param dataStoreRefs
     * @param requiredProperties
     * @param optionalProperties
     * @param supportedOperations
     */
    public ConnectorMetadata(ConnectorName name, String version, List<String> dataStoreRefs,
            List<PropertyType> requiredProperties, List<PropertyType> optionalProperties,
            List<String> supportedOperations) {
        this.name = name;
        this.version = version;
        this.dataStoreRefs = ManifestHelper.convertManifestDataStoreNamesToMetadataDataStoreNames(dataStoreRefs);
        if (requiredProperties != null) {
            this.requiredProperties = ManifestHelper.convertManifestPropertiesToMetadataProperties(requiredProperties);
        } else {
            this.requiredProperties = null;
        }
        if (optionalProperties != null) {
            this.optionalProperties = ManifestHelper.convertManifestPropertiesToMetadataProperties(optionalProperties);
        } else {
            this.optionalProperties = null;
        }

        this.supportedOperations = convertManifestOperationsToMetadataOperations(supportedOperations);
        this.connectorStatus = ConnectorStatus.OFFLINE;

    }

    /**
     * returns connector name
     *
     * @return ConnectorName
     */
    public ConnectorName getName() {
        return name;
    }

    /**
     * returns version
     *
     * @return String
     */
    public String getVersion() {
        return version;
    }

    /**
     * returns dataStoreRefs
     *
     * @return Set<DataStoreName>
     */
    public Set<DataStoreName> getDataStoreRefs() {
        return dataStoreRefs;
    }

    /**
     * Returns requiredProperties
     *
     * @return  Set<PropertyType>
     */
    public Set<PropertyType> getRequiredProperties() {
        return requiredProperties;
    }

    /**
     * returns optionalProperties
     *
     * @return Set<PropertyType>
     */
    public Set<PropertyType> getOptionalProperties() {
        return optionalProperties;
    }

    /**
     * returns supportedOperations;
     *
     * @return Set<Operations>
     */
    public Set<Operations> getSupportedOperations() {
        return supportedOperations;
    }

    /**
     * returns clusterRefs
     *
     * @return Set<ClusterName>
     */
    public Set<ClusterName> getClusterRefs() {
        return clusterRefs;
    }

    /**
     * sets clusterRefs
     *
     */
    public void setClusterRefs(Set<ClusterName> clusterRefs) {
        this.clusterRefs = clusterRefs;
    }

    /**
     * returns clusterProperties
     *
     * @return  Map<ClusterName, Map<Selector, Selector>>
     */
    public Map<ClusterName, Map<Selector, Selector>> getClusterProperties() {
        return clusterProperties;
    }

    /**
     * sets cluster properties
     *
     * @param clusterProperties
     */
    public void setClusterProperties(Map<ClusterName, Map<Selector, Selector>> clusterProperties) {
        this.clusterProperties = clusterProperties;
    }

    /**
     * gets connector status
     *
     * @return ConnectorStatus
     */
    public ConnectorStatus getConnectorStatus() {
        return connectorStatus;
    }

    /**
     * sets connector status
     *
     * @param connectorStatus
     */
    public void setConnectorStatus(ConnectorStatus connectorStatus) {
        this.connectorStatus = connectorStatus;
    }

    /**
     * gets serialized connector actor ref
     *
     * @return String
     */
    public String getActorRef() {
        return actorRef;
    }

    /**
     * Sets serialized actor ref as a String
     *
     * @param actorRef:String
     */
    public void setActorRef(String actorRef) {
        this.connectorStatus = ConnectorStatus.ONLINE;
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
     * adds properties to the cluster
     *
     * @param clusterName
     * @param options
     */
    public void addClusterProperties(ClusterName clusterName, Map<Selector, Selector> options) {
        if(clusterProperties == null){
            this.clusterProperties = new HashMap<>();
        }
        clusterProperties.put(clusterName, options);
    }

    /**
     * Sets version
     *
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * dataStoreRefs setter
     *
     * @param dataStoreRefs
     */
    public void setDataStoreRefs(Set<DataStoreName> dataStoreRefs) {
        this.dataStoreRefs = dataStoreRefs;
    }

    /**
     * setter for requiredProperties
     *
     * @param requiredProperties
     */
    public void setRequiredProperties(Set<PropertyType> requiredProperties) {
        this.requiredProperties = requiredProperties;
    }

    /**
     * setter for optionalProperties
     *
     * @param optionalProperties
     */
    public void setOptionalProperties(Set<PropertyType> optionalProperties) {
        this.optionalProperties = optionalProperties;
    }

    /**
     * setter for supportedOperations
     *
     * @param supportedOperations
     */
    public void setSupportedOperations(Set<Operations> supportedOperations) {
        this.supportedOperations = supportedOperations;
    }

    /**
     * setter for supportedOperations
     *
     * @param supportedOperations
     */
    public void setSupportedOperations(List<String> supportedOperations) {
        this.supportedOperations = convertManifestOperationsToMetadataOperations(supportedOperations);
    }

    private Set<Operations> convertManifestOperationsToMetadataOperations(
            List<String> supportedOperations) {
        Set<Operations> operations = new HashSet<>();
        for (String supportedOperation : supportedOperations) {
            operations.add(Operations.valueOf(supportedOperation.toUpperCase()));
        }
        return operations;
    }

    /**
     * returns the string represantation of the object
     *
     * @return String
     */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder("Connector: ");
        sb.append(name).append(" status: ").append(connectorStatus).append(" actorRef: ").append(actorRef);
        return sb.toString();
    }
}
