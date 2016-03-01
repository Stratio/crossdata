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
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
    import org.json4s.jackson.Serialization._
import scala.util.{Failure, Success, Try}

object CrossdataStreamingApplication extends SparkLoggerComponent with CrossdataSerializer{

  val EphemeralTableNameIndex = 0
  val StreamingCatalogConfigurationIndex = 1
  val CrossdataCatalogIndex = 2

  def main(args: Array[String]): Unit = {
    assert(args.length == 3, s"Invalid number of params: ${args.length}, args: $args")
    Try {
      //val ephemeralTableName = new String(BaseEncoding.base64().decode(args(EphemeralTableNameIndex)))
      val ephemeralTableName = args(EphemeralTableNameIndex)//"ephtable"

      //val zookeeperConfString = new String(BaseEncoding.base64().decode(args(ZookeeperConfigurationIndex)))
      //val zookeeperConf = Try(ConfigFactory.parseString(zookeeperConfString)).getOrElse(...)
      //val zookeeperConf = Try(JSON.parseFull(zookeeperConfString).get.asInstanceOf[Map[String,Any]]).getOrElse(...)

      // args(ZookeeperConfigurationIndex),

      val zookeeperConf = parseMapArguments(args(StreamingCatalogConfigurationIndex)).getOrElse{
       logger.warn(s"Error parsing zookeeper configuration, using defaults with localhost -> ${args(CrossdataCatalogIndex)}")
       Map.empty[String,String] // TODO DefaultZookeeperConfiguration
     }

      val xdCatalogConf = parseMapArguments(args(CrossdataCatalogIndex)).getOrElse{
        logger.warn(s"Error parsing zookeeper configuration, using defaults with localhost :${args(CrossdataCatalogIndex)} ")
        Map.empty[String,String]
      }

      val crossdataStreaming = new CrossdataStreaming(ephemeralTableName, zookeeperConf, xdCatalogConf)
      crossdataStreaming.init()
      
    } match {
      case Success(_) =>
        logger.info("Ephemeral Table Started")
      case Failure(exception) =>
        logger.error(exception.getMessage, exception)
        sys.exit()
    }
  }

  private[streaming] def parseMapArguments(serializedMap: String) : Try[Map[String, String]] =
    Try(read[Map[String,String]](serializedMap))
  


}
