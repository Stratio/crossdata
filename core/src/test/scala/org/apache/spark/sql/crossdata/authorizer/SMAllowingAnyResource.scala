package org.apache.spark.sql.crossdata.authorizer

import com.stratio.crossdata.security.{Action, AuditEvent, CrossdataSecurityManager, Resource}

class SMAllowingAnyResource extends CrossdataSecurityManager{

  def start() : Unit = ()

  def stop(): Unit = ()

  def authorize(userId: String, resource: Resource, action: Action): Boolean = true

  def audit(auditEvent: AuditEvent): Unit = ()
}