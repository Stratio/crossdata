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
package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.{IndexesTypes, SearchDefinition}
import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.expressions.{Attribute, PrettyAttribute}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.sources.{Filter => SourceFilter, _}
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class ElasticSearchQueryProcessorSpec extends BaseXDTest with MockitoSugar {


  "A ElasticSearchQueryProcessor " should "build a Match All query in ES" in {
    //Fixture

    val logicalPlan: LogicalPlan = mock[LogicalPlan]
    val parameters = mock[Map[String, String]]
    val structType = Option(mock[StructType])
    val queryProcessor = ElasticSearchQueryProcessor(logicalPlan, parameters, structType)

    val requiredColums: Array[Attribute] = Array(new PrettyAttribute("title"))
    val filters: Array[SourceFilter] = Array()

    val indexType = IndexesTypes("movies/movie")
    val query = new SearchDefinition(indexType)

    //Experimentation
    val result = queryProcessor.buildNativeQuery(requiredColums, filters, query)

    //Expectations
    result should not be null
    result.toString().replace("\n", "").replace(" ", "") should be("{\"query\":{\"bool\":{}},\"fields\":\"title\"}")
  }


  it should "build a Simple Filter query in ES" in {
    //Fixture

    val logicalPlan: LogicalPlan = mock[LogicalPlan]
    val parameters = mock[Map[String, String]]
    val structType = Option(mock[StructType])
    val queryProcessor = ElasticSearchQueryProcessor(logicalPlan, parameters, structType)

    val requiredColums: Array[Attribute] = Array(new PrettyAttribute("title"))
    val filters: Array[SourceFilter] = Array(EqualTo("year", 1990))

    val indexType = IndexesTypes("movies/movie")
    val query = new SearchDefinition(indexType)

    //Experimentation
    val result = queryProcessor.buildNativeQuery(requiredColums, filters, query)

    //Expectations
    result should not be null
    result.toString().replace("\n", "").replace(" ", "") should be("{\"query\":{\"bool\":{}},\"post_filter\":{\"bool\":{\"must\":{\"term\":{\"year\":\"1990\"}}}},\"fields\":\"title\"}")
  }


  it should "build a AND Query" in {
    //Fixture

    val logicalPlan: LogicalPlan = mock[LogicalPlan]
    val parameters = mock[Map[String, String]]
    val structType = Option(mock[StructType])
    val queryProcessor = ElasticSearchQueryProcessor(logicalPlan, parameters, structType)

    val requiredColums: Array[Attribute] = Array(new PrettyAttribute("title"))
    val filters: Array[SourceFilter] = Array(EqualTo("year", 1990), EqualTo("Name", "Lord"))

    val indexType = IndexesTypes("movies/movie")
    val query = new SearchDefinition(indexType)

    //Experimentation
    val result = queryProcessor.buildNativeQuery(requiredColums, filters, query)

    //Expectations
    result should not be null
    result.toString().replace("\n", "").replace(" ", "") should be("{\"query\":{\"bool\":{}},\"post_filter\":{\"bool\":{\"must\":[{\"term\":{\"year\":\"1990\"}},{\"term\":{\"Name\":\"Lord\"}}]}},\"fields\":\"title\"}")
  }
}
