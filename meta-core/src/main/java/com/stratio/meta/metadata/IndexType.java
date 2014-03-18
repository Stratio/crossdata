package com.stratio.meta.metadata;

/**
 * Types of index supported by Cassandra.
 */
public enum IndexType {

    /**
     * Composites indexed created by default on Cassandra.
     */
    COMPOSITES,

    /**
     * Custom index defined by a user provided Class.
     */
    CUSTOM
}
