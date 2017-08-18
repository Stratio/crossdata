package org.apache.spark.sql.crossdata.daos.impl

import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.EphemeralTableStatusDAO

import scala.util.Try

class EphemeralTableStatusTypesafeDAO (configuration: Config) extends EphemeralTableStatusDAO {

  def prefix: String = Try(configuration.getString(PrefixStreamingCatalogsConfig) + "_") getOrElse ("")

  override val config = new TypesafeConfig(Option(configuration))

}
