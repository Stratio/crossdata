package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models

import org.apache.spark.sql.catalyst.catalog.CatalogTablePartition
import org.apache.spark.sql.crossdata

case class PartitionModel(catalogTablePartition: CatalogTablePartition,
                          version: String =  crossdata.CrossdataVersion) extends CatalogEntityModel