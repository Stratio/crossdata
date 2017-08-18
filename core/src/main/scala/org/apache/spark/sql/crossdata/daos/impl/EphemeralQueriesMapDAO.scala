package org.apache.spark.sql.crossdata.daos.impl

import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.{EphemeralQueriesMapDAO => EphQueriesMapDAO}

import scala.util.Try

class EphemeralQueriesMapDAO(opts: Map[String, String], subPath: Option[String] = None)
  extends EphQueriesMapDAO{

  val memoryMap = opts
  override lazy val config: Config = new DummyConfig(subPath)

  def prefix: String = Try(config.getString(PrefixStreamingCatalogsConfig) + "_") getOrElse ("")
}
