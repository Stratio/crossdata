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
package org.apache.spark.sql.crossdata.execution

import com.stratio.crossdata.security._
import org.apache.log4j.Logger
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.{TableIdentifier, plans}
import org.apache.spark.sql.catalyst.plans.logical.{InsertIntoTable, LogicalPlan}
import org.apache.spark.sql.crossdata.catalyst.execution.{AddApp, AddJar, CreateExternalTable, CreateGlobalIndex, CreateTempView, CreateView, DropAllTables, DropExternalTable, DropTable, DropView, ExecuteApp, ImportTablesUsingWithOptions, InsertIntoTable => XDInsertIntoTable}
import org.apache.spark.sql.crossdata.catalyst.streaming._
import org.apache.spark.sql.crossdata.{XDContext, XDSQLConf}
import org.apache.spark.sql.execution._
import org.apache.spark.sql.execution.datasources.{CreateTableUsing, CreateTableUsingAsSelect, RefreshTable, DescribeCommand => LogicalDescribeCommand}


/**
  * The primary workflow for executing relational queries using Spark.  Designed to allow easy
  * access to the intermediate phases of query execution for developers.
  *
  * While this is not a public class, we should avoid changing the function names for the sake of
  * changing them, because a lot of developers use the feature for debugging.
  */
class XDQueryExecution(sqlContext: SQLContext, parsedPlan: LogicalPlan, catalogIdentifier: String) extends QueryExecution(sqlContext, parsedPlan){

  lazy val logger = Logger.getLogger(classOf[XDQueryExecution])

  lazy val authorized: LogicalPlan = {
    assertAnalyzed()
    val xdContext = sqlContext.asInstanceOf[XDContext]

    xdContext.securityManager.foreach { securityManager =>
      val userId = xdContext.conf.getConfString(XDSQLConf.UserIdPropertyKey)
      if (resourcesAndOperations.isEmpty) {
        logger.debug(s"LogicalPlan ${parsedPlan.treeString} does not access to any resource")
      }
      val isAuthorized = resourcesAndOperations.forall { case (resource, action) =>
        val isAuth = securityManager.authorize(userId, resource, action) // TODO .. vs sql(..., user)
        if (!isAuth) {
          logger.warn(s"Authorization rejected for user $userId: resource=$resource action=$action")
        }
        isAuth
      }
      if (!isAuthorized) {
        throw new RuntimeException("Operation not authorized") // TODO specify the resource/action?
      }
    }

    parsedPlan
  }

  override lazy val analyzed: LogicalPlan = sqlContext.analyzer.execute(authorized)


  lazy val resourcesAndOperations: Seq[(Resource, Action)] = {

    val crossdataInstances: Seq[String] = Seq(sys.env.getOrElse(Resource.CrossdataClusterNameEnvVar, "unknown")) // TODO get crossdataInstances
    val catalogName = catalogIdentifier // TODO get catalogName

    def tableResource(tableIdentifier: TableIdentifier): String = strTableResource(tableIdentifier.unquotedString)
    lazy val allTableResource: String = strTableResource(Resource.AllResourceName)
    def strTableResource(tableName: String): String = // TODO remove Spark 2.0 (required for Uncache plans)
      Seq(catalogName, tableName) mkString "."


    implicit def tupleToSeq( tuple: (Resource, Action)): Seq[(Resource, Action)] = Seq(tuple)

    // TODO filter temporaryCatalogs => add new API to catalog
    def createPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {

      case CreateTableUsing(tableIdent, _, provider, isTemporary, _, _, _ ) if !isTemporary =>
        (Resource(crossdataInstances, CatalogResource, catalogName), Write)

      case CreateView(viewIdentifier, selectPlan, _) =>
        selectPlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
        } :+ (Resource(crossdataInstances, CatalogResource, catalogName), Write)

      case ImportTablesUsingWithOptions(datasource, _) =>
        (Resource(crossdataInstances, CatalogResource, catalogName), Write)

      case _: CreateExternalTable =>
        (Resource(crossdataInstances, CatalogResource, catalogName), Write)

      case CreateTableUsingAsSelect(tableIdent, _, isTemporary, _, _, _, selectPlan) if !isTemporary =>
        selectPlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
        } :+ (Resource(crossdataInstances, CatalogResource, catalogName), Write)

      case CreateTableUsingAsSelect(tableIdent, _, isTemporary, _, _, _, selectPlan) if isTemporary =>
        selectPlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
        }

      case CreateTempView(viewIdentifier, selectPlan, _) =>
        selectPlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
        }



    }

    def insertPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {

      case XDInsertIntoTable(tableIdentifier, _, _) =>
        (Resource( crossdataInstances, TableResource, tableResource(tableIdentifier)), Write)

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

    def dropPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {

      case DropTable(tableIdentifier) =>
        (Resource(crossdataInstances, CatalogResource, catalogName), Write) :+
        (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Drop)

      case DropView(viewIdentifier) =>
        (Resource(crossdataInstances, CatalogResource, catalogName), Write) :+
          (Resource(crossdataInstances, TableResource, tableResource(viewIdentifier)), Drop)

      case DropExternalTable(tableIdentifier) =>
        (Resource(crossdataInstances, CatalogResource, catalogName), Write) :+
          (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Drop)

      case DropAllTables =>
        (Resource(crossdataInstances, CatalogResource, catalogName), Write) :+
          (Resource(crossdataInstances, TableResource, allTableResource), Drop)

    }

    def streamingPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {
      case ShowAllEphemeralStatuses => throw new RuntimeException(s"$parsedPlan is not authorized")
      case DropAllEphemeralTables => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: CreateEphemeralTable => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: AddEphemeralQuery => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: DropEphemeralTable => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: DropAllEphemeralQueries => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: DescribeEphemeralTable => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: ShowEphemeralQueries => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: DropEphemeralQuery => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: ShowEphemeralStatus => throw new RuntimeException(s"$parsedPlan is not authorized")
      case ShowEphemeralTables => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: StopProcess => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: StartProcess => throw new RuntimeException(s"$parsedPlan is not authorized")
    }

    def insecurePlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {
      case _: CreateGlobalIndex => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: AddApp => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: ExecuteApp => throw new RuntimeException(s"$parsedPlan is not authorized")
      case _: AddJar => throw new RuntimeException(s"$parsedPlan is not authorized")
    }

    def metadataPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {

      case ShowTablesCommand(databaseOpt) =>
        (Resource(crossdataInstances, CatalogResource, catalogName), Describe)

      case LogicalDescribeCommand(table, isExtended) => table.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Describe)
      }

      case plans.logical.DescribeFunction(functionName, _) =>
        Seq.empty

      case showFunctions: plans.logical.ShowFunctions =>
        Seq.empty
    }

    def configCommandPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {
      case SetCommand(Some((key, value))) if key == XDSQLConf.UserIdPropertyKey =>
        throw new RuntimeException(s"$parsedPlan is not authorized")
      case SetCommand(Some((key, value))) =>
        logger.info(s"Set command received: $key=$value)") // TODO log
        Seq.empty
    }

    def cachePlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {

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

    def queryPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {

      case queryWithUnresolvedAttributes => // TODO test collect using union and join
        queryWithUnresolvedAttributes.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource(crossdataInstances, TableResource, tableResource(tableIdentifier)), Read)
        }
    }

    def extResAndOps =
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

    extResAndOps(parsedPlan)
  }

}
