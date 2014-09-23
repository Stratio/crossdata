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


import akka.actor.ActorSystem
import com.stratio.meta.common.logicalplan.LogicalWorkflow
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.common.data.CatalogName
import com.stratio.meta2.core.query._
import com.stratio.meta2.server.actors.ConnectorManagerActor
import org.scalatest.FunSuiteLike
//import org.scalamock.scalatest.MockFactory
import org.scalatest.Suite
import org.apache.log4j.Logger
import scala.concurrent.duration.DurationInt

//class CoordinatorActorTest extends ActorReceiveUtils with FunSuiteLike with MockFactory  with ServerConfig{
class ConnectorManagerActorTest extends ActorReceiveUtils with FunSuiteLike  with ServerConfig{
    this:Suite =>


    override lazy val logger =Logger.getLogger(classOf[ConnectorManagerActorTest])
    lazy val system1 = ActorSystem(clusterName,config)

    val connectorManagerActor=system1.actorOf(ConnectorManagerActor.props(null),"ConnectorManagerActor")

    val pq = new SelectPlannedQuery(
        new SelectValidatedQuery(
          new SelectParsedQuery(
              new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog"))
          ,null))
        , new LogicalWorkflow(null)
    )


		test("Should return a KO message") {
		  within(1000 millis){
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
