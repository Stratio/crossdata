/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.execution

import com.stratio.crossdata.security._
import org.apache.log4j.Logger
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.execution.auth.AuthDirectivesExtractor
import org.apache.spark.sql.crossdata.{XDContext, XDSQLConf}
import org.apache.spark.sql.execution._

/**
  * @inheritdoc
  */
class XDQueryExecution(sqlContext: SQLContext, parsedPlan: LogicalPlan, catalogIdentifier: String) extends QueryExecution(sqlContext, parsedPlan){

  lazy val logger = Logger.getLogger(classOf[XDQueryExecution])

  lazy val authorized: LogicalPlan = {
    // TODO assertAnalyzed() execute sqlContext.analyzer.execute(authorized) twice??
    val xdContext = sqlContext.asInstanceOf[XDContext]

    xdContext.securityManager.foreach { securityManager =>
      val userId = xdContext.conf.getConfString(XDSQLConf.UserIdPropertyKey)
      if (resourcesAndActions.isEmpty) {
        logger.debug(s"LogicalPlan ${parsedPlan.treeString} does not access to any resource")
      }
      val isAuthorized = resourcesAndActions.forall { case (resource, action) =>
        val isAuth = securityManager.authorize(userId, resource, action)
        if (!isAuth) {
          logger.warn(s"Authorization rejected for user $userId: resource=$resource action=$action")
        }
        isAuth
      }
      if (!isAuthorized) {
        throw new RuntimeException("Operation not authorized") // TODO specify the resource/action?
      }
    }

    parsedPlan
  }

  override lazy val analyzed: LogicalPlan = sqlContext.analyzer.execute(authorized)


  // Extracts
  lazy val resourcesAndActions: Seq[(Resource, Action)] = {
    val crossdataInstances: Seq[String] = Seq(sys.env.getOrElse(Resource.CrossdataClusterNameEnvVar, "unknown")) // TODO get crossdataInstances

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)
    authDirectivesExtractor.extractResourcesAndActions(parsedPlan)
  }

}
