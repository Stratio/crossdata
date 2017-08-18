package com.stratio.crossdata.test

import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.time.SpanSugar._
import org.scalatest.{FlatSpec, Matchers}

/**
 * Base class for both unit and integration tests
 */
trait BaseXDTest extends FlatSpec with Matchers with TimeLimitedTests {

  val timeLimit = 2 minutes

}
