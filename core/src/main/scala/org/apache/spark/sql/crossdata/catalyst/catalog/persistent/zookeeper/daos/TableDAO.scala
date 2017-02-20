package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos

import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import com.stratio.common.utils.components.dao.GenericDAOComponent
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.typesafe.config.Config
import DAOConstants.{BaseZKPath, PrefixPermantCatalogsConfig, TablesViewsPath}
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.TableModel
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer

import scala.util.Try

class TableDAO(configuration: Config) extends GenericDAOComponent[TableModel]
  with TypesafeConfigComponent with SparkLoggerComponent with CrossdataSerializer with PrefixedDAO {

  override implicit val formats = json4sJacksonFormats

  override val dao: DAO = new GenericDAO(Option(s"$BaseZKPath/$prefix$TablesViewsPath"))

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")
  override val config = new TypesafeConfig(Option(configuration))

}
