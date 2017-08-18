package org.apache.spark.sql.crossdata.daos.impl

import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.EphemeralTableDAO

import scala.util.Try

class EphemeralTableTypesafeDAO(configuration: Config) extends EphemeralTableDAO {

  def prefix: String = Try(configuration.getString(PrefixStreamingCatalogsConfig) + "_") getOrElse ("")

  override val config = new TypesafeConfig(Option(configuration))

}

