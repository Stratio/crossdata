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

package com.stratio.connector.twitter.metadata;

import java.util.HashMap;
import java.util.Map;

import com.stratio.crossdata.common.metadata.TableMetadata;

import twitter4j.TwitterStream;

/**
 * Twitter cluster class for twitter connector.
 */
public class TwitterCluster {

    private final String clusterName;
    private TwitterStream session;
    private final Map<String, TableMetadata> tables = new HashMap<>();

    /**
     * Class constructor.
     * @param clusterName The cluster name.
     * @param session The session.
     */
    public TwitterCluster(String clusterName, TwitterStream session) {
        this.clusterName = clusterName;
        this.session = session;
    }

    /**
     * Class constructor.
     * @param clusterName The cluster name.
     */
    public TwitterCluster(String clusterName) {
        this(clusterName, null);
    }

    public String getClusterName() {
        return clusterName;
    }

    public TwitterStream getSession() {
        return session;
    }

    public void setSession(TwitterStream session) {
        this.session = session;
    }

    public Map<String, TableMetadata> getTables() {
        return tables;
    }

    /**
     * Add a table metadata to the twitter connector tables.
     * @param tableMetadata The table metadata.
     */
    public void addTableMetadata(TableMetadata tableMetadata) {
        tables.put(tableMetadata.getName().getName(), tableMetadata);
    }

    /**
     * Get a table metadata of the twitter connector tables given a table name.
     * @param tableName The table name.
     * @return A {@link com.stratio.crossdata.common.metadata.TableMetadata}.
     */
    public TableMetadata getTableMetadata(String tableName) {
        return tables.get(tableName);
    }
}
