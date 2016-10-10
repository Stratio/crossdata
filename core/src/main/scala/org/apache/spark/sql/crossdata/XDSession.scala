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

import com.stratio.crossdata.security.CrossdataSecurityManager
import com.typesafe.config.Config
import org.apache.spark.Logging
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon
import org.apache.spark.sql.crossdata.catalog.{CatalogChain, XDCatalog}
import org.apache.spark.sql.crossdata.session.{XDSessionState, XDSharedState}

object XDSession {
  // TODO Spark2.0. It will be the main entryPoint, so we should add a XDSession builder to make it easier to work with.
}

/**
  *
  * [[XDSession]], as with Spark 2.0, SparkSession will be the Crossdata entry point for SQL interfaces. It wraps and
  * implements [[XDContext]]. Overriding those methods & attributes which vary among sessions and keeping
  * common ones in the delegated [[XDContext]].
  *
  * Resource initialization is avoided through attribute initialization laziness.
  */
class XDSession(
                 @transient private val xdSharedState: XDSharedState,
                 @transient private val xdSessionState: XDSessionState,
                 @transient private val userCoreConfig: Option[Config] = None
               )
  extends XDContext(xdSharedState.sc, userCoreConfig) with Logging {

  @transient
  override protected[sql] lazy val catalog: XDCatalog = {
    val catalogs: Seq[XDCatalogCommon] = (xdSessionState.temporaryCatalogs :+ xdSharedState.externalCatalog) ++ xdSharedState.streamingCatalog.toSeq
    CatalogChain(catalogs: _*)(this)
  }

  @transient
  override protected[sql] lazy val conf: SQLConf = xdSessionState.sqlConf.enableCacheInvalidation(false)

  @transient
  override protected[crossdata] lazy val securityManager: Option[CrossdataSecurityManager] = xdSharedState.securityManager

  xdSessionState.sqlConf.enableCacheInvalidation(true)

}
