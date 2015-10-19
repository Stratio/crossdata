==========
Benchmarks
==========

At this moment, there is no yet a consistent benchmark to measure the impact of the optimizations created by Crossdata on Apache Spark. Nevertheless, some simple tests have been carried out using simple queries with Cassandra as datastore and with a small distributed environment.

The specification of these tests and of the environment are:

- 3 nodes
- Node:
	- Intel(R) Xeon(R) CPU E5-2630 v3 @ 2.40GHz (32 cores)
	- 1 Tera solid magnetic hard disk
	- 64GB RAM
- Dataset: 30 million of records (3GB 600Mb)

The results are expressed in milliseconds and each of them being the average time after 100 executions of every query in the different scenarios:

+--------------------------------------------------+--------+-----------+--------------------+----------+---------------+
| Query / Mean                                     | Native | XDContext | SQLContext (Spark) | Speed-up | Overhead      |
+==================================================+========+===========+====================+==========+===============+
| SELECT * FROM table WHERE PK = $value            |    3ms |      45ms |              255ms |       6x | (Native) 42ms |
+--------------------------------------------------+--------+-----------+--------------------+----------+---------------+
| SELECT * FROM table WHERE NonIndexedCol > $value |      - |   52035ms |            52013ms |        - | (Spark)  22ms |
+--------------------------------------------------+--------+-----------+--------------------+----------+---------------+

The first query can be executed natively and the execution time was calculated using directly the Datastax Cassandra driver. As reflected in the table, Crossdata executes this query going directly to use the Datastax Cassandra driver, avoiding the unnecessary use (for this query) of the Spark Cluster, which allows to resolve this query 6 times faster than going through the SQLContext of Apache Spark. On the other hand, the execution through the CrossdataContext introduces an overhead of 42 milliseconds approximately but it's important to clarify that most of this overhead is created by the SQL Parser of Spark and that this is a constant time, that is, in queries that require heavy workload, this overhead remains around 42 milliseconds and it become insignificant when a query takes seconds or more.

Regarding the second query, the execution query can't be executed natively so this query is useful in order to know what the actual overhead of the Crossdata calculations is. As expected, Crossdata analyzes the query and it detects that Apache Cassandra is not capable of resolving this query natively and it sends the SparkPlan (similar to an execution tree) to the Spark Cluster to get the query resolved. The table shows a overhead of 22 milliseconds. Again, this overhead is a constant time and, therefore, the overhead is insignificant for any query taking seconds.
