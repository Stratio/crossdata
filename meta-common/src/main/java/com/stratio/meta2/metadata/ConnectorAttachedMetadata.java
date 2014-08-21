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

public class ConnectorAttachedMetadata {
  private final String connectorRef;
  private final String clusterRef;
  private final Map<String,String> properties;

  public ConnectorAttachedMetadata(String connectorRef, String clusterRef,
      Map<String, String> properties) {
    this.connectorRef = connectorRef;
    this.clusterRef = clusterRef;
    this.properties = properties;
  }

  public String getConnectorRef() {
    return connectorRef;
  }

  public String getClusterRef() {
    return clusterRef;
  }

  public Map<String, String> getProperties() {
    return properties;
  }
}
