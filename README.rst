About
*****

Crossdata is a distributed framework and a data layer that unifies the interaction with batch and
streaming sources supporting multiple datastore technologies thanks to its generic architecture and a custom SQL-like language with support for streaming queries. Supporting multiple architectures imposes two main challenges: how to normalize the access to the datastores, and how to cope with datastore limitations. To access multiple technologies, Crossdata defines a common unifying interface containing the set of operations that a datastore may support. New connectors can be easily added to increase its connectivity capabilities. Two types of connectors are defined: native and spark-based. Native connectors are faster for simple operations, while Spark-based connectors offer a larger set of functionality. The Crossdata planner decides which connector will be used for any request based its characteristics. We offer a shell, Java/REST APIs, JDBC and ODBC for BI.

Compiling Crossdata
===================

Compiling Crossdata involves generating a set of files (.tokens, Lexers and Parsers) from the different grammar
files. To automatically build Stratio Crossdata execute the following command::

    > mvn clean install

If you prefer to skip the unitary and integration tests, just type::

    > mvn clean install -DskipUTs -DskipITs


Running the com.stratio.crossdata-server
========================================
::

    > mvn exec:java -pl crossdata-server -Dexec.mainClass="com.stratio.crossdata.server.CrossdataApplication"

or you can run our script located in crossdata-dist::

    > chmod +x crossdata-dist/target/crossdata-dist-[crossdata-version]/bin/crossdata-server
    > cd crossdata-dist/target/crossdata-dist-[crossdata-version]/
    > bin/crossdata-server

or run it like a service::

    > bin/crossdata-server-daemon start
    


Running the crossdata-shell
===========================

The com.stratio.crossdata-shell allows users to launch interactive queries against a set of Crossdata servers. 
Works both in Unix and Windows.
The shell features:

-   History support (arrow navigation)
-   History search (ctrl-r)
-   Token completion (tab)
-   Help command
::

    > mvn exec:java -pl crossdata-shell -Dexec.mainClass="com.stratio.crossdata.sh.Shell"


The default behaviour of the Shell is asynchronous, however, the shell also supports synchronous query execution by
means of the --sync parameter.::

    > mvn exec:java -pl crossdata-shell -Dexec.mainClass="com.stratio.crossdata.sh.Shell" -Dexec.args="--sync"


Additionally, you can execute an script upon launching the shell. The script will be executed first,
and the prompt will be shown afterwards.::


    > mvn exec:java -pl crossdata-shell -Dexec.mainClass="com.stratio.crossdata.sh.Shell" -Dexec.args="--script /path/script.xdql"


You can run our shell too, executing our crossdata-dist script::

    > chmod +x crossdata-dist/target/crossdata-dist-[crossdata-version]/bin/crossdata-sh
    > cd crossdata-dist/target/crossdata-dist-[crossdata-version]/
    > bin/crossdata-sh



Useful commands
===============

Once the shell is running, you can exit the program introducing the word **exit** or **quit** in the query prompt. A command help system is available by introducing the command **help**. A help entry is available per command, to check specify help topics use **help command**.

Send issues to Jira
===================
You can send us issues in https://crossdata.atlassian.net/
You can also find help in https://groups.google.com/forum/#!forum/crossdata-users


Grammar
=======

Grammar specification for this release can be found `here <doc/src/site/sphinx/Grammar.rst>`_.


Getting started
===============
In this `link <GettingStarted.rst>`_ you can follow an example of Crossdata with a Cassandra Connector as an access
to a Cassandra datastore.


Crossdata in a distributed environment
======================================

In this `link <doc/src/site/sphinx/DistributedCrossdata.rst>`_ you can read how to configure crossdata in a
distributed environment.


Connectors
==========

`List of Crossdata Connectors <doc/src/site/sphinx/List-of-Crossdata-Connectors.rst>`_

`InMemory Connector development tutorial <doc/src/site/sphinx/InMemory-Connector-Development-Tutorial.rst>`_

`Definition of Connector Operations <doc/src/site/sphinx/ConnectorOperations.rst>`_


Crossdata compatibility with connectors
========================================

+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| Crossdata       | Cassandra      | Commons        | MongoDB        | ElasticSearch  | SparkSQL       | Deep           | Streaming      | HDFS           |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.1.0           | 0.1.0-001      | 0.1.0          | 0.1.0          | 0.1.0          |  X             | 0.1.0          | 0.1.0          | X              |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.1.0-001       | 0.1.0-001      | 0.1.0          | 0.1.0          | 0.1.0          |  X             | 0.1.0          | 0.1.0          | X              |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.1.0-002       | 0.1.0-001      | 0.1.0          | 0.1.0          | 0.1.0          |  X             | 0.1.0          | 0.1.0          | X              |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.1.1           | 0.1.1          | 0.2.0          | 0.2.0          | 0.2.1          |  X             | 0.2.X          | 0.2.0          | 0.1.0          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.2.0           | 0.2.0          | 0.4.0-001      | 0.3.0          | 0.3.0          |  X             | 0.3.0          | 0.3.0          | 0.2.0          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.2.0-001       | 0.2.0-001      | 0.4.1          | 0.3.0-001      | 0.3.0-001      |  X             | 0.3.0-001      | 0.3.0          | 0.2.0-001      |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.2.1           | 0.2.0-001      | 0.4.1          | 0.3.0-001      | 0.3.0-001      |  X             | 0.3.0-001      | 0.3.0          | 0.3.0          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.3.0           | 0.3.0          | 0.5.0          | 0.4.0          | 0.4.0          | 0.1.0          | X              | 0.4.0          | 0.4.0          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.3.3           | 0.3.3          | 0.5.1          | 0.4.1          | 0.4.1          | 0.1.X          | X              | 0.4.1          | 0.4.X          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+
| 0.3.4           | 0.3.3          | 0.5.1          | 0.4.1          | 0.4.1          | 0.1.X          | X              | 0.4.1          | 0.4.X          |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+ 
| 0.4.0-SNAPSHOT  | 0.4.0-SNAPSHOT | 0.6.0-SNAPSHOT | 0.5.0-SNAPSHOT | 0.5.0-SNAPSHOT | 0.2.0-SNAPSHOT | X              | 0.5.0-SNAPSHOT | 0.5.0-SNAPSHOT |
+-----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+


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
