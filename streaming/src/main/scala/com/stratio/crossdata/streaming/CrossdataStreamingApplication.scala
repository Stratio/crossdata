/**
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

package com.stratio.crossdata.streaming

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent

import scala.util.{Failure, Success, Try}

object CrossdataStreamingApplication extends SparkLoggerComponent {

  val EphemeralTableNameIndex = 0
  val ZookeeperConfigurationIndex = 1

  def main(args: Array[String]): Unit = {
    assert(args.length == 2, s"Invalid number of params: ${args.length}, args: $args")
    Try {
      //val ephemeralTableName = new String(BaseEncoding.base64().decode(args(EphemeralTableNameIndex)))
      val ephemeralTableName = args(EphemeralTableNameIndex)//"ephtable"
      
      val zookeeperConfString = new String(BaseEncoding.base64().decode(args(ZookeeperConfigurationIndex)))
      //val zookeeperConf = Try(ConfigFactory.parseString(zookeeperConfString)).getOrElse(...)
      //val zookeeperConf = Try(JSON.parseFull(zookeeperConfString).get.asInstanceOf[Map[String,Any]]).getOrElse(...)
      val zookeeperConf = Map(
        "connectionString" -> "localhost:2181",
        "connectionTimeout" -> 1500,
        "sessionTimeout" -> 60000,
        "retryAttempts" -> 6,
        "retryInterval" -> 10000
      )
      /*val zookeeperConf =
        Try(ConfigFactory.load(CrossdataStreaming.StreamingResourceConfig)
          .getConfig("zookeeper").atKey("zookeeper")).getOrElse(...)*/

      val crossdataStreaming = new CrossdataStreaming(ephemeralTableName, zookeeperConf)

      crossdataStreaming.init()
    } match {
      case Success(_) =>
        logger.info("Ephemeral Table Started")
      case Failure(exception) =>
        logger.error(exception.getLocalizedMessage, exception)
        sys.exit()
    }
  }
}
