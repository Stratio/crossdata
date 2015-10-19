/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.connector.cassandra



import com.datastax.driver.core.{ProtocolVersion, ResultSet}
import org.apache.spark.Logging
import org.apache.spark.sql.cassandra.{CassandraSQLRow, CassandraXDSourceRelation}
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.planning.crossdata.ExtendedPhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.crossdata.{NativeUDFAttribute, NativeUDF}
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.{Row, sources}
import org.apache.spark.sql.sources.{CatalystToCrossdataAdapter, Filter => SourceFilter}

import com.stratio.crossdata.connector.cassandra.CassandraAttributeRole._

object CassandraQueryProcessor {

  val DefaultLimit = 10000
  type ColumnName = String

  def apply(cassandraRelation: CassandraXDSourceRelation, logicalPlan: LogicalPlan) = new CassandraQueryProcessor(cassandraRelation, logicalPlan)

  def buildNativeQuery(
                        tableQN: String,
                        requiredColumns: Array[String],
                        filters: Array[SourceFilter],
                        limit: Int,
                        udfs: Map[String, NativeUDF] = Map.empty): String = {

    def quoteString(in: Any): String = in match {
      case s: String => s"'$s'"
      case a: Attribute => expandAttribute(a.toString)
      case other => other.toString
    }

    def expandAttribute(att: String): String = {
      udfs get(att) map { udf =>
        val actualParams = udf.children.collect { //TODO: Add type checker (maybe not here)
          case at: NativeUDFAttribute => expandAttribute(at.toString)
          case at: AttributeReference => at.name
          case lit @ Literal(_, DataTypes.StringType) => quoteString(lit.toString)
          case lit: Literal => lit.toString
        } mkString ","
        s"${udf.name}($actualParams)"
      } getOrElse(att.split("#").head.trim) //TODO: Try a more sophisticated way...
    }

    def filterToCQL(filter: SourceFilter): String = filter match {

      case sources.EqualTo(attribute, value) => s"${expandAttribute(attribute)} = ${quoteString(value)}"
      case sources.In(attribute, values) => s"${expandAttribute(attribute)} IN ${values.map(quoteString).mkString("(", ",", ")")}"
      case sources.LessThan(attribute, value) => s"${expandAttribute(attribute)} < ${quoteString(value)}"
      case sources.GreaterThan(attribute, value) => s"${expandAttribute(attribute)} > ${quoteString(value)}"
      case sources.LessThanOrEqual(attribute, value) => s"${expandAttribute(attribute)} <= ${quoteString(value)}"
      case sources.GreaterThanOrEqual(attribute, value) => s"${expandAttribute(attribute)} >= ${quoteString(value)}"
      case sources.And(leftFilter, rightFilter) => s"${filterToCQL(leftFilter)} AND ${filterToCQL(rightFilter)}"

    }

    val filter = if (filters.nonEmpty) filters.map(filterToCQL).mkString("WHERE ", " AND ", "") else ""
    val columns = requiredColumns.map(expandAttribute).mkString(", ")

    s"SELECT $columns FROM $tableQN $filter LIMIT $limit ALLOW FILTERING"
  }

}

// TODO logs, doc, tests
class CassandraQueryProcessor(cassandraRelation: CassandraXDSourceRelation, logicalPlan: LogicalPlan) extends Logging {

  import CassandraQueryProcessor._

  def execute(): Option[Array[Row]] = {
    def annotateRepeatedNames(names: Seq[String]): Seq[String] = {
      val indexedNames = names zipWithIndex
      val m = indexedNames.groupBy(_._1).values.flatMap(_.zipWithIndex.map(x => x._1._2 -> x._2 )).toMap
      indexedNames map { case (name, index) => val c = m(index); if(c>0) s"$name$c" else name }
    }

    try {
      validatedNativePlan.map { case (columnsRequired, filters, udfs: Map[Attribute, NativeUDF], limit) =>
        val cqlQuery = buildNativeQuery(
          cassandraRelation.tableDef.name,
          columnsRequired.map(_.toString),
          filters,
          limit.getOrElse(CassandraQueryProcessor.DefaultLimit),
          udfs map { case (k,v) => k.toString -> v}
        )
        val resultSet = cassandraRelation.connector.withSessionDo { session =>
          session.execute(cqlQuery)
        }
        sparkResultFromCassandra(annotateRepeatedNames(columnsRequired.map(_.name)).toArray, resultSet)
      }
    } catch {
      case exc: Exception => log.warn(s"Exception executing the native query $logicalPlan", exc.getMessage); None
    }

  }


