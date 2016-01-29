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
import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent.TypesafeConfig
import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent.TypesafeConfig
import org.apache.spark.Logging
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.models.{EphemeralQueryModel, EphemeralStatusModel, EphemeralTableModel}

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

  // Streaming parameters
  val kafkaHost = "kafka.connectionHost"
  val kafkaConsumerPort = "kafka.connectionConsumerPort"
  val kafkaProducerPort = "kafka.connectionProducerPort"
  val kafkaTopicName = "kafka.topicName"
  val kafkaGroupId = "kafka.groupId"
  val kafkaPartition = "kafka.partition"

  // TODO add params and default options

  // Default mandatory values


  // Default mandatory Map
  val defaultMapConfig = Map()


  // Parse params
  val configFile = new TypesafeConfig(
    None,
    None,
    Some(CoreConfig.CoreBasicConfig),
    Some(CoreConfig.ParentConfigName + "." + XDContext.StreamingConfigKey)
  )

  //val configDdl

  def parseEphemeralTableFileParams(): Map[String, String] = ???
  def parseEphemeralTableDdlParams(): Map[String, String] = ???


  def parseEphemeralQueryFileParams(): Map[String, String] = ???
  def parseEphemeralQueryDdlParams(): Map[String, String] = ???


  def parseEphemeralStatusFileParams(): Map[String, String] = ???
  def parseEphemeralStatusDdlParams(): Map[String, String] = ???

}