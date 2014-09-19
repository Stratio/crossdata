/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.common.metadata;

import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.DataStoreName;
import com.stratio.meta2.common.statements.structures.selectors.Selector;

import java.io.Serializable;
import java.util.Map;

public class ClusterAttachedMetadata implements Serializable {
  private final ClusterName clusterRef;
  private final DataStoreName dataStoreRef;
  private final Map<Selector, Selector> properties;

  public ClusterAttachedMetadata(ClusterName clusterRef,
                                 DataStoreName dataStoreRef,
                                 Map<Selector, Selector> properties) {
    this.clusterRef = clusterRef;
    this.dataStoreRef = dataStoreRef;
    this.properties = properties;
  }

  public ClusterName getClusterRef() {
    return clusterRef;
  }

  public DataStoreName getDataStoreRef() {
    return dataStoreRef;
  }

  public Map<Selector, Selector> getProperties() {
    return properties;
  }
}
