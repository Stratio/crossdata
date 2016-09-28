package org.apache.spark.sql.crossdata.authorizer

import com.stratio.crossdata.security._
import org.apache.log4j.Logger


class SMAllowingWriteCatalog extends CrossdataSecurityManager{

  private val logger = Logger.getLogger(classOf[SMAllowingWriteCatalog])

  def start() : Unit = ()

  def stop(): Unit = ()

  def authorize(userId: String, resource: Resource, action: Action): Boolean =
    resource.resourceType == CatalogResource && action == Write

  def audit(auditEvent: AuditEvent): Unit = ()
}

