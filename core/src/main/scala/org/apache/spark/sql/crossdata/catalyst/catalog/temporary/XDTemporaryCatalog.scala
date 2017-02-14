package org.apache.spark.sql.crossdata.catalyst.catalog.temporary

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

import scala.collection.mutable.Map

/* TODO: Explore whether or not, Map operation sets fully describes
    the temporary catalog implementation.
  */

/**
  * Temporary catalog interface
  */
trait XDTemporaryCatalog extends Map[String, LogicalPlan]
