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

trait SharedXDContextWithDataTest extends SharedXDContextTest with Logging {

  import org.apache.spark.sql.crossdata.test.SharedXDContextWithDataTest._

  //Template settings: Override them

  type ClientParams  /* Abstract type which should be overridden in order to specify the type of
                      * the native client used in the test to insert test data.
                      */


  val runningError: String                             /* Error message shown when a test is running without a propper
                                                        * environment being set
                                                        */

  val provider: String                                 // Datasource class name (fully specified)
  val defaultOptions: Map[String, String] = Map.empty  // Spark options used to register the test table in the catalog

  def sparkRegisterTableSQL: Seq[SparkTable] = Nil     /* Spark CREATE sentence. Without OPTIONS or USING parts since
                                                        * they'll be generated from `provider` and `defaultOptions`
                                                        * attributes.
                                                        * e.g: override def sparkRegisterTableSQL: Seq[SparkTable] =
                                                        *          Seq("CREATE TABLE T", "CREATE TEMPORARY TABLE S")
                                                        */

  lazy val assumeEnvironmentIsUpAndRunning = {
    assume(isEnvironmentReady, runningError)
  }

  protected def prepareClient: Option[ClientParams]    // Native client initialization
  protected def terminateClient: Unit                  // Native client finalization
  protected def saveTestData: Unit = ()                // Creation and insertion of test data examples
  protected def cleanTestData: Unit                    /* Erases test data from the data source after the test has
                                                        * finished
                                                        */

  //Template: This is the template implementation and shouldn't be modified in any specific test

  implicit def str2sparkTableDesc(query: String): SparkTable = SparkTable(query, defaultOptions)

  var client: Option[ClientParams] = None
  var isEnvironmentReady = false

  protected override def beforeAll(): Unit = {
    super.beforeAll()

    isEnvironmentReady = Try {
      client = prepareClient
      saveTestData
      sparkRegisterTableSQL.foreach { case SparkTable(s, opts) => sql(Sentence(s, provider, opts).toString) }
      client.isDefined
    } recover { case e: Throwable =>
      logError(e.getMessage)
      false
    } get
  }

  protected override def afterAll() = {
    _xdContext.dropAllTables()
    super.afterAll()
    for (_ <- client) cleanEnvironment
  }

  private def cleanEnvironment: Unit = {
    cleanTestData
    terminateClient
  }

}

object SharedXDContextWithDataTest {

  case class Sentence(query: String, provider: String, options: Map[String, String]) {
    override def toString: String = {
      val opt = options.map { case (k, v) => s"$k " + s"'$v'" } mkString ","
      s"$query USING $provider" + options.headOption.fold("")(_ => s" OPTIONS ( $opt ) ")
    }
  }

  case class SparkTable(sql: String, options: Map[String, String])

}