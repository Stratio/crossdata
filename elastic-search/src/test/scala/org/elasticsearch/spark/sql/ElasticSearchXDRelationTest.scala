package org.elasticsearch.spark.sql

import com.stratio.crossdata.connector.elasticsearch.{ElasticSearchQueryProcessor, ElasticSearchQueryProcessorBuilder}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[PowerMockRunner])
@PrepareForTest(Array(classOf[ElasticSearchQueryProcessor]))
class ElasticSearchXDRelationTest extends FlatSpec with Matchers with MockitoSugar {


  "A ElasticSearchXDRelation " should "build a query using ESQueryProcessor" in {
    //Fixture
    val queryExecutor = mock[ElasticSearchQueryProcessor]
    val optimizedLogicalPlan = mock[LogicalPlan]
    val parameters: Map[String, String] = mock[Map[String, String]]
    val sqlContext: SQLContext = mock[SQLContext]
    val schemaProvided: Option[StructType] = None

    PowerMockito.mockStatic(ElasticSearchQueryProcessorBuilder.getClass)
    PowerMockito.when(ElasticSearchQueryProcessorBuilder.apply(optimizedLogicalPlan, parameters, schemaProvided)).thenReturn(queryExecutor)

    val esRelation = new ElasticSearchXDRelation(parameters, sqlContext)

    //Experimentation
    val result = esRelation.buildScan(optimizedLogicalPlan)

    //Expectations
    verify(queryExecutor).execute()
  }

}
