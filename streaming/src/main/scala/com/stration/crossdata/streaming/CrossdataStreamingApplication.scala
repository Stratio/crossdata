package com.stration.crossdata.streaming

import com.google.common.io.BaseEncoding
import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf

import scala.util.{Failure, Success, Try}

object CrossdataStreamingApplication extends SparkLoggerComponent with TypesafeConfigComponent {

  val EphemeralTableIdIndex = 0
  val ZookeeperConfigurationIndex = 1

  val StreamingBasicConfig = "streaming-reference.conf"
  val ParentConfigName = "crossdata-streaming"
  val ConfigPathName = "config"

  /**
   * SPARK CONFIGURATION
   */
  override val config = new TypesafeConfig(None, None, Option(StreamingBasicConfig), Option(ParentConfigName))

  private def configToSparkConf(generalConfig: Option[Config], specificConfig: Map[String, String]): SparkConf = {
    val conf = new SparkConf()

    if (generalConfig.isDefined) {
      val properties = generalConfig.get.toMap
      properties.foreach(e => if (e._1.startsWith(s"spark")) conf.set(e._1, e._2.toString))
    }
    specificConfig.foreach(e => conf.set(e._1, e._2))
    conf
  }

  def main(args: Array[String]): Unit = {
    assert(args.length == 2, s"Invalid number of params: ${args.length}, args: $args")
    Try {
      val ephemeralTableId = new String(BaseEncoding.base64().decode(args(EphemeralTableIdIndex)))
      val zookeeperConfString = new String(BaseEncoding.base64().decode(args(ZookeeperConfigurationIndex)))
      val zookeeperConf = Option(ConfigFactory.parseString(zookeeperConfString))
      val sparkConfig = configToSparkConf(config.getConfig(ConfigPathName), Map.empty)
      val crossdataStreaming = new CrossdataStreaming(ephemeralTableId, zookeeperConf, sparkConfig)

      crossdataStreaming.init()
    } match {
      case Success(_) => logger.info("Ephemeral Table Started")
      case Failure(exception) => logger.error(exception.getLocalizedMessage, exception)
    }
  }
}
