package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.{IndexesTypes, SearchDefinition}
import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.sources.{Filter => SourceFilter, _}
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class ElasticSearchQueryProcessorIT extends BaseXDTest with MockitoSugar {


  "A ElasticSearchQueryProcessor " should "build a Match All query in ES" in {
    //Fixture

    val logicalPlan: LogicalPlan = mock[LogicalPlan]
    val parameters = mock[Map[String, String]]
    val structType = Option(mock[StructType])
    val queryProcessor = ElasticSearchQueryProcessor(logicalPlan, parameters, structType)

    val requiredColums: Array[String] = Array()
    val filters: Array[SourceFilter] = Array()

    val indexType = IndexesTypes("movies/movie")
    val query = new SearchDefinition(indexType)

    //Experimentation
    val result = queryProcessor.buildNativeQuery(requiredColums, filters, query)

    //Expectations
    result should not be null
    result.toString() should be("{ }")
  }


  it should "build a Simple Filter query in ES" in {
    //Fixture

    val logicalPlan: LogicalPlan = mock[LogicalPlan]
    val parameters = mock[Map[String, String]]
    val structType = Option(mock[StructType])
    val queryProcessor = ElasticSearchQueryProcessor(logicalPlan, parameters, structType)

    val requiredColums: Array[String] = Array()
    val filters: Array[SourceFilter] = Array(EqualTo("year", 1990))

    val indexType = IndexesTypes("movies/movie")
    val query = new SearchDefinition(indexType)

    //Experimentation
    val result = queryProcessor.buildNativeQuery(requiredColums, filters, query)

    //Expectations
    result should not be null
    result.toString().replace("\n", "").replace(" ", "") should be("{\"post_filter\":{\"term\":{\"year\":\"1990\"}}}")
  }
}
