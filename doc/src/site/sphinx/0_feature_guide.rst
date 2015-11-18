=============
Feature guide
=============


- **Mix data from different datastores**: Crossdata provides an unified way to access to multiple datastores. At this time,
combining data among Apache Cassandra (Stratio Cassandra shortly), MongoDB and ElasticSearch is currently supported.
More accesses to different datastores will be available soon.

- **Native access to datastores**: When a query can be resolved natively in the datastore,
Crossdata will detect it and the query will be directly redirected to the datastore API in order to skip the unnecessary use of the Spark Cluster.
 This feature gives two advantages:

    - *Faster queries*: for queries that can be resolved natively, avoiding the usage of the Spark Cluster usually save a great amount of network traffic as data
    doesn't have to be spread and collected through the workers without performing any operation in the middle.
    - *Spark Cluster saturation relief*: avoiding the usage of the Spark Cluster let some queries not to be stopped waiting for having enough resources as they
    can go directly to the datastore and, also, that makes that the number of jobs struggling for the resources of the Spark Cluster is reduced.

- **ODBC/JDBC**: Crossdata also can be used via ODBC or JDBC. Thus, any BI tool can leverage the Crossdata features.
 An ODBC installer is provided and it has been tested with the main BI tools.

- **Unified API**: Even if you are accessing to multiple datastores, you only have to deal with only one API.This API is provided both Scala and Java.
 Moreover, a query builder is included to assist in the creation of SQL-like and several methods to get information related to metadata.

- **Improved and extended SparkContext**:
    - *One context, multiple datasources*: CrossdataContext is able to manage the access efficiently to all datasources,
     making optimizations and leveraging the characteristics of every datastore.
    - *Persistent metadata store*: Our metadata store let to persist the tables and other metadata information that the user wants to use frequently,
     avoiding to register all the table and metadata everytime that a context is started.
    - *Improved analyzer and optimizer*: Crossdata adds more complex and completed logic to the Spark planner with the aim of speeding up your queries.
     This logic detects more efficiently when a filter push-down can be applied and creates more effective execution trees so that the native capabilities of every datastore is used and network traffic is avoided.
    - *Extended SQL language*: the SparkSQL language has been expanded in order to give a wider range of analytics features and to add some operational
    command in a SQL-like manner.
    - *Security via Stratio GoSec*: GoSec is one of the module of the Stratio Platform and gives a simple way to add security to your
    cluster. Crossdata uses this project in order to protect the access to your data and also gives auditory capabilities.
    - *Metadata discovery*: Crossdata simplifies the use of your previous data through a metadata discovery system. Import all
    the tables or some of the tables already existent in your cluster without specifying the metadata.
    - *Avoid Spark cluster usage*: If your planning to use only native access to your data, Crossdata offers a way to use the system without
    starting the Spark Cluster. Therefore, you can start Crossdata in a light way when the usage of the Spark Cluster exceeds the expectations of the user.

- **P2P architecture**:
    - *High availability*: Start all the Crossdata servers that you need and don't worry about bottleneck or Single Point of Failure, Crossdata will take care of all.
    - *Scalability*: If your application requires heavy and intensive workload, just add more Crossdata servers on the fly to give more computational power to your cluster.
