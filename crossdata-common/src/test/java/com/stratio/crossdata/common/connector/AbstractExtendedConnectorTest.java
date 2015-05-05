/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.connector;

import com.codahale.metrics.Metric;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectionStatus;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.InitializationException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.security.ICredentials;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import scala.Option;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by lcisneros on 5/05/15.
 */
public class AbstractExtendedConnectorTest {


    private AbstractExtendedConnector abstractExtendedConnector;
    private IConnectorApp connectorApp;

    @BeforeTest
    public void prepateTest(){
        connectorApp = mock(IConnectorApp.class);
        abstractExtendedConnector = new AbstractExtendedConnectorTestImpl(connectorApp);
    }

    @Test
    public void shouldRetunrConnectionStatus(){


        when(connectorApp.getConnectionStatus()).thenReturn(ConnectionStatus.CONNECTED);

        //Experimentation
        ConnectionStatus connectionStatus = abstractExtendedConnector.getConnectionStatus();

        //Expectations
        Assert.assertNotNull(connectionStatus);
        verify(connectorApp).getConnectionStatus();
    }

    @Test
    public void shouldRegisterMetric(){

        Metric metric = mock(Metric.class);
        when(connectorApp.registerMetric("name", metric)).thenReturn(metric);

        //Experimentation
        Metric registeredMetric = abstractExtendedConnector.registerMetric("name", metric);

        //Expectations
        Assert.assertNotNull(registeredMetric);
        verify(connectorApp).registerMetric("name", registeredMetric);
    }



    @Test
    public void shouldreturnCatalogs(){

        Option<List<CatalogMetadata>> catalogs = mock(Option.class);
        ClusterName cluster = mock(ClusterName.class);
        when(connectorApp.getCatalogs(cluster, 10)).thenReturn(catalogs);

        //Experimentation
        Option<List<CatalogMetadata>> result = abstractExtendedConnector.getCatalogs(cluster, 10);

        //Expectations
        Assert.assertNotNull(result);
        verify(connectorApp).getCatalogs(cluster, 10);
    }

    @Test
    public void shouldReturnTableMetadata(){

        TableName tableName = mock(TableName.class);
        ClusterName cluster = mock(ClusterName.class);
        Option<TableMetadata> tableMetadata = mock(Option.class);
        when(connectorApp.getTableMetadata(cluster, tableName, 10)).thenReturn(tableMetadata);

        //Experimentation
        Option<TableMetadata> result = abstractExtendedConnector.getTableMetadata(cluster, tableName, 10);

        //Expectations
        Assert.assertNotNull(result);
        verify(connectorApp).getTableMetadata(cluster, tableName, 10);
    }


    @Test
    public void shouldReturnCatalogMetadata(){


        Option<CatalogMetadata> catalogMetadata = mock(Option.class);
        CatalogName catalogName = mock(CatalogName.class);

        when(connectorApp.getCatalogMetadata(catalogName, 10)).thenReturn(catalogMetadata);

        //Experimentation
        Option<CatalogMetadata> result = abstractExtendedConnector.getCatalogMetadata(catalogName, 10);

        //Expectations
        Assert.assertNotNull(result);
        verify(connectorApp).getCatalogMetadata(catalogName, 10);
    }

    static class AbstractExtendedConnectorTestImpl extends AbstractExtendedConnector{

        public AbstractExtendedConnectorTestImpl(IConnectorApp connectorApp) {
            super(connectorApp);
        }


        @Override
        public String getConnectorName() {
            return null;
        }

        @Override
        public String[] getDatastoreName() {
            return new String[0];
        }

        @Override
        public void init(IConfiguration configuration) throws InitializationException {

        }

        @Override
        public void connect(ICredentials credentials, ConnectorClusterConfig config) throws ConnectionException {

        }

        @Override
        public void close(ClusterName name) throws ConnectionException {

        }

        @Override
        public void shutdown() throws ExecutionException {

        }

        @Override
        public boolean isConnected(ClusterName name) {
            return false;
        }

        @Override
        public IStorageEngine getStorageEngine() throws UnsupportedException {
            return null;
        }

        @Override
        public IQueryEngine getQueryEngine() throws UnsupportedException {
            return null;
        }

        @Override
        public IMetadataEngine getMetadataEngine() throws UnsupportedException {
            return null;
        }
    }
}


