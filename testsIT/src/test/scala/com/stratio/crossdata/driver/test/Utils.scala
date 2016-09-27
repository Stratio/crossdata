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
package com.stratio.crossdata.driver.test

import com.stratio.crossdata.driver.{Driver, DriverFactory, JavaDriver}
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.test.BaseXDTest

import scala.util.Try

object Utils extends BaseXDTest{

  case class DriverTestContext(driverFactory: DriverFactory, optConfig: Option[DriverConf] = None)

  implicit def config2context(config: DriverConf): DriverTestContext =
    DriverTestContext(Driver, Some(config))

  def withDriverDo(block: Driver => Unit)(
    implicit driverCtx: DriverTestContext = DriverTestContext(Driver)
  ): Unit = {

    import driverCtx._

    val driver = optConfig.map(driverFactory.newSession(_)).getOrElse(driverFactory.newSession())

    try {
      block(driver)
    } finally {
      driver.closeSession()
    }
  }

  def withDriverTry(block: Driver => Unit)(implicit optConfig: Option[DriverConf] = None): Try[Unit] =
    Try(optConfig.map(Driver.newSession).getOrElse(Driver.newSession())) map { driver =>
      block(driver)
    }


  def withJavaDriverDo(block: JavaDriver => Unit)(
    implicit driverCtx: DriverTestContext = DriverTestContext(Driver)
  ): Unit = {

    import driverCtx._

    val driver = optConfig.map(
      driverConf => new JavaDriver(driverConf, driverFactory)
    ).getOrElse(new JavaDriver(driverFactory))

    try {
      block(driver)
    } finally {
      driver.closeSession()
    }
  }

  val driverFactories = Seq(Driver -> "through AKKA cluster client", Driver.http -> "through HTTP")

}
