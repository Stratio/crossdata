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

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.routing.RoundRobinRouter
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

object ConnectorActorTest {
}

class ConnectorActorTest extends FunSuite with ConnectConfig with MockFactory {
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[ConnectorActorTest])
  lazy val system1 = ActorSystem(clusterName, config)
  implicit val timeout = Timeout(10 seconds)


  test("Send 2 slow MetadataInProgressQuery to two connectors to test concurrency") {

    val queryId = "queryId"
    val m = mock[IConnector]
    val me = mock[IMetadataEngine]
    val m2 = mock[IConnector]
    val me2 = mock[IMetadataEngine]
    val slowfunc = () => {
      println("very slow function")
      for (i <- 1 to 5) {
        Thread.sleep(1000)
        println(i + " seconds gone by")
      }
      QueryResult.createSuccessQueryResult()
    }
    val slowfunc2 = () => {
      println("very slow function")
      for (i <- 1 to 3) {
        Thread.sleep(1500)
        println(i + " seconds gone by")
      }
      QueryResult.createSuccessQueryResult()
    }
    (me.createTable _).expects(*, *).onCall((ClusterName, TableMetadata) => { slowfunc() })
    (m.getMetadataEngine _).expects().returning(me)

    (me2.createTable _).expects(*, *).onCall((ClusterName, TableMetadata) => { slowfunc2() })
    (m2.getMetadataEngine _).expects().returning(me2)

    val ca1 = system1.actorOf(ConnectorActor.props("myConnector", m))
    val ca2 = system1.actorOf(ConnectorActor.props("myConnector2", m2))

    val message = CreateTable(queryId, new ClusterName("cluster"), new TableMetadata(new TableName("catalog", "mytable"), null, null, null, null, null, null))
    val message2 = CreateTable(queryId + "2", new ClusterName("cluster"), new TableMetadata(new TableName("catalog", "mytable"), null, null, null, null, null, null))

    println("sending message 1")
    var future = ask(ca1, message)
    println("sending message 2")
    var future2 = ask(ca2, message2)
    println("messages sent")

    val result = Await.result(future, 12 seconds).asInstanceOf[MetadataResult]
    println("result.getQueryId()=" + result.getQueryId())
    assert(result.getQueryId() == queryId)

    val result2 = Await.result(future2, 16 seconds).asInstanceOf[MetadataResult]
    println("result.getQueryId()=" + result2.getQueryId())
    assert(result2.getQueryId() == queryId + "2")


  }




  test("Send MetadataInProgressQuery to Connector") {

    val queryId = "queryId"
    val m = mock[IConnector]
    val me = mock[IMetadataEngine]
    val m2 = mock[IConnector]
    val me2 = mock[IMetadataEngine]
    val slowfunc = () => {
      println("very slow function")
      for (i <- 1 to 5) {
        Thread.sleep(1000)
        println(i + " seconds gone by")
      }
      QueryResult.createSuccessQueryResult()
    }
    (me.createTable _).expects(*, *).onCall((ClusterName, TableMetadata) => { slowfunc() })
    (m.getMetadataEngine _).expects().returning(me)

    (me2.createTable _).expects(*, *).onCall((ClusterName, TableMetadata) => { slowfunc() })
    (m2.getMetadataEngine _).expects().returning(me2)

    //val connectorActor= system1.actorOf(ConnectorActor.props("myConnector",
      //m).withRouter(RoundRobinRouter(nrOfInstances=2)), "connectorActorTest")

    val ca1 = system1.actorOf(ConnectorActor.props("myConnector", m))
    val ca2 = system1.actorOf(ConnectorActor.props("myConnector2", m2))
    val routees = Vector[ActorRef](ca1, ca2)
    val connectorActor = system1.actorOf(ConnectorActor.props("myConnector", m).withRouter(RoundRobinRouter(routees = routees)))
    /*
    */

    val message = CreateTable(queryId, new ClusterName("cluster"), new TableMetadata(new TableName("catalog", "mytable"), null, null, null, null, null, null))
    val message2 = CreateTable(queryId + "2", new ClusterName("cluster"), new TableMetadata(new TableName("catalog", "mytable"), null, null, null, null, null, null))
    /*
    class ExecutorForwarder extends Actor {
      def receive = {
        case "start" => connectorActor forward message
        case _ =>  println("error; we shouldn't get this message")
      }
    }
    val executorForwarder=system1.actorOf(Props(new ExecutorForwarder()), "executorForwarder")
    executorForwarder ! "start"
    var future = ask(executorForwarder, "start")
    */

    Thread.sleep(3000)
    println("sending message 1")
    var future = ask(connectorActor, message)
    println("sending message 2")
    var future2 = ask(connectorActor, message2)
    println("messages sent")

    val result = Await.result(future, 12 seconds).asInstanceOf[MetadataResult]
    println("result.getQueryId()=" + result.getQueryId())
    assert(result.getQueryId() == queryId)

    val result2 = Await.result(future2, 16 seconds).asInstanceOf[MetadataResult]
    println("result.getQueryId()=" + result2.getQueryId())
    assert(result2.getQueryId() == queryId + "2")


  }


}


