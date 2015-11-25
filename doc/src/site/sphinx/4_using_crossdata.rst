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

TODO:

- APIs for writing custom transformations, interceptors and SQL Syntax–where applicable

1. APIs
================

1.1 Driver
----------

Driver:

syncQuery [explicar QueryBuilder]
asyncQuery
listDatabases
listTables
describeTable

1.2 Core
----------

Core: Same as spark [url] + XDContext function (dropTable, dropAllTable)


2. SQL
=========

A complete set of available operations are described `here <6_reference_guide.rst>`__

2.1 Cassandra Connector
-----------------

You can view some examples using the Cassandra Connector `here <connectors/connectcassandra_connector.rst>`__


2.2 MongoDB Connector
-----------------

You can view some examples using the MongoDB Connector `here <connectors/mongodb_connector.rst>`__

2.3 Elasticsearch Connector
-----------------------

You can view some examples using the ElasticSearch Connector `here <connectors/elasticsearch_connector.rst>`__

2.4 More Examples
-----------------

Join between C* and ES, etc.