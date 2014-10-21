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

import java.util

import com.stratio.crossdata.common.data.{ClusterName, TableName}
import com.stratio.crossdata.common.metadata.{TableMetadata, ColumnType}
import com.stratio.crossdata.common.result.ErrorResult
import com.stratio.crossdata.common.data.TableName
import com.stratio.crossdata.common.metadata.TableMetadata
import com.stratio.crossdata.core.metadata.MetadataManagerTestHelper
import com.stratio.crossdata.core.planner.{Planner, PlannerBaseTest, PlannerLogicalWorkflowTest}
import com.stratio.crossdata.server.ServerActorTest
import com.stratio.crossdata.server.actors.PlannerActor
import org.apache.log4j.Logger
import org.scalatest.Suite

import scala.concurrent.duration.DurationInt

class PlannerActorIT extends ServerActorTest{
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[PlannerActorIT])

  val plannerActor = system.actorOf(PlannerActor.props(coordinatorActor,new Planner()),"PlannerActor")
  val plannerLogicalWorkflowTest=new PlannerLogicalWorkflowTest()
  val plannerBaseTest=new PlannerBaseTest()
  val mdmth=new MetadataManagerTestHelper()


  //val inputText = "SELECT mycatalog.mytable.name mycatalog.mytable.age FROM mycatalog.mytable;"
  val inputText = "SELECT * FROM mycatalog.mytable;"
  val columns1 = Array( "name", "age" )
  override val columnTypes1 = Array(ColumnType.TEXT, ColumnType.INT)
  override val partitionKeys1 = Array("name")
  override val clusteringKeys1 = Array("name")
  val t1 = mdmth.defineTable(new ClusterName("mycluster"), "mycatalog", "mytable", columns1, columnTypes1, partitionKeys1, clusteringKeys1)
  //val workflow = plannerBaseTest.getWorkflow(inputText, "selTectBasicWhere", t1)
  val tablesMetadata:java.util.List[TableMetadata]=new util.ArrayList[TableMetadata]()
  val tables:java.util.List[TableName]=new util.ArrayList[TableName]()
  tables.add(new TableName("myCatalog","myTable"))
  selectValidatedQueryWrapper.addTableMetadata(t1)
  selectValidatedQueryWrapper.setTables(tables)


   test("Should return a KO message") {
    initialize()
    within(1000 millis) {
      plannerActor ! "anything; this doesn't make any sense"
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


