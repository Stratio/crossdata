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

import com.hazelcast.core.ITopic
import com.stratio.crossdata.util.CacheInvalidator
import org.apache.spark.sql.crossdata.session.HazelcastCacheInvalidator.{CacheInvalidationEvent, ResourceInvalidation}
import org.apache.spark.sql.crossdata.session.XDSessionProvider.SessionID

object HazelcastCacheInvalidator {

  trait CacheInvalidationEvent extends Serializable

  case class ResourceInvalidation(sessionId: SessionID)
      extends CacheInvalidationEvent
  case object ResourceInvalidationForAllSessions extends CacheInvalidationEvent

}

class HazelcastCacheInvalidator(
    sessionID: SessionID,
    topic: ITopic[CacheInvalidationEvent]
) extends CacheInvalidator {

  private val invalidateSessionEvent = ResourceInvalidation(sessionID)

  override def invalidateCache: Unit = {
    topic.publish(invalidateSessionEvent)
  }

}
