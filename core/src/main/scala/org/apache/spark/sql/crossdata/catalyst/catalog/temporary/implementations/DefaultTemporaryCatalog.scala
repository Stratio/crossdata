package org.apache.spark.sql.crossdata.catalyst.catalog.temporary.implementations

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalyst.catalog.temporary.XDTemporaryCatalog

import scala.collection.mutable.HashMap

/**
  * Non-distributed temporary catalog, right only for single Crossdata service deployments.
  */
object DefaultTemporaryCatalog extends HashMap[String, LogicalPlan] with XDTemporaryCatalog
