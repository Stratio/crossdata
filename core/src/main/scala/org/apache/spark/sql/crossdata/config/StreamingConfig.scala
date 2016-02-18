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
package org.apache.spark.sql.crossdata.config


import org.apache.spark.sql.crossdata.config.StreamingConstants._
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.sql.types.StructType
import scala.collection.JavaConversions._

object StreamingConfig extends CoreConfig {

  lazy val streamingConfig = config.getConfig(StreamingConstants.StreamingConfPath)

  lazy val streamingConfigMap: Map[String, String] =
    streamingConfig.entrySet().map(entry => (entry.getKey, streamingConfig.getAnyRef(entry.getKey).toString)).toMap

  def createEphemeralTableModel(ident: String, userSchema: Option[StructType], opts : Map[String, String]) : EphemeralTableModel = {

    val finalOptions = getEphemeralTableOptions(ident, opts)

    val connections = finalOptions(KafkaConnection)
      .split(",").map(_.split(":")).map{
      case c if c.size == 3 => ConnectionHostModel(c(0), c(1), c(2))
    }.toSeq

    val topics = finalOptions(KafkaTopic)
      .split(",").map(_.split(":")).map{
      case l if l.size == 2 => TopicModel(l(0), l(1).toInt)
    }.toSeq

    val groupId = finalOptions(KafkaGroupId)
    val partition = finalOptions.get(KafkaPartition)
    val kafkaAdditionalOptions = finalOptions.filter{case (k, v) => k.startsWith(KafkaAdditionalOptionsKey)}
    val storageLevel = finalOptions(ReceiverStorageLevel)
    val kafkaOptions = KafkaOptionsModel(connections, topics, groupId, partition, kafkaAdditionalOptions, storageLevel)
    val minW = finalOptions(AtomicWindow).toInt
    val maxW = finalOptions(MaxWindow).toInt
    val outFormat = finalOptions(OutputFormat) match {
      case "JSON" => EphemeralOutputFormat.JSON
      case other => EphemeralOutputFormat.ROW
    }

    val checkpointDirectory = s"${finalOptions(CheckpointDirectory)}/$ident"
    val sparkOpts = finalOptions.filter{case (k, v) => k.startsWith(SparkConfPath)}
    val ephemeralOptions = EphemeralOptionsModel(kafkaOptions, minW, maxW, outFormat, checkpointDirectory, sparkOpts)

    EphemeralTableModel(ident, ephemeralOptions, userSchema)
  }

  private def getEphemeralTableOptions(ephTable: String, opts : Map[String, String]): Map[String, String] = {

    listMandatoryEphemeralTableKeys.foreach{ mandatoryOption =>
      if (opts.get(mandatoryOption).isEmpty) notFound(mandatoryOption)
    }
    streamingConfigMap ++ opts
  }

  // Return default value
  def notFound(key: String) = {
    logError(s"Mandatory parameter $key not specified, you have to specify it")
    throw new RuntimeException(key)
  }

}


