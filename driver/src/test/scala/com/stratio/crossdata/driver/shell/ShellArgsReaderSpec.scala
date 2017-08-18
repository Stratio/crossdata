/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.shell

import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ShellArgsReaderSpec extends BaseXDTest {

  "ShellArgsReader" should "parse boolean options adding a flag" in {
    val opts = new ShellArgsReader(List("--tcp")).options

    opts("tcp") shouldBe true
    opts.get("async") shouldBe empty

    val options = new ShellArgsReader(List("--tcp", "--async")).options
    options("tcp") shouldBe true
    options("async") shouldBe true

  }

  it should "parse a query passed as parameter" in {
    val opts = new ShellArgsReader(List("--query", "show tables")).options
    opts("query") shouldBe "show tables"
  }

  it should "parse boolean options indicating the value" in {
    val opts = new ShellArgsReader(List("--tcp", "true")).options
    opts("tcp") shouldBe true

    val options = new ShellArgsReader(List("--tcp", "false")).options
    options("tcp") shouldBe false
  }

}
