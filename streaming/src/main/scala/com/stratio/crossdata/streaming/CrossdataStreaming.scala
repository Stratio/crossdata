/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming

import com.stratio.crossdata.streaming.constants.ApplicationConstants._
import com.stratio.crossdata.streaming.helpers.{CrossdataStatusHelper, CrossdataStreamingHelper}
import org.apache.spark.SparkConf
import org.apache.spark.sql.crossdata.config.StreamingConstants._
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.EphemeralTableMapDAO
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.streaming.StreamingContext

import scala.util.Try

class CrossdataStreaming(ephemeralTableName: String,
                         streamingCatalogConfig: Map[String, String],
                         crossdataCatalogConfiguration: Map[String, String])
  extends EphemeralTableMapDAO {

  private val zookeeperCatalogConfig = streamingCatalogConfig.collect {
    case (key, value) if key.startsWith(ZooKeeperStreamingCatalogPath) =>
      (key.substring(s"$ZooKeeperStreamingCatalogPath.".length), value)
  }

  def prefix:String = Try(zookeeperCatalogConfig.get(PrefixStreamingCatalogsConfig)+"_") getOrElse ("")

  val memoryMap = Map(ZookeeperPrefixName -> zookeeperCatalogConfig)

  def init(): Try[Any] = {
    Try {
      val ephemeralTable = dao.get(ephemeralTableName)
        .getOrElse(throw new IllegalStateException("Ephemeral table not found"))
      val sparkConfig = configToSparkConf(ephemeralTable)

      val ssc = StreamingContext.getOrCreate(ephemeralTable.options.checkpointDirectory,
        () => {
          CrossdataStreamingHelper.createContext(ephemeralTable,
            sparkConfig,
            zookeeperCatalogConfig,
            crossdataCatalogConfiguration
          )
        })

      CrossdataStatusHelper.initStatusActor(ssc, zookeeperCatalogConfig, ephemeralTable.name)

      logger.info(s"Started Ephemeral Table: $ephemeralTableName")
      CrossdataStatusHelper.setEphemeralStatus(
        EphemeralExecutionStatus.Started,
        zookeeperCatalogConfig,
        ephemeralTableName
      )

      ssc.start()
      ssc.awaitTermination()
    }
  }

  private[streaming] def configToSparkConf(ephemeralTable: EphemeralTableModel): SparkConf =
    new SparkConf().setAll(setPrefixSpark(ephemeralTable.options.sparkOptions))

  private[streaming] def setPrefixSpark(sparkConfig: Map[String, String]): Map[String, String] =
    sparkConfig.map { case entry@(key, value) =>
      if (key.startsWith(SparkPrefixName)) entry
      else (s"$SparkPrefixName.$key", value)
    }
}

