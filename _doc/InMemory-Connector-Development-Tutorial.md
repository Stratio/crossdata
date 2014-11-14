# Developing an In Memory Connector for Crossdata #

This tutorial will guide you through the different steps required to implement a Stratio Crossdata connector. The 
tutorial takes as an example the **InMemoryConnector** already include in the Crossdata packages as an example. The 
tutorial is organized as follows:

1. [Preparing the environment](#preparing-the-environment)
2. [Implementing IConnector](#implementing-iconnector)
  1. [Defining the manifests](#defining-the-manifests)
  2. [Implementing IMetadataEngine](#implementing-imetadataengine)
  3. [Implementing IStorageEngine](#implementing-istorageengine)
  4. [Implementing IQueryEngine](#implementing-iqueryengine)
 
Preparing the environment
=========================

1. Download the latest version of Crossdata

    > $ git clone https://github.com/Stratio/crossdata.git

2. Fork the [crossdata-connector-skeleton](https://github.com/Stratio/crossdata-connector-skeleton) in your account
3. Load the skeleton project in your IDE or choice (we have tested it on IntelliJ)

Implementing IConnector
=======================

In order to create a Crossdata connector, you must provide an implementation of the *IConnector* interface. This 
interface defines the basic set of operations required to connect the connector with the datastore of your choice and
 it will be the entry point to the connector system. In this example, the class 
 *com.stratio.connector.inmemory.InMemoryConnector* provides the implementation.
 
First, we should define the name of the connector and the name of the datastores supported by the connector. This 
information will be used by Crossdata to validate the different logical connections in the system. Remember that a 
single connector may access different clusters of the same datastore technology and it may support several datastore 
technologies. In this example, the connector name is *InMemoryConnector* and the datastore technology it access is 
named *InMemoryDatastore*. The usual naming convention is to use *<DatastoreTechnology>Connector* and 
*<DatastoreTechnology>* respectively. For instance the connector that access Cassandra is named CassandraConnector.
 
```java

    @Override
    public String getConnectorName() {
        return "InMemoryConnector";
    }

    @Override
    public String[] getDatastoreName() {
        return new String[]{"InMemoryDatastore"};
    }
```

Then, we should define how are we going to establish the connections with the given datastores. This is done in the 
connect and close methods.

```java

    @Override
    public void connect(ICredentials credentials, ConnectorClusterConfig config) throws ConnectionException {
        ClusterName targetCluster = config.getName();
        Map<String, String> options = config.getOptions();

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
        }else{
            throw new ConnectionException("Cluster " + name + "does not exists");
        }
    }
```

After that, we should provide the implementation of the *IStorageEngine*, *IMetadataEngine*, 
and *IQueryEngine*. Notice that in our implementation, we pass a reference to the *InMemoryConnector* class due to the 
internal design of this class but it may not be necessary in other connectors.

```java

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
```

Finally, we must provide a main entry point for the connector class so it can be executed either using maven or as a 
service.

```java

    /**
     * Run an InMemory Connector using a {@link com.stratio.crossdata.connectors.ConnectorApp}.
     * @param args The arguments.
     */
    public static void main(String [] args){
        InMemoryConnector inMemoryConnector = new InMemoryConnector();
        ConnectorApp connectorApp = new ConnectorApp();
        connectorApp.startup(inMemoryConnector);
    }
```

In order to facilitate the connector development, we provide a *ConnectorApp* class that works as a wrapper for the 
execution of a connector. This class abstracts the inner workings of the Akka communication between the Crossdata 
server and the Actor receiving messages for the connector. In this way, the Crossdata server will automatically detect 
when a connector starts and joins the internal Akka cluster. It will also trigger the required operations when a user
 query is received from a Crossdata server and will transfer the results back to the server when the query have been 
 executed.
 
Once we have a basic skeleton for the main class, we should define the connector capabilities before implementing the
 remainder of the required interfaces.

Defining the manifests
----------------------

In Crossdata there are two important notions to consider: *datastores* and *connectors*. A datastore describes 
a particular storage technology describing its name, its properties and its behaviours. This information is stored in
 a xml manifest file and it is uploaded to Crossdata using the ADD DATASTORE sentence. For instance, 
 the datastore manifest for Cassandra have the following definition:
 
```xml

<?xml version="1.0" encoding="UTF-8"?>
<DataStore>
    <Name>Cassandra</Name>
    <Version>2.0.0</Version>
    <RequiredProperties>
        <Property>
            <PropertyName>Hosts</PropertyName>
            <Description>Host list with its own port. Example: [host1,host2,host3]</Description>
        </Property>
        <Property>
            <PropertyName>Port</PropertyName>
            <Description>Cassandra Port</Description>
        </Property>
    </RequiredProperties>
    <Behaviors>
        <Behavior>UPSERT_ON_INSERT</Behavior>
    </Behaviors>
</DataStore>
```

In this example, the datastore is characterized by a property named Hosts and a property named Port. With this 
information, the user can define as many clusters of type Cassandra as required. Crossdata will 
require the presence of these properties when the ATTACH CLUSTER operation is executed but it is responsibility of 
the connector itself to semantically check that the given parameters can be used.

For more information check the [datastore](https://github.com/Stratio/crossdata/blob/develop/crossdata-common/src/main/resources/com/stratio/crossdata/connector/DataStoreDefinition.xsd)
and [connector](https://github.com/Stratio/crossdata/blob/develop/crossdata-common/src/main/resources/com/stratio/crossdata/connector/ConnectorDefinition.xsd)
manifest schemas.

Implementing IMetadataEngine
----------------------------

Implementing IStorageEngine
---------------------------

Implementing IQueryEngine
-------------------------

