package com.stratio.meta2.server.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props, ReceiveTimeout, RootActorPath}
import akka.cluster.ClusterEvent._
import com.stratio.meta.communication._
import com.stratio.meta2.core.query.InProgressQuery
import akka.cluster.Cluster
import java.util.HashMap
import akka.actor.ActorSelection
import com.stratio.meta2.core.query.MetadataInProgressQuery
import com.stratio.meta2.core.query.SelectInProgressQuery
import com.stratio.meta2.core.query.StorageInProgressQuery

object ConnectorManagerActor {
  def props(): Props = Props(new ConnectorManagerActor)
}

class ConnectorManagerActor extends Actor with ActorLogging {

  log.info("Lifting connector actor")
  val coordinatorActorRef = context.actorSelection("../CoordinatorActor")
  //coordinatorActorRef ! "hola"

  //var connectorsMap:scala.collection.mutable.Map[String, ActorRef] = scala.collection.mutable.Map[String,ActorRef]()
  var connectorsMap:scala.collection.mutable.Map[String, ActorSelection] = scala.collection.mutable.Map[String,ActorSelection]()

  override def preStart(): Unit = {
    //#subscribe
    Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }
  override def postStop(): Unit =
    Cluster(context.system).unsubscribe(self)

  def receive = {

    case mu: MemberUp => {
      log.info("Member is Up: {}" + mu.toString+mu.member.getRoles)
      val it=mu.member.getRoles.iterator()
      while(it.hasNext()){
    	  var rol=it.next()
    	  rol match{
    	    case "connector"=>
    	    	val connectorActorRef = context.actorSelection(RootActorPath(mu.member.address) / "user" / "meta-connector")
    	    	val id=java.util.UUID.randomUUID.toString()
    	    	connectorsMap.put(mu.member.toString, connectorActorRef)
    	    	//connectorActorRef ! "hola"
    	  }
    	  log.info("has role: {}" + rol)
      }
      // connectorsMap += (member.toString -> memberActorRef)
      //memberActorRef ! "hola pichi, estás metaregistrado"
    }


    case query: StorageInProgressQuery=>
      log.info("storage in progress query")
    case query: SelectInProgressQuery=>
      log.info("select in progress query")
    case query: MetadataInProgressQuery=>
      log.info("metadata in progress query")
     

    case state: CurrentClusterState =>
      log.info("Current members: {}", state.members.mkString(", "))

    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)

    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)

    case _: MemberEvent =>
      log.info("Receiving anything else")

    case _: ClusterDomainEvent =>
      println("ClusterDomainEvent")

    case ReceiveTimeout =>
      println("ReceiveTimeout")
      
    case _:ConnectToConnector=>
      println("connecting to connector ")

    case _: DisconnectFromConnector=>
      println("disconnecting from connector")

      /*
    case ex:Execute=>
      println("Executing workflow "+ex.workflow.toString())
      println("initial steps->"+ex.workflow.getInitialSteps())
     */
    //case _: Request=>
    //case _: Response=>
    //case _: MetadataStruct=>
    //case _: StorageQueryStruct=>
    //case _: WorkflowStruct=>

    case toConnector: MetadataStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case toConnector: StorageQueryStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case toConnector: WorkflowStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case response: Response =>
      //connectorsMap += (response.msg -> sender)

    case other =>
      println("connector actor receives event")
    //      sender ! "OK"
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }

}