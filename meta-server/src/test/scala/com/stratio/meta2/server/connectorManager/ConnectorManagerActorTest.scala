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

package com.stratio.meta2.server.connectorManager

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


import com.stratio.connectors.MockConnectorActor
import com.stratio.meta.common.executionplan.ExecutionWorkflow
import com.stratio.meta.communication.Connect
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.common.data.{ClusterName, CatalogName}
import com.stratio.meta2.common.metadata.ColumnType
import com.stratio.meta2.core.query._
import com.stratio.meta2.server.actors.ConnectorManagerActor
import org.scalatest.FunSuiteLike

import com.stratio.meta2.core.metadata.{MetadataManager, MetadataManagerTestHelper}
import com.stratio.meta2.core.planner.SelectValidatedQueryWrapper
//import org.scalamock.scalatest.MockFactory

import org.apache.log4j.Logger
import org.scalatest.Suite

import scala.concurrent.duration.DurationInt

//class CoordinatorActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory  with ServerConfig{
class ConnectorManagerActorTest extends ActorReceiveUtils with FunSuiteLike with ServerConfig{
  this: Suite =>


  override lazy val logger = Logger.getLogger(classOf[ConnectorManagerActorTest])

  val connectorManagerActor = system.actorOf(ConnectorManagerActor.props(null), "ConnectorManagerActorTest")

  val baseQuery = new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog"))
  val selectedQuery = new SelectParsedQuery(baseQuery, null)
  val selectValidatedQuery = new SelectValidatedQuery(selectedQuery)
  val pq = new SelectPlannedQuery(selectValidatedQuery, new ExecutionWorkflow(null, null, null, null))

  test("Should return a KO message") {
    within(5000 millis) {

      val metadataManager=new MetadataManagerTestHelper()
      val myDatastore = metadataManager.createTestDatastore()
      metadataManager.createTestCluster("myCluster", myDatastore)
      metadataManager.createTestCatalog("myCatalog")
      metadataManager.createTestTable(new ClusterName("myCluster"), "myCatalog","table1", Array("name", "age"),
      Array(ColumnType.VARCHAR, ColumnType.INT), Array("name"), Array("name"))

      //val connectorActor = system.actorOf(MockConnectorActor.props(), "ConnectorActor")



      //expectMsg(Connect.getClass)
     }


      /*
      val pq= new SelectPlannedQuery(null,null)
      expectMsg("Ok") // bounded to 1 second

      //val m = mock[IConnector]
      //(m.getConnectorName _).expects().returning("My New CONNECTOR")
      //assert(m.getConnectorName().equals("My New CONNECTOR"))
      */
    }
  }


