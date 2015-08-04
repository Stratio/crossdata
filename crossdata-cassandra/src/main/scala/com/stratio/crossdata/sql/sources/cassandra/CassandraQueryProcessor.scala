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
      // TODO other filters

    }

    val filter = if (filters.nonEmpty) filters.map(filterToCQL).mkString("WHERE ", " AND ", "") else ""

    val query = s"SELECT $columns FROM $tableQN $filter $orderBy LIMIT $limit"
    // TODO allow filtering

    query
  }

}

// TODO logs, doc, tests
case class CassandraQueryProcessor(cassandraRelation: CassandraXDSourceRelation, logicalPlan: LogicalPlan) {

  import com.stratio.crossdata.sql.sources.cassandra.CassandraQueryProcessor._

  private[this] def validateLogicalPlan(lp: LogicalPlan): Boolean = lp match {
    case ln: LeafNode => true // TODO leafNode == LogicalRelation(xdSourceRelation)
    case un: UnaryNode => un match {
      case Limit(_, _) | Project(_, _) | Filter(_, _) => validateLogicalPlan(un.child)
      case _ => false

    }
    case unsupportedLogicalPlan => false // TODO log
  }


  def getValidatedPlan: Option[(Array[ColumnName], Array[SourceFilter], Option[Int])] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num}
    if (validateLogicalPlan(logicalPlan)) {
      def findProjectsFilters(lplan: LogicalPlan): (Array[ColumnName], Array[SourceFilter]) = {
        lplan match {
          case Limit(_, child) => findProjectsFilters(child)
          case PhysicalOperation(projects, filters, _) => CatalystToCrossdataAdapter.getFilterProject(logicalPlan, projects, filters)
        }
      }
      val (projects, filters) = findProjectsFilters(logicalPlan)
      Some(projects, filters, limit)

    } else {
      None
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

  def execute(): Option[Array[Row]] = {
    getValidatedPlan.map { case (columnsRequired, filters, limit) =>
      val cqlQuery = buildNativeQuery(cassandraRelation.tableDef.name, columnsRequired, filters, limit.getOrElse(CassandraQueryProcessor.DefaultLimit))
      val resultSet = cassandraRelation.connector.withSessionDo { session =>
        session.execute(cqlQuery)
      }
      sparkResultFromCassandra(columnsRequired, resultSet)
    }
  }


}


