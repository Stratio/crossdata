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

package com.stratio.meta2.metadata;

import java.util.Map;
import java.util.Set;

public class ClusterMetadata implements IMetadata {
  private final String name;

  private final String dataStoreRef;

  private final Map<String, Object> options;

  private final Set<String> connectorRefs;


  public ClusterMetadata(String name, String dataStoreRef, Map<String, Object> options,
      Map<String, TableMetadata> tables, Set<String> connectorRefs) {
    this.name = name;
    this.options = options;
    this.dataStoreRef = dataStoreRef;
    this.connectorRefs = connectorRefs;
  }



  public String getName() {
    return name;
  }


  public Map<String, Object> getOptions() {
    return options;
  }

  public Set<String> getConnectorRefs() {
    return connectorRefs;
  }

  public String getDataStoreRef() {
    return dataStoreRef;
  }
}
