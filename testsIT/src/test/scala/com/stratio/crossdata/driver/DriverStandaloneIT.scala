/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver

import com.stratio.crossdata.test.BaseXDTest

class DriverStandaloneIT extends BaseXDTest {

  import com.stratio.crossdata.driver.test.Utils._

  driverFactories foreach { case (factory, description) =>

    implicit val ctx = DriverTestContext(factory)

    "Crossdata driver" should s"fail with a timeout when there is no server $description" in {

      the [RuntimeException] thrownBy {
        factory.newSession()
      } should have message s"Cannot establish connection to XDServer: timed out after ${Driver.InitializationTimeout}"

    }

  }



}
