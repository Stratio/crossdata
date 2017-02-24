package org.apache.spark.sql.crossdata

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.crossdata.XDSession.Builder

trait BuilderEnhancer {

  this: Builder =>

  private[this] val ParentConfPrefix = "crossdata-core"
  private[this] val SparkConfPrefix = "spark"
  private[this] val CatalogConfPrefix = "catalog"

  def config(conf: Config): Builder = synchronized {
    setSparkConf(conf)
    setCatalogConf(conf)
    this
  }

  def config(configFile: File): Builder = synchronized {
    if (configFile.exists && configFile.canRead) {
      log.info(s"Configuration file loaded ( ${configFile.getAbsolutePath} ).")
      val conf = ConfigFactory.parseFile(configFile)
      config(conf)
    } else {
      log.warn(s"Configuration file ( ${configFile.getAbsolutePath} ) is not accessible.")
    }

    this
  }

  /**
    * Set Spark related configuration from Typesafe Config
    * @param conf
    */
  private def setSparkConf(conf: Config): Unit = {
    if (conf.hasPath(s"$ParentConfPrefix.$SparkConfPrefix")) {

      val sparkConf: Config = conf
        .getConfig(ParentConfPrefix)
        .withOnlyPath(SparkConfPrefix)

      import scala.collection.JavaConversions._

      sparkConf
        .entrySet()
        .map(entry => (entry.getKey, entry.getValue.unwrapped().toString))
        .foreach {
          case (key, value) => config(key, value)
        }

    } else {
      log.info(s"No spark configuration was found in configuration")
    }
  }

  /**
    * Set  Catalog configuration from Typesafe Config
    * @param conf
    */
  private def setCatalogConf(conf: Config) = {
    if (conf.hasPath(s"$ParentConfPrefix.$CatalogConfPrefix")) {
      import scala.collection.JavaConversions._
      conf
        .withOnlyPath(s"$ParentConfPrefix.$CatalogConfPrefix")
        .entrySet()
        .map(entry => (entry.getKey, entry.getValue.unwrapped().toString))
        .foreach {
          case (key, value) => config(key, value)
        }
    } else {
      log.info(s"No catalog configuration was found in configuration")
    }
  }

  /**
    * Extract Catalog configuration from options map
    * @return Catalog configuration
    */
  private[crossdata] def extractCatalogConf(options: scala.collection.mutable.HashMap[String, String]): Config = {
    val catalogConf = options.filter {
      case (key, _) => key.startsWith(s"$ParentConfPrefix.$CatalogConfPrefix")
    }

    import scala.collection.JavaConversions._
    ConfigFactory.parseMap {
      catalogConf
        .map { t =>
          (t._1.replaceFirst(s"$ParentConfPrefix.$CatalogConfPrefix.", ""), t._2)
        }
        .toMap[String, String]
    }
  }
}
