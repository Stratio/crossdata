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

import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.sql.crossdata.config.StreamingConstants._
import org.apache.spark.sql.types.StructType

object StreamingConfig extends TypesafeConfigComponent{

  def createEphemeralTableModel(ident: String, userSchema: StructType, opts : Map[String, String]) : EphemeralTableModel = {

    val finalOptions = getEphemeralTableOptions(opts, ident)

    val connections = finalOptions(kafkaConnection)
      .split(",").map(_.split(":")).map{
      case c if c.size == 3 => ConnectionHostModel(c(0), c(1), c(2))
    }.toSeq

    val topics = finalOptions(kafkaTopic)
      .split(",").map(_.split(":")).map{
      case l if l.size == 2 => TopicModel(l(0), l(1).toInt)
    }.toSeq

    val groupId = finalOptions(kafkaGroupId)
    val partition = finalOptions.get(kafkaPartition)
    val kafkaAdditionalOptions = finalOptions.filter{case (k, v) => k.startsWith(kafkaAdditionalOptionsKey)}
    val storageLevel = finalOptions.getOrElse(kafkaStorageLevel, defaultKafkaStorageLevel)
    val kafkaOptions = KafkaOptionsModel(connections, topics, groupId, partition, kafkaAdditionalOptions, storageLevel)
    val minW = finalOptions(atomicWindow).toInt
    val maxW = finalOptions(maxWindow).toInt
    val outFormat = finalOptions.getOrElse(outputFormat, defaultOutputFormat) match {
      case "JSON" => EphemeralOutputFormat.JSON
      case other => EphemeralOutputFormat.ROW
    }

    val checkpoint = finalOptions.getOrElse(checkpointDirectory, defaultCheckpointDirectory)
    val sparkOpts = finalOptions.filter{case (k, v) => k.startsWith(sparkOptionsKey)}
    val ephemeralOptions = EphemeralOptionsModel(kafkaOptions, minW, maxW, outFormat, checkpoint, sparkOpts)

    EphemeralTableModel(ident, userSchema, ephemeralOptions)

  }

  // Options with order preference
  private def getEphemeralTableOptions(opts : Map[String, String], ephTable: String): Map[String, String] = {
    // first if opts has confFilePath, we take configFile, and then opts if there is no confFilePath , we take opts
    val filePath = opts.get(streamingConfFilePath)
    val options = filePath.fold(opts)(extractConfigFromFile(_, ephTable)) ++ opts

    options ++ listMandatoryEphemeralTableKeys
      .map(key => key -> options.getOrElse(key, notFound(key, defaultEphemeralTableMapConfig))).toMap

  }

  private def extractConfigFromFile(filePath : String, ephTable: String): Map[String, String] = {

    val configFile =
      new TypesafeConfig(
        None,
        None,
        Some(filePath),
        Some(CoreConfig.ParentConfigName + "." + XDContext.StreamingConfigKey)
      ).getConfig(ephTable).getOrElse(new TypesafeConfig())

    configFile.toStringMap
  }

  // Return default value
  def notFound(key: String, mandatoryOptions: Map[String, String]): String = {
    val logger= Logger.getLogger(loggerName)
    logger.warn(s"Mandatory parameter $key not specified, this could cause errors.")

    mandatoryOptions(key)
  }

  // Return a message
  def notFoundMandatory(key: String): String = {
    val logger= Logger.getLogger(loggerName)
    val message = s"Mandatory parameter $key not specified, you have to specify it."
    logger.error(message)

    "ERROR: " +  message
  }

}
