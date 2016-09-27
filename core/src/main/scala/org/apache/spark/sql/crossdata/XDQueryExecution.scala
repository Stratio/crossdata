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
package org.apache.spark.sql.crossdata

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.plans
import org.apache.spark.sql.catalyst.plans.logical.{DescribeFunction, InsertIntoTable, LogicalPlan}
import org.apache.spark.sql.crossdata.catalyst.execution.{AddApp, AddJar, CreateExternalTable, CreateGlobalIndex, CreateTempView, CreateView, DropAllTables, DropExternalTable, DropTable, DropView, ExecuteApp, ImportTablesUsingWithOptions, InsertIntoTable => XDInsertIntoTable}
import org.apache.spark.sql.crossdata.catalyst.streaming._
import org.apache.spark.sql.crossdata.security.auth.ResourceType._
import org.apache.spark.sql.execution.datasources.{CreateTableUsing, CreateTableUsingAsSelect, CreateTempTableUsing, RefreshTable}
import org.apache.spark.sql.execution.{datasources, _}
import org.apache.spark.sql.crossdata.security.api._

/**
  * The primary workflow for executing relational queries using Spark.  Designed to allow easy
  * access to the intermediate phases of query execution for developers.
  *
  * While this is not a public class, we should avoid changing the function names for the sake of
  * changing them, because a lot of developers use the feature for debugging.
  */
class XDQueryExecution(sqlContext: SQLContext, parsedPlan: LogicalPlan) extends QueryExecution(sqlContext, parsedPlan){

  lazy val authorized: LogicalPlan = {
    assertAnalyzed()
    val xdContext = sqlContext.asInstanceOf[XDContext]

    xdContext.securityManager.foreach { securityManager =>

      val userId = xdContext.conf.getConfString(XDSQLConf.UserIdPropertyKey)

      val isAuthorized = resourcesAndOperations.forall { case (resource, action) =>
        securityManager.authorize(userId, resource, action, AuditAddresses("srcIp", "dstIp"), hierarchy = false) // TODO web do it public vs sql(..., user)
      }

      if (!isAuthorized) throw new RuntimeException("Operation not ") // TODO improve message => specify resource
      // TODO ...
      // TODO warning and log if the seq is empty

      //TODO previous audit (vs authorize logs??)
      resourcesAndOperations.foreach { case (resource, action) =>
        securityManager.audit(
          AuditEvent(
            userId,
            resource,
            action,
            FailAR, //TODO failAR??
            AuditAddresses("srcIp", "dstIp"),
            impersonation = None)) // TODO date public?? //TODO user vs strusr // instead of fail (init) //srcIp and srcDst => sqlSec(sql, user, ips..)
      }
    }

    parsedPlan
  }

  override lazy val analyzed: LogicalPlan = sqlContext.analyzer.execute(authorized)

  lazy val resourcesAndOperations: Seq[(Resource, Action)] = {

    implicit def tupleToSeq( tuple: (Resource, Action)): Seq[(Resource, Action)] = Seq(tuple)

    //(Resource.wildCardAll, View) // TODO tablesAll
    def createPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {

      case CreateTableUsing(tableIdent, _, provider, isTemporary, _, _, _ ) if isTemporary => (Resource( Seq("instances"), TableResource.toString, "none"), Register)//TODO createTable => name = None??

      case CreateTableUsing(tableIdent, _, provider, isTemporary, _, _, _ ) if !isTemporary => (Resource( Seq("instances"), TableResource.toString, "none"), Create)//TODO createTable => name = None??

      case _: CreateExternalTable => (Resource(Seq("instances"), TableResource.toString, "none"), Create)//TODO createTable => name = None??

      case CreateTableUsingAsSelect(tableIdent, _, isTemporary, _, _, _, selectPlan) if isTemporary =>
        selectPlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Read)
        } :+ (Resource( Seq("instances"), TableResource.toString, "none"), Register) // TODO table and tempTable resources?? TODO createTable => name = None??

