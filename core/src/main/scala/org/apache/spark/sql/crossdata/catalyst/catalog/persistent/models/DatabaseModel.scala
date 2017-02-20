package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models

import org.apache.spark.sql.catalog.Database
import org.apache.spark.sql.crossdata

case class DatabaseModel( db: Database,
                          options: Map[String, String] = Map.empty,
                          version: String = crossdata.CrossdataVersion
                        ) extends CatalogEntityModel {

  override def entryId: String = db.name

}