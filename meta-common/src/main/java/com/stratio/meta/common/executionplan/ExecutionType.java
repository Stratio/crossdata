/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.common.executionplan;

/**
 * Operations to be executed on the ConnectorActor.
 */
public enum ExecutionType {

    //IMetadata
    CREATE_CATALOG,
    DROP_CATALOG,
    CREATE_TABLE,
    DROP_TABLE,
    CREATE_INDEX,
    DROP_INDEX,

    //IStorage
    INSERT,
    INSERT_BATCH,

    //IQuery
    SELECT,

    //Connector operations
    ATTACH_CLUSTER,
    DETACH_CLUSTER,
    ATTACH_CONNECTOR,
    DETACH_CONNECTOR

}
