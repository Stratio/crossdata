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

package com.stratio.crossdata.common.utils;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Utility class for error messages management.
 */
public final class XDshUtils {

    public static final Set<String> INITIALS = Sets.newHashSet(
            "CREATE",
            "ALTER",
            "DROP",
            "SET",
            "EXPLAIN",
            "TRUNCATE",
            "INSERT",
            "UPDATE",
            "DELETE",
            "SELECT",
            "USE",
            "ADD",
            "LIST",
            "REMOVE",
            "STOP",
            "EXIT",
            "HELP",
            "QUIT",
            "DESCRIBE",
            "ATTACH",
            "RESET");

    public static final Set<String> NON_INITIALS = Sets.newHashSet(
            "KEYSPACE",
            "NOT",
            "WITH",
            "TABLE",
            "IF",
            "EXISTS",
            "AND",
            "USE",
            "SET",
            "OPTIONS",
            "ANALYTICS",
            "TRUE",
            "FALSE",
            "CONSISTENCY",
            "ALL",
            "ANY",
            "QUORUM",
            "ONE",
            "TWO",
            "THREE",
            "EACH_QUORUM",
            "LOCAL_ONE",
            "LOCAL_QUORUM",
            "PLAN",
            "FOR",
            "INDEX",
            "UDF",
            "PROCESS",
            "TRIGGER",
            "ON",
            "USING",
            "TYPE",
            "PRIMARY",
            "KEY",
            "INTO",
            "COMPACT",
            "STORAGE",
            "CLUSTERING",
            "ORDER",
            "VALUES",
            "WHERE",
            "IN",
            "FROM",
            "WINDOW",
            "LAST",
            "ROWS",
            "INNER",
            "JOIN",
            "BY",
            "LIMIT",
            "DISABLE",
            "DISTINCT",
            "COUNT",
            "AS",
            "BETWEEN",
            "ASC",
            "DESC",
            "LIKE",
            "HASH",
            "FULLTEXT",
            "CUSTOM",
            "GROUP",
            "AGGREGATION",
            "MAX",
            "MIN",
            "AVG",
            "TOKEN",
            "LUCENE",
            "DEFAULT",
            "SECONDS",
            "MINUTES",
            "HOURS",
            "DAYS",
            "MATCH",
            "DATASTORE",
            "CONNECTOR",
            "CONNECTORS",
            "CLUSTER",
            "METADATA",
            "CATALOG");

    /**
     * Private class constructor as all methods are static.
     */
    private XDshUtils() {

    }

    /**
     * Get initial words of the grammar.
     * @return list of initials words of the grammar.
     */
    public static String getInitialsStatements() {
        String str = INITIALS.toString();
        return str.substring(1, str.length() - 1);
    }


    /**
     * Get non-initial words of the grammar.
     * @return list of non-initials words of the grammar.
     */
    public static String getNoInitialsStatements() {
        String str = NON_INITIALS.toString();
        return str.substring(1, str.length() - 1);
    }

}
