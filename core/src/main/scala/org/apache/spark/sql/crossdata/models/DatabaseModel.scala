package org.apache.spark.sql.crossdata.models

import org.apache.spark.sql.catalog.Database
import org.apache.spark.sql.crossdata

case class DatabaseModel( id: String,
                          db: Database,
                          options: Map[String, String] = Map.empty,
                          version: String = crossdata.CrossdataVersion){

  def getExtendedName: String = db.name
  def toPrettyString : String = ModelUtils.modelToJsonString(this)
}
