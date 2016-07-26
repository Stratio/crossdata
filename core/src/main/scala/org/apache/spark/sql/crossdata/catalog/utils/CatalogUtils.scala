/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.catalog.utils

import java.lang.reflect.Constructor

import com.typesafe.config.Config
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDPersistentCatalog, XDStreamingCatalog}
import org.apache.spark.sql.crossdata.config.CoreConfig

object CatalogUtils extends Logging {

  protected[crossdata] def externalCatalog(
      catalystConf: CatalystConf,
      config: Config): XDPersistentCatalog = {
    import CoreConfig.DerbyClass
    val externalCatalogName =
      if (config.hasPath(CoreConfig.ClassConfigKey))
        config.getString(CoreConfig.ClassConfigKey)
      else DerbyClass

    val externalCatalogClass = Class.forName(externalCatalogName)
    val constr: Constructor[_] =
      externalCatalogClass.getConstructor(classOf[CatalystConf])

    constr.newInstance(catalystConf).asInstanceOf[XDPersistentCatalog]
  }

  protected[crossdata] def streamingCatalog(
      catalystConf: CatalystConf,
      serverConfig: Config): Option[XDStreamingCatalog] = {
    if (serverConfig.hasPath(CoreConfig.StreamingCatalogClassConfigKey)) {
      val streamingCatalogClass =
        serverConfig.getString(CoreConfig.StreamingCatalogClassConfigKey)
      val xdStreamingCatalog = Class.forName(streamingCatalogClass)
      val constr: Constructor[_] = xdStreamingCatalog
        .getConstructor(classOf[CatalystConf], classOf[Config])
      Option(
          constr
            .newInstance(catalystConf, serverConfig)
            .asInstanceOf[XDStreamingCatalog])
    } else {
      logWarning("There is no configured streaming catalog")
      None
    }
  }
}
