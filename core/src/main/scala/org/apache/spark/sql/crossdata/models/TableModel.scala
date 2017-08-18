package org.apache.spark.sql.crossdata.models

import org.apache.spark.sql.crossdata

case class TableModel(id: String,
                      name: String,
                      schema: String,
                      dataSource: String,
                      database: Option[String] = None,
                      partitionColumns: Seq[String] = Seq.empty,
                      options: Map[String, String] = Map.empty,
                      version: String = crossdata.CrossdataVersion) {

  def getExtendedName: String =
    database.fold(name) { databaseName => s"$databaseName.$name" }

  def toPrettyString : String = ModelUtils.modelToJsonString(this)
}
