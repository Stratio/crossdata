package org.apache.spark.sql.crossdata.models

import org.apache.spark.sql.crossdata

case class ViewModel(id: String,
                     name: String,
                     database: Option[String] = None,
                     sqlViewField: String,
                     version: String = crossdata.CrossdataVersion) {

  def getExtendedName: String =
    database.fold(name) { databaseName => s"$databaseName.$name" }
}
