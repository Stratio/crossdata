/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalog.utils

import java.lang.reflect.Constructor

import com.typesafe.config.Config
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDPersistentCatalog, XDStreamingCatalog}
import org.apache.spark.sql.crossdata.config.CoreConfig

object CatalogUtils extends Logging {

  protected[crossdata] def externalCatalog(catalystConf: CatalystConf, config: Config): XDPersistentCatalog = {
    import CoreConfig.DerbyClass
    val externalCatalogName = if (config.hasPath(CoreConfig.ClassConfigKey) && !config.getString(CoreConfig.ClassConfigKey).isEmpty)
      config.getString(CoreConfig.ClassConfigKey)
    else DerbyClass

    val externalCatalogClass = Class.forName(externalCatalogName)
    val constr: Constructor[_] = externalCatalogClass.getConstructor(classOf[CatalystConf])

    constr.newInstance(catalystConf).asInstanceOf[XDPersistentCatalog]
  }

  protected[crossdata] def streamingCatalog(catalystConf: CatalystConf, serverConfig: Config): Option[XDStreamingCatalog] = {
    if (serverConfig.hasPath(CoreConfig.StreamingCatalogClassConfigKey)) {
      val streamingCatalogClass = serverConfig.getString(CoreConfig.StreamingCatalogClassConfigKey)
      val xdStreamingCatalog = Class.forName(streamingCatalogClass)
      val constr: Constructor[_] = xdStreamingCatalog.getConstructor(classOf[CatalystConf], classOf[Config])
      Option(constr.newInstance(catalystConf, serverConfig).asInstanceOf[XDStreamingCatalog])
    } else {
      logWarning("There is no configured streaming catalog")
      None
    }
  }
}