  def validatedNativePlan: Option[(Array[Attribute], Array[SourceFilter], Map[Attribute, NativeUDF], Option[Int])] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num }

    def findProjectsFilters(lplan: LogicalPlan):
    (Array[Attribute], Array[SourceFilter], Map[Attribute, NativeUDF], Boolean) = {
      lplan match {
        case Limit(_, child) =>
          findProjectsFilters(child)
        case ExtendedPhysicalOperation(projectList, filterList, _) =>
          CatalystToCrossdataAdapter.getFilterProject(logicalPlan, projectList, filterList)
      }
    }

    val (projects, filters, udfs, filtersIgnored) = findProjectsFilters(logicalPlan)
    Option((projects, filters, udfs, limit)) filter {x => !filtersIgnored && checkNativeFilters (filters, udfs)}
  }


  private[this] def checkNativeFilters(filters: Array[SourceFilter], udfs: Map[Attribute, NativeUDF]):
  Boolean = {

    val udfNames = udfs.keys.map(_.toString).toSet

    val groupedFilters = filters.groupBy {
      case sources.EqualTo(attribute, _) => attributeRole(attribute, udfNames)
      case sources.In(attribute, _) => attributeRole(attribute, udfNames)
      case sources.LessThan(attribute, _) => attributeRole(attribute, udfNames)
      case sources.GreaterThan(attribute, _) => attributeRole(attribute, udfNames)
      case sources.LessThanOrEqual(attribute, _) => attributeRole(attribute, udfNames)
      case sources.GreaterThanOrEqual(attribute, _) => attributeRole(attribute, udfNames)
      case _ => Unknown
    }

    def checksClusteringKeyFilters: Boolean =
      !groupedFilters.contains(ClusteringKey) || {
        // if there is a CK filter then all CKs should be included. Accept any kind of filter
        val clusteringColsInFilter = groupedFilters.get(ClusteringKey).get.flatMap(columnNameFromFilter)
        cassandraRelation.tableDef.clusteringColumns.forall { column =>
          clusteringColsInFilter.contains(column.columnName)
        }
      }

    def checksSecondaryIndexesFilters: Boolean =
      !groupedFilters.contains(Indexed) || {
        //Secondary indexes => equals are allowed
        groupedFilters.get(Indexed).get.forall {
          case sources.EqualTo(_, _) => true
          case _ => false
        }
      }


    def checksPartitionKeyFilters: Boolean =
      !groupedFilters.contains(PartitionKey) || {
        val partitionColsInFilter = groupedFilters.get(PartitionKey).get.flatMap(columnNameFromFilter)

        // all PKs must be present
        cassandraRelation.tableDef.partitionKey.forall { column =>
          partitionColsInFilter.contains(column.columnName)
        }
        // filters condition must be = or IN with restrictions
        groupedFilters.get(PartitionKey).get.forall {
          case sources.EqualTo(_, _) => true
          case sources.In(colName, _) if cassandraRelation.tableDef.partitionKey.last.columnName.equals(colName) => true
          case _ => false
        }
      }

    { !groupedFilters.contains(Unknown) && !groupedFilters.contains(NonIndexed) &&
      checksPartitionKeyFilters && checksClusteringKeyFilters && checksSecondaryIndexesFilters }

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

  private[this] def attributeRole(columnName: String, udfs: Set[String]): CassandraAttributeRole =
    if(udfs contains columnName) Function
    else cassandraRelation.tableDef.columnByName(columnName) match {
      case x if(x.isPartitionKeyColumn) => PartitionKey
      case x if(x.isClusteringColumn) => ClusteringKey
      case x if(x.isIndexedColumn) => Indexed
      case _ => NonIndexed
  }

  private[this] def sparkResultFromCassandra(requiredColumns: Array[ColumnName], resultSet: ResultSet): Array[Row] = {
    import scala.collection.JavaConversions._
    resultSet.all().map(CassandraSQLRow.fromJavaDriverRow(_, requiredColumns)(ProtocolVersion.V3)).toArray
  }

}


