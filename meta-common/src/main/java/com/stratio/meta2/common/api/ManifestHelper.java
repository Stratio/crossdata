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

package com.stratio.meta2.common.api;

import com.stratio.meta2.common.api.generated.connector.ConnectorType;
import com.stratio.meta2.common.api.generated.datastore.ClusterType;
import com.stratio.meta2.common.api.generated.datastore.DataStoreType;
import com.stratio.meta2.common.api.generated.datastore.HostsType;
import com.stratio.meta2.common.api.generated.datastore.PropertyType;

import java.io.Serializable;
import java.util.List;

public class ManifestHelper implements Serializable {

  private static final long serialVersionUID = -6979108221035957858L;

  public static String manifestToString(Manifest manifest){
    String result = null;
    if(manifest instanceof DataStoreType){
      return dataStoreManifestToString((DataStoreType) manifest);
    } else if(manifest instanceof ConnectorType){
      return connectorManifestToString((ConnectorType) manifest);
    }
    return result;
  }

  private static String dataStoreManifestToString(DataStoreType dataStoreType) {
    StringBuilder sb = new StringBuilder("DATASTORE");
    sb.append(System.lineSeparator());

    // NAME
    sb.append("Name: ").append(dataStoreType.getName()).append(System.lineSeparator());

    // VERSION
    sb.append("Version: ").append(dataStoreType.getVersion()).append(System.lineSeparator());

    // REQUIRED PARAMETERS
    sb.append("Required parameters: ").append(System.lineSeparator());
    sb.append("\t").append("Cluster: ").append(System.lineSeparator());
    ClusterType cluster = dataStoreType.getRequiredProperties().getCluster();
    sb.append("\t").append("\t").append("Name: ").append(cluster.getName()).append(System.lineSeparator());
    sb.append("\t").append("\t").append("Hosts: ").append(System.lineSeparator());
    List<HostsType> hostsList = cluster.getHosts();
    for(HostsType hosts: hostsList){
      sb.append("\t").append("\t").append("\t").append("Host: ").append(hosts.getHost()).append(System.lineSeparator());
      sb.append("\t").append("\t").append("\t").append("Port: ").append(hosts.getPort()).append(System.lineSeparator());
    }

    // OPTIONAL PROPERTIES
    sb.append("Optional properties: ").append(System.lineSeparator());
    List<PropertyType> propertiesList = dataStoreType.getOptionalProperties().getProperty();
    for(PropertyType propertyType: propertiesList){
      sb.append("\t").append("Property").append(System.lineSeparator());
      sb.append("\t").append("\t").append("Name: ").append(propertyType.getName()).append(System.lineSeparator());
    }

    // RESULT
    return sb.toString();
  }

  private static String connectorManifestToString(ConnectorType connectorType) {
    StringBuilder sb = new StringBuilder("CONNECTOR");
    sb.append(System.lineSeparator());

    // CONNECTOR NAME
    sb.append("ConnectorName: ").append(connectorType.getConnectorName()).append(System.lineSeparator());

    // DATA STORES NAME
    sb.append("DataStoresName: ").append(System.lineSeparator());
    sb.append("\t").append("Datastore: ").append(connectorType.getDataStoresName().getDatastore()).append(
        System.lineSeparator());

    // VERSION
    sb.append("Version: ").append(connectorType.getVersion()).append(System.lineSeparator());

    // REQUIRED PROPERTIES
    sb.append("Required properties: ").append(System.lineSeparator());
    List<com.stratio.meta2.common.api.generated.connector.PropertyType> propertiesList = connectorType.getRequiredProperties().getProperty();
    for(com.stratio.meta2.common.api.generated.connector.PropertyType property: propertiesList){
      sb.append("\t").append("Property: ").append(System.lineSeparator());
      sb.append("\t").append("\t").append("Name: ").append(property.getName()).append(
          System.lineSeparator());
    }

    // OPTIONAL PROPERTIES
    sb.append("Optional properties: ").append(System.lineSeparator());
    propertiesList = connectorType.getOptionalProperties().getProperty();
    for(com.stratio.meta2.common.api.generated.connector.PropertyType property: propertiesList){
      sb.append("\t").append("Property: ").append(System.lineSeparator());
      sb.append("\t").append("\t").append("Name: ").append(property.getName()).append(
          System.lineSeparator());
    }

    // SUPPORTED OPERATIONS
    sb.append("Supported operations: ").append(System.lineSeparator());
    List<String> operationsList = connectorType.getSupportedOperations().getOperation();
    for(String operation: operationsList){
      sb.append("\t").append("Operation: ").append(operation).append(System.lineSeparator());
    }

    // RESULT
    return sb.toString();
  }

  public static void checkName(String name) throws ManifestException {
    if(!name.matches("[a-zA-Z][a-zA-Z0-9_]*")){
      throw new ManifestException();
    }
  }

}
