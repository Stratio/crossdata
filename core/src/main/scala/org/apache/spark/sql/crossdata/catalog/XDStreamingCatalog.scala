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

package org.apache.spark.sql.crossdata.catalog

import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import org.apache.log4j.Logger
import org.apache.spark.Logging
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.models._

/**
 * CrossdataStreamingCatalog aims to provide a mechanism to persist the
 * Streaming metadata executions.
 */
abstract class XDStreamingCatalog(xdContext: XDContext) extends Logging with Serializable {

  /**
   * Ephemeral Table Functions
   */
  def existsEphemeralTable(tableIdentifier: String): Boolean
  
  def getEphemeralTable(tableIdentifier: String) : Option[EphemeralTableModel]

  def getAllEphemeralTables : Seq[EphemeralTableModel]

  def createEphemeralTable(ephemeralTable: EphemeralTableModel): Either[String, EphemeralTableModel]

  def updateEphemeralTable(ephemeralTable: EphemeralTableModel): Unit

  def dropEphemeralTable(tableIdentifier: String): Unit

  def dropAllEphemeralTables(): Unit

  /**
   * Ephemeral Status Functions
   */
  def getEphemeralStatus(tableIdentifier: String) : Option[EphemeralStatusModel]

  def getAllEphemeralStatuses : Seq[EphemeralStatusModel]

  def updateEphemeralStatus(tableIdentifier: String, status: EphemeralStatusModel) : Unit

  protected[crossdata] def dropEphemeralStatus(tableIdentifier: String): Unit
  
  protected[crossdata] def dropAllEphemeralStatus(): Unit

  /**
   * Ephemeral Queries Functions
   */
  def existsEphemeralQuery(queryAlias: String): Boolean

  def getEphemeralQuery(queryAlias: String) : Option[EphemeralQueryModel]

  def getAllEphemeralQueries : Seq[EphemeralQueryModel]

  def createEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Either[String, EphemeralQueryModel]

  def updateEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Unit

  def dropEphemeralQuery(queryAlias: String): Unit

  def dropAllEphemeralQueries(): Unit

}

object XDStreamingCatalog extends TypesafeConfigComponent{


  val streamingConfFilePath = "streaming.confFilePath"

  /**
  * Ephemeral table config
  *  */

  //kafka
  //Connection //format "host0:consumerPort0:producerPort0,host1:consumerPort1:producerPort1,host2:consumerPort2:producerPort2"
  val kafkaConnection = "kafka.connection"

  //format "topicName1:1,topicName1:2,topicName1:3"
  val kafkaTopic= "kafka.topic"
  val kafkaGroupId = "kafka.groupId"
  val kafkaPartition = "kafka.partition" //optional
  //would go through additionalOptions param.
  // One param for each element map. key = kafka.additionalOptions.x -> value = value
  val kafkaAdditionalOptionsKey = "kafka.options" //optional
  val kafkaStorageLevel = "kafka.storageLevel" //optional
  val atomicWindow ="atomicWindow"
  val maxWindow = "maxWindow"
  val outputFormat = "outputFormat" //optional
  val checkpointDirectory = "checkpointDirectory" //optional
  // One param for each element map. key = sparkOptions.x -> value = value
  val sparkOptionsKey = "sparkoptions" // optional is a Map

  val listAllEphemeralTableKeys = List(
      kafkaConnection,
      kafkaTopic,
      kafkaGroupId,
      kafkaAdditionalOptionsKey,
      kafkaStorageLevel,
      atomicWindow,
      maxWindow,
      outputFormat,
      checkpointDirectory,
      sparkOptionsKey)

  // Default mandatory values
  val defaultKafkaConnection = "127.0.0.1:2181:9092"
  val defaultKafkaTopic = "XDTopic:1"
  val defaultKafkaGroupId = "XDgroup"
  val defaultAtomicWindow = "5000" //ms
  val defaultMaxWindow = "20000" //ms

  // Default values
  val defaultKafkaStorageLevel = "MEMORY_AND_DISK_SER"
  val defaultOutputFormat = "ROW"
  val defaultCheckpointDirectory = "checkpoint/crossdata"

  val listMandatoryEphemeralTableKeys = List(
    kafkaConnection,
    kafkaTopic,
    kafkaGroupId,
    atomicWindow,
    maxWindow)

