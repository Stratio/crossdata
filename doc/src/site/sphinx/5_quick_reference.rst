===============
Quick Reference
===============

Quick SQL Statement reference. More details can be found at `reference guide. <6_reference_guide.rst>`__

CREATE TABLE
------------
Registers a table in Crossdata catalog. A temporary table won't be persisted.

CREATE [TEMPORARY] TABLE [IF NOT EXISTS] \<tablename\> [<schema>] USING \<datasource\> OPTIONS \<options\>

Example:
::

    CREATE TABLE IF NOT EXISTS tablename ( id string, eventdate date)
    USING com.databricks.spark.csv
    OPTIONS (path "events.csv", header "true")

DROP TABLE
----------
Removes the table from Crossdata catalog.

DROP TABLE \<tablename\>

Example:
::

    DROP TABLE dbname.tablename

TABLE CACHING
-------------

- CACHE [LAZY] TABLE \<tablename\>
Caches the underlying RDD.

- UNCACHE TABLE \<tablename\>
Uncaches the table in order to free Spark resources or to revalue the RDD.

- CLEAR CACHE
Uncaches all tables.

Example:
::

    CACHE LAZY TABLE table1

SELECT STATEMENTS
-----------------
Query statements to retrieve data.

::

  SELECT [DISTINCT] \<projections\>
  FROM   ( \<relations\> | \<joinexpressions\> )
  [WHERE \<filters\>]
  [GROUP BY \<expressions\> [ HAVING \<havingexpressions\>]]
  [(ORDER BY | SORT BY) \<orderexpressions\>]
  [LIMIT \<numLiteral\>]

  \<joinexpression\> ::= \<relation\> [ \<jointype\>] JOIN \<relation\> [ ON \<expression\> ]
  \<jointype\> ::= ( INNER        |
                    LEFT SEMI    |
                    LEFT [OUTER] |
                    RIGHT [OUTER]|
                    FULL  [OUTER]
                  )

Examples:
::

    - SELECT t1.product, gender, count(*) AS amount, sum(t1.quantity) AS total_quantity
    FROM (SELECT product, client_id, quantity FROM lineshdfsdemo) t1
    INNER JOIN clients ON client_id=id
    GROUP BY gender, product;


    - SELECT ol_cnt, sum(carrier) AS high_line_count
    FROM testmetastore.orders
    WHERE ol_delivery_d BETWEEN '2013-07-09' AND '2013-08-09' AND country LIKE "C%"
    GROUP BY o_ol_cnt
    ORDER BY high_line_count DESC, low_line_count
    LIMIT 10



ADMIN COMMANDS
--------------

- SHOW TABLES [IN \<database\>]
Lists tables registered in Crossdata catalog (persisted and in-memory).

- SHOW FUNCTIONS [\<functionid\>]
Lists functions registered in Crossdata catalog.

- DESCRIBE [EXTENDED] \<tablename\>
Shows the table metadata.

- DESCRIBE FUNCTION [EXTENDED] \<functionid\>
Shows useful information like description, arguments, etc...

Examples:
::

    SHOW TABLES IN mydatabase
    SHOW FUNCTIONS
    DESCRIBE TABLE mydatabase.mytable