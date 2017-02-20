package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos

import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import com.stratio.common.utils.components.dao.GenericDAOComponent
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.DatabaseModel
import DAOConstants._
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer

import scala.util.Try

class DatabaseDAO(configuration: Config) extends GenericDAOComponent[DatabaseModel]
  with TypesafeConfigComponent with SparkLoggerComponent with CrossdataSerializer with PrefixedDAO {

  override implicit val formats = json4sJacksonFormats

  override val dao: DAO = new GenericDAO(Option(s"$BaseZKPath/$prefix$DatabasesPath"))

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")
  override val config = new TypesafeConfig(Option(configuration))

}