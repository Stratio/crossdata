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

import com.stratio.meta.common.ask.Query
import com.stratio.meta.common.result.QueryStatus
import com.stratio.meta.communication.ACK
import com.stratio.meta2.common.result.ErrorResult
import com.stratio.meta2.core.parser.Parser
import com.stratio.meta2.core.planner.Planner
import com.stratio.meta2.core.validator.Validator
import com.stratio.meta2.server.ServerActorTest
import com.stratio.meta2.server.actors.{ParserActor, PlannerActor, ValidatorActor}
import com.stratio.meta2.server.mocks.{MockCoordinatorActor, MockPlannerActor, MockValidatorActor}
import org.apache.log4j.Logger
import org.scalatest.Suite

import scala.concurrent.duration.DurationInt

class ParserActorIT extends ServerActorTest{
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[ParserActorIT])

  val mockValidatorRef = system.actorOf(MockValidatorActor.props(), "TestMockValidatorActor")
  val mockPlannerRef= system.actorOf(MockPlannerActor.props(), "TestMockPlannerActor")
  val mockCoordinatorActor = system.actorOf(MockCoordinatorActor.props(),  "TestMockCoordinatorActor")

  val plannerRef0= system.actorOf(PlannerActor.props(coordinatorActor,new Planner()), "TestPlannerActor0")
  val validatorRef0 = system.actorOf(ValidatorActor.props(mockPlannerRef,new Validator()), "TestValidatorActor0")
  val validatorRef1 = system.actorOf(ValidatorActor.props(plannerRef0,new Validator()), "TestValidatorActor1")

  val parserActor0 = {
    system.actorOf(ParserActor.props(mockValidatorRef, new Parser()), "TestParserActor0")
  }
  val parserActor1 = {
    system.actorOf(ParserActor.props(validatorRef0, new Parser()), "TestParserActor1")
  }
  val parserActor2 = {
    system.actorOf(ParserActor.props(validatorRef1, new Parser()), "TestParserActor2")
  }


  test("Should return a KO message") {
    initialize()
    within(1000 millis) {
      parserActor0 ! "anything; this doesn't make any sense"
      val exception = expectMsgType[ErrorResult]
    }
  }

  test("Select query; only parser") {
    initialize()
    within(6000 millis) {
      parserActor0 ! Query(queryId + (1), "mycatalog", "SELECT mycatalog.mytable.name FROM mycatalog.mytable;", "user0")
      //val ack = expectMsgType[ACK]
      //assert(ack.queryId == queryId + (1))
      //assert(ack.status==QueryStatus.PARSED)
    }
  }

  test("Select query; parser and validatormock") {
    initialize()
    within(6000 millis) {
      parserActor0 ! Query(queryId + (1), "mycatalog", "SELECT mycatalog.mytable.name FROM mycatalog.mytable;", "user0")
      //val ack = expectMsgType[ACK]
      //assert(ack.queryId == queryId + (1))
      //assert(ack.status==QueryStatus.VALIDATED)
    }
  }

  test("Select query; parser ,validator and plannermock") {
    initialize()
    initializeTablesInfinispan()
    within(6000 millis) {
      parserActor1 ! Query(queryId + (1), "mycatalog", "SELECT mycatalog.mytable.name FROM mycatalog.mytable;", "user0")
      val ack = expectMsgType[ACK]
      assert(ack.queryId == queryId + (1))
      assert(ack.status==QueryStatus.PLANNED)
    }
  }


}


