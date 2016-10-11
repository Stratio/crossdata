package org.apache.spark.sql.execution.datasources.jdbc

import java.util.Properties

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.connector.NativeScan
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.StructType
import org.apache.spark.Partition

class PostgresqlXDRelation( url: String,
                            table: String,
                            parts: Array[Partition],
                            properties: Properties = new Properties(),
                            @transient override val sqlContext: SQLContext,
                            userSchema: Option[StructType] = None)
  extends JDBCRelation(url, table, parts)(sqlContext)
  with NativeScan
  with SparkLoggerComponent {

  override val schema: StructType = userSchema.getOrElse(JDBCRDD.resolveTable(url, table, properties))

  override def buildScan(optimizedLogicalPlan: LogicalPlan): Option[Array[Row]] = ???

    /**
    * Checks the ability to execute a [[LogicalPlan]].
    *
    * @param logicalStep      isolated plan
    * @param wholeLogicalPlan the whole DataFrame tree
    * @return whether the logical step within the entire logical plan is supported
    */
  override def isSupported(logicalStep: LogicalPlan, wholeLogicalPlan: LogicalPlan): Boolean = ???
}
