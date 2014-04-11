/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.common.utils;

import com.google.common.collect.Sets;

import java.util.Set;

public class MetaUtils {        
    
    public static Set<String> initials = Sets.newHashSet(
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
            "ADD",
            "LIST",
            "REMOVE",
            "STOP",
            "EXIT",
            "HELP",
            "QUIT",
            "DESCRIBE");    
    
    public static Set<String> noInitials = Sets.newHashSet(                      
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
            "MATCH");
    
    public static String getInitialsStatements() {
        String str = initials.toString();
        return str.substring(1, str.length()-1);
    }
    
    public static String getNoInitialsStatements() {
        String str = noInitials.toString();
        return str.substring(1, str.length()-1);
    }
    
}
