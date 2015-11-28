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
package org.apache.spark.sql.crossdata.test

import org.apache.spark.Logging

import scala.util.Try

trait SharedXDContextWithDataTest extends SharedXDContextTest  with Logging {

  type ClientParams

  var client: Option[ClientParams] = None
  var isEnvironmentReady = false
  val runningError: String
  val sparkRegisterTableSQL: Seq[String]

  lazy val assumeEnvironmentIsUpAndRunning = {
    assume(isEnvironmentReady, runningError)
  }

  //Template steps: Override them
  protected def prepareClient: Option[ClientParams]
  protected def terminateClient: Unit

  protected def saveTestData: Unit
  protected def cleanTestData: Unit


  //Template
  protected override def beforeAll(): Unit = {
    super.beforeAll()

    isEnvironmentReady = Try {
      client = prepareClient
      saveTestData
      sparkRegisterTableSQL.foreach(sql)
      client.isDefined
    } recover { case e: Throwable =>
      logError(e.getMessage)
      false
    } get
  }

  protected override def afterAll() = {
    _xdContext.dropAllTables()
    super.afterAll()
    for(_ <- client) cleanEnvironment
  }

  private def cleanEnvironment: Unit = {
    cleanTestData
    terminateClient
  }

}
