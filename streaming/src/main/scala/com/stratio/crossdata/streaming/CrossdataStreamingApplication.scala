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
import com.stratio.crossdata.streaming.helpers.CrossdataStatusHelper
import org.apache.spark.sql.crossdata.models.EphemeralExecutionStatus
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.json4s.jackson.Serialization._

import scala.util.{Failure, Success, Try}

object CrossdataStreamingApplication extends SparkLoggerComponent with CrossdataSerializer {

  val EphemeralTableNameIndex = 0
  val StreamingCatalogConfigurationIndex = 1
  val CrossdataCatalogIndex = 2

  def main(args: Array[String]): Unit = {
    assert(args.length == 3, s"Invalid number of params: ${args.length}, args: $args")
    Try {
      val ephemeralTableName = args(EphemeralTableNameIndex)
      val zookeeperConf = parseMapArguments(args(StreamingCatalogConfigurationIndex)).getOrElse {
          val message = s"Error parsing zookeeper argument -> ${args(StreamingCatalogConfigurationIndex)}"
          logger.error(message)
          throw new IllegalArgumentException(message)
        }

      val xdCatalogConf = parseMapArguments(args(CrossdataCatalogIndex)).getOrElse {
        val message = s"Error parsing XDCatalog argument -> ${args(CrossdataCatalogIndex)}"
        logger.error(message)
        throw new IllegalArgumentException(message)
      }

      val crossdataStreaming = new CrossdataStreaming(ephemeralTableName, zookeeperConf, xdCatalogConf)
      crossdataStreaming.init() match {
        case Success(_) =>
          logger.info(s"Ephemeral Table Finished correctly: $ephemeralTableName")
          CrossdataStatusHelper.close()
        case Failure(exception) =>
          logger.error(exception.getMessage, exception)
          CrossdataStatusHelper.setEphemeralStatus(EphemeralExecutionStatus.Error, zookeeperConf, ephemeralTableName)
          CrossdataStatusHelper.close()
          sys.exit(-1)
      }
    } match {
      case Success(_) =>
        logger.info(s"Application finished correctly")
        sys.exit()
      case Failure(exception) =>
        logger.error(exception.getMessage, exception)
        CrossdataStatusHelper.close()
    }
  }

  private[streaming] def parseMapArguments(serializedMap: String): Try[Map[String, String]] =
    Try(read[Map[String, String]](serializedMap))
}
