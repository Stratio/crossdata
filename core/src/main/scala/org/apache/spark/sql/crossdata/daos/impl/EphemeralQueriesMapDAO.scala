package org.apache.spark.sql.crossdata.daos.impl

import org.apache.spark.sql.crossdata.daos.{EphemeralQueriesMapDAO => EphQueriesMapDAO}

class EphemeralQueriesMapDAO(opts: Map[String, String], subPath: Option[String] = None)
  extends EphQueriesMapDAO{

  val memoryMap = opts
  override lazy val config: Config = new DummyConfig(subPath)
}
