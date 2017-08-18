package com.stratio.crossdata.streaming.test

import org.scalatest._
import org.scalatest.concurrent.{Eventually, TimeLimitedTests}
import org.scalatest.time.SpanSugar._

trait BaseSparkStreamingXDTest extends FunSuite
with Matchers
with ShouldMatchers
with BeforeAndAfterAll
with BeforeAndAfter
with Eventually
with TimeLimitedTests {

  val timeLimit = 2 minutes
}
