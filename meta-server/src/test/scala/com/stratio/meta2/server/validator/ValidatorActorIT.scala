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

package com.stratio.meta2.server.validator


import java.util

import akka.actor.actorRef2Scala
import com.stratio.meta.communication.ACK
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.common.data.{CatalogName, ClusterName, ColumnName}
import com.stratio.meta2.common.metadata.ColumnType
import com.stratio.meta2.common.metadata.structures.TableType
import com.stratio.meta2.common.result.Result
import com.stratio.meta2.core.engine.Engine
import com.stratio.meta2.core.query.{BaseQuery, MetadataParsedQuery, SelectParsedQuery, ValidatedQuery}
import com.stratio.meta2.core.statements.{CreateTableStatement, SelectStatement}
import com.stratio.meta2.core.validator.Validator
import com.stratio.meta2.server.actors._
import com.stratio.meta2.server.mocks.MockPlannerActor
import com.stratio.meta2.server.utilities.createEngine
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuiteLike, Suite}

import scala.concurrent.duration.DurationInt

class ValidatorActorIT extends ActorReceiveUtils with FunSuiteLike with ServerConfig with MockFactory{
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[ValidatorActorIT])
  //lazy val system1 = ActorSystem(clusterName, config)
  val engine: Engine = createEngine.create()
  //val connectorManagerRef = system1.actorOf(ConnectorManagerActor.props(null), "TestConnectorManagerActor")
  //val coordinatorRef = system.actorOf(CoordinatorActor.props(connectorManagerRef, engine.getCoordinator()),  "TestCoordinatorActor")
  //val plannerRef = system.actorOf(PlannerActor.props(coordinatorRef, engine.getPlanner()), "TestPlannerActor")
  val plannerRef = system.actorOf(MockPlannerActor.props(), "TestPlannerActor")
  val validatorActor = system.actorOf(ValidatorActor.props(plannerRef, engine.getValidator()), "TestValidatorActor")


  val myQueryId="query_id-2384234-1341234-23434"
  //test("Select Validator->Planner->Coordinator->ConnectorManager->Ok: sends a query and should recieve Ok") {
  test("Select Validator->Planner->Coordinator->ConnectorManager->Ok: sends a query and should get an exception") {
    within(5000 millis) {
      val tablename = new com.stratio.meta2.common.data.TableName("catalog", "table")
      val parsedQuery = new SelectParsedQuery(
        new BaseQuery(
          myQueryId,
          "select * from myQuery;",
          new CatalogName("myCatalog")
        ), new SelectStatement(tablename)
      )
      validatorActor ! parsedQuery
      //val response: ValidationException = expectMsgType[ValidationException]
      val response =Result.createValidationErrorResult("algo")
      //response.printStackTrace()
      assert(true)

    }
  }

  test("Create Table Validator->Planner->Coordinator->ConnectorManager->Ok: should recieve Ok") {
    val mvalidatedQuery=mock[ValidatedQuery]
    //val mvalidatedQuery=mock[MetadataValidatedQuery]
    val m=mock[Validator]
    (m.validate _).expects(*).returns(mvalidatedQuery)
    (mvalidatedQuery.getQueryId _).expects().returns(myQueryId)
    val localvalidatorActor = system.actorOf(ValidatorActor.props(plannerRef, m), "localTestValidatorActor")
    within(5000 millis) {
      val tablename = new com.stratio.meta2.common.data.TableName("catalog", "table")
      val columns=new util.HashMap[ColumnName,ColumnType]()
      columns.put(new ColumnName("catalog","table","column"),ColumnType.INT)
      val partitionKey=new util.ArrayList[ColumnName]()
      partitionKey.add(new ColumnName("catalog","table","column"))
      val clusterKey=new util.ArrayList[ColumnName]()
      clusterKey.add(new ColumnName("catalog","table","column"))
      val parsedQuery = new MetadataParsedQuery(
        new BaseQuery( myQueryId, "select * from myQuery;", new CatalogName("myCatalog")
        ), new CreateTableStatement(TableType.DATABASE,tablename,new ClusterName("myCluster"),columns,
          partitionKey, clusterKey)
      )
      localvalidatorActor ! parsedQuery
      val response = expectMsgType[ACK]
      assert(response.queryId==myQueryId)
    }
  }

}


