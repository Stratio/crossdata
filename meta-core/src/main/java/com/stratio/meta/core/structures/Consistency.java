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
