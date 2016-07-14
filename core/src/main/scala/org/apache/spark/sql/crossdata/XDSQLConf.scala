package org.apache.spark.sql.crossdata

import org.apache.spark.sql.SQLConf

trait XDSQLConf extends SQLConf {
  def enableCacheInvalidation(enable: Boolean): XDSQLConf
}


object XDSQLConf {

  implicit def fromSQLConf(conf: SQLConf): XDSQLConf = new XDSQLConf {

    override def enableCacheInvalidation(enable: Boolean): XDSQLConf = this
    override protected[spark] val settings: java.util.Map[String, String] = conf.settings
  }
}

