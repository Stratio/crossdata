package org.apache.spark.sql.crossdata.daos.impl

import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.daos.DatabaseDAO
import org.apache.spark.sql.crossdata.daos.DAOConstants._

import scala.util.Try

class DatabaseTypesafeDAO(configuration: Config) extends DatabaseDAO {

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")

  override val config = new TypesafeConfig(Option(configuration))

}