      case CreateTableUsingAsSelect(tableIdent, _, isTemporary, _, _, _, selectPlan) if !isTemporary =>
        selectPlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Read)
        } :+ (Resource( Seq("instances"), TableResource.toString, "none"), Create) // TODO table and tempTable resources?? TODO createTable => name = None??

      case CreateView(viewIdentifier, queryPlan, _) =>
        queryPlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Read)
        } :+ (Resource( Seq("instances"), TableResource.toString, "none"), Create)//TODO

      case CreateTempView(viewIdentifier, queryPlan, _) =>
        queryPlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Read)
        } :+ (Resource( Seq("instances"), TableResource.toString, "none"), Register)//TODO

      case ImportTablesUsingWithOptions(datasource, _) =>
        Seq((Resource( Seq("instances"), TableResource.toString, "All"), Read) , (Resource( Seq("instances"), TableResource.toString, "None"), Create)) // TODO all

    }

    def insertPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {
      case XDInsertIntoTable(tableIdentifier, _, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Write)
      case InsertIntoTable(writePlan, _, readPlan, _, _) =>
        val writeResources = writePlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Write)
        }
        val readResources =
          readPlan.collect {
            case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Read)
          }
        writeResources ++ readResources
    }

    def dropPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {
      case DropView(viewIdentifier) => (Resource( Seq("instances"), TableResource.toString, viewIdentifier.unquotedString), Unregister)
      case DropAllTables =>  (Resource( Seq("instances"), TableResource.toString, "all"), Drop) // TODO Drop
      case DropTable(tableIdentifier) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Drop) // TODO Drop
      case DropExternalTable(tableIdentifier) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Drop)

    }

    def streamingPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {
      case ShowAllEphemeralStatuses => throw new RuntimeException(s"$parsedPlan is not authorized") // TODO Spark 2.0
      case DropAllEphemeralTables => throw new RuntimeException(s"$parsedPlan is not authorized") // TODO Spark 2.0
      case _: CreateEphemeralTable => throw new RuntimeException(s"$parsedPlan is not authorized") // TODO Spark 2.0
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

    def metadataPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {
      case ShowTablesCommand(databaseOpt) => (Resource.wildCardAll, View) // TODO tablesAll // database??
      case datasources.DescribeCommand(table, isExtended) => table.collect {
        case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Read)
      }
      case plans.logical.DescribeFunction(functionName, _) => Seq.empty // TODO audit
      case showFunctions: plans.logical.ShowFunctions => Seq.empty // TODO audit
    }

    def setConfigPlanToResourcesAndOps: PartialFunction[LogicalPlan,Seq[(Resource, Action)]] = {
      case setCommand: SetCommand => Seq.empty // TODO audit instead of authorize
    }

    def cachePlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {
      case _: RefreshTable => Seq.empty // TODO
      case ClearCacheCommand => Seq.empty // TODO
      case CacheTableCommand(tableName, Some(toCachePlan), _) =>
        toCachePlan.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Read)
        } :+ (Resource( Seq("instances"), TableResource.toString, "none"), Register) // TODO table and tempTable resources?? TODO createTable => name = None??
      case UncacheTableCommand(tableName) => (Resource( Seq("instances"), TableResource.toString, "none"), Unregister) // TODO

    }

    def queryPlanToResourcesAndOps: PartialFunction[LogicalPlan, Seq[(Resource, Action)]] = {
      case queryWithUnresolvedAttributes =>
        // TODO log other?? whitelist??
        // TODO test collect using union and join
        queryWithUnresolvedAttributes.collect {
          case UnresolvedRelation(tableIdentifier, _) => (Resource( Seq("instances"), TableResource.toString, tableIdentifier.unquotedString), Read)
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
        setConfigPlanToResourcesAndOps orElse
        queryPlanToResourcesAndOps
    // Plans should not match  InsertIntoHadoopFsRelation InsertIntoDatasource CreateTempTableUsing(and Select) Explain

    extResAndOps(parsedPlan)
  }

}
