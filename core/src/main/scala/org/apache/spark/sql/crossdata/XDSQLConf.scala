/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata

import org.apache.spark.sql.SQLConf

trait XDSQLConf extends SQLConf {
  def enableCacheInvalidation(enable: Boolean): XDSQLConf
}


object XDSQLConf {

  val UserIdPropertyKey = "crossdata.security.user"

  implicit def fromSQLConf(conf: SQLConf): XDSQLConf = new XDSQLConf {

    override def enableCacheInvalidation(enable: Boolean): XDSQLConf = this
    override protected[spark] val settings: java.util.Map[String, String] = conf.settings
  }
}

