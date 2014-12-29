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

package com.stratio.connector.inmemory;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.stratio.connector.inmemory.datastore.InMemoryDatastore;
import com.stratio.crossdata.common.connector.ConnectorClusterConfig;
import com.stratio.crossdata.common.connector.IConfiguration;
import com.stratio.crossdata.common.connector.IConnector;
import com.stratio.crossdata.common.connector.IMetadataEngine;
import com.stratio.crossdata.common.connector.IQueryEngine;
import com.stratio.crossdata.common.connector.IStorageEngine;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.InitializationException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.security.ICredentials;
import com.stratio.crossdata.communication.MetadataStruct;
import com.stratio.crossdata.connectors.ConnectorApp;

/**
 * InMemory connector that demonstrates the internals of a crossdata connector.
 * @see <a href="https://github.com/Stratio/crossdata/_doc/InMemory-Connector-Development-Tutorial.md">InMemory Connector
 * development tutorial</a>
 */
public class InMemoryConnector implements IConnector{

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(InMemoryConnector.class);

    /**
     * Map associating the {@link com.stratio.crossdata.common.data.ClusterName}s with
     * the InMemoryDatastores. This type of map usually links with the established connections.
     */
    private final Map<ClusterName, InMemoryDatastore> clusters = new HashMap<>();

    /**
     * Constant defining the required datastore property.
     */
    private static final String DATASTORE_PROPERTY = "TableRowLimit";

    @Override
    public String getConnectorName() {
        return "InMemoryConnector";
    }

    @Override
    public String[] getDatastoreName() {
        return new String[]{"InMemoryDatastore"};
    }

    @Override
    public void init(IConfiguration configuration) throws InitializationException {
        //The initialization method is called when the connector is launched, currently an
        //empty implementation is passed as it will be a future feature of Crossdata.
        LOG.info("InMemoryConnector launched");
    }

    @Override
    public void connect(ICredentials credentials, ConnectorClusterConfig config) throws ConnectionException {
        ClusterName targetCluster = config.getName();
        Map<String, String> options = config.getClusterOptions();
        LOG.info("clusterOptions: " + config.getClusterOptions().toString() + " connectorOptions: " + config.getConnectorOptions());
        if(!options.isEmpty() && options.get(DATASTORE_PROPERTY) != null){
            //At this step we usually connect to the database. As this is an tutorial implementation,
            //we instantiate the Datastore instead.
            InMemoryDatastore datastore = new InMemoryDatastore(Integer.valueOf(options.get(DATASTORE_PROPERTY)));
            clusters.put(targetCluster, datastore);
        }else{
            throw new ConnectionException("Invalid options, expecting TableRowLimit");
        }
    }

    @Override
    public void close(ClusterName name) throws ConnectionException {
        //This method usually closes the session with the given cluster and removes any relevant data.
        if(clusters.get(name) != null) {
            clusters.remove(name);
        } else {
            throw new ConnectionException("Cluster " + name + "does not exist");
        }
    }

    @Override
    public void shutdown() throws ExecutionException {
        LOG.info("Shutting down InMemoryConnector");
    }

    @Override
    public boolean isConnected(ClusterName name) {
        return clusters.get(name) != null;
    }

    @Override
    public IStorageEngine getStorageEngine() throws UnsupportedException {
        return new InMemoryStorageEngine(this);
    }

    @Override
    public IQueryEngine getQueryEngine() throws UnsupportedException {
        return new InMemoryQueryEngine(this);
    }

    @Override
    public IMetadataEngine getMetadataEngine() throws UnsupportedException {
        return new InMemoryMetadataEngine(this);
    }

    @Override public boolean UpdateMetadata(MetadataStruct metadata) {
        //TODO: implement proper method
        return true;
    }

    /**
     * Get the datastore associated to a given cluster.
     * @param cluster The cluster name.
     * @return A {@link com.stratio.connector.inmemory.datastore.InMemoryDatastore}.
     */
    protected InMemoryDatastore getDatastore(ClusterName cluster){
        return this.clusters.get(cluster);
    }

    /**
     * Run an InMemory Connector using a {@link com.stratio.crossdata.connectors.ConnectorApp}.
     * @param args The arguments.
     */
    public static void main(String [] args){
        InMemoryConnector inMemoryConnector = new InMemoryConnector();
        ConnectorApp connectorApp = new ConnectorApp();
        connectorApp.startup(inMemoryConnector);
    }
}
