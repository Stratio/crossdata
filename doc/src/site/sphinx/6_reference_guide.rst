===============
Reference Guide
===============

Apart from the API Crossdata provides a SQL-LIKE language based on standard SparkSQL grammar plus an expansion
described below:

CROSSDATA SQL - Introduction
****************************

This document describes the standard SparkSQL grammar expansion provided by CROSSDATA
that means that any SQL sentences accepted by SparkSQL will be compatible with CROSSDATA.

Through the following lines you will find a description of those sentences provided by
CROSSDATA which are not supported by SparkSQL.

Expansion main features:
-   Added new table import capabilities:
        -   `IMPORT TABLES`: Catalog registration of every single table accessible by a concrete datasource.

Table of Contents
=================

-  `1) General notes`__

-  `2) Expansion main features`__

-  `3) DDL <#data-definition-language>`__

   -  `1.1) IMPORT TABLES <import-tables>`__

IMPORT TABLES..

CREATE [TEMPORARY] TABLE [IF NOT EXISTS] \<tablename\> [<schema>] USING \<datasource\> OPTIONS ( (\<property\>',)\+\<property\> ) [AS \<select\>]
--explain temporary --optional schema --asSelect => DDL

-  `4) DML <#data-manipulation-language>`__

   -  `IMPORT TABLES <import-tables>`__
      -  `Creating a default index <#creating-a-default-index>`__
      -  `Creating a custom index <#creating-a-custom-index>`__

CACHE [LAZY] TABLE \<tablename\> [AS \<select\>..]

UNCACHE TABLE \<tablename\>...

CLEAR CACHE ...

REFRESH TABLE  \<tablename\>

INSERT (OVERWRITE | INTO) TABLE \<tablename\> \<select\>

If overwrite in insert method is true, the old data in the relation should be overwritten with the new data. If overwrite in insert method is false, the new data should be appended.

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




IMPORT TABLES
-------------

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



Notes?
=======

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


-   In the near future, many statements and statement extensions will be supported.

Statements?
----------

The language supports the following set of operations based on the SQL
language.

        The following elements are defined as:

-   Identifier: Used to identify datasources and, databases and tables.
    An identifier is a token matching the regular expression
    ([a-zA-Z0-9\_]+.)*[a-zA-Z0-9\_]+

-   Values: A value is a text representation of any of the supported
    data types.

        The following non-terminal symbols appear in the grammar:

-   \<simple\_identifier\> ::= LETTER (LETTER | DIGIT | '\_')\*
-   \<identifier\> ::= (\<simple\_identifier\>'.')\*\<simple\_identifier\>
-   \<literal\> ::= “ (\~”)\* ” | ‘ (\~')\* '
-   \<datasource\> ::= \<identifier\>
-   \<database\> ::= \<simple\_identifier\>
-   \<tablename\> ::= \<identifier\>
-   \<property\> ::= \<identifier\> \<literal\>
-   \<functionid\> ::= \<simple\_identifier\> | \<literal\>
-   \<schema\> ::= ( (\<columnmd\>',)\+\<columnmd\> )
-   \<columnmd\> ::= \<column-name\> \<data-type\>
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
        decimal [(\<precision\>, \<scale\>) ] |
        date |
        timestamp |
        varchar (\<num\>) |
        array\<\<data-type\>\> |
        map\<\<data-type\>, \<data-type\>\> |
        struct\<  (\<struct-field\>',)\+\<struct-field\> \>

-   \<struct-field\> ::= \<columnname\>:\<data-type\>


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
