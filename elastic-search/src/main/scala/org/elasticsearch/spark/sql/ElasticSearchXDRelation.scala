package org.elasticsearch.spark.sql

import com.stratio.crossdata.connector.NativeScan
import com.stratio.crossdata.connector.elasticsearch.ElasticSearchQueryProcessor
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SQLContext}


class ElasticSearchXDRelation(parameters: Map[String, String], sqlContext: SQLContext, userSchema: Option[StructType] = None)
  extends ElasticsearchRelation(parameters, sqlContext, userSchema) with NativeScan with Logging {


  override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = {
    logDebug(s"Processing ${optimizedLogicalPlan.toString()}")
    val queryExecutor = ElasticSearchQueryProcessor(optimizedLogicalPlan, parameters, userSchema)
    queryExecutor.execute()

  }



  /**
   * Checks the ability to execute a [[LogicalPlan]].
   *
   * @param logicalStep isolated plan
   * @param wholeLogicalPlan the whole DataFrame tree
   * @return whether the logical step within the entire logical plan is supported
   */
  override def isSupported(logicalStep: LogicalPlan, wholeLogicalPlan: LogicalPlan): Boolean = logicalStep match {
    case ln: LeafNode => true // TODO leafNode == LogicalRelation(xdSourceRelation)
    case un: UnaryNode => un match {
      case Limit(_, _) | Project(_, _) | Filter(_, _) => true
      case _ => false

    }
    case unsupportedLogicalPlan =>  false //TODO log.debug(s"LogicalPlan $unsupportedLogicalPlan cannot be executed natively");
  }
}
