package com.stratio.connector

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.meta.communication.{getConnectorName, replyConnectorName}


object ClusterListener{
  def props (connectorName:String):Props = Props (new ClusterListener(connectorName) )
}

class ClusterListener(connectorName:String) extends Actor with ActorLogging {
  

  val cluster = Cluster(context.system)

  // subscribe to cluster changes, re-subscribe when restart 

  def receive = {
    
    /*
    case metadataEngineRequest:MetadataEngineRequest=>
      //getMetadataEngine()
      log.info("->"+"Receiving MetadataRequest")

    case dataStoreNameRequest:DataStoreNameRequest=>
      //getDatastoreName()
      log.info("->"+"Receiving MetadataRequest")
      
    case initRequest:com.stratio.meta.communication.InitRequest=>
      //init(IConfiguration)
      log.info("->"+"Receiving MetadataRequest")
      
    case connectRequest:com.stratio.meta.communication.ConnectRequest=>
      //connect(ICredentials, ConnectorClusterConfig)
      log.info("->"+"Receiving MetadataRequest")
      
    case closeRequest:com.stratio.meta.communication.CloseRequest=>
      //close(ClusterName)
      log.info("->"+"Receiving MetadataRequest")
      
    case isConnectedRequest:com.stratio.meta.communication.IsConnectedRequest=>
      //isConnected(ClusterName)
      log.info("->"+"Receiving MetadataRequest")
      
      
    case storageEngineRequest:com.stratio.meta.communication.StorageEngineRequest=>
      //getStorageEngine()
      log.info("->"+"Receiving MetadataRequest")

    case queryEngineRequest:com.stratio.meta.communication.QueryEngineRequest=>
      //getQueryEngine()d
      log.info("->"+"Receiving MetadataRequest")
      * 
      */

    case msg:getConnectorName=>
    {
      sender ! replyConnectorName(connectorName)
    }

    case s:String=>
      println("->"+"Receiving String: {}"+s)
      log.info("->"+"Receiving String: {}",s)

    case MemberUp(member) =>
      println("member up")
      log.info("*******Member is Up: {} {}!!!!!", member.toString ,member.getRoles)
      //val actorRefe=context.actorSelection(RootActorPath(member.address) / "user" / "clusterListener" )
      //actorRefe ! "hola "+member.address+ "  "+RootActorPath(member.address) 
      
    case state: CurrentClusterState =>
      log.info("Current members: {}", state.members.mkString(", "))
      
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)

    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)

    case _: MemberEvent => 
      log.info("Receiving anything else")

  }
}
