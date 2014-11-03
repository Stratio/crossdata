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

package com.stratio.crossdata.core.structures;

/**
 * Types of list processes supported by CROSSDATA.
 * <ul>
 * <li> PROCESS: List the existing running queries.
 * <li> UDF: List the existing UDF in the system.
 * <li> TRIGGER: List the existing triggers in the system.
 * </ul>
 */
public enum ListType {
    /**
     * List all running processes.
     */
    PROCESS,
    /**
     * List all known UDF.
     */
    UDF,
    /**
     * List all known triggers.
     */
    TRIGGER
}
