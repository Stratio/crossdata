package org.apache.spark.sql.crossdata.authorizer

import com.stratio.crossdata.security._

class SMAllowingWriteCatalogAndDatastore extends BaseSecurityManagerTest{

  override def authorize(userId: String, resource: Resource, action: Action): Boolean = {
    val isWriteCatalog = resource.resourceType == CatalogResource && action == Write && resource.name == SecurityManagerTestConstants.CatalogIdentifier
    val isWriteDatastoreAll = resource.resourceType == DatastoreResource && resource.name == "*" && action == Write

    (isWriteCatalog || isWriteDatastoreAll) && super.authorize(userId, resource, action)
  }

}

