/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.session

import com.hazelcast.core.ITopic
import com.stratio.crossdata.util.CacheInvalidator
import org.apache.spark.sql.crossdata.session.HazelcastCacheInvalidator.{CacheInvalidationEvent, ResourceInvalidation}
import org.apache.spark.sql.crossdata.session.XDSessionProvider.SessionID

object HazelcastCacheInvalidator {


  trait CacheInvalidationEvent extends Serializable

  case class ResourceInvalidation(sessionId: SessionID) extends CacheInvalidationEvent
  case object ResourceInvalidationForAllSessions extends  CacheInvalidationEvent

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
