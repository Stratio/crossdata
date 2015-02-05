# Developing an In Memory Connector for Crossdata #

This tutorial will guide you through the different steps required to implement a Stratio Crossdata connector. The 
tutorial takes the **InMemoryConnector** that is already included in the Crossdata packages, as an example. The 
tutorial is organized as follows:

1. [Preparing the environment](#preparing-the-environment)
2. [Implementing IConnector](#implementing-iconnector)
  1. [Defining the manifests](#defining-the-manifests)
  2. [Implementing IMetadataEngine](#implementing-imetadataengine)
  3. [Implementing IStorageEngine](#implementing-istorageengine)
  4. [Implementing IQueryEngine](#implementing-iqueryengine)
  5. [Throwing Exceptions](#throwing-exceptions)
3. [Running the connector](#running-the-connector)
 
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
single connector may access different clusters of the same datastore technology, and it may support several datastore 
technologies. In this example, the connector name is *InMemoryConnector*, and the datastore technology it access is 
named *InMemoryDatastore*. The usual naming convention is to use *<DatastoreTechnology>Connector* and 
*<DatastoreTechnology>* respectively. For instance, the connector that access Cassandra is named CassandraConnector.
 
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

Then, we should define how to establish the connections with the given datastores. This is done in the 
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
            throw new ConnectionException("Cluster " + name + "does not exist");
        }
    }
```

After that, we should provide the implementation of the *IStorageEngine*, *IMetadataEngine*, 
and *IQueryEngine*. Notice that, in our implementation, we pass a reference to the *InMemoryConnector* class due to the 
internal design of this class, but it may not be necessary in other connectors.

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

Finally, we must provide a main entry point for the connector class, so it can be executed either using maven, or as a 
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
 remaining required interfaces.

Defining the manifests
----------------------

In Crossdata there are two important notions to consider: *datastores*, and *connectors*. A datastore describes 
a particular storage technology describing its name, its properties, and its behaviours. This information is stored in
 a xml manifest file, and it is uploaded to Crossdata using the 
[ADD DATASTORE](Grammar.md#add-datastore)
sentence. For instance, the datastore manifest for Cassandra have the following definition:
 
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

In this example, the datastore is characterized by a property named Hosts, and a property named Port. With this 
information, the user can define as many clusters of type Cassandra as required. In the case of the 
InMemoryConnector given that we are not connecting to any external system, the manifest has the following 
content:

```xml

<?xml version="1.0" encoding="UTF-8"?>
<!-- This file contains the manifest that defines the underlying datastore. -->
<DataStore>
    <!-- Name of the target datastore. Be aware that a manifest may already exists for
    the datastore of your choice. -->
    <Name>InMemoryDatastore</Name>
    <Version>2.0.0</Version>
    <!-- Define the set of required properties the user will be asked for when defining
    a cluster of this datastore. -->

    <RequiredProperties>
        <Property>
            <PropertyName>TableRowLimit</PropertyName>
            <Description>Limit of rows allowed per table</Description>
        </Property>
    </RequiredProperties>

    <!-- List of datastore behaviours -->
    <Behaviors>
        <Behavior>EPHEMERAL</Behavior>
    </Behaviors>
</DataStore>
```

Crossdata will require the presence of these properties when the 
[ATTACH CLUSTER](Grammar.md#attach-cluster)
operation is executed, but it is responsibility of the connector itself to semantically check that the given parameters
can be used.

Once the datastore have been characterized, the next step is to define the properties and capabilities of the 
connector. As before, a XML manifest is used and uploaded to Crossdata with the
[ADD CONNECTOR](Grammar.md#add-connector)
sentence. In the case of the *InMemoryConnector*, the manifest has the following contents:

```xml

<?xml version="1.0" encoding="UTF-8"?>
<!-- This file contains the manifest for the connector. -->
<Connector>
    <!-- Name of the connector as it will be identified in Crossdata -->
    <ConnectorName>InMemoryConnector</ConnectorName>
    <!-- Define the list of datastore this connector is able to access. -->
    <DataStores>
        <DataStoreName>InMemoryDatastore</DataStoreName>
    </DataStores>
    <!-- Connector version -->
    <Version>0.0.1</Version>

    <!-- Define the set of required operations the user will be asked to input
    when attaching the connector -->

    <!--OptionalProperties>
        <Property>
            <PropertyName>TableRowLimit</PropertyName>
            <Description>Limit of rows allowed per table</Description>
        </Property>
    </OptionalProperties-->

    <!-- Define the list of operations supported by the connector.
    Check crossdata/doc/ConnectorOperations.md for more information. -->
    <SupportedOperations>
        <operation>CREATE_CATALOG</operation>
        <operation>DROP_CATALOG</operation>
        <operation>CREATE_TABLE</operation>
        <operation>DROP_TABLE</operation>
        <operation>TRUNCATE_TABLE</operation>
        <operation>INSERT</operation>
        <operation>PROJECT</operation>
        <operation>SELECT_OPERATOR</operation>
        <operation>SELECT_LIMIT</operation>
        <operation>FILTER_PK_EQ</operation>
        <operation>FILTER_PK_GT</operation>
        <operation>FILTER_PK_LT</operation>
        <operation>FILTER_PK_GET</operation>
        <operation>FILTER_PK_LET</operation>
        <operation>FILTER_NON_INDEXED_EQ</operation>
        <operation>FILTER_NON_INDEXED_GT</operation>
        <operation>FILTER_NON_INDEXED_LT</operation>
        <operation>FILTER_NON_INDEXED_GET</operation>
        <operation>FILTER_NON_INDEXED_LET</operation>
    </SupportedOperations>
</Connector>
```

The aforementioned manifest contains the connector name, the list of datastores it may access, 
the properties required by the connector when an
[ATTACH CONNECTOR](Grammar.md#attach-connector)
command is issued, and the list of operations supported by the connector. This last part will be analyzed by 
Crossdata on each query to determine which connector will be used to execute a particular query. For more information
 about the supported operation check the 
[operations documentation](ConnectorOperations.md).

For more information check the [datastore](https://github.com/Stratio/crossdata/blob/develop/crossdata-common/src/main/resources/com/stratio/crossdata/connector/DataStoreDefinition.xsd)
and [connector](https://github.com/Stratio/crossdata/blob/develop/crossdata-common/src/main/resources/com/stratio/crossdata/connector/ConnectorDefinition.xsd)
manifest schemas.

Implementing IMetadataEngine
----------------------------

The *IMetadataEngine* interface defines the set of operations related to metadata management that a connector may
provide to Crossdata. Notice that not all operations must be supported by the connector implementation, 
only those defined in the *SupportedOperations* section of the connector manifest. In our case, we will provide 
implementations for *createCatalog, *createTable*, *dropCatalog*, and *dropTable*. This connector will not support 
*alterTable*, *createIndex*, and *dropIndex*. If a future version implements any other 
operation, the connector manifest should be modified accordingly. As an example, consider the *createTable* method:


```java

    @Override
    public void createTable(ClusterName targetCluster, TableMetadata tableMetadata)
            throws ConnectorException {
        LOG.info("Creating table " + tableMetadata.getName().getQualifiedName() + " on " + targetCluster);
        InMemoryDatastore datastore = connector.getDatastore(targetCluster);
        if(datastore != null){
            String catalogName = tableMetadata.getName().getCatalogName().getQualifiedName();
            String tableName = tableMetadata.getName().getName();

            String [] columnNames = new String[tableMetadata.getColumns().size()];
            Class [] columnTypes = new Class[tableMetadata.getColumns().size()];

            int index = 0;
            for(Map.Entry<ColumnName, ColumnMetadata> column : tableMetadata.getColumns().entrySet()){
                columnNames[index] = column.getKey().getName();
                columnTypes[index] = column.getValue().getColumnType().getDbClass();
                index++;
            }

            List<String> primaryKey = new ArrayList<>();
            for(ColumnName column : tableMetadata.getPrimaryKey()){
                primaryKey.add(column.getName());
            }

            try {
                //Create catalog if not exists
                if(!datastore.existsCatalog(catalogName)){
                    datastore.createCatalog(catalogName);
                }
                datastore.createTable(catalogName, tableName, columnNames, columnTypes, primaryKey);
            } catch (Exception e) {
                throw new ExecutionException(e);
            }

        }else{
            throw new ExecutionException("No datastore connected to " + targetCluster);
        }
    }

```

The first step is to determine whether this connector has an actual connection with the datastore. In a classical
 approach this would involve obtaining the connection previously established through the connect method. After that, we
 transform the Crossdata *TableMetatada* structure into the classes required by our datastore and execute the 
 equivalent createTable statement using the previously obtained connection.
 
It is important to highlight that the abstraction of what a table is for a particular connector needs to be decided 
by the developer creating the connector. For example, a table depending on the connector we are creating may be 
translated in an IRC channel, a directory, a tag for future queries, etc. From the point of view of Crossdata the 
only requirement is to be consistent inside a datastore about the representation of high-level concepts such as 
catalogs, or tables.

Implementing IStorageEngine
---------------------------

The *IStorageEngine* defines the set of operations that are related to storing data in a datastore. In the case of the
 *InMemoryConnector*, we implement *insert*, *batch insert*, and *truncate* as specified in the connector manifest. As
  an practical example, consider the *insert* method:
  
```java

    @Override
    public void insert(ClusterName targetCluster, TableMetadata targetTable, Row row)
            throws ConnectorException {
        InMemoryDatastore datastore = connector.getDatastore(targetCluster);
        if(datastore != null){
            String catalogName = targetTable.getName().getCatalogName().getQualifiedName();
            String tableName = targetTable.getName().getName();
            Map<String, Object> toAdd = new HashMap<>();
            for(Map.Entry<String, Cell> col : row.getCells().entrySet()){
                toAdd.put(col.getKey(), col.getValue().getValue());
            }

            try {
                datastore.insert(catalogName, tableName, toAdd);
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        }else{
            throw new ExecutionException("No datastore connected to " + targetCluster);
        }
    }    
```

Similarly to the *IMetadataEngine* operations, the insert method transforms the data contained in the Crossdata Row 
class into the ones required by the datastore interface, and triggers the insertion.

Implementing IQueryEngine
-------------------------

The *IQueryEngine* defines the operations required to execute a
[SELECT](Grammar.md#select).
Two methods are provided depending on the type of queries supported. The *execute* method is currently used for 
synchronous queries, while the *asyncExecute* method is used for streaming selects. If the query introduced by the 
user contains a **WITH WINDOW** clause, the *asyncExecute* method will be invoked. In the case of the 
InMemoryConnector, as it does not support streaming queries, it only provides an implementation for the *execute* 
method.

```java

    @Override
    public QueryResult execute(LogicalWorkflow workflow) throws ConnectorException {

        List<Object[]> results = null;

        Project projectStep = null;
        Select selectStep = null;

        //Get the project and select steps.
        try {
            projectStep = Project.class.cast(workflow.getInitialSteps().get(0));
            selectStep = Select.class.cast(workflow.getLastStep());
        }catch(ClassCastException e){
            throw new ExecutionException("Invalid workflow received", e);
        }

        List<InMemoryRelation> relations = getInMemoryRelations(projectStep.getNextStep());
        int limit = getLimit(projectStep.getNextStep());
        String catalogName = projectStep.getCatalogName();
        String tableName = projectStep.getTableName().getName();

        InMemoryDatastore datastore = connector.getDatastore(projectStep.getClusterName());
        if(datastore != null){
            List<String> outputColumns = new ArrayList<>();
            for(ColumnName name : selectStep.getColumnOrder()){
                outputColumns.add(name.getName());
            }
            try {
                results = datastore.search(catalogName, tableName, relations, outputColumns);
            } catch (Exception e) {
                throw new ExecutionException("Cannot perform execute operation", e);
            }
        }else{
            throw new ExecutionException("No datastore connected to " + projectStep.getClusterName());
        }
        return toCrossdataResults(selectStep, limit, results);
    }    
```

For each incoming query in Crossdata, the query is analyzed, checked and processed in order to define an 
*ExecutionWorkflow*. In the case of the *SELECT* statement, the *ExecutionWorkflow* contains a *LogicalWorkflow* that
 represents the workflow to be executed in order to obtain the required results. For more information about logical 
 workflow check the
[Logical Workflows explained](Logical-Workflows-Explained.md)
document.

In our case, given that we do not support *UnionStep*, we can take the *Project* and *Select* operator to build the 
query that will be sent to the underlying datastore. Notice that the results returned by our datastore need to be 
transformed in a valid *ResultSet* in order to return a *QueryResult*. It is important to highlight that the returned
 results need to have the column in the order and with the names specified in the *Select* operator.

Throwing Exceptions
-------------------

All operations defined in the different interfaces throw **ConnectorException** in case of failure. When an exception
 is thrown, it is capture by the wrapping actor, and the error is propagated to the Crossdata server that sent the 
 request. That server will then forward the exception to the user issuing the request. The **ConnectorException** 
 hierarchy contains several subclasses: *ConnectionException*, *InitializationException*, *UnsupportedException*, 
 *ExecutionException*, and *CriticalExecutionException*.
 
| Option | Description |
|--------|------------|
| ConnectionException | If the connection with the datastore cannot be established. |
| InitializationException | If the connector initialization fails. |
| UnsupportedException | If the invoked method is not implemented by the connector. |
| ExecutionException | If the operation to be executed fails. |
| CriticalExecutionException | If the operation to be executed fails, and the connector is not longer usable. In this situation the connector service is expected to be rebooted. |

Running the Connector
=====================

The connector can be started in two different ways:

1. Running the connector tests:
    
    > mvn clean verify -DconnectorJar="[CrossdataPath]/crossdata-connector-inmemory/target/crossdata-connector
    -inmemory-0.2.0.jar" -DconnectorDefinition="[CrossdataPath]/crossdata-connector-inmemory/target/crossdata
    -connector-inmemory-0.2.0/conf/InMemoryConnector.xml" -DclusterOptions="[TableRowLimit-100]"
    -DconnectorCluster="TestCluster" -DconnectorMainClass="com.stratio.connector.inmemory.InMemoryConnector"
    
2. Starting the connector with maven:

    > mvn -pl crossdata-connector-inmemory exec:java -DskipTests -Dexec.mainClass="com.stratio.connector.inmemory.InMemoryConnector" 
    
