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

package com.stratio.meta2.server.planner

import java.util

import akka.actor.ActorSystem
import com.stratio.meta.communication.ACK
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.common.data.{ColumnName, CatalogName, ClusterName, TableName}
import com.stratio.meta2.common.metadata.ColumnType
import com.stratio.meta2.core.engine.Engine
import com.stratio.meta2.core.query._
import com.stratio.meta2.core.statements.{CreateTableStatement, SelectStatement}
import com.stratio.meta2.server.actors._
import com.stratio.meta2.server.utilities.createEngine
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuiteLike, Suite}

import scala.concurrent.duration.DurationInt


class PlannerActorIntegrationTest extends ActorReceiveUtils with FunSuiteLike with ServerConfig with MockFactory {
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[PlannerActorIntegrationTest])
  lazy val system1 = ActorSystem(clusterName, config)
  val engine: Engine = createEngine.create()
  val connectorManagerRef = system1.actorOf(ConnectorManagerActor.props(null), "TestConnectorManagerActor")
  val coordinatorRef = system.actorOf(CoordinatorActor.props(connectorManagerRef, engine.getCoordinator()), "TestCoordinatorActor")
  val plannerActor = system.actorOf(PlannerActor.props(coordinatorRef, engine.getPlanner()), "TestPlannerActor")

  var queryId="query_id-2384234-1341234-23434"
  var tablename = new com.stratio.meta2.common.data.TableName("catalog", "table")
  val selectParsedQuery = new SelectParsedQuery(
        new BaseQuery(
          queryId,
          "select * from myQuery;",
          new CatalogName("myCatalog")
        ), new SelectStatement(tablename)
  )
  val map0=new util.HashMap[ColumnName,ColumnType]()
  map0.put(new ColumnName("keyspace","table","column"),ColumnType.BOOLEAN)
  val list0=new util.ArrayList[ColumnName]()
  list0.add(new ColumnName("keyspace","table","column"))
  val metadataStatement=new CreateTableStatement(
    new TableName("mycatalog", "mytable"), new ClusterName("cluster"),map0,list0,list0
  )
  val metadataParsedQuery = new MetadataParsedQuery(
    new BaseQuery(
      queryId,
      "select * from myQuery;",
      new CatalogName("myCatalog")
    ), metadataStatement
  )
  val mdvq=new MetadataValidatedQuery(metadataParsedQuery)
  val svq=new SelectValidatedQuery(selectParsedQuery)

  test("Metadata Planner->Coordinator->ConnectorManager->Ok: sends a query and should recieve Ok") {
    within(5000 millis) {
      plannerActor ! mdvq
      expectMsg(ACK) // bounded to 1 second
      //val ack:ACK=expectMsg(ACK).asInstanceOf[ACK]// bounded to 1 second
      assert(true)
    }
  }

  /*
  test("Select Planner->Coordinator->ConnectorManager->Ok: sends a query and should recieve Ok") {
    within(5000 millis) {
      plannerActor ! svq
      expectMsg(ACK) // bounded to 1 second
      assert(true)
    }
  }
  */

}


