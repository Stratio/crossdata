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

import java.util.UUID
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import org.apache.spark.{Logging, SparkContext}
import com.typesafe.config.Config

/**
  *
  *[[XDSession]], as with Spark 2.0, SparkSession will be the Crossdata entry point for SQL interfaces. It wraps and
  *implements [[XDContext]]. Overriding those methods & attributes which vary among sessions and keeping
  *common ones in the delegated [[XDContext]].
 **
 *Resource initialization is avoided through attribute initialization laziness.
 */
class XDSession (
                  @transient override val sc: SparkContext,
                  session: UUID,
                  userConfig: Option[Config] = None
                )
  extends XDContext(sc) with Logging {

  // xdContext will host common Crossdata context entities
  private val xdContext: XDContext = XDContext.getOrCreate(sc, userConfig) //Delegated XDContext


  //TODO: Use catalog for this session instead fix one
  override protected[sql] lazy val catalog: XDCatalog = xdContext.catalog


}