  // Default mandatory Map
  val defaultEphemeralTableMapConfig = Map(
    kafkaConnection -> defaultKafkaConnection,
    kafkaTopic -> defaultKafkaTopic,
    kafkaGroupId -> defaultKafkaGroupId,
    atomicWindow -> defaultAtomicWindow,
    maxWindow -> defaultMaxWindow)

  def createEphemeralTableModel(ident: String, opts : Map[String, String]) : EphemeralTableModel = {

    val finalOptions = getEphemeralTableOptions(opts)

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

    EphemeralTableModel(ident, ephemeralOptions)

  }

  // Options with order preference
  private def getEphemeralTableOptions(opts : Map[String, String]): Map[String, String] = {
    // first if opts has confFilePath, we take configFile, and then opts if there is no confFilePath , we take opts
    val filePath = opts.get(streamingConfFilePath)
    val options = filePath.fold(opts)(extractConfigFromFile(_)) ++ opts

    val finalOptions = options ++ listMandatoryEphemeralTableKeys
      .map(key => key -> options.getOrElse(key, notFound(key, defaultEphemeralTableMapConfig))).toMap
    finalOptions
  }

  /**
    * Ephemeral Query config
    *  */

  val querySql = "sql"
  val queryAlias = "alias"
  val queryWindow = "window"
  val queryOptionsKey = "queryoptions"

  def createEphemeralQueryModel(ident: String, opts : Map[String, String]) : Either[Seq[String], EphemeralQueryModel] = {

    val finalOptions = getOptions(opts)

    val notPresent = Seq(queryAlias, querySql) filterNot (finalOptions contains _)
    val parametersOk = notPresent.isEmpty

    parametersOk match {
      case true   =>
        val alias = finalOptions(queryAlias)
        val sql = finalOptions(querySql)
        val window = finalOptions.getOrElse(queryWindow, "5").toInt
        val queryOptions = finalOptions.filter{case (k, v) => k.startsWith(queryOptionsKey)}
        Right(EphemeralQueryModel(ident, alias, sql, window, queryOptions))

      case false  =>
        Left(notPresent.map(notFoundMandatory(_)))
    }

  }

  /**
    * Ephemeral Status config
    *  */

  val status = "status"
  val startedTime = "startedTime"
  val stoppedTime = "stoppedTime"

  def createEphemeralStatusModel(ident: String, opts : Map[String, String]) : Either[String, EphemeralStatusModel] = {
    val finalOptions = getOptions(opts)
    finalOptions.contains(status) match {
      case true   =>
        val stat = parseStatus(finalOptions(status))
        val start = finalOptions.get(startedTime).map(_.toLong)
        val stop = finalOptions.get(stoppedTime).map(_.toLong)
        Right(EphemeralStatusModel(ident, stat, start, stop))
      case false  => Left(notFoundMandatory(status))
    }

  }

  //TODO implement this
  val listEphemeralExecutionStatus = ("NotStarted", "Starting", "Started", "Stopping", "Stopped", "Error")
  def parseStatus(value: String): EphemeralExecutionStatus.Value = EphemeralExecutionStatus.Started


  // Options with order preference
  private def getOptions(opts : Map[String, String]): Map[String, String] = {
    // first if opts has confFilePath, we take configFile, and then opts if there is no confFilePath , we take opts
    val filePath = opts.get(streamingConfFilePath)
    filePath.fold(opts)(extractConfigFromFile(_)) ++ opts
  }


  private def extractConfigFromFile(filePath : String): Map[String, String] = {

    val configFile =
      new TypesafeConfig(
      None,
      None,
      Some(filePath),
      Some(CoreConfig.ParentConfigName + "." + XDContext.StreamingConfigKey)
    )

    configFile.toStringMap

  }


    // Return default value and log a message
  def notFound(key: String, mandatoryOptions: Map[String, String]): String = {
    val logger= Logger.getLogger(classOf[XDStreamingCatalog])
    logger.warn(s"Mandatory parameter $key not specified, this could cause errors.")

    mandatoryOptions(key)
  }

  // TODO give another oportunity
  def notFoundMandatory(key: String): String = {
    val logger= Logger.getLogger(classOf[XDStreamingCatalog])
    val message = s"Mandatory parameter $key not specified, you have to specify it."
    logger.error(message)

    "ERROR: " +  message
  }


}