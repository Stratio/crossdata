package org.apache.spark.sql.crossdata.test

import org.apache.spark.Logging

import scala.util.Try

trait SharedXDContextWithDataTest extends SharedXDContextTest  with Logging {

  type ClientParams

  var client: Option[ClientParams] = None
  var isEnvironmentReady = false
  val runningError: String
  val sparkRegisterTableSQL: String

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
      sql(sparkRegisterTableSQL)
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
