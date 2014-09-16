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

import com.stratio.meta2.common.api.generated.datastore.OptionalPropertiesType;
import com.stratio.meta2.common.api.generated.datastore.RequiredPropertiesType;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.DataStoreName;

import java.util.HashMap;
import java.util.Map;

public class DataStoreMetadata implements IMetadata {
  private final DataStoreName name;
  private final String version;
  private final RequiredPropertiesType requiredProperties;
  private final OptionalPropertiesType othersProperties;
  private final Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs;

  public DataStoreMetadata(DataStoreName name, String version, RequiredPropertiesType requiredProperties,
                           OptionalPropertiesType othersProperties) {
    this.name = name;
    this.version = version;
    this.requiredProperties = requiredProperties;
    this.othersProperties = othersProperties;
    this.clusterAttachedRefs = new HashMap<>();
  }

  public DataStoreName getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public RequiredPropertiesType getRequiredProperties() {
    return requiredProperties;
  }

  public OptionalPropertiesType getOthersProperties() {
    return othersProperties;
  }

  public Map<ClusterName, ClusterAttachedMetadata> getClusterAttachedRefs() {
    return clusterAttachedRefs;
  }
}
