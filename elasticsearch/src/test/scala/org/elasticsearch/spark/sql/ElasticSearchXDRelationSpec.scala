package org.elasticsearch.spark.sql

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.{Filter, LeafNode, LogicalPlan, Project}
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class ElasticSearchXDRelationSpec extends BaseXDTest with MockitoSugar {


  "An ElasticSearchXDRelation " should "support Limit Node" in {
    //Fixture
    val logicalStep = mock[Filter]
    val wholeLogicalPlan = mock[LogicalPlan]

    val esRelation = buildElasticSearchRelation()

    //Experimentation
    val result = esRelation.isSupported(logicalStep, wholeLogicalPlan)

    //Expectations
    result should be (true)
  }

  it should "support LeafNode Node" in {
    //Fixture
    val logicalStep = mock[LeafNode]
    val wholeLogicalPlan = mock[LogicalPlan]

    val esRelation = buildElasticSearchRelation()

    //Experimentation
    val result = esRelation.isSupported(logicalStep, wholeLogicalPlan)

    //Expectations
    result should be (true)
  }

  it should "support Project Node" in {
    //Fixture
    val logicalStep = mock[Project]
    val wholeLogicalPlan = mock[LogicalPlan]

    val esRelation = buildElasticSearchRelation()

    //Experimentation
    val result = esRelation.isSupported(logicalStep, wholeLogicalPlan)

    //Expectations
    result should be (true)
  }

  def buildElasticSearchRelation(): ElasticsearchXDRelation = {
    val parameters: Map[String, String] = mock[Map[String, String]]
    val sqlContext: SQLContext = mock[SQLContext]
    val schemaProvided: Option[StructType] = None

    new ElasticsearchXDRelation(parameters, sqlContext, schemaProvided)
  }

}
