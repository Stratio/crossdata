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

package com.stratio.crossdata.common.ask

/**
 * Types of supported API operations.
 */
@SerialVersionUID(1L)
object APICommand extends Enumeration {
  type APICommand = Value
  val LIST_CATALOGS = Value("LIST_CATALOGS")
  val LIST_TABLES = Value("LIST_TABLES")
  val LIST_COLUMNS = Value("LIST_COLUMNS")
  val LIST_CONNECTORS=Value("LIST_CONNECTORS")
  val ADD_MANIFEST = Value("ADD_MANIFEST")
  val DROP_MANIFEST = Value("DROP_MANIFEST")
  val RESET_METADATA = Value("RESET_METADATA")
  val CLEAN_METADATA = Value("CLEAN_METADATA")
  val EXPLAIN_PLAN = Value("EXPLAIN_PLAN")
}
