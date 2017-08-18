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
