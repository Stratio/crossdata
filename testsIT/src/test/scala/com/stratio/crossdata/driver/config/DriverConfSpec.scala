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

}
