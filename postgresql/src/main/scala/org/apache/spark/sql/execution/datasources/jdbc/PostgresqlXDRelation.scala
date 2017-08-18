package org.apache.spark.sql.execution.datasources.jdbc

import java.util.Properties

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.connector.NativeScan
import org.apache.spark.Partition
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.CatalystTypeConverters
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources.{BaseRelation, Filter}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.execution.datasources.jdbc.PostgresqlUtils._


class PostgresqlXDRelation( url: String,
                            table: String,
                            parts: Array[Partition],
                            properties: Properties = new Properties(),
                            @transient override val sqlContext: SQLContext,
                            userSchema: Option[StructType] = None)
  extends JDBCRelation(url, table, parts)(sqlContext)
    with NativeScan
    with SparkLoggerComponent {

  override val schema: StructType = userSchema.getOrElse(resolveSchema(url, table, properties))

  override def buildScan(requiredColumns: Array[String], filters: Array[Filter]): RDD[Row] = {
    // Rely on a type erasure hack to pass RDD[InternalRow] back as RDD[Row]
    PostgresqlRDD.scanTable(
      sqlContext.sparkContext,
      schema,
      url,
      properties,
      table,
      requiredColumns,
      filters,
      parts).asInstanceOf[RDD[Row]]
  }

  override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] =
    throw new RuntimeException("This method should not be called. Sql needed")

  override def buildScan(optimizedLogicalPlan: LogicalPlan, sqlText: String): Option[Array[Row]] = {
    logDebug(s"Processing ${optimizedLogicalPlan.toString()}")
    val queryExecutor = PostgresqlQueryProcessor(this, optimizedLogicalPlan, this.properties, sqlText)

    val toScala = CatalystTypeConverters.createToScalaConverter(optimizedLogicalPlan.schema)

    queryExecutor.execute() map { rows =>
      rows map { iRow =>
        toScala(iRow).asInstanceOf[GenericRowWithSchema]
      }
    }

  }

  /**
    * Checks the ability to execute a [[LogicalPlan]].
    *
    * @param logicalStep      isolated plan
    * @param wholeLogicalPlan the whole DataFrame tree
    * @return whether the logical step within the entire logical plan is supported
    */
  override def isSupported(logicalStep: LogicalPlan, wholeLogicalPlan: LogicalPlan): Boolean = logicalStep match {

    case ln: LeafNode => ln match {
      // It supports native query if all tables are in postgresql
      case LogicalRelation(br: BaseRelation, _) => br.isInstanceOf[PostgresqlXDRelation]
      case _ => true
    }

    case un: UnaryNode => un match {
      case Limit(_, _) | Project(_, _) | Filter(_, _) => true
      case _: Sort => true
      case _: Aggregate => true
      //TODO case _: Subquery => true
      case _ => false
    }
    case bn: BinaryNode => bn match {
      case _: Join | _: Union | _: Intersect | _: Except => true

      case _ => false
    }
    case unsupportedLogicalPlan =>logDebug(s"LogicalPlan $unsupportedLogicalPlan cannot be executed natively"); false
  }
}