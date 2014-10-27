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

import akka.util.Timeout

import com.stratio.crossdata.common.connector.IConnector
import com.stratio.crossdata.common.data.{ClusterName, ColumnName, IndexName}
import com.stratio.crossdata.common.metadata.{ColumnMetadata, IndexMetadata}
import com.stratio.crossdata.common.statements.structures.Selector

import org.apache.log4j.Logger
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.testng.Assert.assertNotNull

import scala.concurrent.duration.DurationInt


class ConnectorActorAppTest extends FunSuite with MockFactory {

  lazy val logger = Logger.getLogger(classOf[ConnectorActorAppTest])
  implicit val timeout = Timeout(3 seconds)

  val connector:String="MyConnector"


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
    assertNotNull(myReference, "Null reference returned")
    c.shutdown()
  }

}

