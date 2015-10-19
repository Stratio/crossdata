===============
Reference Guide
===============

Apart from the API Crossdata provides a SQL-LIKE language based on standard SparkSQL grammar plus an expansion
described below:

CROSSDATA SQL - Introduction
****************************

This document describes the standard SparkSQL grammar expansion provided by CROSSDATA
that means that any SQL sentences accepted by SparkSQL will be compatible with CROSSDATA.

Along with the language description different features like supported types or built-in functions 
are included.

Table of Contents
*****************

-  `1) GENERAL NOTES <#general-notes>`__

-  `2) EXPANSION MAIN FEATURES <#expansion-main-features>`__

-  `3) DDL <#data-definition-language>`__

   -  `3.1) IMPORT EXISTING EXTERNAL TABLES <import-tables>`__
   -  `3.2) REGISTER EXISTING EXTERNAL TABLES <create-table>`__
   -  `3.3) TABLE CACHING <table-caching>`__

-  `4) DML <#data-manipulation-language>`__

   -  `4.1) CREATE TABLE AS SELECT <create-table-as-select>`__
   -  `4.2) INSERT INTO TABLE AS SELECT <insert-into-table-as-select>`__

-  `5) SELECT STATEMENTS <#select-statements>`__

   -  `5.1) GRAMMAR <grammar>`__
   -  `5.2) EXAMPLES <examples>`__

-  `6) OTHER COMMANDS <#other-commands>`__

   -  `6.1) SHOW COMMANDS <show-commands>`__
   -  `6.2) DESCRIBE COMMANDS <describe-commands>`__
   -  `6.3) SET COMMAND <set-command>`__
   
-  `7) SUPPORTED DATA TYPES <#supported-data-types>`__

-  `8) LIST OF CROSSDATA CONNECTORS <#list-of-crossdata-connectors>`__

-  `9) SUPPORTED FUNCTIONS <#supported-functions>`__




1. General Notes
================

-   In general, a quoted (single or double) string refers to a literal
    string whereas a string without quotation marks refers to a column
    name.

-   Identifier: Used to identify datasources, tables and qualified columns.
    An identifier is a token matching the regular expression
    ([a-zA-Z0-9\_]+.)*[a-zA-Z0-9\_]+

Example:
    -   Column name:
        -   total
        -   myTable.total
        -   myCatalog.myTable.total
    -   Literal:
        -   “Madrid”
        -   ‘California'
        -   “New York City”

The following non-terminal elements appear in the grammar:

