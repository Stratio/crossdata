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

import org.apache.spark.sql.crossdata.utils.Reflect._

class XDSharedState(sparkContext: SparkContext, catalogConfig: Config) extends {

  override val externalCatalog: ExternalCatalog = XDSharedState.instantiateCatalog(sparkContext, catalogConfig)

} with SharedState(sparkContext)


object XDSharedState {

  private[crossdata] def instantiateCatalog(sparkContext: SparkContext, catalogConfig: Config ): ExternalCatalog = {

    val classKey: String = "class"

    if (catalogConfig.hasPath(classKey))
      reflect[ExternalCatalog, TypesafeConfigSettings](
        catalogConfig.getString(classKey),
        TypesafeConfigSettings(catalogConfig)
      )
    else { // Fallback to SPARK's default behaviour

      val HIVE_EXTERNAL_CATALOG_CLASS_NAME = "org.apache.spark.sql.hive.HiveExternalCatalog"

      val externalCatalogClassName: String = sparkContext.conf.get(CATALOG_IMPLEMENTATION) match {
        case "hive" => HIVE_EXTERNAL_CATALOG_CLASS_NAME
        case "in-memory" => classOf[InMemoryCatalog].getCanonicalName
      }

      reflect[ExternalCatalog, SparkConf, Configuration](
        externalCatalogClassName,
        sparkContext.conf,
        sparkContext.hadoopConfiguration)

    }

  }

}