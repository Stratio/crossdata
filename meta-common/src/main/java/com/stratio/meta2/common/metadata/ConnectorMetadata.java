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

import com.stratio.meta2.common.api.generated.connector.OptionalPropertiesType;
import com.stratio.meta2.common.api.generated.connector.RequiredPropertiesType;
import com.stratio.meta2.common.api.generated.connector.SupportedOperationsType;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.data.Status;

import java.util.Set;

import akka.actor.ActorRef;

public class ConnectorMetadata implements IMetadata {

  private final ConnectorName name;
  private final String version;
  private final Set<DataStoreName> dataStoreRefs;
  private final RequiredPropertiesType requiredProperties;
  private final OptionalPropertiesType optionalProperties;
  private final SupportedOperationsType supportedOperations;
  private Status status;
  private ActorRef actorRef;

  public ConnectorMetadata(ConnectorName name, String version, Set<DataStoreName> dataStoreRefs,
                           RequiredPropertiesType requiredProperties, OptionalPropertiesType optionalProperties,
                           SupportedOperationsType supportedOperations) {
    this.name = name;
    this.version = version;
    this.dataStoreRefs = dataStoreRefs;
    this.requiredProperties = requiredProperties;
    this.optionalProperties = optionalProperties;
    this.supportedOperations = supportedOperations;
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

  public RequiredPropertiesType getRequiredProperties() {
    return requiredProperties;
  }

  public OptionalPropertiesType getOptionalProperties() {
    return optionalProperties;
  }

  public SupportedOperationsType getSupportedOperations() {
    return supportedOperations;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public void setActorRef(akka.actor.ActorRef actorRef) {
    this.status = Status.ONLINE;
    this.actorRef = actorRef;
  }

  public ActorRef getActorRef() {
    return actorRef;
  }
}
