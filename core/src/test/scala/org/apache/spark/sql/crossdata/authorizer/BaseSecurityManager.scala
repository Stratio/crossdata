package org.apache.spark.sql.crossdata.authorizer

import com.stratio.crossdata.security.{Action, AuditEvent, CrossdataSecurityManager, Resource}

object SecurityManagerTestConstants {
  val XDUser = "mstr"
}

trait BaseSecurityManagerTest extends CrossdataSecurityManager {
  override def start(): Unit = ()

  override def stop(): Unit = ()

  override def audit(auditEvent: AuditEvent): Unit = ()

  def authorize(userId: String, resource: Resource, action: Action): Boolean =
    userId == SecurityManagerTestConstants.XDUser
}
