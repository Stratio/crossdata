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

/***
Ephemeral table config
*/
  //EphemeralTableModel.name //user has to set this param

  //kafka
  //Connection //format "host0:consumerPort0:producerPort0,host1:consumerPort1:producerPort1,host2:consumerPort2:producerPort2"
  val kafkaConnection = "kafka.connection"

  //format "topicName1,topicName2,topicName3"
  val kafkaTopicName = "kafka.topicName"
  val kafkaGroupId = "kafka.groupId"

  val kafkaTopicPartition = "kafka.topicPartition" //optional

  val kafkaPartition = "kafka.partition" //optional
  //would go through additionalOptions param.
  // One param for each element map. key = kafka.additionalOptions.x -> value = value
  val kafkaAdditionalOptionsKey = "kafka.Options" //optional
  val kafkaStorageLevel = "kafka.storageLevel" //optional
  val atomicWindow ="atomicWindow"
  val maxWindow = "maxWindow"
  val outputFormat = "outputFormat" //optional
  val checkpointDirectory = "checkpointDirectory" //optional
  //would go through sparkOptions param.
  // One param for each element map. key = sparkOptions.x -> value = value
  val sparkOptionsKey = "sparkOptions" // optional is a Map

  val listAllEphemeralTableKeys = List(
      kafkaConnection,
      kafkaTopicName,
      kafkaGroupId,
      kafkaTopicPartition,
      kafkaAdditionalOptionsKey,
      kafkaStorageLevel,
      atomicWindow,
      maxWindow,
      outputFormat,
      checkpointDirectory,
      sparkOptionsKey)


  // Default mandatory values
  val defaultKafkaConnection = "127.0.0.1:2181:9092"
  val defaultKafkaTopicName = "XDTopic"
  val defaultKafkaGroupId = "XDgroup"
  val defaultAtomicWindow = "5000" //ms
  val defaultMaxWindow = "20000" //ms

  // Default values
  val defaultKafkaStorageLevel = "MEMORY_AND_DISK_SER"

  val listMandatoryEphemeralTableKeys = List(
    kafkaConnection,
    kafkaTopicName,
    kafkaGroupId,
    atomicWindow,
    maxWindow,
    outputFormat)

  // Default mandatory Map
  val defaultEphemeralTableMapConfig = Map(
    kafkaConnection -> defaultKafkaConnection,
    kafkaTopicName -> defaultKafkaTopicName,
    kafkaGroupId -> defaultKafkaGroupId,
    atomicWindow -> defaultAtomicWindow,
    maxWindow -> defaultMaxWindow)

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

  private def getEphemeralTableOptions(opts : Map[String, String]): Map[String, String] = {
    // first if opts has confFilePath, we take configFile, and then opts if there is no confFilePath , we take opts
    val filePath = opts.get("streaming.confFilePath")
    val options = filePath.fold(opts)(extractConfigFromFile(_))

    val finalOptions = listMandatoryEphemeralTableKeys
      .map(key => key -> options.getOrElse(key, notFound(key, defaultEphemeralTableMapConfig))).toMap

    finalOptions
    //TODO here parse options that has all the user conf

  }

  def createEphemeralTableModel(ident: String, opts : Map[String, String]) : EphemeralTableModel = {

    val finalOptions = getEphemeralTableOptions(opts)

    // Mandatory
    val connections = finalOptions(kafkaConnection)
      .split(",").map(_.split(":")).map(c => ConnectionHostModel(c(0), c(1), c(2))).toSeq
    // TODO review topic: make sense several topics for one table?
    val topicName = finalOptions(kafkaTopicName)
    val topicPartition = finalOptions.getOrElse(kafkaTopicPartition, TopicModel.DefaultPartition).toString
    val topics= Seq(TopicModel(topicName, topicPartition))
    val groupId = finalOptions(kafkaGroupId)
    val partition = finalOptions.get(kafkaPartition)
    val kafkaAdditionalOptions = finalOptions.filter(kv => kv._1.startsWith(kafkaAdditionalOptionsKey))
    val storageLevel = finalOptions.getOrElse(kafkaStorageLevel, defaultKafkaStorageLevel)
    val kafkaOptions = KafkaOptionsModel(connections, topics, groupId, partition, kafkaAdditionalOptions, storageLevel)
    val ephemeralOptions = EphemeralOptionsModel(kafkaOptions)

    EphemeralTableModel(ident, ephemeralOptions)
  }


  def createEphemeralQueryModel(ident: String, opts : Map[String, String]) : EphemeralQueryModel = ???

  def createEphemeralStatusModel(ident: String, opts : Map[String, String]) : EphemeralStatusModel = ???


  def parseEphemeralStatusFileParams(): Map[String, String] = ???
  def parseEphemeralStatusDdlParams(opts : Map[String, String]): Map[String, String] = ???

  // Return default value and log a message
  def notFound(key: String, mandatoryOptions: Map[String, String]): String = {
    val logger= Logger.getLogger(classOf[XDStreamingCatalog])
    logger.warn(s"Mandatory parameter $key not specified, this could cause errors.")

    mandatoryOptions(key)
  }



}