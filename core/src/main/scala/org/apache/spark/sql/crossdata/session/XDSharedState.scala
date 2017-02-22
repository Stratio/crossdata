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
package org.apache.spark.sql.crossdata.session

import com.typesafe.config.Config
import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.catalyst.catalog._
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.XDExternalCatalog.{ExternalCatalogSettings, TypesafeConfigSettings}
import org.apache.spark.sql.internal.SharedState
import org.apache.spark.sql.internal.StaticSQLConf.CATALOG_IMPLEMENTATION

object XDSharedState {

  def apply(
             sparkContext: SparkContext,
             userCoreConfig: Option[Config] = None
            //val sqlConf: SQLConf,
            //val externalCatalog: XDCatalogCommon,
            //val streamingCatalog: Option[XDStreamingCatalog],
            //@transient val securityManager: Option[CrossdataSecurityManager]
  ): SharedState = {

    import org.apache.spark.sql.crossdata.utils.Reflect._

    new {

      //TODO: Change catalog config origin and path
      override val externalCatalog: ExternalCatalog = {
        for(config <- userCoreConfig; key = "catalog.class"; if config.hasPath(key)) yield {
          reflect[ExternalCatalog, ExternalCatalogSettings](
            config.getString(key), //TODO: Change catalog config origin and path
            TypesafeConfigSettings(config.getConfig("catalog")) //TODO: Change catalog config origin and path
          )
        }
      } getOrElse { // Fallback to SPARK's default behaviour

        val HIVE_EXTERNAL_CATALOG_CLASS_NAME = "org.apache.spark.sql.hive.HiveExternalCatalog"

        def externalCatalogClassName(conf: SparkConf): String = {
          conf.get(CATALOG_IMPLEMENTATION) match {
            case "hive" => HIVE_EXTERNAL_CATALOG_CLASS_NAME
            case "in-memory" => classOf[InMemoryCatalog].getCanonicalName
          }
        }

        reflect[ExternalCatalog, SparkConf, Configuration](
          externalCatalogClassName(sparkContext.conf),
          sparkContext.conf,
          sparkContext.hadoopConfiguration)
      }

    } with SharedState(sparkContext)

  }
}
