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

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.InitializationException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.security.ICredentials;
import com.stratio.crossdata.common.data.ClusterName;

/**
 * Common interface for META connectors. A connectormanager provides implementations for storage and query
 * engines. Notice that connectors do not need to provide both functionalities at the same time.
 */
public interface IConnector {

    /**
     * Get the name of the actor.
     *
     * @return A name.
     */
    public String getConnectorName();

    /**
     * Get the names of the datastores required by the connectormanager.
     * Several connectors may declare the same datastore name.
     *
     * @return The names.
     */
    public String[] getDatastoreName();

    /**
     * Initialize the connectormanager service.
     *
     * @param configuration The configuration.
     * @throws InitializationException If the connectormanager initialization fails.
     */
    public void init(IConfiguration configuration) throws InitializationException;

    /**
     * Connect to a datastore using a set of options.
     *
     * @param credentials The required credentials
     * @param config      The cluster configuration.
     * @throws ConnectionException If the connection could not be established.
     */
    public void connect(ICredentials credentials, ConnectorClusterConfig config)
            throws ConnectionException;

    /**
     * Close the connection with the underlying datastore.
     *
     * @throws ConnectionException If the close operation cannot be performed.
     */
    public void close(ClusterName name) throws ConnectionException;

    /**
     * Shutdown send a sing to terminate all operations of the connectormanager and then close all clusters connection.
     *
     * @throws ExecutionException If the shutdown operation cannot be performed.
     */
    public void shutdown() throws ExecutionException;

    /**
     * Retrieve the connectivity status with the datastore.
     *
     * @return Whether it is connected or not.
     */
    public boolean isConnected(ClusterName name);

    /**
     * Get the storage engine.
     *
     * @return An implementation of {@link com.stratio.crossdata.common.connector.IStorageEngine}.
     * @throws UnsupportedException If the connectormanager does not provide this functionality.
     */
    public IStorageEngine getStorageEngine() throws UnsupportedException;

    /**
     * Get the query engine.
     *
     * @return An implementation of {@link com.stratio.crossdata.common.connector.IQueryEngine}.
     * @throws UnsupportedException If the connectormanager does not provide this functionality.
     */
    public IQueryEngine getQueryEngine() throws UnsupportedException;

    /**
     * Get the metadata engine.
     *
     * @return An implementation of {@link com.stratio.crossdata.common.connector.IMetadataEngine}.
     * @throws UnsupportedException If the connectormanager does not provide this functionality.
     */
    public IMetadataEngine getMetadataEngine() throws UnsupportedException;
}
