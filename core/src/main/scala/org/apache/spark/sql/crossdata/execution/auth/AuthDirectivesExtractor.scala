/*
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
package org.apache.spark.sql.crossdata.execution.auth

import com.stratio.crossdata.security.{Action, _}
import org.apache.log4j.Logger
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.plans.logical.{InsertIntoTable, LogicalPlan}
import org.apache.spark.sql.catalyst.{TableIdentifier, plans}
import org.apache.spark.sql.crossdata.XDSQLConf
import org.apache.spark.sql.crossdata.catalyst.execution.{AddApp, AddJar, CreateExternalTable, CreateGlobalIndex, CreateTempView, CreateView, DropAllTables, DropExternalTable, DropTable, DropView, ExecuteApp, ImportTablesUsingWithOptions, InsertIntoTable => XDInsertIntoTable}
import org.apache.spark.sql.crossdata.catalyst.streaming._
import org.apache.spark.sql.crossdata.execution.XDQueryExecution
import org.apache.spark.sql.execution._
import org.apache.spark.sql.execution.datasources.{CreateTableUsing, CreateTableUsingAsSelect, RefreshTable, DescribeCommand => LogicalDescribeCommand}


class AuthDirectivesExtractor(crossdataInstances: Seq[String], catalogIdentifier: String) {

  private lazy val logger = Logger.getLogger(classOf[XDQueryExecution])

  def extractResourcesAndActions(parsedPlan: LogicalPlan): Seq[(Resource, Action)] = extResAndOps(parsedPlan)

  private[auth] def extResAndOps =
    createPlanToResourcesAndOps orElse
      insertPlanToResourcesAndOps orElse
      dropPlanToResourcesAndOps orElse
      streamingPlanToResourcesAndOps orElse
      insecurePlanToResourcesAndOps orElse
      metadataPlanToResourcesAndOps orElse
      cachePlanToResourcesAndOps orElse
      configCommandPlanToResourcesAndOps orElse
      queryPlanToResourcesAndOps

  // TODO Plans should not match  InsertIntoHadoopFsRelation InsertIntoDatasource Explain CreateTableUsing if isTemporary


  implicit def tupleToSeq(tuple: (Resource, Action)): Seq[(Resource, Action)] = Seq(tuple)

  // TODO filter temporaryCatalogs => add new API to catalog
  private[auth] def createPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {


    case CreateTableUsing(tableIdent, _, provider, isTemporary, _, _, _) if !isTemporary =>
      (catalogResource, Write)

    case CreateView(viewIdentifier, selectPlan, _) =>
      selectPlan.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
      } :+ (catalogResource, Write)

    case ImportTablesUsingWithOptions(datasource, _) =>
      (catalogResource, Write)

    case _: CreateExternalTable =>
      (catalogResource, Write)

    case CreateTableUsingAsSelect(tableIdent, _, isTemporary, _, _, _, selectPlan) if !isTemporary =>
      selectPlan.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
      } :+ (catalogResource, Write)

    case CreateTableUsingAsSelect(tableIdent, _, isTemporary, _, _, _, selectPlan) if isTemporary =>
      selectPlan.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
      }

    case CreateTempView(viewIdentifier, selectPlan, _) =>
      selectPlan.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
      }


  }

  private[auth] def insertPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {

    case XDInsertIntoTable(tableIdentifier, _, _) =>
      (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Write)

    case InsertIntoTable(writePlan, _, selectPlan, _, _) => {
      writePlan.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Write)
      }
    } ++ {
      selectPlan.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
      }
    }
  }

  private[auth] def dropPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {

    case DropTable(tableIdentifier) =>
      (catalogResource, Write) :+
        (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Drop)

    case DropView(viewIdentifier) =>
      (catalogResource, Write) :+
        (Resource(crossdataInstances, TableResource, tableResource(viewIdentifier)), Drop)

    case DropExternalTable(tableIdentifier) =>
      (catalogResource, Write) :+
        (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Drop)

    case DropAllTables =>
      (catalogResource, Write) :+
        (Resource(crossdataInstances, TableResource, allTableResource), Drop)

  }

  private[auth] def streamingPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {
    case lPlan@ShowAllEphemeralStatuses => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan@DropAllEphemeralTables => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: CreateEphemeralTable => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: AddEphemeralQuery => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: DropEphemeralTable => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: DropAllEphemeralQueries => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: DescribeEphemeralTable => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: ShowEphemeralQueries => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: DropEphemeralQuery => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: ShowEphemeralStatus => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan@ShowEphemeralTables => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: StopProcess => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: StartProcess => throw new RuntimeException(s"$lPlan is not authorized")
  }

  private[auth] def insecurePlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {
    case lPlan: CreateGlobalIndex => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: AddApp => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: ExecuteApp => throw new RuntimeException(s"$lPlan is not authorized")
    case lPlan: AddJar => throw new RuntimeException(s"$lPlan is not authorized")
  }

  private[auth] def metadataPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {

    case ShowTablesCommand(databaseOpt) =>
      (catalogResource, Describe)

    case LogicalDescribeCommand(table, isExtended) => table.collect {
      case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Describe)
    }

    case plans.logical.DescribeFunction(functionName, _) =>
      Seq.empty

    case showFunctions: plans.logical.ShowFunctions =>
      Seq.empty
  }

  private[auth] def configCommandPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {
    case lPlan@SetCommand(Some((key, value))) if key == XDSQLConf.UserIdPropertyKey =>
      throw new RuntimeException(s"$lPlan is not authorized")
    case SetCommand(Some((key, value))) =>
      logger.info(s"Set command received: $key=$value)") // TODO log
      Seq.empty
  }

  private[auth] def cachePlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {

    case CacheTableCommand(tableName, Some(toCachePlan), _) =>
      toCachePlan.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Cache)
      }

    case UncacheTableCommand(tableIdentifier) =>
      (Resource(crossdataInstances, TableResource, strTableResource(tableIdentifier)), Cache)

    case ClearCacheCommand =>
      (Resource(crossdataInstances, TableResource, allTableResource), Cache)

    case RefreshTable(tableIdentifier) =>
      (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Cache)
  }

  private[auth] def queryPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {

    case queryWithUnresolvedAttributes => // TODO test collect using union and join
      queryWithUnresolvedAttributes.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
      }
  }

  private[auth] lazy val allTableResource: String = strTableResource(Resource.AllResourceName)

  private[auth] def tableResource(tableIdentifier: TableIdentifier): String = strTableResource(tableIdentifier.unquotedString)

  private[auth] def catalogResource = Resource(crossdataInstances, CatalogResource, catalogIdentifier)

  private[auth] def strTableResource(tableName: String): String = // TODO remove Spark 2.0 (required for Uncache plans)
    Seq(catalogIdentifier, tableName) mkString "."

}
