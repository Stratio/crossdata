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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta2.common.api.generated.PropertiesType;
import com.stratio.meta2.common.api.generated.connector.SupportedOperationsType;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.Status;

public class ConnectorMetadata implements IMetadata {

    private final ConnectorName name;
    private final String version;
    private final Set<DataStoreName> dataStoreRefs;
    private final Set<PropertiesType> requiredProperties;
    private final Set<PropertiesType> optionalProperties;
    private final Set<Operations> supportedOperations;
    private Status status;
    private Serializable actorRef;

    public ConnectorMetadata(ConnectorName name, String version, Set<DataStoreName> dataStoreRefs,
            Set<PropertiesType> requiredProperties, Set<PropertiesType> optionalProperties,
            Set<SupportedOperationsType> supportedOperations) {
        this.name = name;
        this.version = version;
        this.dataStoreRefs = dataStoreRefs;
        this.requiredProperties = requiredProperties;
        this.optionalProperties = optionalProperties;
        this.supportedOperations = new HashSet<>();
        for(SupportedOperationsType type: supportedOperations){
            this.supportedOperations.add(type.getOperation());
        }
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

    public Set<PropertiesType> getRequiredProperties() {
        return requiredProperties;
    }

    public Set<PropertiesType> getOptionalProperties() {
        return optionalProperties;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Serializable getActorRef() {
        return actorRef;
    }

    public void setActorRef(Serializable actorRef) {
        this.status = Status.ONLINE;
        this.actorRef = actorRef;
    }

    /**
     * Determine if the connector supports a specific operation.
     * @param operation The required operation.
     * @return Whether it is supported.
     */
    public boolean supports(Operations operation){
        return supportedOperations.contains(operation);
    }

}
