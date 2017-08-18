package org.apache.spark.sql.crossdata.daos.impl

import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.IndexDAO

import scala.util.Try

class IndexTypesafeDAO(configuration: Config) extends IndexDAO {

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")

  override val config = new TypesafeConfig(Option(configuration))

}
