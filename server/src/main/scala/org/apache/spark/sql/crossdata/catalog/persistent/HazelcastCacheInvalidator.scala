package org.apache.spark.sql.crossdata.catalog.persistent

import com.hazelcast.core.ITopic
import com.stratio.crossdata.util.CacheInvalidator
import org.apache.spark.sql.crossdata.XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.catalog.persistent.HazelcastCacheInvalidator.{CacheInvalidationEvent, ResourceInvalidation}

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
