# Developing an In Memory Connector for Crossdata #

This tutorial will guide you through the different steps required to implement a Stratio Crossdata connector. The 
tutorial is organized as follows:

 1. [Preparing the environment](#preparing-the-environment)
 2. [Implementing IConnector](#implementing-iconnector)
 2.1 [Defining the manifests](#defining-the-manifests)
 2.2 [Implementing IMetadataEngine](#implementing-imetadataengine)
 2.3 [Implementing IStorageEngine](#implementing-istorageengine)
 2.4 [Implementing IQueryEngine](#implementing-iqueryengine)
 
Preparing the environment
=========================

1. Download the latest version of Crossdata

    > $ git clone https://github.com/Stratio/crossdata.git

2. Fork the [crossdata-connector-skeleton](https://github.com/Stratio/crossdata-connector-skeleton) in your account
3. Load the skeleton project in your IDE or choice (we have tested it on IntelliJ)

Implementing IConnector
=======================

In order to create a Crossdata connector, you must provide an implementation of the IConnector interface. This 
interface defines the basic set of operations required to connect the connector with the datastore of your choice and
 it will be the entry point to the connector system. In this example, the class "com.stratio.connector.inmemory
 .InMemoryConnector" provides the implementation.
 
First, we should define the name of the connector and the name of the datastores supported by the connector. This 
information will be used by Crossdata to validate the different logical connections in the system. Remember that a 
single connector may access different clusters of the same datastore technology and it may support several datastore 
technologies. In this example, the connector name is "InMemoryConnector" and the datastore technology it access is 
named "InMemoryDatastore". The usual naming convention is to use "<DatastoreTechnology>Connector" and 
"<DatastoreTechnology>" respectively. For instance the connector that access Cassandra is named CassandraConnector.
 
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
            throw new ConnectionException("Invalid options, expeting TableRowLimit");
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

The first step when developing a connector is to determine which class will 

 
