/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
