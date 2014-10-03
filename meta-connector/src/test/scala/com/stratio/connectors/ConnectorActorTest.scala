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

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.stratio.connectors.ConnectorActor
import com.stratio.connectors.config.ConnectConfig
import com.stratio.meta.common.connector.{IConnector, IMetadataEngine}
import com.stratio.meta.common.result.{MetadataResult, QueryResult}
import com.stratio.meta.communication.CreateTable
import com.stratio.meta2.common.data.{ClusterName, TableName}
import com.stratio.meta2.common.metadata.TableMetadata
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Suite}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object ConnectorActorTest{
}

class ConnectorActorTest extends FunSuite with ConnectConfig with MockFactory{
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[ConnectorActorTest])
  lazy val system1 = ActorSystem(clusterName, config)
  implicit val timeout = Timeout(5 seconds)

   test("Send MetadataInProgressQuery to Connector") {

     val m = mock[IConnector]
     val me = mock[IMetadataEngine]
     val slowfunc=() => { Thread.sleep(10000);QueryResult.createSuccessQueryResult() }
     (me.createTable _).expects(*,*).returning(slowfunc())
     (m.getMetadataEngine _).expects().returning(me)
     val message=CreateTable("queryId",new ClusterName("cluster"),new TableMetadata(new TableName("catalog","mytable"), null, null,null,null,null,null) )

     val connectorActor= system1.actorOf(ConnectorActor.props("myConnector",m), "connectorActor")
     class ExecutorForwarder extends Actor {
       var father:ActorRef=null
       def receive = {
         case "start"=> connectorActor forward message
         case _ =>  println("error; we shouldn't get this message")
       }
     }
     val executorForwarder=system1.actorOf(Props(new ExecutorForwarder()), "executorForwarder")
     executorForwarder ! "start"
     var future = ask(connectorActor, message)
     val result = Await.result(future, 3 seconds).asInstanceOf[MetadataResult]
     assert(result.getQueryId()=="queryId")
  }

}


