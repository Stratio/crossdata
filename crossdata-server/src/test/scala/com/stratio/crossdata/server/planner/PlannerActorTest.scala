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

package com.stratio.crossdata.server.planner

import com.stratio.crossdata.common.result.ErrorResult
import com.stratio.crossdata.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.crossdata.core.engine.Engine
import com.stratio.crossdata.server.actors.{ConnectorManagerActor, CoordinatorActor, PlannerActor}
import com.stratio.crossdata.server.utilities.createEngine
import org.apache.log4j.Logger
import org.scalatest.{FunSuiteLike, Suite}

import scala.concurrent.duration.DurationInt

class PlannerActorTest extends ActorReceiveUtils with FunSuiteLike with ServerConfig {
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[PlannerActorTest])
  //lazy val system1 = ActorSystem(clusterName, config)
  val engine: Engine = createEngine.create()
  val connectorManagerRef = system.actorOf(ConnectorManagerActor.props(), "TestConnectorManagerActor")
  val coordinatorRef = system.actorOf(CoordinatorActor.props(connectorManagerRef, engine.getCoordinator()), "TestCoordinatorActor")
  val plannerActor = system.actorOf(PlannerActor.props(coordinatorRef, engine.getPlanner()), "TestPlannerActor")

  test("Should return a KO message") {
    within(1000 millis) {
      plannerActor ! "non-sense making message"
      expectMsg(_:ErrorResult)// bounded to 1 second
      assert(true)
    }
  }
}


