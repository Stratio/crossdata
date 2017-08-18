package org.apache.spark.sql.crossdata.daos.impl

import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.daos.AppDAO
import org.apache.spark.sql.crossdata.daos.DAOConstants._

import scala.util.Try

class AppTypesafeDAO(configuration: Config) extends AppDAO {

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")

  override val config = new TypesafeConfig(Option(configuration))

}
