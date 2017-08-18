/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming.actors

import akka.actor.Actor
import com.stratio.crossdata.streaming.actors.EphemeralQueryActor._
import com.stratio.crossdata.streaming.constants.ApplicationConstants._
import org.apache.spark.sql.crossdata.daos.DAOConstants._
import org.apache.spark.sql.crossdata.daos.EphemeralQueriesMapDAO
import org.apache.spark.sql.crossdata.models.EphemeralQueryModel

import scala.util.Try

class EphemeralQueryActor(zookeeperConfiguration: Map[String, String]) extends Actor
with EphemeralQueriesMapDAO {

  lazy val memoryMap = Map(ZookeeperPrefixName -> zookeeperConfiguration)
  var streamingQueries: List[EphemeralQueryModel] = dao.getAll()

  def prefix:String = Try(memoryMap.get(ZookeeperPrefixName).get(PrefixStreamingCatalogsConfigForActors)+"_") getOrElse ("")

  import context.become

  def receive: Receive = receive(listenerAdded = false)

  def receive(listenerAdded: Boolean): Receive = {
    case GetQueries if listenerAdded =>
      doGetQueries()
    case AddListener if !listenerAdded =>
      doAddListener()
      become(receive(listenerAdded = true))
  }

  private def doGetQueries(): Unit = {
    sender ! EphemeralQueriesResponse(streamingQueries.toSeq)
  }

  private def doAddListener(): Unit = {
    repository.addEntityListener(dao.entity, _ => streamingQueries = dao.getAll())
    sender ! ListenerResponse(true)
  }
}

object EphemeralQueryActor {

  case object GetQueries

  case object AddListener

  case class ListenerResponse(added : Boolean)

  case class EphemeralQueriesResponse(streamingQueries: Seq[EphemeralQueryModel])

}
