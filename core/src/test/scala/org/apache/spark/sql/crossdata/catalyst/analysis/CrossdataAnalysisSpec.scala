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
package org.apache.spark.sql.crossdata.catalyst.analysis

import org.apache.spark.sql.catalyst.dsl.expressions._
import org.apache.spark.sql.catalyst.dsl.plans._

class CrossdataAnalysisSpec extends AnalysisTest {

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
