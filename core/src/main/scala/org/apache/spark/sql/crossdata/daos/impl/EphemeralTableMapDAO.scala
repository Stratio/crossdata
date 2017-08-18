/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.daos.impl

import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.{EphemeralTableMapDAO => EphTableMapDAO}

import scala.util.Try

class EphemeralTableMapDAO (opts: Map[String, Any], subPath: Option[String] = None)
  extends EphTableMapDAO{

  def prefix: String = Try(config.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")

  val memoryMap = opts
  override lazy val config: Config = new DummyConfig(subPath)
}
