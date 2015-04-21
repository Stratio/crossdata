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

package com.stratio.crossdata.common.executionplan;

/**
 * Operations to be executed on the ConnectorActor.
 */
public enum ExecutionType {

    //IMetadata
    CREATE_CATALOG,
    DROP_CATALOG,
    ALTER_CATALOG,
    CREATE_TABLE,
    CREATE_TABLE_AND_CATALOG,
    DROP_TABLE,
    ALTER_TABLE,
    CREATE_INDEX,
    DROP_INDEX,
    DISCOVER_METADATA,
    IMPORT_CATALOGS,
    IMPORT_CATALOG,
    IMPORT_TABLE,
    REGISTER_TABLE,

    //IStorage
    INSERT,
    INSERT_BATCH,
    INSERT_FROM_SELECT,
    DELETE_ROWS,
    UPDATE_TABLE,
    TRUNCATE_TABLE,

    //IQuery
    SELECT,

    //CONNECTOR operations
    ATTACH_CLUSTER,
    DETACH_CLUSTER,
    ATTACH_CONNECTOR,
    DETACH_CONNECTOR,
    ALTER_CLUSTER,
    FORCE_DETACH_CONNECTOR
}
