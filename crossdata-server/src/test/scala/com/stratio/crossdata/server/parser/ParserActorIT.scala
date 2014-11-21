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

package com.stratio.crossdata.server.parser

import com.stratio.crossdata.common.ask.Query
import com.stratio.crossdata.common.result.{StorageResult, MetadataResult, QueryStatus, QueryResult}
import com.stratio.crossdata.communication.ACK
import com.stratio.crossdata.core.parser.Parser
import com.stratio.crossdata.core.planner.Planner
import com.stratio.crossdata.core.validator.Validator
import com.stratio.crossdata.server.ServerActorTest
import com.stratio.crossdata.server.actors.{ParserActor, PlannerActor, ValidatorActor}
import com.stratio.crossdata.server.mocks.{MockCoordinatorActor, MockPlannerActor, MockValidatorActor}
import org.apache.log4j.Logger
import org.scalatest.Suite

import scala.concurrent.duration.DurationInt

class ParserActorIT extends ServerActorTest{

  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[ParserActorIT])

  val mockValidatorRef_i = system.actorOf(MockValidatorActor.props(), "TestMockValidatorActor_i")
  val mockPlannerRef_i= system.actorOf(MockPlannerActor.props(), "TestMockPlannerActor_i")
  val mockCoordinatorActor_i = system.actorOf(MockCoordinatorActor.props(),  "TestMockCoordinatorActor_i")

  val plannerRef0= system.actorOf(PlannerActor.props(mockCoordinatorActor_i,new Planner()), "TestPlannerActor0_i")
  val plannerRef1= system.actorOf(PlannerActor.props(coordinatorActor,new Planner()), "TestPlannerActor1_i")
  val validatorRef0 = system.actorOf(ValidatorActor.props(mockPlannerRef_i,new Validator()), "TestValidatorActor0_i")
  val validatorRef1 = system.actorOf(ValidatorActor.props(plannerRef0,new Validator()), "TestValidatorActor1_i")
  val validatorRef2 = system.actorOf(ValidatorActor.props(plannerRef1,new Validator()), "TestValidatorActor2_i")

  val parserActor0 = {
    system.actorOf(ParserActor.props(mockValidatorRef_i, new Parser()), "TestParserActor0_i")
  }
  val parserActor1 = {
    system.actorOf(ParserActor.props(validatorRef0, new Parser()), "TestParserActor1_i")
  }
  val parserActor2 = {
    system.actorOf(ParserActor.props(validatorRef1, new Parser()), "TestParserActor2_i")
  }
  val parserActor3 = {
    system.actorOf(ParserActor.props(validatorRef2, new Parser()), "TestParserActor3_i")
  }
  val user0="user0"
  val mynewcatalog="mynewCatalog"



  test("Select query; parser ,validator, planner, coordinator and mockConnector") {
    initialize()
    initializeTablesInfinispan()
    within(6000 millis) {
      parserActor3 ! Query(queryId + (1), catalogName, "SELECT " + catalogName + "." + tableName + ".name FROM " +
        catalogName + "." + tableName + "; ", user0)
      fishForMessage(6 seconds){
        case msg:QueryResult =>{
          assert(msg.getQueryId()==queryId + (1))
          true
        }
        case other:Any =>{
          logger.info("receiving message of type" + other.getClass() + " and  ignoring it")
          false
        }
      }
    }
  }

  test("Metadata query; parser ,validator, and mockPlanner") {
    initialize()
    initializeTablesInfinispan()
    within(6000 millis) {
      parserActor1 ! Query(queryId + (1), mynewcatalog, "create catalog " + mynewcatalog + ";", user0)
      fishForMessage(6 seconds) {
        case msg: ACK => {
          logger.info(" receiving message of type " + msg.getClass() + "from " + lastSender)
          assert(msg.queryId == queryId + (1))
          assert(msg.status== QueryStatus.PLANNED)
          true
        }
        case other: Any => {
          logger.info("receiving  message of type " + other.getClass() + " and ignoring it ")
          false
        }
      }
    }
  }

  test("Metadata query; parser ,validator, planner and mockCoordinator") {
    initialize()
    initializeTablesInfinispan()
    within(6000 millis) {
      parserActor2 ! Query(queryId + (1), mynewcatalog, "create catalog " + mynewcatalog + ";", user0)
      fishForMessage(6 seconds){
        case msg:ACK=>{
          logger.info("receiving message of type  " + msg.getClass() + "from  " + lastSender)
          assert(msg.queryId==queryId + (1))
          assert(msg.status== QueryStatus.EXECUTED)
          true
        }
        case other:Any =>{
          logger.info("receiving message of  type " + other.getClass() + "  and ignoring it")
          false
        }
      }
    }
  }

  test("Metadata query; parser ,validator, planner, coordinator and mockConnector") {
    initialize()
    initializeTablesInfinispan()
    within(6000 millis) {
      parserActor3 ! Query(queryId + (1), mynewcatalog, "create catalog mynewCatalog;", user0)
      fishForMessage(6 seconds){
        case msg:MetadataResult =>{
          logger.info("receiving message of type " + msg.getClass() + "from " + lastSender)
          assert(msg.getQueryId==queryId + (1))
          true
        }
        case other:Any =>{
          logger.info("receiving message   of type " + other.getClass() + " and ignoring  it")
          false
        }
      }
    }
    within(6000 millis) {
      val query="create TABLE mynewCatalog.demo ON CLUSTER " + myClusterName + "(field1 varchar PRIMARY KEY , " +
        "field2 varchar);"
      parserActor3 ! Query(queryId + (2), mynewcatalog, query,user0)
      fishForMessage(6 seconds){
        case msg:MetadataResult =>{
          logger.info("   receiving message of type " + msg.getClass() + " from " + lastSender)
          assert(msg.getQueryId==queryId + (2))
          true
        }
        case other:Any =>{
          logger.info("Receiving message of type " + other.getClass() + " and  ignoring it")
          false
        }
      }
    }
  }

  test("Insert query; parser ,validator, and mockPlanner") {
    initialize()
    initializeTablesInfinispan()
    within(6000 millis) {
      val myquery="INSERT INTO  " + catalogName + "." + tableName + " (name, age) VALUES (\"user0\", 88);"
      parserActor1 ! Query(queryId + (1), catalogName, myquery, user0)
      fishForMessage(6 seconds) {
        case msg: ACK => {
          logger.info("receiving    message  of type " + msg.getClass() + "  from " + lastSender)
          assert(msg.queryId == queryId + (1))
          assert(msg.status== QueryStatus.PLANNED)
          true
        }
        case other: Any => {
          logger.info("receiving message   of  type " + other.getClass() + "  and ignoring it  ")
          false
        }
      }
    }
  }

  test("Insert query; parser ,validator, planner and mockCoordinator") {
    initialize()
    initializeTablesInfinispan()
    within(6000 millis) {
      val myquery="INSERT INTO " + catalogName + "." + tableName + "(name, age) VALUES  (\"" + user0 + "\", 88);"
      parserActor2 ! Query(queryId + (1), catalogName, myquery, user0)
      fishForMessage(6 seconds){
        case msg:ACK=>{
          logger.info("receiving message of type " + msg.getClass() + "from   " + lastSender)
          assert(msg.queryId==queryId + (1))
          assert(msg.status== QueryStatus.EXECUTED)
          true
        }
        case other:Any =>{
          logger.info("receiving message  of  type  " + other.getClass() + " and ignoring it   ")
          false
        }
      }
    }
  }

  test("Insert query; parser ,validator, planner, coordinator and mockConnector") {
    initialize()
    initializeTablesInfinispan()
    val myquery="INSERT INTO " + catalogName + "." + tableName + "(name, age) VALUES (\"" + user0 + "\", 88);"
    within(6000 millis) {
      parserActor3 ! Query(queryId + (1), catalogName, myquery, user0)
      fishForMessage(6 seconds){
        case msg:StorageResult =>{
          logger.info(" receiving  message  of  type " + msg.getClass() + " from  " + lastSender)
          assert(msg.getQueryId==queryId + (1))
          true
        }
        case other:Any =>{
          logger.info("receivin g message of  type  " + other.getClass() + "  and ignoring it")
          false
        }
      }
    }
  }

}


