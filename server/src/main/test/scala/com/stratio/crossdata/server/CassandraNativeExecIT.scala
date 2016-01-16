/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.server

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.contrib.pattern.ClusterClient
import akka.pattern.ask
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.stratio.crossdata.common.result.ErrorResult
import com.stratio.crossdata.common.{SQLCommand, SQLResult}
import com.stratio.crossdata.server.TestKitUsageSpec.{ProxyActor, ServerControllerActor}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

@RunWith(classOf[JUnitRunner])
class CassandraNativeExecIT
  extends TestKit(ActorSystem("TestKitUsageSpec"))
  with DefaultTimeout
  with ImplicitSender
  with CassandraIT {

  val serverController = system.actorOf(Props[ServerControllerActor], "serverController")

  serverController ! "init"

  Thread.sleep(20000)

  val initialContacts = Set(system.actorSelection("127.0.0.1:13420"))

  val clusterClient = system.actorOf(ClusterClient.props(initialContacts))

  val proxy = system.actorOf(ProxyActor.props(clusterClient), "proxy")

  val registerTable  =
    s"""IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS (
        |cluster '$ClusterName',
        |keyspace '$Catalog',
        |table '$Table',
        |spark_cassandra_connection_host '$CassandraHost')""".stripMargin


  val result1 = Try {
    Await.result(proxy.ask(SQLCommand(registerTable)).mapTo[SQLResult], FiniteDuration(20, TimeUnit.SECONDS))
  } getOrElse ErrorResult(java.util.UUID.randomUUID, "Table registration failed. Timeout was exceed.")

  if(result1.isInstanceOf[ErrorResult]){
    fail(result1.asInstanceOf[ErrorResult].cause.getOrElse(
      new RuntimeException(result1.asInstanceOf[ErrorResult].message)))
  }

  val query =
    s"""SELECT * FROM $Catalog.$Table WHERE date = '2015-06-23 10:30+0100'"""

  val result2 = Try {
    Await.result(proxy.ask(SQLCommand(query)).mapTo[SQLResult], FiniteDuration(20, TimeUnit.SECONDS))
  } getOrElse ErrorResult(java.util.UUID.randomUUID, "Query failed. Timeout was exceed.")

  if(result2.isInstanceOf[ErrorResult]){
    fail(result2.asInstanceOf[ErrorResult].cause.getOrElse(
      new RuntimeException(result2.asInstanceOf[ErrorResult].message)))
  }

  assert(result2.isInstanceOf[SQLResult], s"SQLResult was expected for query instead of ${result2.toString}")
  assert(result2.asInstanceOf[SQLResult].resultSet.length > 0, "One or more row were expected here.")

  serverController ! "stop"
}

object TestKitUsageSpec {

  class ServerControllerActor() extends Actor {
    def receive = {
      case "init" => CrossdataApplication.
      case "stop" => context.system.shutdown
    }
  }

  object ProxyActor {
    def props(clusterClientActor: ActorRef): Props =
      Props(new ProxyActor(clusterClientActor))
  }

  class ProxyActor(clusterClientActor: ActorRef) extends Actor {
      override def receive: Receive = {
        case sqlCommand@SQLCommand(query, _, _) =>
          clusterClientActor forward ClusterClient.Send("/user/crossdata-server", sqlCommand, false)
      }
  }

}
