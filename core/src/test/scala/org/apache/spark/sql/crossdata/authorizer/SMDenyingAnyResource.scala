package org.apache.spark.sql.crossdata.authorizer

import com.stratio.crossdata.security.{Action, AuditEvent, CrossdataSecurityManager, Resource}

class SMDenyingAnyResource extends CrossdataSecurityManager{

  def start() : Unit = ()

  def stop(): Unit = ()

  def authorize(userId: String, resource: Resource, action: Action): Boolean = false

  def audit(auditEvent: AuditEvent): Unit = ()
}