package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos

import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import com.stratio.common.utils.components.dao.GenericDAOComponent
import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.PartitionModel
import DAOConstants._
import com.stratio.common.utils.components.logger.impl.Slf4jLoggerComponent
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer

import scala.util.Try

class PartitionDAO(configuration: Config) extends GenericDAOComponent[PartitionModel]
  with TypesafeConfigComponent with Slf4jLoggerComponent with CrossdataSerializer with PrefixedDAO {

  override implicit val formats = json4sJacksonFormats

  override val dao: DAO = new GenericDAO(Option(s"$BaseZKPath/$prefix$PartitionsPath"))

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")
  override val config = new TypesafeConfig(Option(configuration))

}
