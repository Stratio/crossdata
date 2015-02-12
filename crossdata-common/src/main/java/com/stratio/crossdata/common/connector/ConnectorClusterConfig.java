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

package com.stratio.crossdata.common.connector;

import java.io.Serializable;
import java.util.Map;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;

/**
 * Configuration used by a connector to establish a connection to a specific cluster.
 */
public class ConnectorClusterConfig implements Serializable {

    private static final long serialVersionUID = 8865765972661082002L;
    /**
     * Name of the target cluster.
     */
    private final ClusterName name;

    private DataStoreName dataStoreName;

    /**
     * Map of connector options required by a connector in order to be able to establish a connection
     * to an existing datastore cluster.
     */
    private final Map<String, String> connectorOptions;

    /**
     * Map of cluster options required by a connector in order to be able to establish a connection to an
     * existing datastore cluster.
     */
    private Map<String, String> clusterOptions;

    /**
     * Class constructor.
     *
     * @param name    Name of the target cluster.
     * @param connectorOptions Map of connector options.
     * @param clusterOptions Map of cluster options.
     */
    public ConnectorClusterConfig(
            ClusterName name,
            Map<String, String> connectorOptions,
            Map<String, String> clusterOptions) {
        this.name = name;
        this.connectorOptions = connectorOptions;
        this.clusterOptions = clusterOptions;
    }

    public DataStoreName getDataStoreName() {
        return dataStoreName;
    }

    public void setDataStoreName(DataStoreName dataStoreName) {
        this.dataStoreName = dataStoreName;
    }

    /**
     * Get the connector options.
     * @return A map of options.
     */
    public Map<String, String> getConnectorOptions() {
        return connectorOptions;
    }

    /**
     * Get the cluster options.
     * @return A map of options.
     */
    public Map<String, String> getClusterOptions() {
        return clusterOptions;
    }

    /**
     * Get the name of the target cluster.
     *
     * @return A {@link com.stratio.crossdata.common.data.ClusterName}.
     */
    public ClusterName getName() {
        return name;
    }

}
