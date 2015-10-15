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
- Verify the Crossdata Server configuration_.
- Ensure that your Spark cluster has the Crossdata-Core and Data Sources providers jars distributed across all the nodes.
- Ensure full bidirectional network connectivity between the Server machine, the Spark Cluster and the Datastore machines.
- For distributed Crossdata servers, you need to change some parameters in the Crossdata servers configuration_.



ODBC:
-----
- Verify the Crossdata server is up and running.
- Ensure full bidirectional network connectivity between local ODBC machine and the Crossdata Server.


.. _configuration: 2_getting_started.html


