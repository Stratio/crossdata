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

package org.apache.spark.sql.crossdata.hive

import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.crossdata.XDDataframe
import org.apache.spark.sql.hive.HiveContext

/**
 * CrossdataHiveContext leverages the features of [[org.apache.spark.sql.hive.HiveContext]]
 * and adds the features of the [[org.apache.spark.sql.crossdata.XDContext]].
 *
 * @param sc A [[org.apache.spark.SparkContext]].
 */
class XDHiveContext(sc: SparkContext) extends HiveContext(sc) {

  override def sql(sqlText: String): DataFrame = {
    XDDataframe(this, parseSql(sqlText))
  }
}

