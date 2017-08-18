package org.apache.spark.sql.execution.datasources.jdbc

import org.apache.spark.Partition

trait PostgresqlRelationUtils {

  protected def columnPartition(partitioning: JDBCPartitioningInfo): Array[Partition] =
    JDBCRelation.columnPartition(partitioning)
  protected def createJDBCPartitioningInfo( column: String,
                                            lowerBound: Long,
                                            upperBound: Long,
                                            numPartitions: Int): JDBCPartitioningInfo =
    JDBCPartitioningInfo(column, lowerBound, upperBound, numPartitions)
}
