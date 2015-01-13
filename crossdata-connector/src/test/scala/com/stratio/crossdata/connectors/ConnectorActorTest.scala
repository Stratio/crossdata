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

import Mocks.DummyIConnector
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.routing.RoundRobinRouter
import akka.util.Timeout
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.metadata.{CatalogMetadata, ColumnMetadata, IndexMetadata, TableMetadata}
import com.stratio.crossdata.common.result.MetadataResult
import com.stratio.crossdata.common.statements.structures.Selector
import com.stratio.crossdata.communication.{PatchMetadata, CreateTable, UpdateMetadata}
import com.stratio.crossdata.connectors.ConnectorActor
import com.stratio.crossdata.connectors.config.ConnectConfig
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Suite}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object ConnectorActorTest

class ConnectorActorTest extends FunSuite with ConnectConfig with MockFactory {
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[ConnectorActorTest])
  lazy val system1 = ActorSystem(clusterName, config)
  implicit val timeout = Timeout(10 seconds)
  val myconnector: String="myConnector"
  val myluster:String="cluster"
  val mycatalog:String="catalog"
  val mytable:String="mytable"
  val sm2:String="2"
  val a:Option[java.util.Map[Selector, Selector]] = Some(new java.util.HashMap[Selector, Selector]())
  val b:Option[java.util.LinkedHashMap[ColumnName,ColumnMetadata]] = Some(new java.util.LinkedHashMap[ColumnName, ColumnMetadata]())
  val c:Option[java.util.Map[IndexName,IndexMetadata]] = Some(new java.util.HashMap[IndexName, IndexMetadata]())
  val d:Option[ClusterName]= Some(new ClusterName(myluster))
  val e:Option[java.util.LinkedList[ColumnName]]=Some(new java.util.LinkedList[ColumnName]())

  test("Send 2 slow MetadataInProgressQuery to two connectors to test concurrency") {
    val queryId = "queryId"

    val m = new DummyIConnector()
    val m2 =new DummyIConnector()

    val ca1 = system1.actorOf(ConnectorActor.props(myconnector, m))
    val ca2 = system1.actorOf(ConnectorActor.props(myconnector, m2))

    val message = CreateTable(queryId, new ClusterName(myluster), new TableMetadata(new TableName(mycatalog, mytable),
      a.get, b.get, c.get, d.get, e.get, e.get))
    val message2 = CreateTable(queryId + sm2, new ClusterName(myluster), new TableMetadata(new TableName(mycatalog, mytable),
      a.get, b.get, c.get, d.get, e.get, e.get))

    logger.debug("sending message 1")
    var future = ask(ca1, message)
    logger.debug("sending message 2")
    var future2 = ask(ca2, message2)
    logger.debug("messages sent")

    val result = Await.result(future, 12 seconds).asInstanceOf[MetadataResult]
    logger.debug(" result.getQueryId()=" + result.getQueryId())
    assert(result.getQueryId() == queryId)

    val result2 = Await.result(future2, 16 seconds).asInstanceOf[MetadataResult]
    logger.debug("result.getQueryId()=" + result2.getQueryId())
    assert(result2.getQueryId() == queryId + sm2)
  }

  test("Send MetadataInProgressQuery to Connector") {

    val queryId = "queryId"
    val m=new DummyIConnector()
    val m2=new DummyIConnector()
    val ca1 = system1.actorOf(ConnectorActor.props(myconnector, m))
    val ca2 = system1.actorOf(ConnectorActor.props(myconnector, m2))
    val routees = Vector[ActorRef](ca1, ca2)
    val connectorActor = system1.actorOf(ConnectorActor.props(myconnector, m).withRouter(RoundRobinRouter(routees
    = routees)))


    val message = CreateTable(queryId, new ClusterName(myluster), new TableMetadata(new TableName(mycatalog, mytable),
      a.get, b.get, c.get, d.get, e.get, e.get))
    val message2 = CreateTable(queryId + sm2, new ClusterName(myluster), new TableMetadata(new TableName(mycatalog, mytable),
      a.get, b.get, c.get, d.get, e.get, e.get))
    /**
     * Time to wait the make the test
     * */
    val timesleep:Int=3000
    Thread.sleep(timesleep)
    logger.debug("sending message 1")
    var future = ask(connectorActor, message)
    logger.debug("sending message 2")
    var future2 = ask(connectorActor, message2)
    logger.debug("messages sent")

    val result = Await.result(future, 12 seconds).asInstanceOf[MetadataResult]
    logger.debug("result.getQueryId() =" + result.getQueryId())
    assert(result.getQueryId() == queryId)

    val result2 = Await.result(future2, 16 seconds).asInstanceOf[MetadataResult]
    logger.debug("result.getQueryId()= " + result2.getQueryId())
    assert(result2.getQueryId() == queryId + sm2)

  }

  test("Send updateMetadata to Connector") {
    val m=new DummyIConnector()
    val ca1 = system1.actorOf(ConnectorActor.props(myconnector, m))
    val table=new TableMetadata(new TableName("catalog","name"),null,null,null,null,null,null)
    val catalog = new CatalogMetadata(new CatalogName("catalog"),null,null)
    catalog.getTables.put(table.getName,table)
    val future1 = ask(ca1, UpdateMetadata(catalog))
    val result = Await.result(future1, 12 seconds).asInstanceOf[Boolean]
    assert(result == true)
  }

  /*
  test("Send patched updateMetadata to Connector") {
    //TODO: this test is not complete (still a Proof Of Concept)
    val m=new DummyIConnector()
    val ca1 = system1.actorOf(ConnectorActor.props(myconnector, m))
    val table=new TableMetadata(new TableName("catalog","name"),null,null,null,null,null,null)
    val classTableMetadata=table.getClass
    val catalog = new CatalogMetadata(new CatalogName("catalog"),null,null)
    catalog.getTables.put(table.getName,table)
    val future1 = ask(ca1, PatchMetadata(null, metadataClass = classTableMetadata))
    val result = Await.result(future1, 12 seconds).asInstanceOf[Boolean]
    assert(result == true)

  }

*/
}


