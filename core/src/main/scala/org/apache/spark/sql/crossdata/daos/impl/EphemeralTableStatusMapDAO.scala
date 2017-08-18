package org.apache.spark.sql.crossdata.daos.impl

import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.{EphemeralTableStatusMapDAO => EphTableStatusMapDAO}

import scala.util.Try
class EphemeralTableStatusMapDAO (opts: Map[String, String], subPath: Option[String] = None)
  extends EphTableStatusMapDAO{

  val memoryMap = opts
  override lazy val config: Config = new DummyConfig(subPath)

  def prefix: String = Try(config.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")
}
