package com.stratio.crossdata.connector.elasticsearch

import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElasticSearchNewTypesIT extends ElasticDataTypes {

  doTypesTest("ElasticSearch")

}
