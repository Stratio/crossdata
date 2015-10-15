package com.stratio.crossdata.connector.elasticsearch

import org.apache.spark.Logging
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.types.StructType

object ElasticSearchQueryProcessorBuilder {
  def apply(logicalPlan: LogicalPlan, parameters: Map[String, String], schemaProvided: Option[StructType] = None) = new ElasticSearchQueryProcessor(logicalPlan, parameters, schemaProvided)
}

class ElasticSearchQueryProcessor(logicalPlan: LogicalPlan, parameters: Map[String, String], schemaProvided: Option[StructType] = None) extends Logging {
  def execute(): Option[Array[Row]] = ???
}