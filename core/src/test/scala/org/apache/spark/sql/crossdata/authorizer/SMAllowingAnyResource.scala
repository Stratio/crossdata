package org.apache.spark.sql.crossdata.authorizer

import com.stratio.crossdata.security.{Action, Resource}

class SMAllowingAnyResource extends BaseSecurityManagerTest{

  override def authorize(userId: String, resource: Resource, action: Action): Boolean =
    super.authorize(userId, resource, action)

}