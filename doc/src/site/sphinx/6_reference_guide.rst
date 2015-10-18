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
=================

-  `1) General notes <#general-notes>`__

-  `2) Expansion main features <#expansion-main-features>`__

-  `3) DDL <#data-definition-language>`__

   -  `3.1) IMPORT EXISTING EXTERNAL TABLES <import-tables>`__
   -  `3.2) REGISTER EXISTING EXTERNAL TABLES <create-table>`__
   -  `3.3) TABLE CACHING <table-caching>`__

-  `4) DML <#data-manipulation-language>`__

 -  `4.1) CREATE TABLE AS SELECT <create-table-as-select>`__
 -  `4.2) INSERT INTO TABLE AS SELECT <insert-into-table-as-select>`__

-  `5) Select Statements <#select>`__

   -  `Step 4: Insert into collection students <#step-4-insert-into-collection-students>`__

      -  `Insert if not exists <#insert-if-not-exists>`__

WITH \<tablename\> AS  \<select\> (\<select\> | \<insert\>)


\<select\> :: = ( \<selectstatement\> | \<subquery\> )
                [(UNION ALL | INTERSECT | EXCEPT | UNION DISTINCT) \<select\>]
-- cartesian, intersection, first substract second, distinct (union)

\<subquery\> = (\<\<selectstatement\>\>)

