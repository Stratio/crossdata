=======================
Crossdata in production
=======================

The following checklists provide recommendations that will help you avoid issues in your production Crossdata deployment.


Operations Checklist
====================

Standalone:
-----------

- Ensure that your Spark cluster has the Crossdata-Core and Data Sources providers jars distributed across all the nodes.
- Use Crossdata Context instead Spark ones.


SERVER:
-------
- Verify the Crossdata Server configuration_. In especial server-application.conf options, see "Server Configuration for Distributed Environment" for details.
- Ensure that your Spark cluster has the Crossdata-Core and Data Sources providers jars distributed across all the nodes, in $SPARK-HOME/lib/ :
    - crossdata-core-{version}.jar
    - crossdata-cassandra-{version}.jar
    - crossdata-elasticsearch-{version}.jar
    - crossdata-mongodb-{version}.jar
- Ensure full bidirectional network connectivity between the Server machine, the Spark Cluster and the Datastore machines:
    - The port *13420* used by Crossdata crossdata-server ir required to be open.
    - The Datastore specifics ports must be accessible (27000 for MongoDB, 9200 & 9300 for Elastic, 7000 for Cassandra, etc)
- For distributed Crossdata servers, you need to change some parameters in the Crossdata servers configuration_.


ODBC:
-----
TODO: Documentation for ODBC is required.
- Verify the Crossdata server is up and running.
- Ensure full bidirectional network connectivity between local ODBC machine and the Crossdata Server.


.. _configuration: 3_configuration.rst

