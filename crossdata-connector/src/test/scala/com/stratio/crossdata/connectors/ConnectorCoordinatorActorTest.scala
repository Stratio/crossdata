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

package com.stratio.crossdata.connectors

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestProbe, ImplicitSender, TestKit}
import com.stratio.crossdata.common.executionplan._
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow
import com.stratio.crossdata.common.result.ErrorResult
import com.stratio.crossdata.communication.TriggerExecution
import com.stratio.crossdata.connectors.config.ConnectConfig
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Suite, FunSuite}

import scala.concurrent.duration._

class ConnectorCoordinatorActorTest  extends TestKit(ActorSystem("CoordTest")) with ImplicitSender with FunSuite with ConnectConfig with MockFactory{
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[ConnectorCoordinatorActorTest])




  test("coordinator should send the operation to the worker"){
    val connectorWorkerProbe = TestProbe()
    val conCoordinatorActor = TestActorRef(new ConnectorCoordinatorActor(connectorWorkerProbe.ref))
    val logicalWorkflow = new LogicalWorkflow(null, null, 5)
    val queryId = "001"
    val queryWorkflow = new QueryWorkflow(queryId, "b", ExecutionType.SELECT, ResultType.RESULTS, logicalWorkflow , false)
    val triggerOperationMessage = TriggerExecution(queryWorkflow, new ExecutionInfo)
    conCoordinatorActor ! triggerOperationMessage
    connectorWorkerProbe.expectMsg(queryWorkflow.getExecuteOperation(queryId))
  }

  test("coordinator should store the job"){
    val connectorWorkerProbe = TestProbe()
    val conCoordinatorActor = TestActorRef(new ConnectorCoordinatorActor(connectorWorkerProbe.ref))
    val logicalWorkflow = new LogicalWorkflow(null, null, 5)
    val queryId = "002"
    val queryWorkflow = new QueryWorkflow(queryId, "b", ExecutionType.SELECT, ResultType.RESULTS, logicalWorkflow , false)
    val nextExecutionInfo = new ExecutionInfo
    val triggerOperationMessage = TriggerExecution(queryWorkflow, nextExecutionInfo)
    conCoordinatorActor ! triggerOperationMessage
    connectorWorkerProbe.expectMsg(queryWorkflow.getExecuteOperation(queryId))
    val runningJobs = conCoordinatorActor.underlyingActor.runningJobs
    assert( runningJobs.contains(queryId))
    assert( runningJobs.get(queryId).get._2 == nextExecutionInfo )

  }

  test("coordinator should delete the job on error"){
    val connectorWorkerProbe = TestProbe()
    val conCoordinatorActor = TestActorRef(new ConnectorCoordinatorActor(connectorWorkerProbe.ref))
    val logicalWorkflow = new LogicalWorkflow(null, null, 5)
    val queryId = "002"
    val queryWorkflow = new QueryWorkflow(queryId, "b", ExecutionType.SELECT, ResultType.RESULTS, logicalWorkflow , false)
    val nextExecutionInfo = new ExecutionInfo
    val triggerOperationMessage = TriggerExecution(queryWorkflow, nextExecutionInfo)
    conCoordinatorActor ! triggerOperationMessage
    connectorWorkerProbe.expectMsg(queryWorkflow.getExecuteOperation(queryId))
    val runningJobs = conCoordinatorActor.underlyingActor.runningJobs
    assert( runningJobs.contains(queryId))
    assert( runningJobs.get(queryId).get._2 == nextExecutionInfo )

    val errorResult = new ErrorResult(new RuntimeException())
    errorResult.setQueryId(queryId)

    conCoordinatorActor ! errorResult
    connectorWorkerProbe.expectNoMsg(1 seconds)
    assert(conCoordinatorActor.underlyingActor.runningJobs.isEmpty)
  }

  test("coordinator should send the next workflow to the next connector actor"){
    val connectorWorkerProbe = TestProbe()
    val conCoordinatorActor = TestActorRef(new ConnectorCoordinatorActor(connectorWorkerProbe.ref))
    val logicalWorkflow = new LogicalWorkflow(null, null, 5)
    val queryId = "002"
    val queryWorkflow = new QueryWorkflow(queryId, "b", ExecutionType.SELECT, ResultType.RESULTS, logicalWorkflow , false)
    val nextExecutionInfo = new ExecutionInfo
    val triggerOperationMessage = TriggerExecution(queryWorkflow, nextExecutionInfo)
    conCoordinatorActor ! triggerOperationMessage
    connectorWorkerProbe.expectMsg(queryWorkflow.getExecuteOperation(queryId))
    val runningJobs = conCoordinatorActor.underlyingActor.runningJobs
    assert( runningJobs.contains(queryId))
    assert( runningJobs.get(queryId).get._2 == nextExecutionInfo )

    val errorResult = new ErrorResult(new RuntimeException())
    errorResult.setQueryId(queryId)

    conCoordinatorActor ! errorResult
    connectorWorkerProbe.expectNoMsg(1 seconds)
    assert(conCoordinatorActor.underlyingActor.runningJobs.isEmpty)
  }




}