-   \<simpleidentifier\> ::= [a-zA-Z0-9\_]+
-   \<identifier\> ::= (\<simpleidentifier\>'.')*\<simpleidentifier\>
-   \<stringliteral\> ::= ( “ (\~”)\* ” | ‘ (\~')\* ' )
-   \<intliteral\> ::= [0-9]+
-   \<datasource\> ::= \<identifier\>
-   \<database\> ::= \<simpleidentifier\>
-   \<tablename\> ::= \<identifier\>
-   \<property\> ::= \<identifier\> \<stringliteral\>
-   \<functionid\> ::= (\<simple\_identifier\> | \<stringliteral\>)
-   \<schema\> ::= ( (\<columndefinition\>',)* \<columndefinition\> )
-   \<columndefinition\> ::= \<columnname\> \<datatype\>
-   \<columnname\> ::= \<simple\_identifier\>
-   \<data-type\> ::=
        string   |
        float    |
        integer  |
        tinyint  |
        smallint |
        double   |
        (bigint | long) |
        binary   |
        boolean  |
        decimal [(\<intliteral\>, \<intliteral\>)] |
        date     |
        timestamp|
        varchar (\<intliteral\>) |
        array\<\<datatype\>\>    |
        map\<\<datatype\>, \<datatype\>\> |
        struct\< (\<structfield\>',)* \<structfield\>\>
-   \<structfield\> ::= \<columnname\>:\<data-type\>

Please, check SparkSQL documentation for further information about specific statements. 




2. Expansion main features
==========================

Through the following lines you will find a description of those sentences provided by
CROSSDATA which are not supported by SparkSQL.

Expansion main features:
-   Added new table import capabilities:
        -   IMPORT TABLES: Catalog registration of every single table accessible by a concrete datasource.
        



3. Data Definition Language
===========================

The most important thing to understand how the DDL works is to be aware of how Crossdata manages the metadata.
So, the basics are:

 - Crossdata leverages in different datasources to store data.
 - Crossdata has a persistent catalog plus a cache where temporary tables could be stored in addition. The catalog contains metadata necessary to access datasouces data as well as statistics to speed up the queries.
 
 Crossdata is focused on analytics, so the main use case of Crossdata is create a table to register 
 the metadata in the Crossdata catalog. However, when a create table is performed, it is not actually
 created in the specific datasource. For instance, if you are working with Cassandra, the table created in 
 Crossdata should have been created previously on Cassandra. There are some exceptions to this behaviour:
 a well-known use case is to store the result of an analytical query in a new table; in that case, it will be 
 possible to create a table as select which will create the table in both the datasource and the Crossdata
 catalog. CREATE TABLE AS SELECT is described in `DML <#data-manipulation-language>`_.
 
 
3.1 IMPORT TABLES
-----------------

Import all the tables from a specific datasource to the Crossdata catalog. It incorporates all the underlying metadata
needed by the datasource provider in order to create a Spark BaseRelation.

Once the tables are imported, they are persisted. If there is an existing table with the same name, this table will be
ignored.

IMPORT TABLES USING \<datasource\> OPTIONS ( (\<property\>',)\+\<property\> )

Example:

-   Cassandra:
::

    IMPORT TABLES
    USING com.stratio.crossdata.connector.cassandra
    OPTIONS (
        cluster "Test Cluster",
        spark_cassandra_connection_host '127.0.0.1'
    )

-   MongoDB:
::

    IMPORT TABLES
    USING com.stratio.crossdata.connector.mongodb
    OPTIONS (
       host '127.0.0.1:27017',
       schema_samplingRatio  '0.1'
    )
    
        
3.2 CREATE TABLE
----------------

CREATE [TEMPORARY] TABLE [IF NOT EXISTS] \<tablename\> [<schema>] USING \<datasource\> OPTIONS ( (\<property\>',)\+\<property\> )

Temporary: A temporary table won't be persisted in Crossdata catalog.
  
Example:
::

    CREATE TABLE IF NOT EXISTS tablename ( id string, eventdate date)
    USING com.databricks.spark.csv 
    OPTIONS (path "events.csv", header "true")


3.3) TABLE CACHING
------------------

It is possible to cache a table or a temporary table using the following commands:

- CACHE [LAZY] TABLE \<tablename\> [AS \<select\>..]

Lazy: If lazy is omitted a count * will be performed in order to bring the whole RDD to memory without
waiting for the first time the data is needed.

- UNCACHE TABLE \<tablename\>

- CLEAR CACHE

- REFRESH TABLE \<tablename\> (coming soon) => Refresh the metadata cache.




4. DATA MANIPULATION LANGUAGE
-----------------------------

4.1 CREATE TABLE AS SELECT
--------------------------

The table will be created in both he Crossdata catalog and the target datasource indicated within the query:

CREATE [TEMPORARY] TABLE [IF NOT EXISTS] \<tablename\> [<schema>] USING \<datasource\> OPTIONS ( (\<property\>',)\+\<property\> ) AS \<select\>

Example:
::

    CREATE TABLE mongodbtable
    USING com.databricks.spark.csv
    OPTIONS (path "events.csv", header "true")
    SELECT sum(price), day
    FROM cassandratable
    GROUP BY day

4.2 INSERT INTO TABLE AS SELECT
-------------------------------

* INSERT INTO TABLE \<tablename\> \<select\>

Example:
::

    INSERT INTO TABLE mongodbtable 
    SELECT sum(price), day
    FROM cassandratable
    GROUP BY day
    
* INSERT OVERWRITE TABLE \<tablename\> \<select\>

It is quite similar to the previous one, but the old data in the relation will be overwritten with the new data instead of appended.




5. SELECT STATEMENTS
====================

The language supports the following set of operations based on the SQL language.

5.1 Grammar
-----------
::

 \<select\> ::= ( \<selectstatement\> | \<subquery\> ) [ \<setoperation\> \<select\>]
 \<subquery\> ::= ( \<selectstatement\> )
 \<setoperation\> ::= ( UNION ALL |
                        INTERSECT |
                        EXCEPT    |
                        UNION DISTINCT )

 \<selectstatement\> ::=
      SELECT [DISTINCT] (\<selectexpression\>' [AS \<aliasname\>],)* \<selectexpression\> [AS \<aliasname\>]
      FROM   ( \<relations\> | \<joinexpressions\> )
      [WHERE \<expressions\>]
      [GROUP BY \<expressions\> [ HAVING \<expressions\>]]
      [(ORDER BY | SORT BY) \<orderexpressions\>]
      [LIMIT \<numLiteral\>]

 \<relations\> ::= (\<relation\> [\<alias\>],)* \<relation\> [\<alias\>]
 \<relation\> ::= (\<tablename\> | \<subquery\>)
 \<alias\> ::=  [AS] \<aliasname\>
 \<aliasname\> ::= \<simpleidentifier\>
 \<joinexpression\> ::= \<relation\> [ \<jointype\>] JOIN \<relation\> [ ON \<expression\> ]
 \<jointype\> ::= ( INNER        |
                    LEFT SEMI    |
                    LEFT [OUTER] |
                    RIGHT [OUTER]|
                    FULL  [OUTER]
                  )
 \<orderexpressions\> ::= (\<orderexpression\>,)* \<orderexpression\>
 \<orderexpression\> ::= (\<identifier\> | \<expression\>) [ (ASC | DESC) ]

 \<expression\> ::=
    CombinationExpressions => AND | OR
    NotExpression => NOT
    ComparisonExpressions =>
        = | < | <= | > | >= | (!= | <>)
       | <=> (equal null safe)
       | [NOT] BETWEEN _ AND _
       | [NOT] LIKE | (RLIKE | REGEXP)
       | [NOT] IN
       | IS [NOT] NULL
    ArithmeticExpressions =>  + | - | * | / | %
    BitwiseExpressions => & | '|' | ^
    CaseWhenExpression =>   CASE [ \<expression\> ]
                            ( WHEN \<expression\> THEN \<expression\>)+
                            [ ELSE \<expression\> ]
                            END
    FunctionExpression => \<functionname\> ( \<functionparameters\> ) => See `supported functions <#supported-functions>`_
        Special cases:  [ APPROXIMATE [ ( unsigned_float )] ] function ( [DISTINCT] params )


Though most language is similar to SQL, let's go deeper to some specific grammar for querying over partitioned data:

- Ordering statements
ORDER BY: means global sorting apply for entire data set.
SORT BY: means sorting only apply within the partition.

- Set statements
UNION ALL: combines the result.
INTERSECT: collects first query elements that also belong the the second one.
EXCEPT: subtracts the second query result to the first one.
UNION DISTINCT: deletes duplicates.


5.2 Examples
------------

Some different examples with common structures are shown below:
::

    - SELECT name, id FROM table1
    UNION ALL
    SELECT name, id FROM table2


    - SELECT t1.product, gender, count(*) AS amount, sum(t1.quantity) AS total_quantity
    FROM (SELECT product, client_id, quantity FROM lineshdfsdemo) t1
    INNER JOIN clients ON client_id=id
    GROUP BY gender, product;


    - SELECT ol_cnt, sum(CASE
                       WHEN o_carrier_id = 1 OR o_carrier_id = 2 THEN 1
                       ELSE 0 END
                       ) AS high_line_count
    FROM testmetastore.orders
    WHERE ol_delivery_d <to_date('2013-07-09') AND country LIKE "C%"
    GROUP BY o_ol_cnt
    ORDER BY high_line_count DESC, low_line_count
    LIMIT 10




6. OTHER COMMANDS
=================

6.1 Show commands
-----------------

SHOW TABLES [IN \<database\>]

SHOW FUNCTIONS [\<functionid\>]

6.2 Describe commands
---------------------

DESCRIBE [EXTENDED] \<tablename\>

DESCRIBE FUNCTION [EXTENDED] \<functionid\>

6.3 Set command
---------------

SET key=value



7. SUPPORTED DATA TYPES
=======================

Those supported by SparkSQL:

-  Numeric types:

   -  ByteType: Represents 1-byte signed integer numbers.
   -  ShortType: Represents 2-byte signed integer numbers.
   -  IntegerType: Represents 4-byte signed integer numbers.
   -  LongType: Represents 8-byte signed integer numbers.
   -  FloatType: Represents 4-byte single-precision floating point numbers.
   -  DoubleType: Represents 8-byte double-precision floating point numbers.
   -  DecimalType: Represents arbitrary-precision signed decimal numbers. Backed internally by java.math.BigDecimal.

-  Datetime types:

   -  DateType: year, month, day.
   -  TimestampType: year, month, day, hour, minute, and second.

-  StringType

-  BooleanType

-  BinaryType

-  Complex types:

   -  ArrayType[ElementType]: Sequence of elements.
   -  MapType[KeyType, ValueType]: Set of key-value pairs.
   -  StructType: Sequence of StructFields.

     -  StructField(name, datatype, nullable): Represents a field in a StructType.



8. LIST OF CROSSDATA CONNECTORS
===============================

This document maintains an updated list of connector that work with current versions of Crossdata. Take into account
that each connector listed may require different version of Crossdata.

-  Datasources => Implement some methods of SparkSQL Datasource API
-  Connectors => Implement both SparkSQL Datasource API and Crossdata API.

Although connectors and datasources take advantage of Crossdata core only connectors can support certain capabilities
like native execution, Native built-in functions or table discovery.

Connectors taking advantage of Crossdata extension
--------------------------------------------------

-  connector-cassandra
-  connector-mongodb
-  connector-elasticsearch (coming soon)

List of Datasources (or Spark-based Connectors)
-----------------------------------------------

Datasources within SparkSQL

-  `parquet: <https://github.com/apache/spark/tree/master/sql>`_
-  `jdbc: <https://github.com/apache/spark/tree/master/sql>`_
-  `json: <https://github.com/apache/spark/tree/master/sql>`_

External datasources

-  `elasticsearch: <https://github.com/elastic/elasticsearch-hadoop>`_
-  `csv: <https://github.com/databricks/spark-csv>`_
-  `avro: <https://github.com/databricks/spark-avro>`_

A more completed list of external Datasources could be find at `spark packages <http://spark-packages.org/?q=tags%3A%22Data%20Sources%22>`_


9. SUPPORTED FUNCTIONS
----------------------

-  Native built-in functions:

 - (coming soon) => cassandra-connector _link
 - (coming soon) => mongodb-connector _link

Spark built-in functions (last update: Spark v1.5.1):

-  Aggregate functions
avg
count
first
last
max
min
sum

-  Misc non-aggregate functions
abs
array
coalesce
explode
greatest
if
isnan
isnull
isnotnull
least
rand
randn
sqrt

-  Math functions
acos
asin
atan
atan2
bin
cbrt
ceil
ceiling
cos
conv
exp
floor
factorial
hypot
hex
log
ln
log10
pow
pmod
positive
round
rint
sign
sin
sinh
tan
tanh
degrees
radians

-  String functions
ascii
base64
concat
format_number
get_json_object
lower
length
regexp_extract
regexp_replace
ltrim
printf
rtrim
split
substring
substring_index
trim
upper


-  Datetime functions
current_date
current_timestamp
datediff
date_add
date_format
date_sub
day
dayofyear
dayofmonth
from_unixtime
from_utc_timestamp
hour
last_day
minute
month
months_between
next_day
quarter
second
to_date
to_utc_timestamp
unix_timestamp
weekofyear
year


-  Collection functions
size
sort_array
array_contains

-  Misc functions
crc32
md5
sha
