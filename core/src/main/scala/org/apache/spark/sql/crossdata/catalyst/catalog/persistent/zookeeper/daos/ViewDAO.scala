package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos

import com.typesafe.config.Config
import DAOConstants.{BaseZKPath, ViewsPath}

class ViewDAO(configuration: Config) extends TableDAO(configuration) {
  override val dao: DAO = new GenericDAO(Option(s"$BaseZKPath/$prefix$ViewsPath"))
}
