package org.apache.spark.sql.crossdata.daos.impl

import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.ViewDAO

import scala.util.Try

class ViewTypesafeDAO(configuration: Config) extends ViewDAO {

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")

  override val config = new TypesafeConfig(Option(configuration))

}
