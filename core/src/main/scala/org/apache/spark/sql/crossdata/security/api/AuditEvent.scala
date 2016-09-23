
package org.apache.spark.sql.crossdata.security.api



case class AuditEvent(userId: String, resource: Resource, action: Action, result: AuditResult,
                      auditAddresses: AuditAddresses, policy: Option[String] = None,
                      impersonation: Option[Boolean] = None)

case class AuditAddresses(sourceIp: String, destIp: String)