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


import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta2.common.data.CatalogName
import org.apache.log4j.Logger

import scala.concurrent.duration.DurationInt
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.config.{ServerConfig, ActorReceiveUtils}
import com.stratio.meta2.server.actors.{ConnectorManagerActor, CoordinatorActor}
import akka.actor.{ActorSystem, actorRef2Scala}
import com.stratio.meta2.core.query._
//import org.scalamock.scalatest.MockFactory
import org.scalatest.{Suite, BeforeAndAfterAll}
import com.stratio.meta2.core.coordinator.Coordinator

//class CoordinatorActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory  with ServerConfig{
class CoordinatorActorTest extends ActorReceiveUtils with FunSuiteLike  with ServerConfig{
    this:Suite =>


    override lazy val logger =Logger.getLogger(classOf[CoordinatorActorIntegrationTest])
    lazy val system1 = ActorSystem(clusterName,config)

    val connectorManagerActor=system1.actorOf(ConnectorManagerActor.props(null),"ConnectorManagerActor")
    val coordinatorActor=system1.actorOf(CoordinatorActor.props(connectorManagerActor,new Coordinator),"CoordinatorActor")

    val pq = new SelectPlannedQuery(
        new ValidatedQuery(
          new NormalizedQuery(
            new SelectParsedQuery(
              new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog") )
              ,null)
          )
        ), new LogicalWorkflow(null)
    )


		test("Should return a KO message") {
		  within(1000 millis){
	  		val coordinatorActor=system.actorOf(CoordinatorActor.props(null,new Coordinator),"CoordinatorActor")
        coordinatorActor! "anything; this doesn't make any sense"
        expectMsg("KO") // bounded to the remainder of the 1 second
        /*
        val pq= new SelectPlannedQuery(null,null)
	  		expectMsg("Ok") // bounded to 1 second

	  		//val m = mock[IConnector]
	  		//(m.getConnectorName _).expects().returning("My New Connector")
	  		//assert(m.getConnectorName().equals("My New Connector"))
	  		*/
	  		}
		}

}