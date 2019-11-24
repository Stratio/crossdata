/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.{IndexAndTypes, IndexesAndTypes, SearchDefinition}
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
    val queryProcessor = ElasticSearchQueryProcessor(logicalPlan, parameters, None)

    val requiredColums: Array[Attribute] = Array(new PrettyAttribute("title"))
    val filters: Array[SourceFilter] = Array()

    val indexType = IndexAndTypes("movies/movie")
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
    val queryProcessor = ElasticSearchQueryProcessor(logicalPlan, parameters, None)

    val requiredColums: Array[Attribute] = Array(new PrettyAttribute("title"))
    val filters: Array[SourceFilter] = Array(EqualTo("year", 1990))

    val indexType = IndexAndTypes("movies/movie")
    val query = new SearchDefinition(indexType)

    //Experimentation
    val result = queryProcessor.buildNativeQuery(requiredColums, filters, query)

    //Expectations
    result should not be null
    result.toString().replace("\n", "").replace(" ", "") should be("{\"query\":{\"bool\":{}},\"post_filter\":{\"bool\":{\"must\":{\"term\":{\"year\":1990}}}},\"fields\":\"title\"}")
  }


  it should "build a AND Query" in {
    //Fixture

    val logicalPlan: LogicalPlan = mock[LogicalPlan]
    val parameters = mock[Map[String, String]]
    val queryProcessor = ElasticSearchQueryProcessor(logicalPlan, parameters, None)

    val requiredColums: Array[Attribute] = Array(new PrettyAttribute("title"))
    val filters: Array[SourceFilter] = Array(EqualTo("year", 1990), EqualTo("Name", "Lord"))

    val indexType = IndexesAndTypes("movies/movie")
    val query = new SearchDefinition(indexType)

    //Experimentation
    val result = queryProcessor.buildNativeQuery(requiredColums, filters, query)

    //Expectations
    result should not be null
    result.toString().replace("\n", "").replace(" ", "") should be("{\"query\":{\"bool\":{}},\"post_filter\":{\"bool\":{\"must\":[{\"term\":{\"year\":1990}},{\"term\":{\"Name\":\"Lord\"}}]}},\"fields\":\"title\"}")
  }
}
