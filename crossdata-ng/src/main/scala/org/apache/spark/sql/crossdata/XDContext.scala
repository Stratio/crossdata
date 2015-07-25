/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.spark.sql.crossdata

import java.lang.reflect.Constructor

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SQLContext
import org.apache.spark.{Logging, SparkContext}

/**
 * CrossdataContext leverages the features of [[org.apache.spark.sql.SQLContext]]
 * and adds some features of the Crossdata system.
 *
 * @param sc A [[org.apache.spark.SparkContext]].
 */
class XDContext(sc: SparkContext) extends SQLContext(sc) with Logging {

  val catalogClass: String = ConfigFactory.load.getString("crossdata.catalog")

  val xdCatalog = Class.forName(catalogClass)

  val constr: Constructor[_] = xdCatalog.getConstructors()(0)

  override protected[sql] lazy val catalog: XDCatalog =
    constr.newInstance(super.conf, Some(this), None).asInstanceOf[XDCatalog]

  catalog.open()

}

