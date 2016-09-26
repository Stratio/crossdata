package org.apache.spark.sql.crossdata.security.api

trait CrossdataSecurityManager {

  def start() : Unit
  def stop(): Unit

  def authorize(userId: String, resource: Resource, action: Action, auditAddresses: AuditAddresses, hierarchy: Boolean): Boolean

  def audit(auditEvent: AuditEvent): Unit

}

class DummyCrossdataSecurityManager extends CrossdataSecurityManager{

  def start() : Unit = ()

  def stop(): Unit = ()

  def authorize(userId: String, resource: Resource, action: Action, auditAddresses: AuditAddresses, hierarchy: Boolean) = true

  def audit(auditEvent: AuditEvent): Unit = ()
}
