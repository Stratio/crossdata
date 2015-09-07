/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.stratio.crossdata.sql.sources.cassandra


import java.sql.Timestamp
import java.util.Date

import com.datastax.driver.core.{ProtocolVersion, ResultSet}
import com.datastax.spark.connector.GettableData
import com.stratio.crossdata.sql.sources.cassandra.CassandraColumnRole._
import org.apache.spark.Logging
import org.apache.spark.sql.cassandra.{CassandraSQLRow, CassandraXDSourceRelation}
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.sources
import org.apache.spark.sql.sources.crossdata.CatalystToCrossdataAdapter
import org.apache.spark.sql.sources.{Filter => SourceFilter}
import org.apache.spark.sql.types.UTF8String


object CassandraQueryProcessor {

  val DefaultLimit = 10000
  type ColumnName = String

  def apply(cassandraRelation: CassandraXDSourceRelation, logicalPlan: LogicalPlan) = new CassandraQueryProcessor(cassandraRelation, logicalPlan)

  def buildNativeQuery(tableQN: String, requiredColums: Array[ColumnName], filters: Array[SourceFilter], limit: Int): String = {
    val columns = requiredColums.mkString(", ")
    val orderBy = ""

    def quoteString(in: Any): String = in match {
      case s: UTF8String => s"'$s'"
      case other => other.toString
    }

    def filterToCQL(filter: SourceFilter): String = filter match {

      case sources.EqualTo(attribute, value) => s"$attribute = ${quoteString(value)}"

      case sources.In(attribute, values) => s"$attribute IN ${values.map(quoteString).mkString("(", ",", ")")}"

      case sources.LessThan(attribute, value) => s"$attribute < $value"

      case sources.GreaterThan(attribute, value) => s"$attribute > $value"

      case sources.LessThanOrEqual(attribute, value) => s"$attribute <= $value"

      case sources.GreaterThanOrEqual(attribute, value) => s"$attribute => $value"

    }

    val filter = if (filters.nonEmpty) filters.map(filterToCQL).mkString("WHERE ", " AND ", "") else ""

    s"SELECT $columns FROM $tableQN $filter $orderBy LIMIT $limit ALLOW FILTERING"
  }

}

// TODO logs, doc, tests
class CassandraQueryProcessor(cassandraRelation: CassandraXDSourceRelation, logicalPlan: LogicalPlan) extends Logging {

  import com.stratio.crossdata.sql.sources.cassandra.CassandraQueryProcessor._

  def execute(): Option[Array[Row]] = {
    try {
      validatedNativePlan.map { case (columnsRequired, filters, limit) =>
        val cqlQuery = buildNativeQuery(cassandraRelation.tableDef.name, columnsRequired, filters, limit.getOrElse(CassandraQueryProcessor.DefaultLimit))
        val resultSet = cassandraRelation.connector.withSessionDo { session =>
          session.execute(cqlQuery)
        }
        sparkResultFromCassandra(columnsRequired, resultSet)
      }
    } catch {
      case exc: Exception => log.warn(s"Exception executing the native query $logicalPlan", exc.getMessage); None
    }

  }


