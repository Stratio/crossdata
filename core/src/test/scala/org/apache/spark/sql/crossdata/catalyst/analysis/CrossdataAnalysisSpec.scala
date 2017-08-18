/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalyst.analysis

import org.apache.spark.sql.catalyst.dsl.expressions._
import org.apache.spark.sql.catalyst.dsl.plans._

class CrossdataAnalysisSpec extends AnalysisTest{

  "CrossdataAggregateAlias rule" should "resolve alias references within the group by clause" in {
    val col1 = testRelation.output(0)
    val col2 = testRelation.output(1)

    val plan = testRelation.groupBy('alias)('col1 as 'alias)
    val expected = testRelation.groupBy(col1)(col1 as 'alias)

    checkAnalysis(plan, expected)
  }

  it should "resolve multiple alias references within the group by clause" in {
    val col1 = testRelation.output(0)
    val col2 = testRelation.output(1)

    val plan = testRelation.groupBy('alias1, 'alias2)('col1 as 'alias1, 'col2 as 'alias2)
    val expected = testRelation.groupBy(col1, col2)(col1 as 'alias1, col2 as 'alias2)

    checkAnalysis(plan, expected)
  }
}
