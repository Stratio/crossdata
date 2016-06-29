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
package org.apache.spark.sql.crossdata


import com.typesafe.config.Config
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDCatalogCommon, XDStreamingCatalog}
import org.apache.spark.sql.crossdata.config.CoreConfig

import scala.collection.JavaConversions._

object XDSharedState {
   // TODO move method
   implicit def sqlPropsToSQLConf(sparkSQLProps: Map[String, String]): SQLConf = {
    val sqlConf = new SQLConf
    sparkSQLProps.foreach { case (key, value) =>
      sqlConf.setConfString(key, value)
    }
    sqlConf
  }
}

// TODO rename XDSharedState
class XDSharedState( // TODO externalCatalog
                     @transient val sc: SparkContext,
                     userConfig: Option[Config] = None, // TODO this is server config. Probably should be core => the server should remove the config prefix (config.spark.sql...)
                     val externalCatalog: XDCatalogCommon,
                     val streamingCatalog: Option[XDStreamingCatalog]
                     ) {

  import XDSharedState._

  lazy val sparkSQLProps: Map[String,String] ={
    userConfig.map{ conf =>
      conf.entrySet()
        .map(e => (e.getKey, e.getValue.unwrapped().toString))
        .toMap
        .filterKeys(_.startsWith(CoreConfig.SparkSqlConfigPrefix))
        .map(e => (e._1.replace("config.", ""), e._2))
    }.getOrElse(Map.empty)
  }

  lazy val sqlConf: SQLConf = sparkSQLProps


}