\<selectstatement\> ::=
      SELECT [DISTINCT] (\<selectexpression\>' [AS \<aliasname\>],)\+\<selectexpression\> [AS \<aliasname\>]
      FROM   \<relations\> [ \<joinexpressions\> ]
      [WHERE \<expressions\>]
      [GROUP BY \<expressions\> [ HAVING \<expressions\>]]
      [ (ORDER BY| SORT BY ]
      => TODO explain what SORT BY mean

      [LIMIT  \<numLiteral\>]

\<relations\> ::= csv ( \<tablename\> [\<alias\>] , \<subquery\> [\<alias\>])
\<alias\> ::=  [AS] \<aliasname\>

\<joinexpression\> ::= \<relation\> [ \<jointype\>] JOIN \<relation\> [ ON \<expression\> ]

\<jointype\> ::= INNER
                | LEFT SEMI
                | LEFT [OUTER]
                | RIGHT [OUTER]
                | FULL  [OUTER]


 protected lazy val sortType: Parser[LogicalPlan => LogicalPlan] =
    ( ORDER ~ BY  ~> ordering ^^ { case o => l: LogicalPlan => Sort(o, true, l) }
    | SORT ~ BY  ~> ordering ^^ { case o => l: LogicalPlan => Sort(o, false, l) }
    )

  protected lazy val ordering: Parser[Seq[SortOrder]] =
    ( rep1sep(expression ~ direction.? , ",") ^^ {
        case exps => exps.map(pair => SortOrder(pair._1, pair._2.getOrElse(Ascending)))
      }
    )

  protected lazy val direction: Parser[SortDirection] =
    ( ASC  ^^^ Ascending
    | DESC ^^^ Descending
    )

\<selectexpression\> similar to expression??

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
    BitwiseExpressions => & | '|' | | ^
    CaseWhenExpression =>   CASE [ \<expression\> ]
                            ( WHEN \<expression\> THEN \<expression\>)+
                            [ ELSE \<expression\> ]
                            END
    FunctionExpression => \<functionname\> ( \<functionparameters\> ) => See supported functions <supported-functions>
        Special cases:  [ APPROXIMATE [ ( unsigned_float )] ] function ( [DISTINCT] params )


-  `6) Other commands <#commands>`__

   -  `Step 4: Insert into collection students <#step-4-insert-into-collection-students>`__

     -  `Insert if not exists <#insert-if-not-exists>`__

SET key=value

SHOW TABLES [IN \<database\>]

DESCRIBE [EXTENDED] \<tablename\>

SHOW FUNCTIONS  [\<functionid\>] -> It's possible to specify certain function

DESCRIBE FUNCTION [EXTENDED] \<functionid\>

-  `5) Supported data types <#supported--types>`__



1) General Notes
================

-   In general, a quoted (single or double) string refers to a literal
    string whereas a string without quotation marks refers to a column
    name.

Example:
    -   Column name:
        -   total
        -   myTable.total
        -   myCatalog.myTable.total
    -   Literal:
        -   “Madrid”
        -   ‘California'
        -   “New York City”

-   Identifier: Used to identify datasources, tables and qualified columns.
    An identifier is a token matching the regular expression
    ([a-zA-Z0-9\_]+.)*[a-zA-Z0-9\_]+


The following non-terminal elements appear in the grammar:

-   \<simpleidentifier\> ::= [a-zA-Z0-9\_]+
-   \<identifier\> ::= (\<simpleidentifier\>'.')\*\<simpleidentifier\>
-   \<stringliteral\> ::= “ (\~”)\* ” | ‘ (\~')\* '
-   \<intliteral\> ::= [0-9]+
-   \<datasource\> ::= \<identifier\>
-   \<database\> ::= \<simpleidentifier\>
-   \<tablename\> ::= \<identifier\>
-   \<property\> ::= \<identifier\> \<stringliteral\>
-   \<functionid\> ::= \<simple\_identifier\> | \<stringliteral\>
-   \<schema\> ::= ( (\<columndefinition\>',)*\<columndefinition\> )
-   \<columndefinition\> ::= \<columnname\> \<datatype\>
-   \<columnname\> ::= \<simple\_identifier\>
-   \<data-type\> ::=
        string |
        float|
        integer|
        tinyint |
        smallint |
        double |
        (bigint|long) |
        binary |
        boolean |
        decimal [(\<intliteral\>, \<intliteral\>) ] |
        date |
        timestamp |
        varchar (\<intliteral\>) |
        array\<\<datatype\>\> |
        map\<\<datatype\>, \<datatype\>\> |
        struct\<  (\<structfield\>',)*\<structfield\> \>
-   \<structfield\> ::= \<columnname\>:\<data-type\>

Please, check SparkSQL documentation for further information about specific statements. 


2) Expansion main features
==========================

Through the following lines you will find a description of those sentences provided by
CROSSDATA which are not supported by SparkSQL.

Expansion main features:
-   Added new table import capabilities:
        -   `IMPORT TABLES`: Catalog registration of every single table accessible by a concrete datasource.
        

3) Data Definition Language       
===========================

The most important thing to understand how the DDL works is to be aware of how Crossdata manage the metadata. 
So, the basics are:
 - Crossdata leverages in different datasources to store data.
 - Crossdata has a persistent catalog plus a cache where temporary tables could
 be stored in addition. That catalog contains metadata necessary to access datasouces data 
 as well as statistics to speed up the queries.
 
 Crossdata is focused on analytics, so the main use case of Crossdata is create a table to register 
 the metadata in the Crossdata catalog. However, when we perform a create table, it is not actually 
 created in the specific datasource. For instance, if you are working with Cassandra, the table created in 
 Crossdata should have been created previously on Cassandra. There are some exceptions to this behaviour:
 a well-known use case is to store the result of an analytical query in a new table; in that case, it will be 
 possible to create a table as select which will create the table in both the datasource and  the Crossdata 
 catalog. CREATE TABLE AS SELECT is described in DML<#data-manipulation-language>. 
 
 
3.1) IMPORT TABLES
------------------

Import all the tables from a specific datasource to the Crossdata catalog. It incorporates all the underlying metadata
needed by the datasource provider in order to create a Spark BaseRelation.

Once the tables are imported, they are persisted. If there is an existing table with the same name, this table will be
ignored.

IMPORT TABLES USING \<datasource\> OPTIONS ( (\<property\>',)\+\<property\> )

Example:

-   Cassandra:

    IMPORT TABLES
    USING com.stratio.crossdata.connector.cassandra
    OPTIONS (
        cluster "Test Cluster",
        spark_cassandra_connection_host '127.0.0.1'
    )

-   MongoDB:

    IMPORT TABLES
    USING com.stratio.crossdata.connector.mongodb
    OPTIONS (
       host '127.0.0.1:27017',
       schema_samplingRatio  '0.1'
    )
    
        
3.2) CREATE TABLE
-----------------

CREATE [TEMPORARY] TABLE [IF NOT EXISTS] \<tablename\> [<schema>] USING \<datasource\> OPTIONS ( (\<property\>',)\+\<property\> )

Temporary: A temporary table won't be persisted in Crossdata catalog.
  
Example:

    CREATE TABLE IF NOT EXISTS tablename ( id string, eventdate date)
    USING com.databricks.spark.csv 
    OPTIONS (path "events.csv", header "true")


3.3) TABLE CACHING
------------------

It is possible to cache a table or a temporary table using the following commands:

* CACHE [LAZY] TABLE \<tablename\> [AS \<select\>..]

Lazy: If lazy is ommited a count * will be performed in order to bring the whole RDD to memory without
waiting for the first time the data is needed.

* UNCACHE TABLE \<tablename\>

* CLEAR CACHE 

* REFRESH TABLE \<tablename\> (coming soon) => Refresh the cache.

4) DATA MANIPULATION LANGUAGE
-----------------------------

4.1) CREATE TABLE AS SELECT
---------------------------

The table will be created in both he Crossdata catalog and the target datasource indicated within the query:

CREATE [TEMPORARY] TABLE [IF NOT EXISTS] \<tablename\> [<schema>] USING \<datasource\> OPTIONS ( (\<property\>',)\+\<property\> ) AS \<select\>

4.2) INSERT INTO TABLE AS SELECT
--------------------------------

* INSERT INTO TABLE \<tablename\> \<select\>

Example:

    INSERT INTO TABLE mongodbtable 
    SELECT sum(price), day FROM cassandratable GROUP BY day
    
* INSERT OVERWRITE TABLE \<tablename\> \<select\>

It is quite similar to the previous one, but the the old data in the relation will be overwritten with the new data instead of appended.



 
The language supports the following set of operations based on the SQL
language.        

Supported types
---------------

Those supported by SparkSQL:

Numeric types:
* ByteType: Represents 1-byte signed integer numbers.
* ShortType: Represents 2-byte signed integer numbers.
* IntegerType: Represents 4-byte signed integer numbers.
* LongType: Represents 8-byte signed integer numbers.
* FloatType: Represents 4-byte single-precision floating point numbers.
* DoubleType: Represents 8-byte double-precision floating point numbers.
* DecimalType: Represents arbitrary-precision signed decimal numbers. Backed internally by java.math.BigDecimal.

Datetime types:
* DateType: year, month, day.
* TimestampType: year, month, day, hour, minute, and second.

StringType

BooleanType

BinaryType

Complex types:
* ArrayType[ElementType]: Sequence of elements.
* MapType[KeyType, ValueType]: Set of key-value pairs.
* StructType: Sequence of StructFields.
  * StructField(name, datatype, nullable): Represents a field in a StructType.


Supported functions
-------------------

Native build-in functions:

 _link => cassandra-datasource
 _link => mongodb-datasource

Spark built-in functions:

 Last update: Spark v1.5.1

// aggregate functions
avg
count
first
last
max
min
sum

// misc non-aggregate functions
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

// math functions
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

// string functions
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


// datetime functions
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


// collection functions
size
sort_array
array_contains

// misc functions
crc32
md5
sha
