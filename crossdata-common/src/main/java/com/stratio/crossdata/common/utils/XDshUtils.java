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

import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Utility class for error messages management.
 */
public final class XDshUtils implements Serializable {

    private static final long serialVersionUID = 5133240276761110019L;

    public static final Set<String> INITIALS = Sets.newHashSet(
            "ADD",
            "ALTER",
            "ATTACH",
            "CLEAN",
            "CREATE",
            "DELETE",
            "DESCRIBE",
            "DETACH",
            "DISCOVER",
            "DROP",
            "EXIT",
            "EXPLAIN",
            "HELP",
            "IMPORT",
            "INSERT",
            "QUIT",
            "REGISTER",
            "REMOVE",
            "RESET",
            "SELECT",
            "TRUNCATE",
            "UNREGISTER",
            "UPDATE",
            "USE"
            );

    public static final Set<String> NON_INITIALS = Sets.newHashSet(
            "ADD",
            "ALTER",
            "AND",
            "AS",
            "ASC",
            "BETWEEN",
            "BIGINT",
            "BOOLEAN",
            "BY",
            "CASE",
            "CATALOG",
            "CATALOGS",
            "CLUSTER",
            "CLUSTERS",
            "CONNECTOR",
            "CONNECTORS",
            "CROSS",
            "CUSTOM",
            "DATASTORE",
            "DAY",
            "DAYS",
            "DEFAULT",
            "DESC",
            "DOUBLE",
            "DROP",
            "EXISTS",
            "END",
            "FALSE",
            "FLOAT",
            "FOR",
            "FROM",
            "FULL",
            "FULL_TEXT",
            "HAVING",
            "HOUR",
            "HOURS",
            "IF",
            "INDEX",
            "INNER",
            "INT",
            "INTO",
            "JOIN",
            "LEFT",
            "LIKE",
            "LIST",
            "LIMIT",
            "KEY",
            "MATCH",
            "MAP",
            "METADATA",
            "MINS",
            "MINUTES",
            "MINUTE",
            "NOT",
            "NULL",
            "OR",
            "ORDER",
            "ON",
            "OPTIONS",
            "OUTER",
            "PLAN",
            "PRIMARY",
            "PRIORITY",
            "PROCESS",
            "RIGHT",
            "SEC",
            "SECS",
            "SECOND",
            "SECONDS",
            "SERVERDATA",
            "SET",
            "STORAGE",
            "SYSTEM",
            "TABLE",
            "TABLES",
            "TEXT",
            "THEN",
            "TO",
            "TRUE",
            "TYPE",
            "USING",
            "VALUES",
            "WHERE",
            "WHEN",
            "WINDOW",
            "WITH"
            );

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
