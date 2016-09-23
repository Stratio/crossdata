package org.apache.spark.sql.crossdata.security.api

/**
  * WARNING: Update crossdata plugin when modifying the actions
  */
sealed trait Action

case object Read extends Action

case object Write extends Action

case object Execute extends Action

case object Create extends Action

case object Drop extends Action

case object Describe extends Action

case object Other extends Action

case object Access extends Action // TODO access???

case object View extends Action

case object Register extends Action

case object Unregister extends Action