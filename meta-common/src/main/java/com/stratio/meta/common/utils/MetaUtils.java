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
            "QUIT");    
    
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
            "DAYS");
    
    public static String getInitialsStatements() {
        StringBuilder sb = new StringBuilder(initials.toString());
        return sb.substring(1, sb.length()-1);
    }
    
    public static String getNoInitialsStatements() {
        StringBuilder sb = new StringBuilder(noInitials.toString());
        return sb.substring(1, sb.length()-1);
    }
    
}
