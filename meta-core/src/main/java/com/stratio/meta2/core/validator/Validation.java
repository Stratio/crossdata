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

package com.stratio.meta2.core.validator;

public enum Validation {
    MUST_NOT_EXIST_CATALOG,
    MUST_EXIST_CATALOG,
    MUST_EXIST_TABLE,
    MUST_NOT_EXIST_TABLE,
    MUST_NOT_EXIST_CLUSTER,
    MUST_EXIST_CLUSTER,
    MUST_EXIST_CONNECTOR,
    MUST_NOT_EXIST_CONNECTOR,
    MUST_EXIST_DATASTORE,
    MUST_NOT_EXIST_DATASTORE,
    VALID_DATASTORE_MANIFEST, VALID_CLUSTER_OPTIONS, VALID_CONNECTOR_OPTIONS, MUST_EXIST_ATTACH_CONNECTOR_CLUSTER, VALID_CONNECTOR_MANIFEST,
    MUST_EXIST_PROPERTIES,
    MUST_NOT_EXIST_INDEX,
    MUST_EXIST_INDEX,
    MUST_EXIST_COLUMN,
    MUST_NOT_EXIST_COLUMN,
    VALIDATE_TYPES,
    VALIDATE_SELECT

}
