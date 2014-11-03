/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.metadata;

/**
 * Operations supported by an {@link com.stratio.crossdata.common.connector.IConnector}.
 */
public enum Operations {

    /**
     * The engine supports creating new catalogs.
     */
    CREATE_CATALOG("CREATE_CATALOG"),

    /**
     * The engine supports catalog alteration given an existing catalog.
     */
    ALTER_CATALOG("ALTER_CATALOG"),

    /**
     * The engine supports creating deleting existing catalogs.
     */
    DROP_CATALOG("DROP_CATALOG"),

   /**
     * The engine supports creating new tables given an existing catalog.
     */
    CREATE_TABLE("CREATE_TABLE"),

    /**
     * The engine supports table alteration given an existing table.
     */
    ALTER_TABLE("ALTER_TABLE"),

    /**
     * The engine supports deleting new tables given an existing catalog.
     */
    DROP_TABLE("DROP_TABLE"),

    /**
     * The engine supports updating the data of a table according to some conditions.
     */
    UPDATE_TABLE("UPDATE_TABLE"),

    /**
     * The engine supports deleting all the data from a table without removing its metadata.
     */
    TRUNCATE_TABLE("TRUNCATE_TABLE"),

    /**
     * The engine supports inserting data in existing tables.
     */
    INSERT("INSERT"),

    /**
     * The engine supports deleting elements from existing tables.
     */
    DELETE("DELETE"),

    /**
     * The engine supports index creation from existing tables.
     */
    CREATE_INDEX("CREATE_INDEX"),

    /**
     * The engine supports index deletion from existing tables.
     */
    DROP_INDEX("DROP_INDEX"),


    /**
     * The engine supports retrieving a set of columns from a specific table.
     */
    PROJECT("PROJECT"),

    /**
     * The engine supports asynchronous query execution.
     */
    ASYNC_QUERY("ASYNC_QUERY"),

    /**
     * The engine supports aliasing output names.
     */
    SELECT_OPERATOR("SELECT_OPERATOR"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.statements.structures.window.Window} logical
     * plans for streaming-like datastores.
     */
    SELECT_WINDOW("SELECT_WINDOW"),

    /**
     * The engine supports limiting the number of results returned in a query.
     */
    SELECT_LIMIT("SELECT_LIMIT"),

    /**
     * The engine supports inner joins.
     */
    SELECT_INNER_JOIN("SELECT_INNER_JOIN"),

    /**
     * The engine supports inner joins with partial results.
     */
    SELECT_INNER_JOIN_PARTIALS_RESULTS("SELECT_INNER_JOIN_PARTIALS_RESULTS"),

    /**
     * The engine supports order by clauses.
     */
    SELECT_ORDER_BY("SELECT_ORDER_BY"),

    /**
     * The engine supports group by clauses.
     */
    SELECT_GROUP_BY("SELECT_GROUP_BY"),

    /**
     * The engine supports aggregator operations (e.g., sum, avg, etc.) on a Select statement.
     */
    SELECT_FUNCTIONS("SELECT_FUNCTIONS"),

    /**
     * The engine supports in relationships in {@link com.stratio.crossdata.common.logicalplan.Filter}
     * operations.
     */
    SELECT_WHERE_IN("SELECT_WHERE_IN"),

    /**
     * The engine supports between relationships in {@link com.stratio.crossdata.common.logicalplan.Filter}
     * operations.
     */
    SELECT_WHERE_BETWEEN("SELECT_WHERE_BETWEEN"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are part of the primary key with an equal operator.
     */
    FILTER_PK_EQ("FILTER_PK_EQ"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are part of the primary key with greater than operator.
     */
    FILTER_PK_GT("FILTER_PK_GT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are part of the primary key with less than operator.
     */
    FILTER_PK_LT("FILTER_PK_LT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are part of the primary key with greater than or equal operator.
     */
    FILTER_PK_GET("FILTER_PK_GET"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are part of the primary key with less than or equal operator.
     */
    FILTER_PK_LET("FILTER_PK_LET"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are part of the primary key with distinct operator.
     */
    FILTER_PK_DISTINCT("FILTER_PK_DISTINCT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are part of the primary key with like operator.
     */
    FILTER_PK_LIKE("FILTER_PK_LIKE"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are not indexed by the underlying datastore with an equal operator.
     */
    FILTER_NON_INDEXED_EQ("FILTER_NON_INDEXED_EQ"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are not indexed by the underlying datastore with a greater than operator.
     */
    FILTER_NON_INDEXED_GT("FILTER_NON_INDEXED_GT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are not indexed by the underlying datastore with a less than operator.
     */
    FILTER_NON_INDEXED_LT("FILTER_NON_INDEXED_LT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are not indexed by the underlying datastore with greater than or equal operator.
     */
    FILTER_NON_INDEXED_GET("FILTER_NON_INDEXED_GET"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are not indexed by the underlying datastore with less than or equal operator.
     */
    FILTER_NON_INDEXED_LET("FILTER_NON_INDEXED_LET"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that are not indexed by the underlying datastore with a distinct operator.
     */
    FILTER_NON_INDEXED_DISTINCT("FILTER_NON_INDEXED_DISTINCT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that have an associated index in the underlying datastore with an equal operator.
     */
    FILTER_INDEXED_EQ("FILTER_INDEXED_EQ"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that have an associated index in the underlying datastore with a greater than operator.
     */
    FILTER_INDEXED_GT("FILTER_INDEXED_GT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that have an associated index in the underlying datastore with a less than operator.
     */
    FILTER_INDEXED_LT("FILTER_INDEXED_LT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that have an associated index in the underlying datastore with a greater than or equal operator.
     */
    FILTER_INDEXED_GET("FILTER_INDEXED_GET"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that have an associated index in the underlying datastore with a less than or equal operator.
     */
    FILTER_INDEXED_LET("FILTER_INDEXED_LET"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations on columns
     * that have an associated index in the underlying datastore with a distinct operator.
     */
    FILTER_INDEXED_DISTINCT("FILTER_INDEXED_DISTINCT"),

    /**
     * The engine supports full text search syntax in {@link com.stratio.crossdata.common.logicalplan.Filter}
     * operations.
     */
    FILTER_FULLTEXT("FILTER_FULLTEXT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations using
     * a function as part of a relation using with an equal operator.
     */
    FILTER_FUNCTION_EQ("FILTER_FUNCTION_EQ"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations using
     * a function as part of a relation using with a greater than operator.
     */
    FILTER_FUNCTION_GT("FILTER_FUNCTION_GT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations using
     * a function as part of a relation using with a less than operator.
     */
    FILTER_FUNCTION_LT("FILTER_FUNCTION_LT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations using
     * a function as part of a relation using with a greater than or equal operator.
     */
    FILTER_FUNCTION_GET("FILTER_FUNCTION_GET"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations using
     * a function as part of a relation using with a less than or equal operator.
     */
    FILTER_FUNCTION_LET("FILTER_FUNCTION_LET"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.Filter} operations using
     * a function as part of a relation using with a distinct operator.
     */
    FILTER_FUNCTION_DISTINCT("FILTER_FUNCTION_DISTINCT"),

    /**
     * The engine supports {@link com.stratio.crossdata.common.logicalplan.PartialResults} operations
     * to read a list of partial results.
     */
    PARTIAL_RESULTS("PARTIAL_RESULTS");

    private String operationsStr;

    Operations(String operationsStr) {
        this.operationsStr = operationsStr;
    }

    public String getOperationsStr() {
        return operationsStr;
    }

}
