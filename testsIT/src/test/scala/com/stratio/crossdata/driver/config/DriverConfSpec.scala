/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.config

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.ConfigValueFactory
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DriverConfSpec extends BaseXDTest{


  "DriverConf" should "load default config" in {
    val conf = new DriverConf()

    conf.get("config.cluster.name") shouldBe "CrossdataServerCluster"
    conf.get("config.flatten-tables") shouldBe false
  }

  it should "allow to write config programmatically" in {
    val conf = new DriverConf()
    conf.set("config.cluster.name", ConfigValueFactory.fromAnyRef("CrossdataCluster"))

    conf.get("config.cluster.name") shouldBe "CrossdataCluster"
  }

  it should "allow to set common properties" in {
    val conf = new DriverConf().setTunnelTimeout(10).setClusterContactPoint("1.1.1.1:1000", "2.2.2.2:2000").setFlattenTables(true)

    conf.getFlattenTables shouldBe true
    conf.getClusterContactPoint should have length 2
    conf.getClusterContactPoint should contain allOf
      ("akka.tcp://CrossdataServerCluster@1.1.1.1:1000/system/receptionist", "akka.tcp://CrossdataServerCluster@2.2.2.2:2000/system/receptionist")

    conf.get("akka.contrib.cluster.receptionist.response-tunnel-receive-timeout") shouldBe 10000
  }

  it should "allow to set HTTP host & port" in {

    val host = "crossdata.com"
    val port = 12345

    val conf = new DriverConf().setHttpHostAndPort(host, port)

    conf.get(DriverConf.Http.Host) should equal(host)
    conf.get(DriverConf.Http.Port) should equal(port)
    conf.getCrossdataServerHttp should equal(s"$host:$port")

  }

}
