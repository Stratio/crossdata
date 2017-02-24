package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.zookeeper.daos

trait PrefixedDAO {
  def prefix: String
}
