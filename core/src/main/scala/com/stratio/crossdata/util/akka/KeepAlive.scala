package com.stratio.crossdata.util.akka

import akka.actor.{Actor, ActorRef, Cancellable}

import scala.concurrent.duration._

/**
  * [[KeepAlive]] object provides the tools to easily implement a dead man switch
  * (https://en.wikipedia.org/wiki/Dead_man%27s_switch) for actors. This dead switch is not dependant on
  * low-level details, neither on the actor reference or address within an ActorSystem, remoting or akka clustering
  * mechanisms.
  *
  * Each monitored actor provides a customized id whereby the [[KeepAlive.Master]] or controller identifies it.
  *
  */
object KeepAlive {

  /**
    * Upon this message reception, the [[Master]] actor'll start checking whether heartbeats from any actor providing
    * the same id are being received at each period.
    * @param id expected to be verified at each period
    * @param period for which heartbeats, from a specific id, are expected to be received at least once before
    *               raising miss alarms for that id.
    */
  case class DoCheck[ID](id: ID, period: FiniteDuration)

  private[KeepAlive] case class HeartBeat[ID](sourceId: ID)


  /**
    * Stackable modifications trait for building master actors, that is, actors able to monitor heartbeats.
    * https://issues.scala-lang.org/browse/SI-912
    */
  trait Master[ID] extends Actor {

    /**
      * Method called when an alarm for an Id has been raised, that is, when no heartbeats have been received during
      * a period. Its return value determines whether the target actor should still be monitored or otherwise forgotten
      * by the master actor.
      * @param id
      * @return `true` iff the id should still be monitored.
      */
    protected def onMiss(id: ID): Boolean

    abstract override def receive: Receive = super.receive orElse receive(Set.empty)

    def receive(pending: Set[ID]): Receive = super.receive orElse keepAliveControlReceive(pending)
    private def keepAliveControlReceive(pending: Set[ID]): Receive = {
      case HeartBeat(id: ID @ unchecked) => context.become(receive(pending - id))
      case m @ DoCheck(id: ID @ unchecked, period) =>
        import context.dispatcher
        val missed = (pending contains id)
        if(missed && onMiss(id) || !missed) {
          context.system.scheduler.scheduleOnce(period, self, m)
          context.become(receive(pending + id))
        }
    }


  }

  /**
    * Stackable modifications trait to be mixed at those actors which to be monitored.
    */
  trait LiveMan[ID] extends Actor {

    // Attributes to be implemented by the actors mixing this trait

    val master: ActorRef // Actor reference to the master (monitor) actor.
    val period: FiniteDuration // Duration of the time interval between two heartbeat messages.
    val keepAliveId: ID // Actor id in the KeepAlive schema.

    val initialDelay: FiniteDuration = 0 seconds

    // Internal implementation

    private var ticks: Option[Cancellable] = None
    private lazy val tick: HeartBeat[ID] = HeartBeat(keepAliveId)

    abstract override def preStart(): Unit = {
      super.preStart()
      ticks = Some {
        import context.dispatcher
        context.system.scheduler.schedule(initialDelay, period) {
          master ! tick
        }
      }
    }

    abstract override def postStop(): Unit = {
      ticks.foreach(_.cancel)
      super.postStop()
    }

  }

}
