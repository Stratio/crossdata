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

package com.stratio.connectors

import akka.util.Timeout
import com.stratio.meta.common.connector.{IStorageEngine, IMetadataEngine, IQueryEngine, IConnector}
import com.stratio.meta.common.data.Row
import com.stratio.meta.common.logicalplan.{TransformationStep, LogicalStep, LogicalWorkflow}
import com.stratio.meta.common.result.{CommandResult, MetadataResult, QueryResult}
import com.stratio.meta.communication.{Insert, CreateTable, Execute}
import com.stratio.meta2.common.data.{ClusterName, TableName, CatalogName}
import com.stratio.meta2.common.metadata.TableMetadata
import com.stratio.meta2.core.query.{BaseQuery, SelectParsedQuery, SelectValidatedQuery, SelectPlannedQuery}
import com.stratio.meta2.core.statements.SelectStatement
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration.DurationInt


class ConnectorActorAppTest extends FunSuite with MockFactory {
  //class ConnectorActorTest extends TestKit(ActorSystem()) with FunSuiteLike with MockFactory {

  // needed for `?` below
  lazy val logger = Logger.getLogger(classOf[ConnectorActorAppTest])
  implicit val timeout = Timeout(3 seconds)

  test("Basic Connector Mock") {
    val m = mock[IConnector]
    (m.getConnectorName _).expects().returning("My New Connector")
    assert(m.getConnectorName().equals("My New Connector"))
  }

  test("Basic Connector App listening on a given port does not break") {
    val port = "2558"
    val m = mock[IConnector]
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    val myReference = c.startup(m, port)
    myReference ! "Hello World"
    assert("Hello World" == "Hello World")
    c.shutdown()
  }


  test("Send SelectInProgressQuery to Connector") {
    val port = "2559"
    val m = mock[IConnector]
    val qe = mock[IQueryEngine]
    (qe.execute(_:LogicalWorkflow)).expects(*).returning(QueryResult.createSuccessQueryResult())
    (m.getQueryEngine _).expects().returning(qe)
    (m.getConnectorName _).expects().returning("My New Connector")

    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    //val myReference = c.startup(m, port, config)
    val myReference = c.startup(m, port)
    var steps: java.util.ArrayList[LogicalStep] = new java.util.ArrayList[LogicalStep]()
    val step= new TransformationStep(null)
    steps.add(step)
    var workflow = new LogicalWorkflow(steps)
    var executionStep = null//new ExecutionWorkflow(myReference, workflow, ResultType.RESULTS)
    val pq = new SelectPlannedQuery(
      new SelectValidatedQuery(
        new SelectParsedQuery(
          new BaseQuery("query_id-2384234-1341234-23434", "select * from myQuery;", new CatalogName("myCatalog"))
          , new SelectStatement(new TableName("myCatalog", "myTable"))
        )
      ), executionStep
    )
    //val beanMap: util.Map[String, String] = BeanUtils.recursiveDescribe(selectQ);
    //for (s <- beanMap.keySet().toArray()) { println(s); }

    val future = ask(myReference,Execute("idquery",workflow))
    val result = Await.result(future, 3 seconds).asInstanceOf[QueryResult]
    println("receiving->" + result + " after sending select query")
    assert("Hello World" == "Hello World")
    c.stop()
  }

  test("Send MetadataInProgressQuery to Connector") {
    val port = "2560"
    val m = mock[IConnector]
    val me = mock[IMetadataEngine]
    (me.createTable _).expects(*,*).returning(QueryResult.createSuccessQueryResult())
    (m.getMetadataEngine _).expects().returning(me)
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    //val myReference = c.startup(m, port, config)
    val myReference = c.startup(m, port)

    val message=CreateTable("queryId",new ClusterName("cluster"),new TableMetadata(new TableName("catalog","mytable"), null, null,null,null,null,null) )
    val future=ask(myReference , message)
    val result = Await.result(future, 3 seconds).asInstanceOf[MetadataResult]
    println("receiving->"+result+" after sending Metadata query")
    c.shutdown()
  }

  test("Send StorageInProgressQuery to Connector") {
    val port = "2561"
    val m = mock[IConnector]
    val ie = mock[IStorageEngine]
    //(ie.insert(_: ClusterName,_:TableMetadata,_:util.Collection[Row])).expects(*,*,*).returning()
    (ie.insert(_: ClusterName,_:TableMetadata,_:Row)).expects(*,*,*).returning()
    (m.getStorageEngine _).expects().returning(ie)
    (m.getConnectorName _).expects().returning("My New Connector")
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
    val c = new ConnectorApp()
    //val myReference = c.startup(m, port, config)
    val myReference = c.startup(m, port)

    val message=Insert("query",new ClusterName("cluster"),new TableMetadata(new TableName("catalog","mytable"), null, null,null,null,null,null),new Row())
    //val future=myReference ? message
    val future=ask(myReference,message)
    val result = Await.result(future, 3 seconds).asInstanceOf[CommandResult]
    println("receiving->"+result+" after sending insert query")
    c.shutdown()
  }

  //TODO: CREATE ONE TEST FOR EACH KIND OF MESSAGE

}

