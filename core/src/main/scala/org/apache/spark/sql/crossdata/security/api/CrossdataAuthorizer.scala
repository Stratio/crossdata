package org.apache.spark.sql.crossdata.security.api

trait CrossdataAuthorizer {

  def start() : Unit
  def stop(): Unit

  def auth(userId: String, resource: Resource, action: Action, auditAddresses: AuditAddresses, hierarchy: Boolean): Boolean

  def audit(auditEvent: AuditEvent): Unit

}

class DummyCrossdataAuthorizer extends CrossdataAuthorizer{

  def start() : Unit = ()

  def stop(): Unit = ()

  def auth(userId: String, resource: Resource, action: Action, auditAddresses: AuditAddresses, hierarchy: Boolean) = true

  def audit(auditEvent: AuditEvent): Unit = ()
}
