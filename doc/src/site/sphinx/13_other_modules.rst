======================================
Interaction with other Stratio modules
======================================

Crossdata is well integrated into the main platform by both being intensively used by other modules and using others
as dependencies.

Modules using Crossdata
-----------------------

Acting as an integrated medium for accessing data, it shouldn't be a surprise that top-level modules use Crossdata to
simplify their integration with data sources.

In this regard, Crossdata currently relates to the following Stratio modules:

+ **Viewer**
+ **Explorer**

Modules used by Crossdata
-------------------------

On the other hand, other modules efforts to provide and improve access to mainstream data sources from Spark are
extremely convenient when trying to expand Crossdata functionality. Thus *GoSec*, *Spark-Mongodb* and *Stratio's Cassandra Lucene Index plugin*
are direct Crossdata dependencies.

+ **Spark-Mongodb** is Stratio's library which enables the use of *MongoDB* from *Apache Spark*. It is therefore the first requirement for Crossdata in order to interact with this database.
+ **Stratio's Cassandra Lucene Index plugin** is the plugin which seamlessly integrates *Apache Lucene* index with *Cassandra*. Using any Lucene index functionality from Crossdata requires this plugin to be configured within the C* database.
+ **GoSec** is a new module dedicated to authentication, authorization and event tracking in Stratio platform so the team is actively working on integrating it as the Crossdata security layer.


