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

import com.stratio.meta.common.exceptions.ExecutionException
import com.stratio.meta.common.result.{CommandResult, MetadataResult, QueryResult}
import com.stratio.meta2.server.ServerActorTest
import org.apache.log4j.Logger
import org.scalatest.Suite

import scala.concurrent.duration.DurationInt


class CoordinatorActorTest extends ServerActorTest{
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[CoordinatorActorTest])
  //lazy val system1 = ActorSystem(clusterName, config)

  test("Should return a KO message") {
    initialize()
    within(1000 millis) {
      coordinatorActor ! "anything; this doesn't make any sense"
      val exception = expectMsgType[ExecutionException]
    }
  }

  test("Select query") {
    initialize()
    connectorActor !(queryId + (1), "updatemylastqueryId")
    coordinatorActor ! selectPlannedQuery
    expectMsgType[QueryResult]
  }

  test("Storage query") {
    initialize()
    initializeTablesInfinispan()
    connectorActor !(queryId + (2), "updatemylastqueryId")
    coordinatorActor ! storagePlannedQuery
    expectMsgType[CommandResult]
  }

  test("Metadata query") {
    initialize()
    connectorActor !(queryId + (3), "updatemylastqueryId")
    coordinatorActor ! metadataPlannedQuery0
    expectMsgType[MetadataResult]

    initializeTablesInfinispan()
    connectorActor !(queryId + (4), "updatemylastqueryId")
    coordinatorActor ! metadataPlannedQuery1
    expectMsgType[MetadataResult]

  }

}
