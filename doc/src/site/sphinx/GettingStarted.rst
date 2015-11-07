Getting started
***************
Here is an example of Crossdata with a Cassandra Connector as an access to a Cassandra data store.

First of all `Stratio Cassandra <https://github.com/Stratio/stratio-cassandra>`_ is needed and must be installed and
running.

At this point Crossdata server must be running and starting Crossdata shell is needed. In this shell we will create
a new catalog, with a new table, and afterwards we will make a Select query.

Now, we need the `Cassandra Connector <https://github.com/Stratio/stratio-connector-cassandra>`_, to install it::

    > mvn crossdata-connector:install

And then, to run it::

    > target/stratio-connector-cassandra-[version]/bin/stratio-connector-cassandra-[version] start


**NOTE:** All the connectors have to be started once CrossdataServer is already running!

Now, from the Crossdata Shell we can write the following commands:

Attach cluster on a data store. The data store name must be the same as the defined in the data store manifest (see next table). 
::

    xdsh:user>  ATTACH CLUSTER <cluster_name> ON DATASTORE <datastore_name> WITH OPTIONS {'Hosts': '[<ipHost_1,ipHost_2,...ipHost_n>]', 'Port': <cassandra_port>};


Here is a table with the available data stores:

+----------------+----------------------------------------------+
| datastore_name | connector_name available for each data store |
+================+==============================================+
| Cassandra      | CassandraConnector and SparkSQLConnector     |
+----------------+----------------------------------------------+
| elasticsearch  | elasticsearchconnector and SparkSQLConnector |
+----------------+----------------------------------------------+
| Mongo          | MongoConnector and SparkSQLConnector         |
+----------------+----------------------------------------------+
| Decision       | DecisionConnector                            |
+----------------+----------------------------------------------+
| hdfs           | hdfsconnector and SparkSQLConnector          |
+----------------+----------------------------------------------+
| jdbc           | SparkSQLConnector                            |
+----------------+----------------------------------------------+
| json           | SparkSQLConnector                            |
+----------------+----------------------------------------------+

**NOTE:** Native connectors like CassandraConnector cover basic functionalities (queries, insert, etc), and SparkSQLConnector provides additional operations not supported by the data store.

Attach the connector to the previously defined cluster. The connector name must match the one defined in the
Connector Manifest (see previous table), and the cluster name must match the previously defined in the ATTACH CLUSTER command.::

    xdsh:user>  ATTACH CONNECTOR <connector name> TO <cluster name> WITH OPTIONS {'DefaultLimit': '1000'};


At this point, we can start to send queries, that Crossdata executes with the specified connector.::


    xdsh:user> CREATE CATALOG catalogTest;

    xdsh:user> USE catalogTest;

    xdsh:user> CREATE TABLE tableTest ON CLUSTER <cluster name> (id int PRIMARY KEY, name text);

    xdsh:user> INSERT INTO tableTest(id, name) VALUES (1, 'stratio');

    xdsh:user> SELECT * FROM tableTest;

