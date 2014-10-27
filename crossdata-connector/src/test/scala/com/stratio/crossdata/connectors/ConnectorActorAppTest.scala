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

package com.stratio.crossdata.connectors

import akka.pattern.ask
import akka.util.Timeout
import com.stratio.crossdata.common.connector.{IConnector, IMetadataEngine, IQueryEngine, IStorageEngine}
import com.stratio.crossdata.common.data.{Row, TableName, CatalogName, ClusterName, IndexName, ColumnName}
import com.stratio.crossdata.common.executionplan.ExecutionWorkflow
import com.stratio.crossdata.common.logicalplan.{LogicalStep, LogicalWorkflow, TransformationStep}
import com.stratio.crossdata.common.metadata.{IndexMetadata, ColumnMetadata, Operations, TableMetadata}
import com.stratio.crossdata.common.result.{MetadataResult, QueryResult, StorageResult}
import com.stratio.crossdata.common.statements.structures.Selector
import com.stratio.crossdata.communication.{CreateTable, Execute, Insert}
import com.stratio.crossdata.core.query.{BaseQuery, SelectParsedQuery, SelectPlannedQuery, SelectValidatedQuery}
import com.stratio.crossdata.core.statements.SelectStatement
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt


class ConnectorActorAppTest extends FunSuite with MockFactory {
  //class ConnectorActorTest extends TestKit(ActorSystem()) with FunSuiteLike with MockFactory {

  // needed for `?` below
  lazy val logger = Logger.getLogger(classOf[ConnectorActorAppTest])
  implicit val timeout = Timeout(3 seconds)
  //connector noum
  val connector:String="MyConnector"


  //val to initialize and dont use null
  val a:Option[java.util.Map[Selector,Selector]]=None
  val b:Option[java.util.Map[ColumnName, ColumnMetadata]]=None
  val d:Option[java.util.Map[IndexName,IndexMetadata]]=None
  val e:Option[ClusterName]=None
  val f:Option[java.util.List[ColumnName]]=None



  test("Basic Connector Mock") {
    val m = mock[IConnector]
    (m.getConnectorName _).expects().returning(connector)
    assert(m.getConnectorName().equals(connector))
  }

  test("Basic Connector App listening on a given port does not break") {
    val m = mock[IConnector]
    (m.init _).expects(*).returning(None)
    (m.getConnectorName _).expects().returning(connector)
    val c = new ConnectorApp()
    val myReference = c.startup(m)
    assert(true)
    c.shutdown()
  }


  test("Send SelectInProgressQuery to Connector") {
    val m = mock[IConnector]
    val qe = mock[IQueryEngine]
    (m.getQueryEngine _).expects().returning(qe)
    (qe.execute(_:LogicalWorkflow)).expects(*).returning(QueryResult.createSuccessQueryResult())
    (m.init _).expects(*).returning(None)
    (m.getConnectorName _).expects().returning(connector)
    val c = new ConnectorApp()
    val myReference = c.startup(m)
    var steps: java.util.ArrayList[LogicalStep] = new java.util.ArrayList[LogicalStep]()
    val a:Option[Operations]= None
    val step= new TransformationStep(a.get)
    steps.add(step)
    var workflow = new LogicalWorkflow(steps)
    val executionStep:Option[ExecutionWorkflow] = None//new ExecutionWorkflow(myReference, workflow, ResultType.RESULTS)
    val pq = new SelectPlannedQuery(
      new SelectValidatedQuery(
        new SelectParsedQuery(
          new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog"))
          , new SelectStatement(new TableName("myCatalog", "myTable"))
        )
      ), executionStep.get
    )

    val future = ask(myReference,Execute("idquery",workflow))
    val result = Await.result(future, 3 seconds).asInstanceOf[QueryResult]
    logger.debug("receiving->" + result + " after sending select query")
    assert(true)
    c.stop()
  }

  test("Send MetadataInProgressQuery to Connector") {
    val port = "2560"
    val m = mock[IConnector]
    val me = mock[IMetadataEngine]
    (me.createTable _).expects(*,*).returning(QueryResult.createSuccessQueryResult())
    (m.getMetadataEngine _).expects().returning(me)
    (m.init _).expects(*).returning(None)
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    val myReference = c.startup(m)

    val message=CreateTable("queryId",new ClusterName("cluster"),new TableMetadata(new TableName("catalog","mytable"),  a.get, b.get,d.get,e.get,f.get,f.get) )
    val future=ask(myReference , message)
    val result = Await.result(future, 3 seconds).asInstanceOf[MetadataResult]
    logger.debug("receive->" + result + " after sending Metadata query")
    c.shutdown()
  }

  test("Send StorageInProgressQuery to Connector") {
    val port = "2561"
    val m = mock[IConnector]
    val ie = mock[IStorageEngine]
    (ie.insert(_: ClusterName,_:TableMetadata,_:Row)).expects(*,*,*).returning()
    (m.init _).expects(*).returning(None)
    (m.getStorageEngine _).expects().returning(ie)
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    val myReference = c.startup(m)


    val message=Insert("query",new ClusterName("cluster"),new TableMetadata(new TableName("catalog","mytable"),
      a.get, b.get,d.get,e.get,f.get,f.get),new Row())
    //val future=myReference ? message
    val future=ask(myReference,message)
    val result = Await.result(future, 3 seconds).asInstanceOf[StorageResult]
    logger.debug("receiv ing->" + result + " after sending insert query")
    c.shutdown()
  }

  //TODO: CREATE ONE TEST FOR EACH KIND OF MESSAGE

}

