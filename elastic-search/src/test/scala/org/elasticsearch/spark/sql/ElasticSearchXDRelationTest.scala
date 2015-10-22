package org.elasticsearch.spark.sql

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.{Project, LeafNode, Filter, LogicalPlan}
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

@RunWith(classOf[JUnitRunner])
class ElasticSearchXDRelationTest extends BaseXDTest with MockitoSugar {


  "A ElasticSearchXDRelation " should "build a query using ESQueryProcessor" in {
    //Fixture
    val optimizedLogicalPlan = mock[LogicalPlan]
    val esRelation = buildElasticSearchRelation

    //Experimentation
    val result = esRelation.buildScan(optimizedLogicalPlan)

    //Expectations
    result should not be null
  }

  it should "support Limit Node" in {
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

  it should "support Project Node" in {
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
