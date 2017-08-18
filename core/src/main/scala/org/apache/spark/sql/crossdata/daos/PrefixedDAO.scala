package org.apache.spark.sql.crossdata.daos

trait PrefixedDAO {
  def prefix: String
}
