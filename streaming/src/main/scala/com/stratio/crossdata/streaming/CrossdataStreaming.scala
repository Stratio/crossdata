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

