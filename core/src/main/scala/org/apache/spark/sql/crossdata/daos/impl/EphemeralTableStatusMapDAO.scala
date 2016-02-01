package org.apache.spark.sql.crossdata.daos.impl

import org.apache.spark.sql.crossdata.daos.{EphemeralTableStatusMapDAO => EphTableStatusMapDAO }
class EphemeralTableStatusMapDAO (opts: Map[String, String], subPath: Option[String] = None)
  extends EphTableStatusMapDAO{

  val memoryMap = opts
  override lazy val config: Config = new DummyConfig(subPath)
}
