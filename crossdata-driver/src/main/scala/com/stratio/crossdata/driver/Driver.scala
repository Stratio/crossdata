/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.driver

import java.util

import akka.actor.ActorSystem
import com.stratio.crossdata.driver.config.DriverConfig
import com.typesafe.config.{ConfigValueFactory, Config}
import org.apache.log4j.Logger

object Driver extends DriverConfig {
  override lazy val logger = Logger.getLogger(getClass)
}

class Driver(val seedNodes: java.util.List[String] = new util.ArrayList[String]()) {

  private lazy val logger = Driver.logger

  val finalConfig = seedNodes match {
    case c if !c.isEmpty => Driver.config.withValue(
      "akka.cluster.seed-nodes",
      ConfigValueFactory.fromAnyRef(seedNodes))
    case _ => Driver.config
  }

  private val system = ActorSystem("CrossdataSystem", finalConfig)
  logger.info(" === CONFIGURATION === ")
  system.logConfiguration()
}
