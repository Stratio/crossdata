package org.apache.spark.sql.crossdata.daos.impl

import org.apache.spark.sql.crossdata.daos.{EphemeralTableMapDAO => EphTableMapDAO}

class EphemeralTableMapDAO (opts: Map[String, String], subPath: Option[String] = None)
  extends EphTableMapDAO{

  val memoryMap = opts
  override lazy val config: Config = new DummyConfig(subPath)
}
