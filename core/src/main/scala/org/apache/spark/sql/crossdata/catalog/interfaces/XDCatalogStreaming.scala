package org.apache.spark.sql.crossdata.catalog.interfaces

import org.apache.spark.sql.crossdata.models.{EphemeralQueryModel, EphemeralStatusModel, EphemeralTableModel}

trait XDCatalogStreaming {

  /**
    * Ephemeral Table Functions
    */
  def existsEphemeralTable(tableIdentifier: String): Boolean

  def getEphemeralTable(tableIdentifier: String) : Option[EphemeralTableModel]

  def getAllEphemeralTables : Seq[EphemeralTableModel]

  def createEphemeralTable(ephemeralTable: EphemeralTableModel): Either[String, EphemeralTableModel]

  def dropEphemeralTable(tableIdentifier: String): Unit

  def dropAllEphemeralTables(): Unit

  /**
    * Ephemeral Status Functions
    */
  protected[crossdata] def createEphemeralStatus(tableIdentifier: String, ephemeralStatusModel: EphemeralStatusModel): EphemeralStatusModel

  protected[crossdata] def getEphemeralStatus(tableIdentifier: String) : Option[EphemeralStatusModel]

  protected[crossdata] def getAllEphemeralStatuses : Seq[EphemeralStatusModel]

  protected[crossdata] def updateEphemeralStatus(tableIdentifier: String, status: EphemeralStatusModel) : Unit

  protected[crossdata] def dropEphemeralStatus(tableIdentifier: String): Unit

  protected[crossdata] def dropAllEphemeralStatus(): Unit

  /**
    * Ephemeral Queries Functions
    */
  def existsEphemeralQuery(queryAlias: String): Boolean

  def getEphemeralQuery(queryAlias: String) : Option[EphemeralQueryModel]

  def getAllEphemeralQueries : Seq[EphemeralQueryModel]

  def createEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Either[String, EphemeralQueryModel]

  def dropEphemeralQuery(queryAlias: String): Unit

  def dropAllEphemeralQueries(): Unit

}
