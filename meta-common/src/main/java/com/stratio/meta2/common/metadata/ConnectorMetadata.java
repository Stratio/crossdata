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

package com.stratio.meta2.common.metadata;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta2.common.api.ManifestHelper;
import com.stratio.meta2.common.api.PropertyType;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.Status;
import com.stratio.meta2.common.statements.structures.selectors.Selector;

public class ConnectorMetadata implements IMetadata {

    private final ConnectorName name;
    private final String version;
    private final Set<DataStoreName> dataStoreRefs;

    private Set<ClusterName> clusterRefs;

    private Map<ClusterName, Map<Selector, Selector>> clusterProperties;

    private Status status;
    private String actorRef;

    private final Set<PropertyType> requiredProperties;
    private final Set<PropertyType> optionalProperties;
    private final Set<Operations> supportedOperations;

    public ConnectorMetadata(ConnectorName name, String version, Set<DataStoreName> dataStoreRefs,
            Set<ClusterName> clusterRefs, Map<ClusterName, Map<Selector, Selector>> clusterProperties,
            Set<PropertyType> requiredProperties, Set<PropertyType> optionalProperties,
            Set<Operations> supportedOperations) {
        this.name = name;
        this.version = version;
        this.dataStoreRefs = dataStoreRefs;
        this.clusterRefs = clusterRefs;
        this.clusterProperties = clusterProperties;
        this.requiredProperties = requiredProperties;
        this.optionalProperties = optionalProperties;
        this.supportedOperations = supportedOperations;
        this.status = Status.OFFLINE;
    }

    //TODO remove this constructor and fix test
    @Deprecated
    public ConnectorMetadata(ConnectorName name, String version, Set<DataStoreName> dataStoreRefs,
            Set<PropertyType> requiredProperties, Set<PropertyType> optionalProperties,
            Set<Operations> supportedOperations) {
        this.name = name;
        this.version = version;
        this.dataStoreRefs = dataStoreRefs;
        this.requiredProperties = requiredProperties;
        this.optionalProperties = optionalProperties;
        this.supportedOperations = supportedOperations;
        this.status = Status.OFFLINE;
    }

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
        this.supportedOperations = ManifestHelper.convertManifestOperationsToMetadataOperations(supportedOperations);
        this.status = Status.OFFLINE;
    }

    public ConnectorName getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Set<DataStoreName> getDataStoreRefs() {
        return dataStoreRefs;
    }

    public Set<PropertyType> getRequiredProperties() {
        return requiredProperties;
    }

    public Set<PropertyType> getOptionalProperties() {
        return optionalProperties;
    }

    public Set<Operations> getSupportedOperations() {
        return supportedOperations;
    }

    public Set<ClusterName> getClusterRefs() {
        return clusterRefs;
    }

    public void setClusterRefs(Set<ClusterName> clusterRefs) {
        this.clusterRefs = clusterRefs;
    }

    public Map<ClusterName, Map<Selector, Selector>> getClusterProperties() {
        return clusterProperties;
    }

    public void setClusterProperties(Map<ClusterName, Map<Selector, Selector>> clusterProperties) {
        this.clusterProperties = clusterProperties;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getActorRef() {
        return actorRef;
    }

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

}
