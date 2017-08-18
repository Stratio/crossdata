/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
