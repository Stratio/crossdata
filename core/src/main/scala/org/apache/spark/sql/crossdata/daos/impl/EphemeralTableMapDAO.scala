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
