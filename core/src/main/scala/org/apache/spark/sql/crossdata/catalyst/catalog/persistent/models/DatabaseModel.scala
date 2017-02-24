package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models

import org.apache.spark.sql.catalyst.catalog.CatalogDatabase
import org.apache.spark.sql.crossdata

case class DatabaseModel(db: CatalogDatabase,
                         options: Map[String, String] = Map.empty,
                         version: String = crossdata.CrossdataVersion
                        ) extends CatalogEntityModel