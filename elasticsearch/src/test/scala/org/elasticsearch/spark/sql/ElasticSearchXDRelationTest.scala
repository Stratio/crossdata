/**
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
package org.elasticsearch.spark.sql

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.{Filter, LeafNode, LogicalPlan, Project}
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class ElasticSearchXDRelationTest extends BaseXDTest with MockitoSugar {


  "A ElasticSearchXDRelation "
  ignore should "build a query using ESQueryProcessor" in {
    //Fixture
    val optimizedLogicalPlan = mock[LogicalPlan]
    val esRelation = buildElasticSearchRelation

    //Experimentation
    val result = esRelation.buildScan(optimizedLogicalPlan)

    //Expectations
    result should not be null
  }

  ignore should "support Limit Node" in {
    //Fixture
    val logicalStep = mock[Filter]
    val wholeLogicalPlan = mock[LogicalPlan]

    val esRelation = buildElasticSearchRelation

    //Experimentation
    val result = esRelation.isSupported(logicalStep,wholeLogicalPlan)

    //Expectations
    result should be (true)
  }

  it should "support LeafNode Node" in {
    //Fixture
    val logicalStep = mock[LeafNode]
    val wholeLogicalPlan = mock[LogicalPlan]

    val esRelation = buildElasticSearchRelation

    //Experimentation
    val result = esRelation.isSupported(logicalStep,wholeLogicalPlan)

    //Expectations
    result should be (true)
  }

  ignore should "support Project Node" in {
    //Fixture
    val logicalStep = mock[Project]
    val wholeLogicalPlan = mock[LogicalPlan]

    val esRelation = buildElasticSearchRelation

    //Experimentation
    val result = esRelation.isSupported(logicalStep,wholeLogicalPlan)

    //Expectations
    result should be (true)
  }

  def buildElasticSearchRelation() = {
    val parameters: Map[String, String] = mock[Map[String, String]]
    val sqlContext: SQLContext = mock[SQLContext]
    val schemaProvided: Option[StructType] = None

    new ElasticSearchXDRelation(parameters, sqlContext, schemaProvided)
  }

}
