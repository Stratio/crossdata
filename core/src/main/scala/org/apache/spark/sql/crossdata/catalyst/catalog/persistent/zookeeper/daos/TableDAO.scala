package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos

import akka.cluster.Member
import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import com.stratio.common.utils.components.dao.GenericDAOComponent
import com.stratio.common.utils.components.logger.impl.Slf4jLoggerComponent
import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models.TableModel
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos.DAOConstants.{BaseZKPath, PrefixPermantCatalogsConfig, TablesViewsPath}
import org.apache.spark.sql.crossdata.serializers._
import org.apache.spark.sql.crossdata.serializers.akka.{AkkaClusterMemberSerializer, AkkaMemberStatusSerializer}
import org.json4s.Formats

import scala.util.Try

class TableDAO(configuration: Config) extends GenericDAOComponent[TableModel]
  with TypesafeConfigComponent with Slf4jLoggerComponent with CrossdataSerializer with PrefixedDAO {

  override implicit val formats: Formats = json4sJacksonFormats + SQLResultSerializer + UUIDSerializer +
    StructTypeSerializer + FiniteDurationSerializer + CommandSerializer + StreamedSuccessfulSQLResultSerializer +
    AkkaMemberStatusSerializer + AkkaClusterMemberSerializer + new SortedSetSerializer[Member]()

  override val dao: DAO = new GenericDAO(Option(s"$BaseZKPath/$prefix$TablesViewsPath"))

  def prefix: String = Try(configuration.getString(PrefixPermantCatalogsConfig) + "_") getOrElse ("")
  override val config = new TypesafeConfig(Option(configuration))

}
