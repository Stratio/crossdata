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

package com.stratio.meta2.server.parser

import com.stratio.meta2.common.result.ErrorResult
import com.stratio.meta2.core.parser.Parser
import com.stratio.meta2.server.ServerActorTest
import com.stratio.meta2.server.actors.ParserActor
import com.stratio.meta2.server.mocks.MockValidatorActor
import org.apache.log4j.Logger
import org.scalatest.Suite

import scala.concurrent.duration.DurationInt

class ParserActorIT extends ServerActorTest{
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[ParserActorIT])

  val validatorRef = system.actorOf(MockValidatorActor.props(), "TestValidatorActor")
  val parserActor = {
    system.actorOf(ParserActor.props(validatorRef, new Parser()), "TestParserActor")
  }

   test("Should return a KO message") {
    initialize()
    within(1000 millis) {
      parserActor ! "anything; this doesn't make any sense"
      val exception = expectMsgType[ErrorResult]
    }
  }

  /*
  test("Select query") {
    initialize()
    connectorActor !(queryId + (1), "updatemylastqueryId")
    plannerActor ! selectValidatedQueryWrapper
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

    connectorActor !(queryId + (4), "updatemylastqueryId")
    coordinatorActor ! metadataPlannedQuery1
    expectMsgType[MetadataResult]

  }
  */


}


