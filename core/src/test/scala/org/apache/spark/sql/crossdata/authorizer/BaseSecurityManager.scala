package org.apache.spark.sql.crossdata.authorizer

import com.stratio.crossdata.security._

object SecurityManagerTestConstants {
  val XDUser = "mstr"
  val CatalogIdentifier = "mstrCatalog" // also known as prefix
}

trait BaseSecurityManagerTest extends CrossdataSecurityManager {

  import SecurityManagerTestConstants._

  override def start(): Unit = ()

  override def stop(): Unit = ()

  override def audit(auditEvent: AuditEvent): Unit = ()

  def authorize(userId: String, resource: Resource, action: Action): Boolean = {
    val isExpectedUser: Boolean = userId == SecurityManagerTestConstants.XDUser
    val hasValidTableName: Boolean =
    if (resource.resourceType == TableResource) resource.name startsWith CatalogIdentifier else true
    isExpectedUser && hasValidTableName
  }

}
