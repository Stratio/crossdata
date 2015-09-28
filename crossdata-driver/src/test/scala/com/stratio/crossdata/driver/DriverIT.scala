package com.stratio.crossdata.driver

/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import akka.util.Timeout
import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.common.result.ErrorResult
import com.stratio.crossdata.server.CrossdataServer
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class DriverIT extends DriverAndServerTest {

  "The driver" should "return a ErrorResult when there is no server" in {
    val driver: Driver = new Driver
    val result = driver.syncQuery(SQLCommand("select * from any"), Timeout(1 seconds), 1)
    result shouldBe an[ErrorResult]
  }


  it should "return a ErrorResult when running an unparseable query" in {
    val driver: Driver = new Driver
    val result = driver.syncQuery(SQLCommand("select select"))
    result shouldBe an[ErrorResult]
  }

  it should "return a SuccessfulQueryResult when executing a list tables" in {
    val driver: Driver = new Driver
    val result = driver.syncQuery(SQLCommand("LIST TABLES"))
    result shouldBe an[ErrorResult]
  }

}

trait DriverAndServerTest extends FlatSpec with Matchers with BeforeAndAfter {

  var crossdataServer: Option[CrossdataServer]

  def init() = {
    crossdataServer = Some(new CrossdataServer)
    crossdataServer.foreach(_.init(null))
    crossdataServer.foreach(_.start())
  }

  def stop() = {
    crossdataServer.foreach(_.stop())
    crossdataServer.foreach(_.destroy())
  }

  override protected def before(fun: => Any): Unit = {
    init()
  }

  override protected def after(fun: => Any): Unit = {
    stop()
  }
}