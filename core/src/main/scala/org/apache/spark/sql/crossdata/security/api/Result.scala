package org.apache.spark.sql.crossdata.security.api

sealed trait AuditResult

case object SuccessAR extends AuditResult

case object FailAR extends AuditResult
