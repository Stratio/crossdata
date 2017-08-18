package org.apache.spark.sql.crossdata.daos.impl

import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.daos.TableDAO
import org.apache.spark.sql.crossdata.daos.DAOConstants._

import scala.util.Try

class TableTypesafeDAO(configuration: Config) extends TableDAO {

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")

  override val config = new TypesafeConfig(Option(configuration))

}
