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

import akka.actor.{ActorSystem, actorRef2Scala}
import com.stratio.connectors.MockConnectorActor
import com.stratio.meta.common.executionplan.{ExecutionType, MetadataWorkflow, ResultType}
import com.stratio.meta.common.result.MetadataResult
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.common.data.CatalogName
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta2.core.query._
import com.stratio.meta2.server.actors.{ConnectorManagerActor, CoordinatorActor}
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuiteLike, Suite}

import scala.concurrent.duration.DurationInt

class CoordinatorActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory with ServerConfig {
  //class CoordinatorActorIntegrationTest extends ActorReceiveUtils with FunSuiteLike with ServerConfig {
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[CoordinatorActorTest])
  lazy val system1 = ActorSystem(clusterName, config)

  val mockConnectorActor = system1.actorOf(MockConnectorActor.props(), "MockConnectorActor")
  val connectorManagerActor = system1.actorOf(ConnectorManagerActor.props(null), "ConnectorManagerActor")
  val coordinatorActor = system1.actorOf(CoordinatorActor.props(null, new Coordinator), "CoordinatorActor")

  val pq = new MetadataPlannedQuery(
    new MetadataValidatedQuery(
      new MetadataParsedQuery(
        new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog"))
        , null)
    )
    , new MetadataWorkflow("query_id-2384234-1341234-23434", mockConnectorActor, ExecutionType.CREATE_TABLE,
      ResultType.RESULTS)
  )

  test("Receiving MetadataQuery test (has to get back a successful response with the same queryId)") {
    within(5000 millis) {
      coordinatorActor ! pq
      val response = expectMsgType[MetadataResult]
      assert(response.getQueryId == "query_id-2384234-1341234-23434")
    }
  }

}
