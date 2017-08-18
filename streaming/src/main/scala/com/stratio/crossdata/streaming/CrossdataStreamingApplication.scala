/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming

import com.google.common.io.BaseEncoding
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.streaming.helpers.CrossdataStatusHelper
import com.typesafe.config.{Config, ConfigFactory}
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

      val zookConfigurationRendered = new String(BaseEncoding.base64().decode(args(StreamingCatalogConfigurationIndex)))

      val zookeeperConf = parseConf(zookConfigurationRendered).getOrElse {
          val message = s"Error parsing zookeeper argument -> $zookConfigurationRendered"
          logger.error(message)
          throw new IllegalArgumentException(message)
        }

      val xdCatalogConfRendered = new String(BaseEncoding.base64().decode(args(CrossdataCatalogIndex)))

      val xdCatalogConf = parseConf(xdCatalogConfRendered).getOrElse {
        val message = s"Error parsing XDCatalog argument -> $xdCatalogConfRendered"
        logger.error(message)
        throw new IllegalArgumentException(message)
      }

      val crossdataStreaming = new CrossdataStreaming(ephemeralTableName, typeSafeConfigToMapString(zookeeperConf), typeSafeConfigToMapString(xdCatalogConf))
      crossdataStreaming.init() match {
        case Success(_) =>
          logger.info(s"Ephemeral Table Finished correctly: $ephemeralTableName")
          CrossdataStatusHelper.close()
        case Failure(exception) =>
          logger.error(exception.getMessage, exception)
          CrossdataStatusHelper.setEphemeralStatus(EphemeralExecutionStatus.Error, typeSafeConfigToMapString(zookeeperConf), ephemeralTableName)
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

  private def parseConf(renderedConfig: String): Try[Config] =
    Try {
      ConfigFactory.parseString(renderedConfig)
    }

  private def typeSafeConfigToMapString(config: Config, path: Option[String]= None): Map[String, String] = {
    import scala.collection.JavaConversions._
    val conf = path.map(config.getConfig).getOrElse(config)
    conf.entrySet().toSeq.map( e =>
      (s"${path.fold("")(_+".")+ e.getKey}", conf.getAnyRef(e.getKey).toString)
    ).toMap
  }


}
