package org.apache.spark.sql.crossdata.catalyst.optimizer

import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.catalyst.analysis.UnresolvedAttribute
import org.apache.spark.sql.catalyst.expressions.{AttributeReference, Expression, In, Literal}
import org.apache.spark.sql.catalyst.optimizer.{DefaultOptimizer, Optimizer}
import org.apache.spark.sql.catalyst.plans.logical
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataIndex
import org.apache.spark.sql.crossdata.catalyst.ExtendedUnresolvedRelation
import org.apache.spark.sql.crossdata.catalyst.execution.DDLUtils
import org.apache.spark.sql.crossdata.catalyst.globalindex.IndexUtils
import org.apache.spark.sql.crossdata.{XDContext, XDDataFrame}
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources.BaseRelation
import org.apache.spark.sql.types.{DataType, StructType}

import scala.annotation.tailrec

case class XDOptimizer(xdContext: XDContext, conf: CatalystConf) extends Optimizer(conf) {

  val defaultOptimizer = DefaultOptimizer(conf)

  def convertStrategy(strategy: defaultOptimizer.Strategy): Strategy = strategy.maxIterations match {
    case 1 => Once
    case n => FixedPoint(n)
  }

  def convertBatches(batch: defaultOptimizer.Batch): Batch =
    Batch(batch.name, convertStrategy(batch.strategy), batch.rules: _*)

  override val batches: List[Batch] =
    (defaultOptimizer.batches map (convertBatches(_))) ++ Seq(Batch("Global indexes phase", Once, CheckGlobalIndexInFilters(xdContext)))
}



case class CheckGlobalIndexInFilters(xdContext: XDContext) extends Rule[LogicalPlan] {

  def apply(plan: LogicalPlan): LogicalPlan = plan transform {

    case FilterWithIndexLogicalPlan(filters, projects, ExtendedUnresolvedRelation(tableIdentifier, relation)) =>

      val crossdataIndex = {
        xdContext.catalog.indexMetadataByTableIdentifier(tableIdentifier)
      } getOrElse {
        sys.error("Unexpected error. Can't find index for enhance query with indexes")
      }

      //Change the filters that has indexed rows, with a Filter IN with ES results or LocalRelation if we don't have results
      val newFilters: Seq[LogicalPlan] = filters map { filter =>
        if (IndexUtils.areAllAttributeIndexedInExpr(filter.condition, crossdataIndex.indexedCols)) {
          val indexLogicalPlan = buildIndexRequestLogicalPlan(filter.condition, crossdataIndex)
          val indexedRows = XDDataFrame(xdContext, indexLogicalPlan).collect() //TODO: Warning memory issues
          if (indexedRows.nonEmpty) {

            //Convert to query with filter IN
            val lr = relation.collectFirst { case lr: LogicalRelation => lr }.get
            val pkSchema = DDLUtils.extractSchema(Seq(crossdataIndex.pk), lr.schema)
            val pkAttribute = schemaToAttribute(pkSchema).head
            analyzeAndOptimize(
              logical.Filter(In(pkAttribute, resultPksToLiterals(indexedRows, pkSchema.fields.head.dataType)), relation)
            )

          } else {
            LocalRelation(filter.output)
          }
        } else {
          filter
        }
      }

      //If LocalRelation appear there are no results
      val noResults: Option[LocalRelation] = newFilters collectFirst {
        case localRelation: LocalRelation => localRelation
      }

      noResults getOrElse {
        //If projects exists, just remain the first in the tree + Filters + Relation
        val combined: LogicalPlan = combineFiltersAndRelation(newFilters, relation)
        if (projects.nonEmpty) {
          analyzeAndOptimize(projects.head.withNewChildren(Seq(combined)))
        } else {
          analyzeAndOptimize(combined)
        }
      }

  }


  private def analyze(plan: LogicalPlan): LogicalPlan = {
    val analyzed = xdContext.analyzer.execute(plan)
    xdContext.analyzer.checkAnalysis(analyzed)
    analyzed
  }

  private def analyzeAndOptimize(plan: LogicalPlan): LogicalPlan = {
    xdContext.optimizer.execute(analyze(plan))
  }

  private def schemaToAttribute(schema: StructType): Seq[UnresolvedAttribute] =
    schema.fields map {field => UnresolvedAttribute(field.name)}

  private def resultPksToLiterals(rows: Array[Row], dataType:DataType): Seq[Literal] =
    rows map { row =>
      val valTransformed = row.get(0)
      Literal.create(valTransformed, dataType)
    } //TODO compound PK

  private def buildIndexRequestLogicalPlan(condition: Expression, index: CrossdataIndex): LogicalPlan = {

    val logicalRelation = xdContext.catalog.lookupRelation(index.indexIdentifier.asTableIdentifierNormalized.toTableIdentifier) match {
      case Subquery(_, logicalRelation @ LogicalRelation(_: BaseRelation, _)) => logicalRelation
    }

    //We need to retrieve all the retrieve cols for use the filter
    val pkAndColsIndexed: Seq[UnresolvedAttribute] = schemaToAttribute(DDLUtils.extractSchema(Seq(index.pk)++index.indexedCols, logicalRelation.schema))

    //Old attributes reference have to be updated
    val convertedCondition = condition transform {
      case UnresolvedAttribute(name) => (pkAndColsIndexed filter (_.name == name)).head
      case AttributeReference(name, _, _, _) => (pkAndColsIndexed filter (_.name == name)).head
    }

    Filter(convertedCondition, Project(pkAndColsIndexed, logicalRelation))
  }

  def combineFiltersAndRelation(filters: Seq[LogicalPlan], relation: LogicalPlan): LogicalPlan =
    filters.foldRight(relation){(filter, accum) => filter.withNewChildren(Seq(accum))}

}


// TODO comment?
object FilterWithIndexLogicalPlan {
  type ReturnType = (Seq[Filter], Seq[Project], ExtendedUnresolvedRelation)

  def unapply(plan: LogicalPlan): Option[ReturnType] = plan match {
    case f @ logical.Filter(condition, child: LogicalPlan) =>
      recoverFilterAndProjects(Seq(f), Seq.empty, child)

    case _ => None
  }

  @tailrec
  def recoverFilterAndProjects(
                                filters: Seq[logical.Filter],
                                projects: Seq[logical.Project],
                                current: LogicalPlan
                              ): Option[ReturnType] = current match {

    case f@logical.Filter(_, child: LogicalPlan) =>
      recoverFilterAndProjects(filters :+ f, projects, child)

    case p@logical.Project(_, child: LogicalPlan) =>
      recoverFilterAndProjects(filters, projects :+ p, child)

    case u: ExtendedUnresolvedRelation =>
      Some(filters, projects, u)

    case _ => None
  }
}