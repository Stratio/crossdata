======================================
Interaction with other Stratio modules
======================================

*Crossdata* is well integrated into the main platform by both being intensively used by other modules and using others
as dependencies.

Modules using Crossdata
-----------------------

Acting as an integrated medium for accessing data, it shouldn't be a surprise that top-level modules use *Crossdata* to
simplify their integration with data sources.

In this regard, *Crossdata* currently relates to the following Stratio modules: *Viewer*, *Explorer* and *Manager*.

Viewer
^^^^^^

Viewer uses *Crossdata*'s driver to perform queries to its server. That's is why *Viewer* project includes it as a
maven dependency.

..
    Explorer
    ^^^^^^^^


Manager
^^^^^^^

*Crossdata* is part of Stratio platform thus becoming a package available to be installed by the *Manager* tool
which, besides data storage solutions and spark (both being *Crossdata* dependencies), is able to install
the *Crossdata* server.


Modules used by Crossdata
-------------------------

Other modules efforts to provide and improve access to mainstream data sources from Spark are
extremely convenient when trying to expand *Crossdata* functionality. Hence, *Spark-Mongodb* and *Stratio's Cassandra
Lucene Index plugin* are direct *Crossdata* dependencies.

Spark-Mongodb
^^^^^^^^^^^^^

Spark-Mongodb_ is Stratio's library which enables the use of *MongoDB* from *Apache Spark*. It is therefore the first requirement for *Crossdata* in order to interact with this datastore.

*Spark-Mongodb* is referenced by *Crossdata* as a maven dependency so it will be pulled during the project building
process.

Stratio's Cassandra Lucene Index plugin
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

`Stratio's Cassandra Lucene Index plugin`_ is the plugin which seamlessly integrates *Apache Lucene* index with *Cassandra*. Using any Lucene index functionality from *Crossdata* requires this plugin to be configured within the C* datastore.

The Plugin should be `installed`_ as a plugin for the Cassandra server to which Crossdata will connect at run time.
That is, the server addressed by the datasource registration options provided as an option for the `table creation
sentence`_.

.. _Spark-Mongodb: https://github.com/Stratio/spark-mongodb
.. _`Stratio's Cassandra Lucene Index plugin`: https://github.com/Stratio/cassandra-lucene-index
.. _installed: https://github.com/Stratio/cassandra-lucene-index#build-and-install
.. _`table creation sentence`: https://github.com/Stratio/Crossdata/blob/master/doc/src/site/sphinx/6_reference_guide.rst#create-table