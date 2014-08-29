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

package com.stratio.meta.communication

import com.stratio.meta.common.result.QueryStatus

case class ACK(queryId: String, status: QueryStatus)
case class Connect(msg: String)
case class Reply(msg: String)
case class Disconnect(userId: String)
//Connector messages
case class MetadataEngineRequest(msg: String) //TODO: change params
case class DataStoreNameRequest(msg: String) //TODO: change params
case class InitRequest(msg: String) //TODO: change params
case class ConnectRequest(msg: String) //TODO: change params
case class CloseRequest(msg: String) //TODO: change params
case class IsConnectedRequest(msg: String) //TODO: change params
case class StorageEngineRequest(msg: String) //TODO: change params
case class QueryEngineRequest(msg: String) //TODO: change params
 

