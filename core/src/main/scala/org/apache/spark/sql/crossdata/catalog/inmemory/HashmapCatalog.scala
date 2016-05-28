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
package org.apache.spark.sql.crossdata.catalog.inmemory

import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext

import scala.collection.mutable
import scala.collection.mutable.HashMap

class HashmapCatalog(
                      val conf: CatalystConf = new SimpleCatalystConf(true),
                      override val xdContext: XDContext
                    ) extends MapCatalog {


  override protected def newMap: mutable.Map[String, LogicalPlan] = new HashMap[String, LogicalPlan]

  override def checkConnectivity: Boolean = true
}