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

package com.stratio.meta.core.structures;

/**
 * Consistency levels in Cassandra.
 * @see <a href="http://www.datastax.com/documentation/cassandra/2.0/cassandra/dml/dml_config_consistency_c.html">Cassandra documentation</a>
 */
public enum Consistency {
    /**
     * All replica nodes should commit the operation.
     */
    ALL,

    /**
     * Any replica node may commit the operation.
     */
    ANY,

    /**
     * A set of replica nodes that form a quorum should commit the operation.
     */
    QUORUM,

    /**
     * One replica node should commit the operation.
     */
    ONE,

    /**
     * Two replica nodes should commit the operation.
     */
    TWO,

    /**
     * Three replica nodes should commit the operation.
     */
    THREE,

    /**
     * A quorum of replicas per datacenter should commit the operation.
     */
    EACH_QUORUM,

    /**
     * Only one replica node in the current datacenter should commit the operation.
     */
    LOCAL_ONE,

    /**
     * Only the quorum of replica nodes in the current datacenter should commit the operation.
     */
    LOCAL_QUORUM
}
