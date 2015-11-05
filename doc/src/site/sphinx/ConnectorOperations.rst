
Connector Operations
********************

This document will provide a detailed description of the different operations that can be supported by a Crossdata 
connector.

Definition of Connector Operation
=================================

A connector operation defines a capability a connector provides. All connector operations must be declared in their 
associated manifest. As an example, consider the following *InMemoryConnector.xml* manifest ::

    <?xml version="1.0" encoding="UTF-8"?>
    <!-- This file contains the manifest for the connector. -->
    <Connector>
        <!-- Name of the connector as it will be identified in Crossdata -->
        <ConnectorName>InMemoryConnector</ConnectorName>
        <!-- Define the list of datastore this connector is able to access. -->
        <DataStores>
            <DataStoreName>InMemoryDatastore</DataStoreName>
        </DataStores>
        <!-- Connector version -->
        <Version>0.3.4</Version>

        <!-- Define the set of required operations the user will be asked to input
        when attaching the connector -->

        <!--OptionalProperties>
            <Property>
                <PropertyName>TableRowLimit</PropertyName>
                <Description>Limit of rows allowed per table</Description>
            </Property>
        </OptionalProperties-->

        <!-- Define the list of operations supported by the connector.
        Check crossdata/doc/ConnectorOperations.md for more information. -->
        <SupportedOperations>
            <operation>CREATE_CATALOG</operation>
            <operation>DROP_CATALOG</operation>
            <operation>CREATE_TABLE</operation>
            <operation>DROP_TABLE</operation>
            <operation>TRUNCATE_TABLE</operation>
            <operation>INSERT</operation>
            <operation>PROJECT</operation>
            <operation>SELECT_OPERATOR</operation>
            <operation>SELECT_FUNCTIONS</operation>
            <operation>SELECT_ORDER_BY</operation>
            <operation>SELECT_LIMIT</operation>
            <operation>FILTER_PK_EQ</operation>
            <operation>FILTER_PK_GT</operation>
            <operation>FILTER_PK_LT</operation>
            <operation>FILTER_PK_GET</operation>
            <operation>FILTER_PK_LET</operation>
            <operation>FILTER_NON_INDEXED_EQ</operation>
            <operation>FILTER_NON_INDEXED_GT</operation>
            <operation>FILTER_NON_INDEXED_LT</operation>
            <operation>FILTER_NON_INDEXED_GET</operation>
            <operation>FILTER_NON_INDEXED_LET</operation>
            <operation>IMPORT_METADATA</operation>
            <operation>PAGINATION</operation>
        </SupportedOperations>
    </Connector>


Each operation is associated with the methods found in *IMetadataEngine*, *IStorageEngine*, 
and *IQueryEngine*. The following section provides a detailed description of each operation organized by the type of 
operation.

Types of Operations
===================

The connector operations are organized attending to the type of operation to be performed: Metadata, Storage, 
and Query. In order to simplify the definition of the different connectors, two concepts are taken into account: 
*column_type*, and *relationship*.

The column type is identified by Crossdata during the query analysis with the following types:

+-------------+------------------------------------------+
| column_type | Description                              |
+=============+==========================================+
| PK          | The column is part of the primary key.   |
+-------------+------------------------------------------+
| INDEXED     | The column is indexed.                   |
+-------------+------------------------------------------+
| NON_INDEXED | The column is not indexed.               |
+-------------+------------------------------------------+
| FUNCTION    | A function is associated with the column.|
+-------------+------------------------------------------+

With respect to the relationship, Crossdata supports:

+--------------+------------------------------------------+
| relationship | Description                              |
+==============+==========================================+
| EQ           | Equals.                                  |
+--------------+------------------------------------------+
| GT           | Greater than.                            |
+--------------+------------------------------------------+
| LT           | Lower than.                              |
+--------------+------------------------------------------+
| GET          | Greater or equal than.                   |
+--------------+------------------------------------------+
| LET          | Lower or equal than.                     |
+--------------+------------------------------------------+
| ASSIGN       | A value is to be assigned to a column.   |
+--------------+------------------------------------------+
| MATCH        | Compare with a Lucene-like expression.   |
+--------------+------------------------------------------+
| DISTINCT     | Distinct operator in a column.           |
+--------------+------------------------------------------+
| LIKE         | Search for a specified pattern.          |
+--------------+------------------------------------------+
| NOT_LIKE     | Opposite of LIKE operator.               |
+--------------+------------------------------------------+
| IN           | Search value in a list of values.        |
+--------------+------------------------------------------+
| NOT_IN       | Opposite of IN operator.                 |
+--------------+------------------------------------------+
| BETWEEN      | Select values within a range.            |
+--------------+------------------------------------------+
| NOT_BETWEEN  | Opposite of BETWEEN operator.            |
+--------------+------------------------------------------+
| DISTINCT     | Distinct modifier for select expressions.|
+--------------+------------------------------------------+

Metadata
--------

Metadata operations are those associated with the *IMetadataEngine* interface.

*   **CREATE_CATALOG**: Create new catalogs.
*   **ALTER_CATALOG**: Alter existing catalogs.
*   **DROP_CATALOG**: Delete existing catalogs.
*   **CREATE_TABLE**: Create a new table.
*   **ALTER_TABLE**: Alter the definition of an existing table.
*   **DROP_TABLE**: Drop an existing table.
*   **CREATE_INDEX**: Create a new index.
*   **DROP_INDEX**: Drop an existing index.

The next operations are related to Data Discovery.

*   **IMPORT_METADATA**: Provide metadata of the existent tables.

Storage
-------

Storage operations are those associated with the *IStorageEngine* interface.

*   **INSERT**: Insert a new row in the datastore. By default inserts are considered upsert operations.
*   **INSERT_IF_NOT_EXISTS**: Insert a new row in the datastore if that row does not exist without the upsert behaviour.
*   **DELETE_<column_type>_<relationship>**: Delete rows for the given type of columns and relations.
*   **UPDATE_<column_type>_<relationship>**: Update rows for the give type of columns and relations.
*   **TRUNCATE_TABLE**: Truncate the contents of an existing table.

Query
-----

Storage operations are those associated with the *IQueryEngine* interface.

*   **ASYNC_QUERY**: Support for asynchronous query execution.
*   **PROJECT**: Retrieve a set of columns from a specific table.
*   **SELECT_OPERATOR**: Select a set of output columns for a query with alias.
*   **SELECT_WINDOW**: Supports WINDOW clauses for streaming-like queries.
*   **SELECT_LIMIT**: Limit the number of output rows.
*   **SELECT_<join_type>_JOIN**: Support for INNER JOIN operations.
*   **SELECT_<join_type>_JOIN_PARTIAL_RESULTS**: Support for INNER JOIN with one source already defined.
*   **SELECT_ORDER_BY**: Support for ORDER BY clauses.
*   **SELECT_GROUP_BY**: Support for GROUP BY clauses.
*   **SELECT_CASE_WHEN**: Support for selecting a value with a CASE WHEN structure.
*   **SELECT_FUNCTIONS**: Support for using functions as a selector.
*   **SELECT_SUBQUERY**: Support for using virtual tables.
*   **FILTER_<column_type>_<relationship>**: Filter a column by a particular relationship.


The next type of joins are supported:

*   INNER
*   LEFT_OUTER
*   RIGHT_OUTER
*   FULL_OUTER
*   FULL_NATURAL
*   CROSS

More information
================

For more information, check the project `documentation <http://stratio.github.io/crossdata>`_.

