/*
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
package com.stratio.crossdata.streaming.test

import org.scalatest._
import org.scalatest.concurrent.{Eventually, TimeLimitedTests}
import org.scalatest.time.SpanSugar._

trait BaseSparkStreamingXDTest
    extends FunSuite
    with Matchers
    with ShouldMatchers
    with BeforeAndAfterAll
    with BeforeAndAfter
    with Eventually
    with TimeLimitedTests {

  val timeLimit = 2 minutes
}
