About
*****

|https://travis-ci.org/databricks/spark-csv|_

.. |https://travis-ci.org/databricks/spark-csv| image:: https://api.travis-ci.org/Stratio/crossdata.svg?branch=new-generation

Crossdata is a fast and general-purpose computing system powered by Apache Spark. It adds some libraries to provide
native access to datastores when they are able to resolve the query avoiding the use of the Spark cluster.
We include some Spark connectors optimized to access to each datasource, but Crossdata is fully compatible with any connector
developed by the Spark community.
-  Cassandra connector powered by Datastax-Spark-Connector

===================

Compiling Crossdata

    > mvn clean install -Pcrossdata-all

If you prefer to install solely some connectors::

    > mvn clean install -Pcassandra


Building Crossdata
========================================

TODO
    


Running the crossdata-shell
===========================

TODO


Send issues to Jira
===================
You can send us issues in https://crossdata.atlassian.net/
You can also find help in https://groups.google.com/forum/#!forum/crossdata-users


Grammar
=======

TODO


Getting started
===============

TODO. See Crossdata examples.


Connectors
==========

TODO


Sandbox
=======

If you want to test Crossdata you can get our Sandbox follow the instructions of this `link <doc/src/site/sphinx/Sandbox.rst>`_

License
=======

Stratio Crossdata is licensed as `Apache2 <http://www.apache.org/licenses/LICENSE-2.0.txt>`_

Licensed to STRATIO (C) under one or more contributor license agreements.
See the NOTICE file distributed with this work for additional information 
regarding copyright ownership.  The STRATIO (C) licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