  def validatedNativePlan: Option[(Array[ColumnName], Array[SourceFilter], Option[Int])] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num}

    def findProjectsFilters(lplan: LogicalPlan): (Array[ColumnName], Array[SourceFilter], Boolean) = {
      lplan match {
        case Limit(_, child) => findProjectsFilters(child)
        case PhysicalOperation(projectList, filterList, _) => CatalystToCrossdataAdapter.getFilterProject(logicalPlan, projectList, filterList)
      }
    }

    val (projects, filters, filtersIgnored) = findProjectsFilters(logicalPlan)

    if (filtersIgnored || ! checkNativeFilters(filters)) {
      None
    } else {
      Some(projects, filters, limit)
    }

  }





  private[this] def checkNativeFilters(filters: Array[SourceFilter]): Boolean = {
    // TODO test filter on PK (=) => filter on all partition keys
    // TODO test filter on PK (IN) => last column of partition key
    // TODO test filter on PKs and CKs(=) => filter on all pks and cks
    // TODO test filter on PKs (=) and CKs(any) 
    // TODO test filter only on CKs => ALLOW FILTERING
    // TODO test filter on secondaryIndex => equal operator
    // TODO test filter on secondary + PK (=) + CK(any)

    val groupedFilters = filters.groupBy {
      case sources.EqualTo(attribute, _) => columnRole(attribute)
      case sources.In(attribute, _) => columnRole(attribute)
      case sources.LessThan(attribute, _) => columnRole(attribute)
      case sources.GreaterThan(attribute, _) => columnRole(attribute)
      case sources.LessThanOrEqual(attribute, _) => columnRole(attribute)
      case sources.GreaterThanOrEqual(attribute, _) => columnRole(attribute)
      case _ => Unknown
    }

    def checksClusteringKeyFilters: Boolean = {
      if (groupedFilters.contains(ClusteringKey)) {
        // if there is a CK filter then all CKs should be included. Accept any kind of filter
        val clusteringColsInFilter = groupedFilters.get(ClusteringKey).get.flatMap(columnNameFromFilter)
        cassandraRelation.tableDef.clusteringColumns.forall { column =>
          clusteringColsInFilter.contains(column.columnName)
        }
      } else {
        true
      }
    }

    def checksSecondaryIndexesFilters: Boolean = {
      if (groupedFilters.contains(Indexed)) {
        //Secondary indexes => equals are allowed
        groupedFilters.get(ClusteringKey).get.forall {
          case sources.EqualTo(_, _) => true
          case _ => false
        }
      } else {
        true
      }
    }

    def checksPartitionKeyFilters: Boolean = {
      if (groupedFilters.contains(PartitionKey)) {
        val partitionColsInFilter = groupedFilters.get(PartitionKey).get.flatMap(columnNameFromFilter)

        // all PKs must be presents
        cassandraRelation.tableDef.partitionKey.forall { column =>
          partitionColsInFilter.contains(column.columnName)
        }
        // filters condition must be = or IN with restrictions
        groupedFilters.get(PartitionKey).get.forall {
          case sources.EqualTo(_, _) => true
          case sources.In(colName, _) if cassandraRelation.tableDef.partitionKey.last.columnName.equals(colName) => true
          case _ => false
        }
      } else {
        true
      }
    }

    if (groupedFilters.contains(Unknown) || groupedFilters.contains(NonIndexed)) {
      false
    } else {
      checksPartitionKeyFilters && checksClusteringKeyFilters && checksSecondaryIndexesFilters
    }

  }

  private[this] def columnNameFromFilter(sourceFilter: SourceFilter): Option[ColumnName] = sourceFilter match {
    case sources.EqualTo(attribute, _) => Some(attribute)
    case sources.In(attribute, _) => Some(attribute)
    case sources.LessThan(attribute, _) => Some(attribute)
    case sources.GreaterThan(attribute, _) => Some(attribute)
    case sources.LessThanOrEqual(attribute, _) => Some(attribute)
    case sources.GreaterThanOrEqual(attribute, _) => Some(attribute)
    case _ => None
  }

  private[this] def columnRole(columnName: String): CassandraColumnRole = {
    val columnDef = cassandraRelation.tableDef.columnByName(columnName)
    if (columnDef.isPartitionKeyColumn) {
      PartitionKey
    } else if (columnDef.isClusteringColumn) {
      ClusteringKey
    } else if (columnDef.isIndexedColumn) {
      Indexed
    } else {
      NonIndexed
    }
  }

  private[this] def sparkResultFromCassandra(requiredColumns: Array[ColumnName], resultSet: ResultSet): Array[Row] = {
    // TODO efficiency, createWithSchema?
    import scala.collection.JavaConversions._
    val sparkRowList = resultSet.all().map { row =>
      val data = new Array[Object](requiredColumns.length)
      for (i <- requiredColumns.indices) {

        data(i) = GettableData.get(row, i)(ProtocolVersion.V3)
        data(i) match {
          case date: Date => data.update(i, new Timestamp(date.getTime))
          case str: String => data.update(i, UTF8String(str))
          case set: Set[_] => data.update(i, set.toSeq)
          case _ =>
        }
      }
      new CassandraSQLRow(requiredColumns, data)
    }
    sparkRowList.toArray
  }

}


