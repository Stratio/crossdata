===============
Using Crossdata
===============

Table of Contents
*****************

-  `1) APIS <#apis>`__

   -  `1.1) Driver <#driver>`__
   -  `1.2) Core <#core>`__

-  `2) SQL <#sql>`__

   -  `2.1) Cassandra Connector <#cassandra-connector>`__
   -  `2.2) MongoDB Connector <#mongodb-connector>`__
   -  `2.3) Elasticsearch Connector <#elasticsearch-connector>`__
   -  `2.4) More examples <#more-examples>`__



If you want to test Crossdata you can get our Sandbox follow the instructions of this
`link <Sandbox.rst>`__ (Coming soon)


1. APIs
================

1.1 Driver
----------

- **syncQuery**: Executes a SQL sentence in a synchronous way. Returns a list of rows with the result of the query. Params are:
    - *sqlCommand*: The SQL Command.
    - *timeout*: The timeout in seconds.
    - *retries*: Number of retries if the timeout was exceeded.

- **asyncQuery**:  Executes a SQL sentence in a synchronous way. Returns a list of rows with the result of the query. Params are:
    - *sqlCommand*: The SQL Command.
    - *timeout*: The timeout in seconds.
    - *retries*: Number of retries if the timeout was exceeded.

- **listTables**: Gets a list of tables from a database or all if the database is None. Returns a sequence of tables and the database. Params are:
    - *databaseName*: The name of the database.

- **describeTable**: Gets the metadata from a specific table. Returns a sequence with the metadata fields of the table. Params are:
    - *database*: The database of the table.
    - *tableName*: The name of the table.

1.2 Core
----------

XDContext extends SqlContext, so you can perform every operation of the `SQLContext <https://spark.apache.org/docs/1.5.1/api/scala/index.html#org.apache.spark.package>`__.

XDContext also add new functions described as follows:

- **importTables**: Imports tables from a DataSource in the persistent catalog.
    - *datasource*: Datasource name.
    - *opts*: The options maps with the specific datasource options, check datasource documentation for more info. 

- **dropTable**: Drops the table in the persistent catalog. It applies only to metadata, so data do not be deleted.Params are:
    - *tableIdentifier*: the table to be dropped


- **dropAllTables**: Drops all the tables in the persistent catalog. It applies only to metadata, so data do not be deleted.

2. SQL
=========

A complete set of available operations are described `here <6_reference_guide.rst>`__

2.1 Cassandra Connector
------------------------

You can view some examples using the Cassandra Connector `here <connectors/cassandra_connector.rst>`__


2.2 MongoDB Connector
----------------------

You can view some examples using the MongoDB Connector `here <connectors/mongodb_connector.rst>`__

2.3 Elasticsearch Connector
----------------------------

You can view some examples using the ElasticSearch Connector `here <connectors/elasticsearch_connector.rst>`__

2.4 More Examples
-----------------

You can perform queries that involves more than one datastores. The next example shows a join between Cassandra and ElasticSearch.

First of all, we need to register the tables as follows::

    xdContext.sql(
            s"""|CREATE TEMPORARY TABLE studentsCassandra
                |USING com.stratio.crossdata.connector.cassandra
                |OPTIONS (
                |spark_cassandra_connection_host '127.0.0.1',
                |keyspace 'highschool',
                |table 'students',
                |cluster 'Test Cluster'
                |)
             """.stripMargin.replaceAll("\n", " "))

    xdContext.sql(
        s"""|CREATE TEMPORARY TABLE studentsES
            |(id INT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN, birthday DATE)
            |USING com.stratio.crossdata.connector.elasticsearch
            |OPTIONS (
            |resource 'highschool/students',
            |es.node 'localhost',
            |es.port '9200',
            |es.nativePort '9300',
            |es.cluster 'elasticCluster'
            |)
         """.stripMargin.replaceAll("\n", " "))


And now you can performs queries in a natural way::

    xdContext.sql(s"SELECT * FROM studentsES JOIN studentsCassandra ON studentsES.id = studentsCassandra.id")


