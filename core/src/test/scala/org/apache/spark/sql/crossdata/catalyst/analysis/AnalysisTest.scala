/*
 * Modifications and adaptations - Copyright (C) 2015 Stratio (http://stratio.com)
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

package org.apache.spark.sql.crossdata.catalyst.analysis

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.{TableIdentifier, SimpleCatalystConf}
import org.apache.spark.sql.catalyst.analysis.Analyzer
import org.apache.spark.sql.catalyst.analysis.EmptyFunctionRegistry
import org.apache.spark.sql.catalyst.analysis.SimpleCatalog
import org.apache.spark.sql.catalyst.expressions.Alias
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.catalyst.expressions.ExprId
import org.apache.spark.sql.catalyst.plans.logical.LocalRelation
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.catalyst.util.sideBySide
import org.apache.spark.sql.types.StringType

trait AnalysisTest extends BaseXDTest {

  val testRelation = LocalRelation(AttributeReference("col1", StringType)(),
                                   AttributeReference("col2", StringType)())

  val caseSensitiveAnalyzer = {
    val caseSensitiveConf = new SimpleCatalystConf(true)
    val caseSensitiveCatalog = new SimpleCatalog(caseSensitiveConf)
    caseSensitiveCatalog.registerTable(TableIdentifier("table"), testRelation)

    new Analyzer(caseSensitiveCatalog,
                 EmptyFunctionRegistry,
                 caseSensitiveConf) {
      override val extendedResolutionRules = ResolveAggregateAlias :: Nil
    }
  }

  protected def checkAnalysis(inputPlan: LogicalPlan,
                              expectedPlan: LogicalPlan): Unit = {
    val analyzer = caseSensitiveAnalyzer
    val actualPlan = analyzer.execute(inputPlan)
    analyzer.checkAnalysis(actualPlan)
    comparePlans(actualPlan, expectedPlan)
  }

  protected def normalizeExprIds(plan: LogicalPlan) = {
    plan transformAllExpressions {
      case a: AttributeReference =>
        AttributeReference(a.name, a.dataType, a.nullable)(exprId = ExprId(0))
      case a: Alias =>
        Alias(a.child, a.name)(exprId = ExprId(0))
    }
  }

  protected def comparePlans(plan1: LogicalPlan, plan2: LogicalPlan) {
    val normalized1 = normalizeExprIds(plan1)
    val normalized2 = normalizeExprIds(plan2)
    if (normalized1 != normalized2) {
      fail(s"""
           |== FAIL: Plans do not match ===
           |${sideBySide(normalized1.treeString, normalized2.treeString)
            .mkString("\n")}
         """.stripMargin)
    }
  }
}
