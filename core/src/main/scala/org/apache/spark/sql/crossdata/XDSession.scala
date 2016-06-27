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
import org.apache.spark.Logging
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon
import org.apache.spark.sql.crossdata.catalog.{CatalogChain, XDCatalog}


object XDSession{
  // TODO It will be the main entryPoint, so we should add a XDSession builder to make it easier to work with.
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
                 xdSharedState: XDSharedState,
                 xdSessionState: XDSessionState,
                 userConfig: Option[Config] = None
                 )
  extends XDContext(xdSharedState.sc) with Logging {


  // TODO move context to sharedState => userConfig should be read before. Otherwise properties will be the same for all
  // TODO we can't move XDContext to XDSession because RunnableCommand use XDContext instead of SparkSession.sessionCatalog

  //TODO: +1 Use catalog for this session instead fix one
  override protected[sql] lazy val catalog: XDCatalog = {
    val catalogs: Seq[XDCatalogCommon] = (xdSessionState.temporaryCatalogs :+ externalCatalog) ++ streamingCatalog.toSeq
    CatalogChain(catalogs: _*)(this)
  }

  // XDContext does not allow to override conf so far.
  override protected[sql] lazy val conf: SQLConf = xdSessionState.sqlConf
}