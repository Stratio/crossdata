// scalastyle:off
/* Modification and adapations - Copyright (C) 2015 Stratio (http://stratio.com)
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// scalastyle:on

package org.apache.spark.sql.crossdata

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.SparkPlan
import org.apache.spark.sql.sources.CreateTableUsing
import org.apache.spark.sql.{SQLContext, Strategy}


private[crossdata] trait XDStrategies {
  self: SQLContext#SparkPlanner =>

  val XDContext: XDContext

  object XDDDLStrategy extends Strategy {
    def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {
      case CreateTableUsing(
      tableName, userSpecifiedSchema, provider, false, opts, allowExisting, managedIfNoPath) =>
        val xdCatalog=new DefaultCatalog
        xdCatalog.open()
        xdCatalog.persistTableXD(tableName, userSpecifiedSchema, provider,false, opts, allowExisting, managedIfNoPath)
        xdCatalog.close()
        //TODO CAMBIAR POR RUNNABLE COMMAND
        Nil
      case _ => Nil
    }
  }

}
