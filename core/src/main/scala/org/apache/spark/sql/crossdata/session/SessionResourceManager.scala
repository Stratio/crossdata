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
package org.apache.spark.sql.crossdata.session

import org.apache.spark.sql.crossdata.session.XDSessionProvider.SessionID

import scala.util.Try

trait SessionResourceManager[V] {

  //NOTE: THIS METHOD SHOULD NEVER BE CALLED TWICE WITH THE SAME ID
  def newResource(key: SessionID, from: Option[V]): V

  def getResource(key: SessionID): Try[V]

  def deleteSessionResource(key: SessionID): Try[Unit]

  def clearAllSessionsResources(): Unit

  def invalidateLocalCaches(key: SessionID): Unit

  def invalidateAllLocalCaches: Unit

}
